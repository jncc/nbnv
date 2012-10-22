package uk.gov.nbn.data.gis.processor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * The following class is responsible for handling requests which come into a
 * servlet and processing them to ultimately send the client some results.
 * 
 * This class is merely a helper for a servlet class, but has been decoupled 
 * from a servlet instance for greater integration with spring
 * @author Christopher Johnson
 */
@Component
public class MapServerServletHelper {
    @Autowired ApplicationContext context;
    @Autowired MapServiceMethodFactory serviceFactory;
    @Autowired InterceptorFactory interceptorFactory;
    @Autowired MapFileGenerator mapFileGenerator;
    
    public void processRequest(HttpServletRequest request, 
            HttpServletResponse servletResponse) throws ServletException, IOException {
        try {
            MapServiceMethod mapMethod = serviceFactory.getMapServiceMethod(request.getPathInfo());
            Response interceptedResponse = interceptorFactory.getResponse(mapMethod.call(request));
            servletResponse.setContentType(interceptedResponse.getContentType());
            copyAndClose(interceptedResponse.getResponse(), servletResponse);
        }
        catch(MapServiceUndefinedException msue) {
            servletResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "Could not find: " + Arrays.toString(request.getPathInfo().substring(1).split("/")));
        }
        catch(InvocationTargetException ite) {
            handleMapServiceException(ite.getTargetException(), servletResponse);
        }
        catch(Throwable mapEx) {
            throw new ServletException(mapEx);
        }
    }
    
    
    /*Handle execptions which were thrown during the construction of map services*/
    private static void handleMapServiceException(Throwable e, HttpServletResponse response) throws IOException {
        if(e instanceof IllegalArgumentException) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
        else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }  
    
    private static void copyAndClose(InputStream in, HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        try {
            try {
                IOUtils.copy(in, out);
                in.close();
            }
            finally {
                in.close();
            }
        }
        finally {
            out.close();
        }
    }
}

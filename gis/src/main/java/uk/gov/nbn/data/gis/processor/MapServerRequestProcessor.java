package uk.gov.nbn.data.gis.processor;

import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
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
public class MapServerRequestProcessor {
    @Autowired ApplicationContext context;
    @Autowired MapServiceMethodFactory serviceFactory;
    @Autowired InterceptorFactory interceptorFactory;
    @Autowired MapFileGenerator mapFileGenerator;
    
    public void processRequest(HttpServletRequest request, 
            HttpServletResponse servletResponse) throws ServletException, IOException {
        try {
            MapServiceMethod mapMethod = serviceFactory.getMatchingPart(request.getPathInfo());
            Response interceptedResponse = interceptorFactory.getResponse(mapMethod, request);
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
    
    /**
     * The following method will obtain a mapping response for the given map 
     * service method and request. If you need to modify the details of the request
     * either use a @see InterceptedHttpServletRequest or the InterceptorFactory
     * @param mapMethod The method to process
     * @param request The request to execute
     * @return A Response containing the mime type and InputStream of the data to read
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws InvocationTargetException
     * @throws ProviderException
     * @throws TemplateException 
     */
    public Response getResponse(MapServiceMethod mapMethod, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, IOException, InvocationTargetException, ProviderException, TemplateException {
        File toSubmitToMapServer = mapFileGenerator.getMapFile(request, mapMethod);
        try {
            URL mapServerURL = serviceFactory.getMapServiceURL(toSubmitToMapServer, request);
            HttpURLConnection openConnection = (HttpURLConnection)mapServerURL.openConnection();
            return new Response(openConnection.getContentType(),openConnection.getInputStream());
        }
        finally {
            toSubmitToMapServer.delete();
        }
    }
    
    /*Handle execptions which were thrown during the construction of map services*/
    private static void handleMapServiceException(Throwable e, HttpServletResponse response) throws IOException {
        if(e instanceof IllegalArgumentException) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getCause().getMessage());
        }
        else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getCause().getMessage());
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

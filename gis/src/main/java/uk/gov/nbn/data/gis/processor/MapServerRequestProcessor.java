package uk.gov.nbn.data.gis.processor;

import uk.gov.nbn.data.gis.processor.atlas.AtlasGradeProcessor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.Interceptor.Response;

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
    @Autowired MapFileGenerator mapFileGenerator;
    
    private Collection<Interceptor> interceptors;
    
    @PostConstruct void init() {
        interceptors = context.getBeansOfType(Interceptor.class).values();
    }
    
    private Interceptor getInterceptor(Map<String, String[]> query) {
        for(Interceptor curr : interceptors) {
            if(curr.intercepts(query)) {
                return curr;
            }
        }
        return null;
    } 
    
    public void processRequest(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
        try {
            MapServiceMethod mapMethod = serviceFactory.getMatchingPart(request.getPathInfo());
            
            ServletOutputStream out = response.getOutputStream();
            try {
                File toSubmitToMapServer = mapFileGenerator.getMapFile(request, mapMethod);
                try {
                    InputStream in;
                    Interceptor interceptor = getInterceptor(request.getParameterMap());
                    if(interceptor != null) { //perform interception (TODO consider if interception is the correct thing to be doing)
                        Response toReturn = interceptor.intercepts(toSubmitToMapServer, request.getParameterMap());
                        response.setContentType(toReturn.getContentType()); 
                        in = toReturn.getResponse();
                    }
                    else {
                        URL mapServerURL = serviceFactory.getMapServiceURL(toSubmitToMapServer, getMapServerRequest(mapMethod,request.getParameterMap()));
                        HttpURLConnection openConnection = (HttpURLConnection)mapServerURL.openConnection();
                        response.setContentType(openConnection.getContentType()); 
                        in = openConnection.getInputStream();
                    }
                    IOUtils.copy(in, out);
                    in.close();
                }
                finally {
                    toSubmitToMapServer.delete();
                }
            }
            catch(InvocationTargetException ite) {
                handleMapServiceException(ite.getTargetException(), response);
            }
            catch(Throwable mapEx) {
                throw new ServletException(mapEx);
            }
            finally {
                out.close();
            }
        }
        catch(MapServiceUndefinedException msue) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Could not find: " + Arrays.toString(request.getPathInfo().substring(1).split("/")));
        }
    }
    
    private Map<String, String[]> getMapServerRequest(MapServiceMethod mapMethod, Map<String, String[]> requestParams) {
        if(mapMethod.isAtlasGrade()) {
            Map<String, String[]> toReturn = new HashMap<String, String[]>(requestParams);
            for(AtlasGradeProcessor currProcessor : mapMethod.getAtlasGradeProcessors()) {
                toReturn.putAll(currProcessor.processRequestParameters(mapMethod, requestParams));
            }
            return toReturn;
        }
        else {
            return requestParams;
        }
    }
    
    /*Handle execptions which were thrown during the construction of map services*/
    private static void handleMapServiceException(Throwable e, HttpServletResponse response) throws IOException {
        if(e instanceof IllegalArgumentException) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getCause().getMessage());
        }
    }  
}

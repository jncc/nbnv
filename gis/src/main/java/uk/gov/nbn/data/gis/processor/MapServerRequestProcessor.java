package uk.gov.nbn.data.gis.processor;

import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The following class is responsible for requesting maps from an underlying
 * MapServer. This is the class which will ultimately push a request to a map server
 * and return its response in a response object.
 * 
 * Under the hood. All requests which go to the map server utilise post requests.
 * This allows (practically) unlimited size modifications of the query
 * @author Christopher Johnson
 */
@Component
public class MapServerRequestProcessor {
    private static final String URL_PARAMETER_ENCODING = "UTF-8";
    @Autowired MapFileGenerator mapFileGenerator;
    @Autowired Properties properties;
    
    /**
     * The following method will obtain a mapping response for the given map 
     * service method and request. If you need to modify the details of the request
     * either use a @see InterceptedHttpServletRequest or the InterceptorFactory
     * @param mapMethodCall The method to process
     * @param request The request to execute
     * @return A Response containing the mime type and InputStream of the data to read
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws InvocationTargetException
     * @throws ProviderException
     * @throws TemplateException 
     */
    public Response getResponse(MapServiceMethod.Call mapMethodCall, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, IOException, InvocationTargetException, ProviderException, TemplateException {
        File mapFile = mapFileGenerator.getMapFile(mapMethodCall);
        try {
            URL mapServerURL = new URL(properties.getProperty("mapserver"));
            HttpURLConnection conn = (HttpURLConnection)mapServerURL.openConnection();
            conn.setDoOutput(true);
            String requestToPost = getQueryFromMap(getMapServerRequest(mapFile, request));
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            try {
                wr.write(requestToPost);
                wr.flush();
            }
            finally {
                wr.close();
            }
            return new Response(conn.getContentType(),conn.getInputStream());
        }
        finally {
            mapFile.delete();
        }
    }
    
    private static Map<String, String[]> getMapServerRequest(File mapFile, HttpServletRequest request) {
        Map<String, String[]> modifiedQuery = new HashMap<String, String[]>(request.getParameterMap());
        modifiedQuery.put("map", new String[] {mapFile.getAbsolutePath()});
        return modifiedQuery;
    }
    
    private static String getQueryFromMap(Map<String, String[]> query) throws UnsupportedEncodingException {
        StringBuilder toReturn = new StringBuilder();
        
        for(Map.Entry<String, String[]> entry : query.entrySet()) {
            for(String currValue : entry.getValue()) {
                toReturn.append(URLEncoder.encode(entry.getKey(), URL_PARAMETER_ENCODING))
                        .append("=")
                        .append(URLEncoder.encode(currValue, URL_PARAMETER_ENCODING))
                        .append("&");
            }
        }
        return toReturn.substring(0, toReturn.length()-1);
    }
}

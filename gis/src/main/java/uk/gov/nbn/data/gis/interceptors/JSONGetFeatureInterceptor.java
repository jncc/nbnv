package uk.gov.nbn.data.gis.interceptors;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.Interceptor;
import uk.gov.nbn.data.gis.processor.MapServiceMethodFactory;

/**
 * The following interceptor enables JSONP and JSON requests for getfeatureinfo 
 * on maps with queryable layers. 
 * @author Christopher Johnson
 */
@Component
public class JSONGetFeatureInterceptor implements Interceptor {
    private static final String JSONP_CALLBACK_PARAMETER = "callback";    
    @Autowired MapServiceMethodFactory serviceFactory;
    
    @Override
    public boolean intercepts(Map<String, String[]> query) {
        return 
            mapContainsKeyValue(query, "REQUEST", "GetFeatureInfo") &&
            mapContainsKeyValue(query, "INFO_FORMAT", "application/json");
    }

    @Override
    public Response intercepts(File mapFile, Map<String, String[]> query) {
        try {
            JSONObject toReturn = obtainJSONObjectFeatureInfo(mapFile, query);
            
            if(query.containsKey(JSONP_CALLBACK_PARAMETER)){ 
               return new Response("application/javascript", 
                    new ByteArrayInputStream(
                       new StringBuilder(query.get(JSONP_CALLBACK_PARAMETER)[0])
                            .append("(").append(toReturn.toString()).append(");")
                       .toString().getBytes()
                    )); 
            }
            else { 
                return new Response("application/json", new ByteArrayInputStream(toReturn.toString().getBytes()));
            }
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private JSONObject obtainJSONObjectFeatureInfo(File mapFile, Map<String, String[]> query) throws IOException, JSONException {
        Map<String, String[]> newQuery = new HashMap<String, String[]>(query);
        newQuery.put("INFO_FORMAT", new String[]{"gml"});
        
        URL mapServerURL = serviceFactory.getMapServiceURL(mapFile, newQuery);
        HttpURLConnection openConnection = (HttpURLConnection)mapServerURL.openConnection();
        InputStream in = openConnection.getInputStream();
        try {
            return XML.toJSONObject(IOUtils.toString(in));
        }
        finally {
            in.close();
        }
        
    }
    private static boolean mapContainsKeyValue(Map<String, String[]> query, String key, String value) {
        return query.containsKey(key) && Arrays.asList(query.get(key)).contains(value);
    }
}

package uk.gov.nbn.data.gis.interceptors;

import freemarker.template.TemplateException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.InterceptedHttpServletRequest;
import uk.gov.nbn.data.gis.processor.Interceptor;
import uk.gov.nbn.data.gis.processor.Intercepts;
import uk.gov.nbn.data.gis.processor.MapServerRequestProcessor;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.MapServiceMethod.Type;
import uk.gov.nbn.data.gis.processor.ProviderException;
import uk.gov.nbn.data.gis.processor.Response;

/**
 * The following interceptor enables JSONP and JSON requests for getfeatureinfo 
 * on maps with queryable layers. 
 * @author Christopher Johnson
 */
@Component
@Interceptor
public class JSONGetFeatureInterceptor {
    private static final String JSONP_CALLBACK_PARAMETER = "callback";    
    @Autowired MapServerRequestProcessor requestProcessor;
    
    private static boolean isToInterceptQuery(Map<String, String[]> query) {
        return 
            mapContainsKeyValue(query, "REQUEST", "GetFeatureInfo") &&
            mapContainsKeyValue(query, "INFO_FORMAT", "application/json");
    }

    @Intercepts(Type.STANDARD)
    public Response intercepts(MapServiceMethod method, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, IOException, InvocationTargetException, ProviderException, TemplateException, JSONException {
        Map<String, String[]> query = request.getParameterMap();
        if(isToInterceptQuery(request.getParameterMap())) {
            JSONObject toReturn = obtainJSONObjectFeatureInfo(method, request);

            if(query.containsKey(JSONP_CALLBACK_PARAMETER)){ 
               return new Response("application/javascript", 
                    new ByteArrayInputStream(
                       new StringBuilder(request.getParameter(JSONP_CALLBACK_PARAMETER))
                            .append("(").append(toReturn.toString()).append(");")
                       .toString().getBytes()
                    )); 
            }
            else { 
                return new Response("application/json", new ByteArrayInputStream(toReturn.toString().getBytes()));
            }
        }
        else {
            return null;
        }
    }
    
    private JSONObject obtainJSONObjectFeatureInfo(MapServiceMethod mapMethod, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, IOException, InvocationTargetException, ProviderException, TemplateException, JSONException{
        InterceptedHttpServletRequest manipRequest = new InterceptedHttpServletRequest(request);
        manipRequest.setParameterValues("INFO_FORMAT", new String[]{"gml"});
        InputStream in = requestProcessor.getResponse(mapMethod, manipRequest).getResponse();
        
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

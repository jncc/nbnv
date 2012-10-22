package uk.gov.nbn.data.gis.interceptors;

import freemarker.template.TemplateException;
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
    @Autowired MapServerRequestProcessor requestProcessor;
    
    private static boolean isToInterceptQuery(Map<String, String[]> query) {
        return 
            mapContainsKeyValue(query, "REQUEST", "GetFeatureInfo") &&
            mapContainsKeyValue(query, "INFO_FORMAT", "application/json");
    }

    @Intercepts(Type.STANDARD)
    public Response intercepts(MapServiceMethod.Call methodCall, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, IOException, InvocationTargetException, ProviderException, TemplateException, JSONException {
        if(isToInterceptQuery(request.getParameterMap())) {
            JSONObject toReturn = obtainJSONObjectFeatureInfo(methodCall, request);
            return Response.getJSONPResponse(request, toReturn);
        }
        else {
            return null;
        }
    }
    
    private JSONObject obtainJSONObjectFeatureInfo(MapServiceMethod.Call mapMethodCall, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, IOException, InvocationTargetException, ProviderException, TemplateException, JSONException{
        InterceptedHttpServletRequest manipRequest = new InterceptedHttpServletRequest(request);
        manipRequest.setParameterValues("INFO_FORMAT", new String[]{"gml"});
        InputStream in = requestProcessor.getResponse(mapMethodCall.getMethod().call(manipRequest)).getResponse();
        
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

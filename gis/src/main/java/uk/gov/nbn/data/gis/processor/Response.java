package uk.gov.nbn.data.gis.processor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;

/**
 * The following object can be returned from interceptors in order to return 
 * content which is completely different to that of the map server response
 * @author Christopher Johnson
 */
public class Response {
    private static final String JSONP_CALLBACK_PARAMETER = "callback"; 
    
    private final String contentType;
    private final InputStream response;
    
    public Response(String contentType, InputStream response) {
        this.contentType = contentType;
        this.response = response;
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getResponse() {
        return response;
    }
    
    /**
     * The following static method is provided as a convenience to the interceptors
     * It will form a JSON/JSONWithPadding response dependant on weather or not
     * the request contains the JSONP_CALLBACK_PARAMETER
     * @param request The request which was made
     * @param json JSON data to return as either json or jsonp
     * @return The response formed from the json data
     */
    public static Response getJSONPResponse(HttpServletRequest request, JSONObject json) {
        Map<String, String[]> query = request.getParameterMap();
        if(query.containsKey(JSONP_CALLBACK_PARAMETER)){ 
            return new Response("application/javascript", 
                 new ByteArrayInputStream(
                    new StringBuilder(request.getParameter(JSONP_CALLBACK_PARAMETER))
                         .append("(").append(json.toString()).append(");")
                    .toString().getBytes()
                 )); 
         }
         else { 
             return new Response("application/json", new ByteArrayInputStream(json.toString().getBytes()));
         }
    }
}

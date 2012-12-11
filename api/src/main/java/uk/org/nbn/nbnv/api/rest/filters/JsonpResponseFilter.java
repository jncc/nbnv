package uk.org.nbn.nbnv.api.rest.filters;


import com.sun.jersey.api.json.JSONWithPadding;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The following class will wrap up the response of any json returning resource
 * and wrap it up in a JSONWithPadding object. 
 * @author Christopher Johnson
 */
public class JsonpResponseFilter implements ContainerResponseFilter {
    private static final String JSONP_CALLBACK_PARAMETER = "callback";
    private static final MediaType APPLICATION_JAVASCRIPT_TYPE = MediaType.valueOf("application/x-javascript");
    private static final List<MediaType> ACCEPTABLE_JAVASCRIPT_TYPES = Arrays.asList(APPLICATION_JAVASCRIPT_TYPE);
    
    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        String callback = request.getQueryParameters().getFirst(JSONP_CALLBACK_PARAMETER);
        
        MediaType acceptableJavascriptType = request.getAcceptableMediaType(ACCEPTABLE_JAVASCRIPT_TYPES);
        boolean jsonpPossible = acceptableJavascriptType != null && callback != null && !callback.isEmpty();
        
        if (isResponseJSON(response) && jsonpPossible && response.getEntity() != null) {
            response.setResponse(Response.ok(   //Browsers will not execute none 200 codes
                    new JSONWithPadding(response.getEntity(), callback), 
                    acceptableJavascriptType
                ).build());
        }

        return response;
    } 
   
    private static boolean isResponseJSON(ContainerResponse response) {
        MediaType mediaType = response.getMediaType();
        return mediaType == null || !mediaType.equals(MediaType.APPLICATION_JSON_TYPE);
    }
}
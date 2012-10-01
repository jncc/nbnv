package uk.gov.nbn.data.gis.processor;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * The following interface represents the interface which must be implemented
 * in order for map requests to be intercepted and manipulated
 * @author Christopher Johnson
 */
public interface Interceptor {
    static class Response {
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
        
    }
    boolean intercepts(Map<String, String[]> query);
    Response intercepts(File mapFile, Map<String, String[]> query);
}

package uk.gov.nbn.data.gis.processor;

import java.io.InputStream;

/**
 * The following object can be returned from interceptors in order to return 
 * content which is completely different to that of the map server response
 * @author Christopher Johnson
 */
public class Response {
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

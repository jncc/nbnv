package uk.gov.nbn.data.powerless.request;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLEncoder;

/**
 * The following class wraps up a key and value pair and presents it as an
 * HttpRequestParameter
 * @author Christopher Johnson
 */
public class HttpRequestParameter {
    public String key, value;
    
    public HttpRequestParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public String getKey() {
        return key;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getEncodedParameter() throws IOException {
         StringWriter toReturn = new StringWriter();
         writeEncodedParameter(toReturn);
         return toReturn.toString();
    }
    
    public void writeEncodedParameter(Writer toWriteTo) throws IOException {
        toWriteTo.write(URLEncoder.encode(getKey(), "UTF-8"));
        toWriteTo.write("=");
        toWriteTo.write(URLEncoder.encode(getValue(), "UTF-8"));
    }
}

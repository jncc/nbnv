package uk.gov.nbn.data.gis.config;

import java.io.IOException;
import java.util.Arrays;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * The following spring mvc MessageConverter is responsible for generating JSON
 * messages which are wrapped up in a function call (otherwise known as jsonp)
 * 
 * This allows you to call json services in a browser across domains.
 * 
 * Personally I think that spring mvc should provide this functionality out of 
 * the box, however at the time of writing this is not the case.
 *
 * http://en.wikipedia.org/wiki/JSONP
 * @author Christopher Johnson
 */
public class MappingJacksonJsonpHttpMessageConverter extends MappingJacksonHttpMessageConverter {
 
    /**
     * Default constructor, registers x-javascript as the sole mediatype
     */
    public MappingJacksonJsonpHttpMessageConverter() {
        setSupportedMediaTypes(Arrays.asList(new MediaType("application", "x-javascript", DEFAULT_CHARSET)));
    }
    
    /**
     * The JSONP Message converter only every creates jsonp, it never reads it
     * @param clazz The type for a message to be read in as
     * @param mediaType The mediatype requested to be read
     * @return always false
     */ 
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }
    
    /**
     * Determine if we are in a position to produce some JSONP wrapped content
     * @param clazz A class which a may or may not be mappable to json
     * @param mediaType A valid mediatype which may or may not be compatible with javascript
     * @return true if the padding callback is present and not empty and if it is
     *  possible to convert the type to json
     */
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        String padding = getJSONPadding();
        return padding != null && !padding.isEmpty() && super.canWrite(clazz, mediaType);
    }
    
    /**
     * Produce some json output wrapped in json padding
     * @param object The object instance which is to be wrapped in padding
     * @param outputMessage output to write to
     * @throws IOException
     * @throws HttpMessageNotWritableException 
     */
    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
        JsonGenerator jsonGenerator = this.getObjectMapper().getJsonFactory().createJsonGenerator(outputMessage.getBody(), encoding);        
 
        try {
            jsonGenerator.writeRaw(getJSONPadding());
            jsonGenerator.writeRaw("(");
            this.getObjectMapper().writeValue(jsonGenerator, object);
            jsonGenerator.writeRaw(");");
            jsonGenerator.flush();
        } catch (JsonProcessingException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON:"  + ex.getMessage(), ex);
        }
    }
    
    private String getJSONPadding() {
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
                    .getRequest()
                    .getParameter("callback");
    }
}

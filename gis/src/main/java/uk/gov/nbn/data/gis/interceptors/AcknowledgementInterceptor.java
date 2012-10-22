package uk.gov.nbn.data.gis.interceptors;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.Acknowledgement;
import uk.gov.nbn.data.gis.processor.Interceptor;
import uk.gov.nbn.data.gis.processor.Intercepts;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.ProviderException;
import uk.gov.nbn.data.gis.processor.ProviderFactory;
import uk.gov.nbn.data.gis.processor.Response;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;

/**
 * The following interceptor will create an acknowledgement html page
 * @author Christopher Johnson
 */
@Component
@Interceptor
public class AcknowledgementInterceptor {
    @Autowired ProviderFactory providerFactory;
    @Autowired Properties properties;
    
    private final Template acknowledgementTemplate;
    
    public AcknowledgementInterceptor() throws IOException {
        Configuration config = new Configuration();
        config.setClassForTemplateLoading(getClass(), "/");
        acknowledgementTemplate = config.getTemplate("acknowledgement.ftl");
    }
    
    @Intercepts(MapServiceMethod.Type.ACKNOWLEDGMENT)
    public Response processRequestParameters(   HttpServletRequest request, 
                                                MapServiceMethod method,
                                                @QueryParam(key="css") String css) 
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ProviderException, TemplateException, IOException {
        
        Acknowledgement acknowledgement = method.getUnderlyingMapMethod().getAnnotation(Acknowledgement.class);
        Object instance = method.getUnderlyingInstance();
        Method ackMethod = getMethod(method.getUnderlyingInstance(), acknowledgement);
       
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("externalCss", css);
        data.put("properties", properties);
        data.put("providers", providerFactory.provideForMethodAndExecute(
                                            instance, ackMethod, method, request));

        return processTemplate(acknowledgementTemplate, data);
    }
    
    private static Response processTemplate(Template template, Map<String, Object> data) throws TemplateException, IOException {
        StringWriter writer = new StringWriter();
        template.process(data, writer);
        return new Response("text/html", new ByteArrayInputStream(writer.toString().getBytes()));
    }
    
    private static Method getMethod(Object instance, Acknowledgement acknowledgement) {
        for(Method currMethod : instance.getClass().getMethods()) {
            if(     currMethod.getName().equals(acknowledgement.method()) &&
                    currMethod.getReturnType().equals(List.class)) {
                return currMethod;
            }
        }
        throw new IllegalArgumentException("Unable to find a method which matches the method name "
                + acknowledgement.method() + " and returns a list");
    }
}

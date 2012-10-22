package uk.gov.nbn.data.gis.providers;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.Annotations;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.MethodArgumentFactory;
import uk.gov.nbn.data.gis.processor.MethodArgumentFactory.Argument;
import uk.gov.nbn.data.gis.processor.Provider;
import uk.gov.nbn.data.gis.processor.ProviderException;
import uk.gov.nbn.data.gis.processor.ProviderFactory;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.gov.nbn.data.gis.providers.annotations.ServiceURL;
import uk.gov.nbn.data.gis.providers.annotations.Utilises;

/**
 * The following provider will return the url for the map service method which is 
 * currently being provided for. That is the pathInfo which was used in the servlet
 * request along with the a query string which contains parameters which were used
 * to generate the map file.
 * 
 * It should be noted that the url produced by this method conforms to WMS Standards
 * That is, the url will either be finished with a '?' or a '&'
 * @author Chris Johnson
 */
@Component
public class ServiceURLProvider implements Provider {
    private static final String URL_ENCODING = "UTF-8";
    
    @Autowired MethodArgumentFactory methodArgumentFactory;
    @Autowired ProviderFactory providerFactory;

    @Override
    public boolean isProviderFor(Class<?> clazz, Type type, Annotations annotations) {
        return annotations.containsKey(ServiceURL.class);
    }

    @Override
    public String provide(Class<?> clazz, Type type, MapServiceMethod.Call methodCall, Annotations annotations) throws ProviderException {
        try {
            HttpServletRequest request = methodCall.getRequest();
            StringBuilder toReturn = new StringBuilder(request.getRequestURL()).append("?");
            Set<String> mapServiceApplicableQueryParams = getMapServiceApplicableQueryParams(methodCall.getMethod());
            Map<String, String[]> parameters = request.getParameterMap();
            for(Entry<String, String[]> paramEntry : parameters.entrySet()) {
                String paramKey = paramEntry.getKey();
                if(mapServiceApplicableQueryParams.contains(paramKey)) {
                    for(String paramValue : paramEntry.getValue()) {
                        toReturn
                            .append(URLEncoder.encode(paramKey, URL_ENCODING))
                            .append("=")
                            .append(URLEncoder.encode(paramValue, URL_ENCODING))
                            .append("&");
                    }
                }
            }
            return toReturn.toString();
        } catch (UnsupportedEncodingException ex) {
            throw new ProviderException("Query string could not be encodded", ex);
        }
    }
    
    /* Create a set of queryparam keys which apply to this map method */
    private Set<String> getMapServiceApplicableQueryParams(MapServiceMethod method) {
        Set<String> toReturn = new HashSet<String>();
        Argument[] arguments = methodArgumentFactory.getArguments(method.getUnderlyingMapMethod());
        for(Argument argument: arguments) {
            Class<?> providerClass = providerFactory.getProviderFor(argument).getClass();
            
            Utilises providerUtilises = providerClass.getAnnotation(Utilises.class);
            
            if(providerUtilises != null) {
                for(QueryParam currQueryParam : providerUtilises.value()) {
                    toReturn.add(currQueryParam.key());
                }
            }
            else if(providerClass.equals(QueryParamProvider.class)) {
                toReturn.add(argument.getAnnotationMap().get(QueryParam.class).key());
            }
        }
        return toReturn;
    }
}

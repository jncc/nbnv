package uk.gov.nbn.data.gis.providers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.Annotations;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.Provider;

/**
 * Simple provider to allow the use of HttpServletRequest object in a mapserver method
 * @author Chris Johnson
 */
@Component
public class HttpServletRequestProvider implements Provider {

    @Override
    public boolean isProviderFor(Class<?> clazz, Annotations annotations) {
        return clazz.equals(HttpServletRequest.class);
    }

    @Override
    public HttpServletRequest provide(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, Annotations annotations) {
        return request;
    }
}

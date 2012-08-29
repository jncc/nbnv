package uk.gov.nbn.data.gis.providers;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.Provider;

/**
 *
 * @author Chris Johnson
 */
public class HttpServletRequestProvider implements Provider {

    @Override
    public boolean isProviderFor(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, List<Annotation> annotations) {
        return clazz.equals(HttpServletRequest.class);
    }

    @Override
    public HttpServletRequest provide(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, List<Annotation> annotations) {
        return request;
    }
}

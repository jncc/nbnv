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
public class HttpServletRequestProvider implements Provider<HttpServletRequest, HttpServletRequest> {

    @Override
    public HttpServletRequest providesFor(MapServiceMethod method, HttpServletRequest request, Class<?> clazz, List<Annotation> annotations) {
        return request;
    }

    @Override
    public HttpServletRequest process(HttpServletRequest providesForResponse, MapServiceMethod method, HttpServletRequest request, Class<?> clazz, List<Annotation> annotations) {
        return providesForResponse;
    }
    
}

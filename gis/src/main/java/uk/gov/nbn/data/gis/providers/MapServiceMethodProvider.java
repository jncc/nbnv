package uk.gov.nbn.data.gis.providers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.Annotations;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.Provider;

/**
 * Simple provider to allow obtain the MapServiceMethod from a provided for method
 * @author Chris Johnson
 */
@Component
public class MapServiceMethodProvider implements Provider {

    @Override
    public boolean isProviderFor(Class<?> clazz, Annotations annotations) {
        return clazz.equals(MapServiceMethod.class);
    }

    @Override
    public MapServiceMethod provide(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, Annotations annotations) {
        return method;
    }
}

package uk.gov.nbn.data.gis.providers;

import java.lang.reflect.Type;
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
    public boolean isProviderFor(Class<?> clazz, Type type, Annotations annotations) {
        return clazz.equals(MapServiceMethod.Call.class);
    }

    @Override
    public MapServiceMethod.Call provide(Class<?> clazz, Type type, MapServiceMethod.Call methodCall, Annotations annotations) {
        return methodCall;
    }
}

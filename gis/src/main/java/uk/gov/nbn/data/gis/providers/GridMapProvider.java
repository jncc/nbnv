package uk.gov.nbn.data.gis.providers;

import java.lang.reflect.Type;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.Annotations;
import uk.gov.nbn.data.gis.processor.GridMap;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.Provider;

/**
 * Simple provider to allow obtain the grid map annotation from a provided for method
 * @author Chris Johnson
 */
@Component
public class GridMapProvider implements Provider {

    @Override
    public boolean isProviderFor(Class<?> clazz, Type type, Annotations annotations) {
        return clazz.equals(GridMap.class);
    }

    @Override
    public GridMap provide(Class<?> clazz, Type type, MapServiceMethod method, HttpServletRequest request, Annotations annotations) {
        return method.getUnderlyingMapMethod().getAnnotation(GridMap.class);
    }
}

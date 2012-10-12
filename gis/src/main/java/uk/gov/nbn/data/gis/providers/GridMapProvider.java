package uk.gov.nbn.data.gis.providers;

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
    public boolean isProviderFor(Class<?> clazz, Annotations annotations) {
        return clazz.equals(GridMap.class);
    }

    @Override
    public GridMap provide(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, Annotations annotations) {
        return method.getUnderlyingMapMethod().getAnnotation(GridMap.class);
    }
}

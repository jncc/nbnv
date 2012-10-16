package uk.gov.nbn.data.gis.providers;

import java.lang.reflect.Type;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.Annotations;
import uk.gov.nbn.data.gis.processor.GridMap;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.Provider;

/**
 * The following provider will return the grid mapping layer which was 
 * requested from the grid map.
 * @author Chris Johnson
 */
@Component
public class GridMapLayerProvider implements Provider {
    @Autowired GridMapProvider gridMapProvider;
    
    @Override
    public boolean isProviderFor(Class<?> clazz, Type type, Annotations annotations) {
        return clazz.equals(GridMap.GridLayer.class);
    }

    @Override
    public GridMap.GridLayer provide(Class<?> clazz, Type type, MapServiceMethod method, HttpServletRequest request, Annotations annotations) {
        return getResolution(request.getParameter("resolution"), gridMapProvider.provide(clazz, type, method, request, annotations));
    }
    
    public static GridMap.GridLayer getResolution(String resolution, GridMap gridMapProperties) {
        //Work out which layer to use. Either one requested or this Grid maps default
        String resolutionToUse = (resolution != null) ? resolution : gridMapProperties.defaultLayer();
        //Find the layer which corresponds to this resolution
        for(GridMap.GridLayer currLayer : gridMapProperties.layers()) {
            if(resolutionToUse.equals(currLayer.name())) {
                return currLayer;
            }
        }
        throw new IllegalArgumentException("This map service does not support the resolution " + resolutionToUse);
    }
}

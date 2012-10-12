package uk.gov.nbn.data.gis.interceptors;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.GridMap;
import uk.gov.nbn.data.gis.processor.Interceptor;
import uk.gov.nbn.data.gis.processor.Intercepts;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;

/**
 *
 * @author Christopher Johnson
 */
@Component
@Interceptor
public class GridMapLegendInterceptor {

    @Intercepts(MapServiceMethod.Type.LEGEND)
    public Map<String, String[]> processRequestParameters(GridMap.GridLayer layer) {
        Map<String, String[]> toReturn = new HashMap<String, String[]>();
        toReturn.put("SERVICE", new String[]{"WMS"});
        toReturn.put("VERSION", new String[]{"1.1.1"});
        toReturn.put("REQUEST", new String[]{"GetLegendGraphic"});
        toReturn.put("TRANSPARENT", new String[]{"true"});
        toReturn.put("FORMAT", new String[]{"image/png"});
        toReturn.put("LAYER", new String[]{ layer.layer() });
        return toReturn;
    }
}

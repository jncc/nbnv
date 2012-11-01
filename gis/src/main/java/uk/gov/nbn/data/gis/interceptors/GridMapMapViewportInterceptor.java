package uk.gov.nbn.data.gis.interceptors;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.interceptors.GridMapRequestFactory.GridMapRequest;
import uk.gov.nbn.data.gis.processor.GridMap;
import uk.gov.nbn.data.gis.processor.Interceptor;
import uk.gov.nbn.data.gis.processor.Intercepts;
import uk.gov.nbn.data.gis.processor.MapServiceMethod.Type;
import uk.gov.nbn.data.gis.providers.annotations.DefaultValue;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.org.nbn.nbnv.api.model.BoundingBox;

@Component
@Interceptor
public class GridMapMapViewportInterceptor {
    @Autowired GridMapRequestFactory helper;
    
    @Intercepts(Type.MAP)
    public Map<String, String[]> processRequestParameters(
                    GridMap gridMapProperties, 
                    GridMap.GridLayer layer,
                    @QueryParam(key="imagesize", validation="1[0-5]|[1-9]") @DefaultValue("10") String imagesizeStr,
                    @QueryParam(key="feature") String featureId,
                    @QueryParam(key="nationalextent") String nationExtent) {
        Map<String, String[]> toReturn = new HashMap<String,String[]>();
        BoundingBox featureToFocusOn = helper.getFeatureToFocusOn(featureId, nationExtent, gridMapProperties);
        GridMapRequest request = helper.getGridMapRequest(featureToFocusOn, layer.resolution(), Integer.parseInt(imagesizeStr));
        
        if(!request.isValidRequest()) {
            throw new IllegalArgumentException("It is not possible to create an image for the given parameters.");
        }
        
        toReturn.put("SRS",     new String[]{ featureToFocusOn.getEpsgCode() });
        toReturn.put("HEIGHT",  new String[]{ Integer.toString(request.getHeight()) });
        toReturn.put("WIDTH",   new String[]{ Integer.toString(request.getWidth()) });
        toReturn.put("BBOX", new String[] {   request.getBBox()});
        return toReturn;
    }
}

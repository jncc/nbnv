package uk.gov.nbn.data.gis.interceptors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.gov.nbn.data.gis.processor.AtlasGrade;
import uk.gov.nbn.data.gis.processor.Interceptor;
import uk.gov.nbn.data.gis.processor.Intercepts;
import uk.gov.nbn.data.gis.processor.MapServiceMethod.Type;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;

/**
 * The following interceptor will determine which layers should be requested in 
 * the wms request. This is a combination of requested background layers and the 
 * requested (or defaulted) background layer
 * @author Administrator
 */
@Component
@Interceptor
public class AtlasGradeMapLayersInterceptor {
   
    @Intercepts(Type.MAP)
    public Map<String, String[]> processRequestParameters(
            AtlasGrade atlasGradeProperties,
            @QueryParam(key="background") List<String> requestedBackgroundLayers,
            AtlasGrade.Layer layer) {
        Map<String, String[]> toReturn = new HashMap<String,String[]>();
        
        List<String> layersToRequest = new ArrayList<String>(
                getBackgroundLayersToRequest(atlasGradeProperties, requestedBackgroundLayers));       
        layersToRequest.add(layer.layer()); //add the resolution layer
        
        toReturn.put("LAYERS",  new String[]{ StringUtils.collectionToCommaDelimitedString(layersToRequest) });
        return toReturn;
    }
    
    private List<String> getBackgroundLayersToRequest(AtlasGrade atlasGradeProperties, List<String> requestedBackgroundLayers) {
        List<String> supportedBackgroundLayers = Arrays.asList(atlasGradeProperties.backgrounds()); //get the valid background layers as a list
        List<String> backgroundLayers = (requestedBackgroundLayers != null)     //get the requested background layers
                ? requestedBackgroundLayers 
                : Arrays.asList(atlasGradeProperties.defaultBackgrounds()); 
        
        //check the requested list is valid
        for(String currRequestedLayer : backgroundLayers) {
            if(!supportedBackgroundLayers.contains(currRequestedLayer)) {
                throw new IllegalArgumentException("The background layer " + currRequestedLayer + 
                        " was requested but is not valid. Valid layers are " + supportedBackgroundLayers);
            }
        }   
        return backgroundLayers;
    }
}

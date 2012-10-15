package uk.gov.nbn.data.gis.interceptors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.gov.nbn.data.gis.processor.GridMap;
import uk.gov.nbn.data.gis.processor.GridMap.Layer;
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
public class GridMapMapLayersInterceptor {
   
    @Intercepts(Type.MAP)
    public Map<String, String[]> processRequestParameters(
            GridMap gridMapProperties,
            @QueryParam(key="background") List<String> requestedBackgroundLayers,
            @QueryParam(key="overlay") List<String> requestedOverlayLayers,
            GridMap.GridLayer layer) {
        Map<String, String[]> toReturn = new HashMap<String,String[]>();
        
        List<String> layersToRequest = new ArrayList<String>();
        
        layersToRequest.addAll(getLayersToRequest(requestedBackgroundLayers, gridMapProperties.backgrounds(), gridMapProperties.defaultBackgrounds()));
        layersToRequest.add(layer.layer()); //add the resolution layer
        layersToRequest.addAll(getLayersToRequest(requestedOverlayLayers, gridMapProperties.overlays(), gridMapProperties.defaultOverlays()));
        
        toReturn.put("LAYERS",  new String[]{ StringUtils.collectionToCommaDelimitedString(layersToRequest) });
        return toReturn;
    }
    
    private List<String> getLayersToRequest(List<String> requestedBackgroundLayers, Layer[] validLayers, String[] defaultLayers) {
        List<Layer> supportedBackgroundLayers = Arrays.asList(validLayers); //get the valid background layers as a list
        List<String> backgroundLayers = (requestedBackgroundLayers != null)     //get the requested background layers
                ? requestedBackgroundLayers 
                : Arrays.asList(defaultLayers); 
        
        List<Layer> toReturn = new ArrayList<Layer>();
        //check the requested list is valid
        for(String currRequestedLayer : backgroundLayers) {
            toReturn.add(getBackground(currRequestedLayer, supportedBackgroundLayers));
        }   
        return getWMSLayersFromLayerList(toReturn);
    }
    
    private static Layer getBackground(String name, List<Layer> toFindIn) {
        for(Layer currBackground : toFindIn) {
            if(currBackground.name().equals(name)) {
                return currBackground;
            }
        }
        throw new IllegalArgumentException("The background layer " + name + 
                        " was requested but is not valid.");
    }
    
    private static List<String> getWMSLayersFromLayerList(List<Layer> layerList) {
        List<String> toReturn = new ArrayList<String>();
        for(Layer currBackground : layerList) {
            toReturn.add(currBackground.layer());
        }
        return toReturn;
    } 
}

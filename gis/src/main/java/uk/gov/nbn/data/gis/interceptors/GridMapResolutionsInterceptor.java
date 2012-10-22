package uk.gov.nbn.data.gis.interceptors;

import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.GridMap;
import uk.gov.nbn.data.gis.processor.Interceptor;
import uk.gov.nbn.data.gis.processor.Intercepts;
import uk.gov.nbn.data.gis.processor.MapServiceMethod.Type;
import uk.gov.nbn.data.gis.processor.Response;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.org.nbn.nbnv.api.model.BoundingBox;

/**
 * The following interceptor will handle resolution requests and produce a
 * JSON object response whose keys are the zoom levels for the grid map
 * and values are arrays of the valid resolutions for the respective zoom level
 * @author Christopher Johnson
 */
@Component
@Interceptor
public class GridMapResolutionsInterceptor {
    @Autowired GridMapRequestFactory helper;
    
    @Intercepts(Type.RESOLUTIONS)
    public Response processRequestParameters(
            HttpServletRequest request,
            GridMap gridMapProperties,
            @QueryParam(key="feature") String featureId) throws JSONException {
        JSONObject toReturn = new JSONObject();
        
        BoundingBox featureToFocusOn = helper.getFeatureToFocusOn(featureId, gridMapProperties);
        
        for(int i=1; i<=GridMapRequestFactory.ZOOM_LEVELS; i++) {
            toReturn.put(Integer.toString(i), getAvailableResolutionListForImagesSize(
                gridMapProperties.layers(), featureToFocusOn, i
            ));
        }
        return Response.getJSONPResponse(request, toReturn);
    }
    
    private JSONArray getAvailableResolutionListForImagesSize(GridMap.GridLayer[] layers, BoundingBox featureToFocusOn, int imageSize){
        JSONArray toReturn = new JSONArray();
        for(GridMap.GridLayer currLayer : layers) {
            if(helper.getGridMapRequest(featureToFocusOn, currLayer.resolution(), imageSize).isValidRequest()) {
                toReturn.put(currLayer.name());
            }
        }
        return toReturn;
    }
}

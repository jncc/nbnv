package nbn.webmapping.params.rational;

import java.util.List;
import java.util.Map;
import nbn.webmapping.params.AbstractParameterNormaliser;
import nbn.webmapping.params.ParameterNormalisationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class RationalParameterNormaliser extends AbstractParameterNormaliser {
    private Map<String,String[]> parameterMap;

    private static final int BBOX_XMIN_POSITION = 0;
    private static final int BBOX_YMIN_POSITION = 1;
    private static final int BBOX_XMAX_POSITION = 2;
    private static final int BBOX_YMAX_POSITION = 3;
    private static final int BBOX_PARAMETER_AMOUNT = 4;

    private static final String EXTENT_PARAMETER = "bbox";

    private static final String MODE_PARAMETER = "mode";
    private static final String HABITAT_PARAMETER = "habitats";
    private static final String BOUNDARY_PARAMETER = "boundary";
    private static final String BASE_LAYER_PARAMETER = "baselayer";

    public RationalParameterNormaliser(Map<String,String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    @Override
    public JSONObject createMapOptionsJSON() throws JSONException, ParameterNormalisationException {
        JSONObject toReturn = new JSONObject();
        toReturn.put("extent", createExtentJSON());
        if(parameterMap.containsKey(BASE_LAYER_PARAMETER))
            toReturn.put("baselayer", parameterMap.get(BASE_LAYER_PARAMETER)[0]);
        return toReturn;
    }

    private JSONObject createExtentJSON() throws JSONException,ParameterNormalisationException {
        if(!parameterMap.containsKey(EXTENT_PARAMETER))
            return null;
        else {
            JSONObject toReturn = new JSONObject();
            String[] bboxParts = parameterMap.get(EXTENT_PARAMETER)[0].split(",");
            if(bboxParts.length != BBOX_PARAMETER_AMOUNT)
                throw new ParameterNormalisationException("You attempted to create a bbox, but " + BBOX_PARAMETER_AMOUNT + " parameters are required. Usage: bbox=xmin,ymin,xmax,ymax");
            toReturn.put("xmin",bboxParts[BBOX_XMIN_POSITION]);
            toReturn.put("ymin",bboxParts[BBOX_YMIN_POSITION]);
            toReturn.put("xmax",bboxParts[BBOX_XMAX_POSITION]);
            toReturn.put("ymax",bboxParts[BBOX_YMAX_POSITION]);
            return toReturn;
        }
    }

    @Override
    public JSONArray createInitialLayersJSON() throws JSONException, ParameterNormalisationException {
        JSONArray toReturn = new JSONArray();
        if(parameterMap.containsKey(MODE_PARAMETER))
            toReturn.put(createNBNSpeciesLayer());
        if(parameterMap.containsKey(HABITAT_PARAMETER))
            toReturn.put(createNBNHabitatsLayer());
        if(parameterMap.containsKey(BOUNDARY_PARAMETER))
            toReturn.put(createNBNBoundaryLayer());
        return (toReturn.length() !=0) ? toReturn : null;
    }

    private JSONObject createNBNHabitatsLayer() throws JSONException {
        JSONObject toReturn = new JSONObject();
        toReturn.put("type", "Habitat");
        toReturn.put("habitats", parameterMap.get(HABITAT_PARAMETER)[0]);
        return toReturn;
    }

    private JSONObject createNBNBoundaryLayer() throws JSONException {
        JSONObject toReturn = new JSONObject();
        toReturn.put("type", "Boundary");
        toReturn.put("boundary", parameterMap.get(BOUNDARY_PARAMETER)[0]);
        return toReturn;
    }

    private JSONObject createNBNSpeciesLayer() throws JSONException {
        JSONObject toReturn = new JSONObject();
        SpeciesLayerMode currMode = SpeciesLayerMode.getSpeciesLayerMode(parameterMap.get(MODE_PARAMETER)[0]);
        List<SpeciesLayerModeParameter<?>> requiredParams = currMode.getRequiredParameterNames();
        toReturn.put("type","Species");
        toReturn.put("mode",currMode.getModeAsString());
        for(SpeciesLayerModeParameter currParameter : requiredParams) {
            if(parameterMap.containsKey(currParameter.getQueryStringParameterName())) {
                String parameterFromQueryString = parameterMap.get(currParameter.getQueryStringParameterName())[0];
                toReturn.put(currParameter.getJSONRepresentationParameterName(),currParameter.getParameterInCorrectForm(parameterFromQueryString));
            }
            else if(!currParameter.isOptional())
                throw new IllegalArgumentException("In order to create a " + currMode.getModeAsString() + " a " + currParameter.getQueryStringParameterName() + " parameter is needed");
        }
        return toReturn;
    }

    public static boolean canCreateInteractiveMapOptionsJSONString(Map<String,String[]> parameterMap) {
        return parameterMap.containsKey(EXTENT_PARAMETER) || parameterMap.containsKey(MODE_PARAMETER) || parameterMap.containsKey(HABITAT_PARAMETER) || parameterMap.containsKey(BOUNDARY_PARAMETER) || parameterMap.containsKey(BASE_LAYER_PARAMETER);
    }
}

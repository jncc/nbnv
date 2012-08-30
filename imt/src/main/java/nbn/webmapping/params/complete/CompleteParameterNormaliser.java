/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.params.complete;

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
public class CompleteParameterNormaliser  extends AbstractParameterNormaliser {
    private static final String INITIAL_LAYERS_PARAMETER = "nbnLayers";
    private static final String MAP_OPTIONS_PARAMETER = "map";
    private Map<String,String[]> parameterMap;

    public CompleteParameterNormaliser(Map<String,String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    @Override
    public JSONArray createInitialLayersJSON() throws JSONException, ParameterNormalisationException {
        return parameterMap.containsKey(INITIAL_LAYERS_PARAMETER) ? new JSONArray(parameterMap.get(INITIAL_LAYERS_PARAMETER)[0]) : new JSONArray();
    }

    public static boolean canCreateInteractiveMapOptionsJSONString(Map<String,String[]> parameterMap) {
       return parameterMap.containsKey(INITIAL_LAYERS_PARAMETER) || parameterMap.containsKey(MAP_OPTIONS_PARAMETER);
    }

    @Override
    public JSONObject createMapOptionsJSON() throws JSONException, ParameterNormalisationException {
        return parameterMap.containsKey(MAP_OPTIONS_PARAMETER) ? new JSONObject(parameterMap.get(MAP_OPTIONS_PARAMETER)[0]) : new JSONObject();
    }

}

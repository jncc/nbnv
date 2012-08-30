package nbn.webmapping.params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public abstract class AbstractParameterNormaliser implements ParameterNormaliser {

    public String createInteractiveMapOptionsJSONString() throws ParameterNormalisationException {
        try {
            JSONObject toReturn = new JSONObject();            
            putObject(toReturn, "map", createMapOptionsJSON());
            putObject(toReturn, "initialLayers", createInitialLayersJSON());
            return toReturn.toString();
        }
        catch(JSONException jsonex) {
            throw new ParameterNormalisationException("A JSON exception occured whilst converting the Parameter Map into a JSON Object needed for creating the Interactive Map Client", jsonex);
        }
    }

    public abstract JSONObject createMapOptionsJSON() throws JSONException, ParameterNormalisationException;
    public abstract JSONArray createInitialLayersJSON() throws JSONException, ParameterNormalisationException;

    private <T extends Object> void putObject(JSONObject objToAddTo, String key, T value) throws JSONException {
        objToAddTo.put(key,value);
    }
}

package nbn.webmapping.params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class NullParameterNormaliser extends AbstractParameterNormaliser {
    public @Override JSONArray createInitialLayersJSON() throws JSONException, ParameterNormalisationException {
        return null;
    }

    @Override
    public JSONObject createMapOptionsJSON() throws JSONException, ParameterNormalisationException {
        return null;
    }
}

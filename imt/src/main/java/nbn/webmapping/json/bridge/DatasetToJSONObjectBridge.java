package nbn.webmapping.json.bridge;

import nbn.common.bridging.AbstractNameLookupableBridge;
import nbn.common.bridging.BridgingException;
import nbn.common.dataset.Dataset;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author	    :- Christopher Johnson
 * @date		    :- 07-Dec-2010
 * @description	    :-
 */
public abstract class DatasetToJSONObjectBridge<T extends Dataset> extends AbstractNameLookupableBridge<T, JSONObject> {
    public JSONObject convert(T toConvert) throws BridgingException{
        try {
            JSONObject toReturn = new JSONObject();
            toReturn.put("name", toConvert.getDatasetTitle());
            toReturn.put("organisation", toConvert.getDatasetProvider().getOrganisationName());
            toReturn.put("datasetKey", toConvert.getDatasetKey());
            return toReturn;
        } catch (JSONException jsone) {
            throw new BridgingException("A JSON Exception has occured", jsone);
        }
    }

    @Override
    public String getLookupableName(T toName) {
        return toName.getDatasetKey();
    }
}

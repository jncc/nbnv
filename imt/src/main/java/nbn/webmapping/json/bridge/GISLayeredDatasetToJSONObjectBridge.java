package nbn.webmapping.json.bridge;

import nbn.common.dataset.GISLayeredDataset;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author	    :- Christopher Johnson
 * @date		    :- 07-Dec-2010
 * @description	    :-
 */
public class GISLayeredDatasetToJSONObjectBridge<T extends GISLayeredDataset> extends DatasetToJSONObjectBridge<T> {
    public  @Override JSONObject convert(T toConvert) {
        try {
            JSONObject toReturn = super.convert(toConvert);
            toReturn.put("gisLayerID", toConvert.getGisLayerID());
            return toReturn;
        } catch (JSONException jsone) {
            throw new RuntimeException("A JSON Exception has occured", jsone);
        }
    }
}

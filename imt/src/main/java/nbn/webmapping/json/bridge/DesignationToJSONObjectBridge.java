package nbn.webmapping.json.bridge;

import nbn.common.bridging.Bridge;
import nbn.common.bridging.BridgingException;
import nbn.common.taxon.designation.Designation;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author	    :- Christopher Johnson
 * @date            :- 18-March-2011
 * @description	    :-
 */
public class DesignationToJSONObjectBridge implements Bridge<Designation, JSONObject> {
    public JSONObject convert(Designation toConvert) throws BridgingException{
        try {
            JSONObject toReturn = new JSONObject();
            toReturn.put("name", toConvert.getName());
            toReturn.put("designationKey", toConvert.getKey());
            return toReturn;
        }
        catch (JSONException jsone) {
            throw new BridgingException("A JSON Exception has occured", jsone);
        }
    }
}

package nbn.webmapping.json.bridge;

import java.util.List;
import nbn.common.bridging.Bridge;
import org.json.JSONArray;
import org.json.JSONObject;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 29-Nov-2010
* @description	    :-
*/
public class JSONObjectListToJSONArrayBridge implements Bridge<List<JSONObject>,JSONArray>{
    public JSONArray convert(List<JSONObject> toConvert) {
	JSONArray toReturn = new JSONArray();
	for(JSONObject currJObject : toConvert)
	    toReturn.put(currJObject);
	return toReturn;
    }
}

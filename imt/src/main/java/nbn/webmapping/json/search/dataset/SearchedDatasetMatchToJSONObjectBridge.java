package nbn.webmapping.json.search.dataset;

import nbn.common.bridging.Bridge;
import nbn.common.bridging.BridgingException;
import nbn.common.dataset.Dataset;
import nbn.common.searching.SearchMatch;
import org.json.JSONException;
import org.json.JSONObject;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 29-Nov-2010
* @description	    :-
*/
public class SearchedDatasetMatchToJSONObjectBridge<T extends Dataset> implements Bridge<SearchMatch<T>,JSONObject> {
    public JSONObject convert(SearchMatch<T> toConvert) throws BridgingException{
	try {
	    JSONObject toReturn = new JSONObject();
            T match = toConvert.getMatch();
	    toReturn.put("value", toConvert.getMatchedTerm());
            toReturn.put("name", match.getDatasetTitle());
	    toReturn.put("key", match.getDatasetKey());
	    return toReturn;
	}
	catch (JSONException ex) {
	    throw new BridgingException("A JSON Exception Occured", ex);
	}
    }
}

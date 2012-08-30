package nbn.webmapping.json.search.taxon;

import nbn.common.bridging.Bridge;
import nbn.common.bridging.BridgingException;
import nbn.common.searching.SearchMatch;
import nbn.common.taxon.Taxon;
import org.json.JSONException;
import org.json.JSONObject;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 29-Nov-2010
* @description	    :-
*/
public class SearchedTaxonMatchToJSONObjectBridge<T extends Taxon> implements Bridge<SearchMatch<T>,JSONObject> {
    public JSONObject convert(SearchMatch<T> toConvert) throws BridgingException {
	try {
	    JSONObject toReturn = new JSONObject();
            T match = toConvert.getMatch();
	    toReturn.put("value", toConvert.getMatchedTerm());
	    toReturn.put("name", match.getName());
	    toReturn.put("key", match.getTaxonVersionKey());
            toReturn.put("taxonGroup", match.getTaxonGroup().toUpperCase());
            toReturn.put("authority", match.getAuthority());
            toReturn.put("taxonRank", match.getTaxonRank().toUpperCase());
	    return toReturn;
	}
	catch (JSONException ex) {
	    throw new BridgingException("A JSON Exception Occured", ex);
	}
    }
}

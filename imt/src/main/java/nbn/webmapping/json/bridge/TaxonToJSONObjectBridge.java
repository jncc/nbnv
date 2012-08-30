package nbn.webmapping.json.bridge;

import nbn.common.bridging.AbstractNameLookupableBridge;
import nbn.common.bridging.BridgingException;
import nbn.common.taxon.Taxon;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author	    :- Christopher Johnson
 * @date		    :- 07-Dec-2010
 * @description	    :-
 */
public class TaxonToJSONObjectBridge extends AbstractNameLookupableBridge<Taxon, JSONObject> {

    public JSONObject convert(Taxon toConvert) throws BridgingException{
        try {
            JSONObject toReturn = new JSONObject();
            toReturn.put("name", getName(toConvert));
            toReturn.put("scientifName", toConvert.getName());
            toReturn.put("authority", toConvert.getAuthority());
            toReturn.put("commonName", toConvert.getCommonName());
            toReturn.put("taxonGroup", toConvert.getTaxonGroup());
            toReturn.put("taxonVersionKey", toConvert.getTaxonVersionKey());
            toReturn.put("link", "http://data.nbn.org.uk/searchengine/search.jsp?searchTermHeader=" + toConvert.getName());
            return toReturn;
        } catch (JSONException jsone) {
            throw new BridgingException("A JSON Exception has occured", jsone);
        }
    }

    private String getName(Taxon toConvert) {
        if((toConvert.getCommonName() != null) && (!toConvert.getCommonName().equals(""))){
            return toConvert.getName() + " (" + toConvert.getCommonName() + ")";
        } else {
            return toConvert.getName();
        }
    }

    @Override
    public String getLookupableName(Taxon toName) {
        return toName.getTaxonVersionKey();
    }
}

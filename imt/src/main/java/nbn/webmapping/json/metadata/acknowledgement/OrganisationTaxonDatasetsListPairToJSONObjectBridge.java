
package nbn.webmapping.json.metadata.acknowledgement;

import java.util.List;
import nbn.common.organisation.Organisation;
import nbn.common.bridging.Bridge;
import nbn.common.bridging.BridgingException;
import nbn.common.bridging.ListBridge;
import nbn.common.dataset.TaxonDataset;
import nbn.common.user.User;
import nbn.common.util.Pair;
import nbn.webmapping.json.bridge.TaxonDatasetToJSONObjectBridge;
import nbn.webmapping.json.bridge.JSONObjectListToJSONArrayBridge;
import org.json.JSONException;
import org.json.JSONObject;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 10-Jan-2011
* @description	    :-
*/
public class OrganisationTaxonDatasetsListPairToJSONObjectBridge implements Bridge<Pair<Organisation,List<TaxonDataset>>,JSONObject> {
    private User privilegedUser;

    public OrganisationTaxonDatasetsListPairToJSONObjectBridge(User privilegedUser) {
        this.privilegedUser = privilegedUser;
    }

    public JSONObject convert(Pair<Organisation,List<TaxonDataset>> toConvert) throws BridgingException {
	try {
	    JSONObject toReturn = new JSONObject();
	    Organisation organisationToConvert = toConvert.getA();
            toReturn.put("name", organisationToConvert.getOrganisationName());
            toReturn.put("organisationKey", organisationToConvert.getOrganisationKey());
            toReturn.put("imageUrl", organisationToConvert.getThumbnailLogoUrl());
            ListBridge<TaxonDataset,JSONObject> datasetsListBridge = new ListBridge<TaxonDataset,JSONObject>(new TaxonDatasetToJSONObjectBridge(privilegedUser));
            toReturn.put("datasets", new JSONObjectListToJSONArrayBridge().convert(datasetsListBridge.convert(toConvert.getB())));
	    return toReturn;
	}
	catch(JSONException jsone){
	    throw new BridgingException("A JSON Exception has occured",jsone);
	}
    }
}


package nbn.webmapping.json.bridge;

import nbn.common.bridging.Bridge;
import nbn.common.bridging.BridgingException;
import nbn.common.feature.Feature;
import nbn.common.feature.GridSquare;
import nbn.common.feature.Resolution;
import nbn.common.siteboundary.SiteBoundary;
import org.json.JSONException;
import org.json.JSONObject;

public class FeatureToJSONObjectBridge implements Bridge<Feature, JSONObject> {
    public void addAdditionalInformation(SiteBoundary site, JSONObject toReturn) throws JSONException {
        toReturn.put("link", "../siteInfo/siteSpeciesGroups.jsp?allDs=1&useIntersects=1&siteKey=" + site.getId());
    }

    public void addAdditionalInformation(GridSquare gridSquareFeature, JSONObject toReturn) throws JSONException {
        if(gridSquareFeature.getResolution() == Resolution._10km)
            toReturn.put("link", "../gridSquares/10kmSquareSpeciesGroups.jsp?parentSq=" + gridSquareFeature.getGridRef() + "&allDs=1");
    }

    public JSONObject convert(Feature toConvert) throws BridgingException {
        try {
            JSONObject toReturn = new JSONObject();
            toReturn.put("name", toConvert.getName());
            if(toConvert instanceof GridSquare)
                addAdditionalInformation((GridSquare)toConvert,toReturn);
            else if(toConvert instanceof SiteBoundary)
                addAdditionalInformation((SiteBoundary)toConvert,toReturn);
            return toReturn;
        }
        catch (JSONException jsonEx) {
            throw new BridgingException("A JSON Exception has occurred", jsonEx);
        }
    }
}


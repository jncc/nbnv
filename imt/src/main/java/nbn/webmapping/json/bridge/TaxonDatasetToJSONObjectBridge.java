package nbn.webmapping.json.bridge;

import java.sql.SQLException;
import nbn.common.dataset.TaxonDataset;
import nbn.common.dataset.TaxonDatasetContext;
import nbn.common.dataset.privileges.Privileges;
import nbn.common.user.User;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author	    :- Christopher Johnson
 * @date		    :- 07-Dec-2010
 * @description	    :-
 */
public class TaxonDatasetToJSONObjectBridge extends DatasetToJSONObjectBridge<TaxonDataset>{
    private User privilegedUser;
    private boolean includeMetadata;

    public TaxonDatasetToJSONObjectBridge() {
        this(false);
    }

    public TaxonDatasetToJSONObjectBridge(boolean includeMetadata) {
        this(null,includeMetadata);
    }

    public TaxonDatasetToJSONObjectBridge(User privilegedUser) {
        this(privilegedUser, false);
    }

    public TaxonDatasetToJSONObjectBridge(User privilegedUser, boolean includeMetadata) {
        this.privilegedUser = privilegedUser;
        this.includeMetadata = includeMetadata;
    }

    public @Override JSONObject convert(TaxonDataset toConvert) {
        try {
            JSONObject toReturn = super.convert(toConvert);
            toReturn.put("datasetResolution",toConvert.getMaxResolution().getName());
            toReturn.put("recordCount",toConvert.getAmountOfRecords());
            toReturn.put("link","http://data.nbn.org.uk/datasetInfo/taxonDataset.jsp?dsKey="+ toConvert.getDatasetKey());

            if(privilegedUser != null) { //is this a privilegedUser???
                Privileges privileges = toConvert.getPrivileges(privilegedUser);

                toReturn.put("userResolution",privileges.getBlurLevel().getName());
                toReturn.put("sensitiveAccess",privileges.hasViewSensitive());
                toReturn.put("downloadRawData",privileges.hasDownloadRawData());
                toReturn.put("viewAttributes",privileges.hasViewAttributes());
                toReturn.put("viewRecorder",privileges.hasViewRecorder());
                toReturn.put("hasFullAccess", toConvert.getMaxResolution().equals(privileges.getBlurLevel().getResolutionBlurredTo()));
            }

            if(includeMetadata) {
                toReturn.put("description", StringEscapeUtils.unescapeHtml(toConvert.getDescription()));
                toReturn.put("captureMethod", toConvert.getDataCaptureMethod());
                toReturn.put("purpose", toConvert.getPurpose());
                toReturn.put("geographicalCoverage", toConvert.getGeographicalCoverage());
                toReturn.put("temporalCoverage", toConvert.getTemporalCoverage());
                toReturn.put("dataQuality", toConvert.getDataQuality());
                toReturn.put("updateFrequency", toConvert.getUpdateFrequency());
                toReturn.put("accessConstraint", toConvert.getAccessConstraint());
                toReturn.put("useConstraint", toConvert.getUseConstraint());
                toReturn.put("lastEdited", toConvert.getLastEdited());
                toReturn.put("additionalInformation", toConvert.getAdditionalInformation());
                toReturn.put("dateUploaded", toConvert.getDateUploaded());
                toReturn.put("amountOfSamples", toConvert.getAmountOfSamples());
                toReturn.put("amountOfSites", toConvert.getAmountOfSites());
                toReturn.put("amountOfSpecies", toConvert.getAmountOfSpecies());
                toReturn.put("captureEndYear", toConvert.getCaptureEndYear());
                toReturn.put("captureStarYear", toConvert.getCaptureStartYear());
            }

            if(toConvert instanceof TaxonDatasetContext)
                toReturn.put("contextRecordCount", ((TaxonDatasetContext)toConvert).getRecordCount());
            return toReturn;
        }
        catch (JSONException jsone) {
            throw new RuntimeException("A JSON Exception has occured",jsone);
        }
        catch (SQLException sql) {
            throw new RuntimeException("An SQL Exception has occured",sql);
        }
    }
}

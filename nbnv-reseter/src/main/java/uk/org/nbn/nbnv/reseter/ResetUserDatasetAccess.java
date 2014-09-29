package uk.org.nbn.nbnv.reseter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.core.OperationalUserAccessRequestMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalUserTaxonObservationAccessMapper;
import uk.org.nbn.nbnv.api.model.UserAccessRequest;
import uk.org.nbn.nbnv.api.model.meta.AccessRequestJSON;
import uk.org.nbn.nbnv.reseter.utils.AccessRequestUtils;

/**
 *
 * @author cjohn
 */
@Component
public class ResetUserDatasetAccess {
    @Autowired
    OperationalUserAccessRequestMapper oUserAccessRequestMapper;
    @Autowired
    OperationalUserTaxonObservationAccessMapper oUserTaxonObservationAccessMapper;
    @Autowired
    AccessRequestUtils accessRequestUtils;
        
    /**
     * Strip any access given by a User Access Request
     *
     * @param id A filter ID identifying a request
     *
     * @return If the operation was successful or not
     *
     * @throws IOException An Error occurred mapping a JSON string to an object
     */
    public boolean resetAllAccess(String dataset) throws IOException {
        oUserTaxonObservationAccessMapper.removeAllUserAccessForDataset(dataset);
        List<UserAccessRequest> uars = oUserAccessRequestMapper.getGrantedRequestsByDataset(dataset);

        for (UserAccessRequest req : uars) {
            giveAccess(req);
        }
        return true;

    }
    
    /**
     * Apply access rules given by a particular User Access Request
     *
     * @param uar The request to be actioned
     *
     * @return If the operation was a success or not
     *
     * @throws IOException An Error occurred mapping a JSON string to an object
     */
    private boolean giveAccess(UserAccessRequest uar) throws IOException {
        AccessRequestJSON accessRequest = parseJSON(uar.getFilter().getFilterJSON());

        List<String> species = accessRequestUtils.createSpeciesList(accessRequest);
        List<String> datasets = new ArrayList<String>();
        datasets.add(uar.getDatasetKey());
        oUserTaxonObservationAccessMapper.addUserAccess(uar.getUser(), accessRequest.getYear().getStartYear(), accessRequest.getYear().getEndYear(), datasets, species, accessRequest.getSpatial().getMatch(), accessRequest.getSpatial().getFeature(), (accessRequest.getSensitive().equals("sans") ? true : false), accessRequest.getTaxon().getDesignation(), accessRequest.getTaxon().getOutput(), accessRequest.getTaxon().getOrgSuppliedList(), accessRequest.getSpatial().getGridRef(), "");
        return true;
    }
    
    /**
     * Create an AccessRequestJSON object from a JSON string representation
     *
     * @param json The JSON represented as a string
     *
     * @return The AccessRequestJSON object represented by the input string
     *
     * @throws IOException An Error occurred mapping a JSON string to an object
     */
    private AccessRequestJSON parseJSON(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, AccessRequestJSON.class);
    }
}

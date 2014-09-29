/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv.reseter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationAccessRequestMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationTaxonObservationAccessMapper;
import uk.org.nbn.nbnv.api.model.OrganisationAccessRequest;
import uk.org.nbn.nbnv.api.model.meta.AccessRequestJSON;
import uk.org.nbn.nbnv.api.utils.AccessRequestUtils;

/**
 *
 * @author cjohn
 */
@Component
public class ResetOrganisationDatasetAccess {
    @Autowired OperationalOrganisationAccessRequestMapper oOrganisationAccessRequestMapper;
    @Autowired OperationalOrganisationTaxonObservationAccessMapper oOrganisationTaxonObservationAccessMapper;
    @Autowired
    AccessRequestUtils accessRequestUtils;
        
    public boolean resetAllAccess(String dataset) throws IOException {
        oOrganisationTaxonObservationAccessMapper.removeAllOrganisationAccessForDataset(dataset);
        List<OrganisationAccessRequest> uars = oOrganisationAccessRequestMapper.getGrantedRequestsByDataset(dataset);
        
        for (OrganisationAccessRequest req : uars) {
            giveAccess(req);
        }
        return true;
        
    }
    
    /**
     * Give access according to a given request
     * 
     * @param uar The request to be actioned
     * @return A boolean denoting the success of this operation
     * @throws IOException 
     */
    private boolean giveAccess(OrganisationAccessRequest uar) throws IOException {
        AccessRequestJSON accessRequest = parseJSON(uar.getFilter().getFilterJSON());

        List<String> species = accessRequestUtils.createSpeciesList(accessRequest);        
        List<String> datasets = new ArrayList<String>();
        datasets.add(uar.getDatasetKey());
        oOrganisationTaxonObservationAccessMapper.addOrganisationAccess(uar.getOrganisation(), accessRequest.getYear().getStartYear(), accessRequest.getYear().getEndYear(), datasets, species, accessRequest.getSpatial().getMatch(), accessRequest.getSpatial().getFeature(), (accessRequest.getSensitive().equals("sans") ? true : false), accessRequest.getTaxon().getDesignation(), accessRequest.getTaxon().getOutput(), accessRequest.getTaxon().getOrgSuppliedList(), accessRequest.getSpatial().getGridRef(), "");
        return true;
    }
    
    /**
     * Parse JSON object into an AccessRequestJSON object
     * 
     * @param json Input string representation of JSON object
     * @return An AccessRequestJSON representation of the json string
     * @throws IOException 
     */
    private AccessRequestJSON parseJSON(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, AccessRequestJSON.class);
    }
    
}

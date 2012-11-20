/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.io.IOException;
import java.sql.Date;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalUserAccessRequestMapper;
import uk.org.nbn.nbnv.api.model.TaxonObservationFilter;
import uk.org.nbn.nbnv.api.model.meta.AccessRequestJSON;

/**
 *
 * @author Paul Gilbertson
 */
@Component
@Path("/user/userAccesses")
public class UserAccessRequestResource {
    @Autowired OperationalTaxonObservationFilterMapper oTaxonObservationFilterMapper;
    @Autowired OperationalUserAccessRequestMapper oUserAccessRequestMapper;
    
    @PUT
    @Path("/requests")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createRequest(String json) throws IOException {
        AccessRequestJSON accessRequest = parseJSON(json);
        
        TaxonObservationFilter filter = new TaxonObservationFilter();
        filter.setFilterJSON(json);
        filter.setFilterText("Test");
    
        oTaxonObservationFilterMapper.createFilter(filter);
        
        for (String datasetKey : accessRequest.getDatasetselection().getDatasets()) {
            oUserAccessRequestMapper.createRequest(filter.getId(), 1, datasetKey, accessRequest.getRequest().getRole(), accessRequest.getRequest().getPurpose(), accessRequest.getRequest().getDetails(), new Date(new java.util.Date().getTime()));
        }
        return Response.ok("success").build();
    }
    
    private AccessRequestJSON parseJSON(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, AccessRequestJSON.class);
    }
}

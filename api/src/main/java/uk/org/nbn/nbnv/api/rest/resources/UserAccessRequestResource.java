/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.io.IOException;
import java.lang.String;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalUserAccessRequestMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonObservationMapper;
import uk.org.nbn.nbnv.api.model.TaxonDatasetWithQueryStats;
import uk.org.nbn.nbnv.api.model.TaxonObservationFilter;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.UserAccessRequest;
import uk.org.nbn.nbnv.api.model.meta.AccessRequestJSON;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenAccessRequestAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.utils.AccessRequestUtils;

/**
 *
 * @author Paul Gilbertson
 */
@Component
@Path("/user/userAccesses")
public class UserAccessRequestResource extends AbstractResource {
    @Autowired OperationalTaxonObservationFilterMapper oTaxonObservationFilterMapper;
    @Autowired OperationalUserAccessRequestMapper oUserAccessRequestMapper;
    
    @PUT
    @Path("/requests")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createRequest(@TokenUser(allowPublic=false) User user, String json) throws IOException {
        AccessRequestJSON accessRequest = parseJSON(json);

        // Fail if this is an organisation request
        if (accessRequest.getReason().getOrganisationID() > -1) {
            return Response.serverError().build();
        }
        
        if (!accessRequest.getSpatial().isAll() && !accessRequest.getDataset().isSecret()) {
            accessRequest.setSensitive("ns");
        }

        TaxonObservationFilter filter = AccessRequestUtils.createFilter(json, accessRequest);
        List<String> species = AccessRequestUtils.createSpeciesList(accessRequest);        
        List<String> datasets = AccessRequestUtils.createDatasetList(accessRequest, species, user);
        
        
        for (String datasetKey : datasets) {
            oTaxonObservationFilterMapper.createFilter(filter);
            oUserAccessRequestMapper.createRequest(filter.getId(), user.getId(), datasetKey, accessRequest.getReason().getPurpose(), accessRequest.getReason().getDetails(), new Date(new java.util.Date().getTime()));
        }

        return Response.ok("success").build();
    }
    
    @GET
    @Path("/requests")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getRequests(@TokenUser(allowPublic=false) User user) throws IOException {
        return oUserAccessRequestMapper.getUserRequests(user.getId());
    }
    
    @GET
    @Path("/requests/granted")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getGrantedRequests(@TokenUser(allowPublic=false) User user) throws IOException {
        return oUserAccessRequestMapper.getGrantedUserRequests(user.getId());
    }

    @GET
    @Path("/requests/admin")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getRequestsForAdmin(@TokenUser(allowPublic=false) User user) {
        return oUserAccessRequestMapper.getAdminableRequests(user.getId());
    }
    
    @GET
    @Path("/requests/admin/pending")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getRequestsPendingForAdmin(@TokenUser(allowPublic=false) User user) {
        return oUserAccessRequestMapper.getPendingAdminableRequests(user.getId());
    }

    @GET
    @Path("/requests/admin/granted")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getRequestsGrantedForAdmin(@TokenUser(allowPublic=false) User user) {
        return oUserAccessRequestMapper.getGrantedAdminableRequests(user.getId());
    }

    @GET
    @Path("/requests/admin/denied")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getRequestsDeniedForAdmin(@TokenUser(allowPublic=false) User user) {
        return oUserAccessRequestMapper.getDeniedAdminableRequests(user.getId());
    }

    @GET
    @Path("/requests/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserAccessRequest getRequest(@TokenAccessRequestAdminUser(path="id") User user
            , @PathParam("id") int filterID) {
        return oUserAccessRequestMapper.getRequest(filterID);
    }
    
    @POST
    @Path("/requests/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRequest(@TokenAccessRequestAdminUser(path="id") User user
            , @PathParam("id") int filterID
            , @FormParam("action") String action
            , @FormParam("reason") String reason
            , @FormParam("expires") @DefaultValue("") String expires) throws ParseException {
        if ("accept".equalsIgnoreCase(action)) {
            return acceptRequest(filterID, reason, expires);
        } else if ("deny".equalsIgnoreCase(action)) {
            return denyRequest(filterID, reason);
        } else {
            return Response.serverError().build();
        }
    }
       
    private Response acceptRequest(int filterID, String reason, String expires) throws ParseException {
        if (expires.isEmpty()) {
            oUserAccessRequestMapper.acceptRequest(filterID, reason, new Date(new java.util.Date().getTime()));
            return Response.ok("success").build();
        } else {
            DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
            java.util.Date expiresDate = df.parse(expires);
            oUserAccessRequestMapper.acceptRequestWithExpires(filterID, reason, new Date(new java.util.Date().getTime()), new Date(expiresDate.getTime()));
            return Response.ok("success").build();
        }
    }

    private Response denyRequest(int filterID, String reason) {
        oUserAccessRequestMapper.denyRequest(filterID, reason, new Date(new java.util.Date().getTime()));
        return Response.ok("success").build();
    }

    private AccessRequestJSON parseJSON(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, AccessRequestJSON.class);
    }
}

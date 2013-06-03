/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationAccessRequestMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationTaxonObservationAccessMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.OrganisationAccessRequest;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.TaxonObservationFilter;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.UserAccessRequest;
import uk.org.nbn.nbnv.api.model.meta.AccessRequestJSON;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenAccessRequestAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenOrganisationJoinRequestUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenOrganisationUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.utils.AccessRequestUtils;

/**
 *
 * @author Paul Gilbertson
 */
@Component
@Path("/organisation/organisationAccesses")
public class OrganisationAccessRequestResource extends AbstractResource {
    @Autowired OperationalTaxonObservationFilterMapper oTaxonObservationFilterMapper;
    @Autowired OperationalOrganisationAccessRequestMapper oOrganisationAccessRequestMapper;
    @Autowired OperationalOrganisationTaxonObservationAccessMapper oOrganisationTaxonObservationAccessMapper;
    @Autowired OrganisationMapper organisationMapper;
    @Autowired AccessRequestUtils accessRequestUtils;
    
    
    @PUT
    @Path("/requests")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createRequest(@TokenUser(allowPublic=false) User user, String json) throws IOException {
        AccessRequestJSON accessRequest = parseJSON(json);

        // Fail if this is an user request
        if (accessRequest.getReason().getOrganisationID() == -1) {
            return Response.serverError().build();
        }
        
        Organisation org = organisationMapper.selectByID(accessRequest.getReason().getOrganisationID());
        
        if (!accessRequest.getSpatial().isAll() && !accessRequest.getDataset().isSecret()) {
            accessRequest.setSensitive("ns");
        }

        TaxonObservationFilter filter = accessRequestUtils.createFilter(json, accessRequest);
        List<String> species = accessRequestUtils.createSpeciesList(accessRequest);        
        List<String> datasets = accessRequestUtils.createDatasetList(accessRequest, species, org);
        
        for (String datasetKey : datasets) {
            oTaxonObservationFilterMapper.createFilter(filter);
            oOrganisationAccessRequestMapper.createRequest(filter.getId(), org.getId(), datasetKey, accessRequest.getReason().getPurpose(), accessRequest.getReason().getDetails(), new Date(new java.util.Date().getTime()));
        }

        return Response.ok("success").build();
    }
    
    @PUT
    @Path("/requests/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response editRequest(@TokenAccessRequestAdminUser(path="id") User user, @PathParam("id") int filterID, String json) throws IOException {
        AccessRequestJSON accessRequest = parseJSON(json);

        // Fail if this is an user request
        if (accessRequest.getReason().getOrganisationID() == -1) {
            return Response.serverError().build();
        }
        TaxonObservationFilter filter = accessRequestUtils.createFilter(json, accessRequest);
        oTaxonObservationFilterMapper.editFilter(filterID, filter.getFilterText(), filter.getFilterJSON());

        return Response.ok("success").build();
    }

    @GET
    @Path("/{id}/requests")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationAccessRequest> getRequests(@TokenOrganisationUser(path="id", roles = OrganisationMembership.Role.administrator) User user, @PathParam("id") int orgID) throws IOException {
        return oOrganisationAccessRequestMapper.getOrganisationRequests(orgID);
    }
    
    @GET
    @Path("{id}/requests/granted")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationAccessRequest> getGrantedRequests(@TokenOrganisationUser(path="id", roles = OrganisationMembership.Role.administrator) User user, @PathParam("id") int orgID) throws IOException {
        return oOrganisationAccessRequestMapper.getGrantedOrganisationRequests(orgID);
    }

    @GET
    @Path("/requests/admin")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationAccessRequest> getRequestsForAdmin(@TokenUser(allowPublic=false) User user) {
        return oOrganisationAccessRequestMapper.getAdminableRequests(user.getId());
    }
    
    @GET
    @Path("/requests/admin/pending")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationAccessRequest> getRequestsPendingForAdmin(@TokenUser(allowPublic=false) User user) {
        return oOrganisationAccessRequestMapper.getPendingAdminableRequests(user.getId());
    }

    @GET
    @Path("/requests/admin/granted")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationAccessRequest> getRequestsGrantedForAdmin(@TokenUser(allowPublic=false) User user) {
        return oOrganisationAccessRequestMapper.getGrantedAdminableRequests(user.getId());
    }

    @GET
    @Path("/requests/admin/denied")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationAccessRequest> getRequestsDeniedForAdmin(@TokenUser(allowPublic=false) User user) {
        return oOrganisationAccessRequestMapper.getDeniedAdminableRequests(user.getId());
    }

    @GET
    @Path("/requests/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public OrganisationAccessRequest getRequest(@TokenAccessRequestAdminUser(path="id") User user, @PathParam("id") int filterID) {
        return oOrganisationAccessRequestMapper.getRequest(filterID);
    }
    
    @POST
    @Path("/requests/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRequest(@TokenAccessRequestAdminUser(path="id") User user
            , @PathParam("id") int filterID
            , @FormParam("action") String action
            , @FormParam("reason") String reason
            , @FormParam("expires") @DefaultValue("") String expires) throws ParseException {
        if ("grant".equalsIgnoreCase(action)) {
            return acceptRequest(filterID, reason, expires);
        } else if ("deny".equalsIgnoreCase(action)) {
            return denyRequest(filterID, reason);
        } else if ("close".equalsIgnoreCase(action)) {
            return closeRequest(filterID, reason);
        } else if ("revoke".equalsIgnoreCase(action)) {
            return revokeRequest(filterID, reason);
        } else {
            return Response.serverError().build();
        }
    }
       
    private Response acceptRequest(int filterID, String reason, String expires) throws ParseException {
        if (expires.isEmpty()) {
            oOrganisationAccessRequestMapper.acceptRequest(filterID, reason, new Date(new java.util.Date().getTime()));
            return Response.status(Response.Status.OK).entity("{}").build();
        } else {
            DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
            java.util.Date expiresDate = df.parse(expires);
            oOrganisationAccessRequestMapper.acceptRequestWithExpires(filterID, reason, new Date(new java.util.Date().getTime()), new Date(expiresDate.getTime()));
            return Response.status(Response.Status.OK).entity("{}").build();
        }
    }

    private Response denyRequest(int filterID, String reason) {
        oOrganisationAccessRequestMapper.denyRequest(filterID, reason, new Date(new java.util.Date().getTime()));
        return Response.status(Response.Status.OK).entity("{}").build();
    }

    private Response closeRequest(int filterID, String reason) {
        oOrganisationAccessRequestMapper.closeRequest(filterID, reason, new Date(new java.util.Date().getTime()));
        return Response.status(Response.Status.OK).entity("{}").build();
    }

    private Response revokeRequest(int filterID, String reason) {
        oOrganisationAccessRequestMapper.revokeRequest(filterID, reason, new Date(new java.util.Date().getTime()));
        return Response.status(Response.Status.OK).entity("{}").build();
    }

    private boolean stripAccess(int id) throws IOException {
        OrganisationAccessRequest uar = oOrganisationAccessRequestMapper.getRequest(id);
        AccessRequestJSON accessRequest = parseJSON(uar.getFilter().getFilterJSON());

        List<String> species = accessRequestUtils.createSpeciesList(accessRequest);        
        List<Integer> records = accessRequestUtils.getRecordSet(accessRequest, species, uar.getDatasetKey(), uar.getOrganisation());
        
        for (int i : records) {
            oOrganisationTaxonObservationAccessMapper.RemoveAccess(uar.getOrganisation().getId(), i);
        }

        List<OrganisationAccessRequest> uars = oOrganisationAccessRequestMapper.getGrantedOrganisationRequestsByDataset(uar.getDatasetKey(), uar.getOrganisation().getId());
        
        for (OrganisationAccessRequest req : uars) {
            giveAccess(req);
        }
        return true;
    }

    private boolean giveAccess(int id) throws IOException {
        OrganisationAccessRequest uar = oOrganisationAccessRequestMapper.getRequest(id);
        return giveAccess(uar);
    }
    
    private boolean giveAccess(OrganisationAccessRequest uar) throws IOException {
        AccessRequestJSON accessRequest = parseJSON(uar.getFilter().getFilterJSON());

        List<String> species = accessRequestUtils.createSpeciesList(accessRequest);        
        List<Integer> records = accessRequestUtils.getRecordSet(accessRequest, species, uar.getDatasetKey(), uar.getOrganisation());
        
        for (int i : records) {
            oOrganisationTaxonObservationAccessMapper.AddAccess(uar.getOrganisation().getId(), i);
        }

        return true;
    }
    
    private AccessRequestJSON parseJSON(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, AccessRequestJSON.class);
    }
}

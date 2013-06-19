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
import uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalUserAccessRequestAuditHistoryMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalUserAccessRequestMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalUserTaxonObservationAccessMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.model.TaxonObservationFilter;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.UserAccessRequest;
import uk.org.nbn.nbnv.api.model.UserAccessRequestAuditHistory;
import uk.org.nbn.nbnv.api.model.meta.AccessRequestJSON;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenAccessRequestAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;
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
    @Autowired OperationalUserTaxonObservationAccessMapper oUserTaxonObservationAccessMapper;
    @Autowired OperationalUserAccessRequestAuditHistoryMapper oUserAccessRequestAuditHistoryMapper;
    @Autowired DatasetAdministratorMapper datasetAdministratorMapper;
    @Autowired AccessRequestUtils accessRequestUtils;
    
    /**
     * Create a Access Request for a user
     * 
     * @param user The current user (Must be logged in)
     * @param json The Access Request wrapped up as a JSON object
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @throws IOException 
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
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

        TaxonObservationFilter filter = accessRequestUtils.createFilter(json, accessRequest);
        List<String> species = accessRequestUtils.createSpeciesList(accessRequest);        
        List<String> datasets = accessRequestUtils.createDatasetList(accessRequest, species, user);
        
        if (accessRequest.getDataset().isSecret()) {
            List<String> sensitive = accessRequestUtils.createSensitiveDatasetList(accessRequest, species, user);
            for (String datasetKey : sensitive) {
                oTaxonObservationFilterMapper.createFilter(filter);
                oUserAccessRequestMapper.createRequest(filter.getId(), user.getId(), datasetKey, accessRequest.getReason().getPurpose(), accessRequest.getReason().getDetails(), new Date(new java.util.Date().getTime()), true);
                oUserAccessRequestAuditHistoryMapper.addHistory(filter.getId(), user.getId(), "Created request for: '" + filter.getFilterText() + "'");
            }
        }
        
        
        for (String datasetKey : datasets) {
            oTaxonObservationFilterMapper.createFilter(filter);
            oUserAccessRequestMapper.createRequest(filter.getId(), user.getId(), datasetKey, accessRequest.getReason().getPurpose(), accessRequest.getReason().getDetails(), new Date(new java.util.Date().getTime()), false);
            oUserAccessRequestAuditHistoryMapper.addHistory(filter.getId(), user.getId(), "Created request for: '" + filter.getFilterText() + "'");
        }

        return Response.ok("success").build();
    }
    
    @PUT
    @Path("/requests/admin/granted")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createGrant(@TokenUser(allowPublic=false) User user, String json) throws IOException, ParseException {
        AccessRequestJSON accessRequest = parseJSON(json);

        // Fail if this is an organisation request
        if (accessRequest.getReason().getOrganisationID() > -1) {
            return Response.serverError().build();
        }
        
        // Fail if the dataset request is not admined by the requestor
        if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), accessRequest.getDataset().getDatasets().get(0))) {
            return Response.serverError().build();
        }

        TaxonObservationFilter filter = accessRequestUtils.createFilter(json, accessRequest);
        List<String> species = accessRequestUtils.createSpeciesList(accessRequest);        
        List<String> datasets = accessRequest.getDataset().getDatasets();
        
        for (String datasetKey : datasets) {
            oTaxonObservationFilterMapper.createFilter(filter);
            oUserAccessRequestMapper.createRequest(filter.getId(), accessRequest.getReason().getUserID(), datasetKey, accessRequest.getReason().getPurpose(), accessRequest.getReason().getDetails(), new Date(new java.util.Date().getTime()), false);
            oUserAccessRequestAuditHistoryMapper.addHistory(filter.getId(), user.getId(), "Created and granted request for: '" + filter.getFilterText() + "'");
            acceptRequest(filter.getId(), accessRequest.getReason().getReason(), accessRequest.getTime().isAll() ? "" : accessRequest.getTime().getDate().toString());
        }

        return Response.ok("success").build();
    }

    /**
     * Edit an existing User Access Request 
     * 
     * @param user The current user (Must have admin rights over the specified 
     * Access Request)
     * @param filterID A filter ID for this request
     * @param json A JSON wrapper for an access request object
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @throws IOException 
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @PUT
    @Path("/requests/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response editRequest(@TokenAccessRequestAdminUser(path="id") User user, @PathParam("id") int filterID, String json) throws IOException {
        AccessRequestJSON accessRequest = parseJSON(json);

        // Fail if this is an organisation request
        if (accessRequest.getReason().getOrganisationID() > -1) {
            return Response.serverError().build();
        }
        TaxonObservationFilter filter = accessRequestUtils.createFilter(json, accessRequest);
        TaxonObservationFilter orig = oTaxonObservationFilterMapper.selectById(filterID);
        oTaxonObservationFilterMapper.editFilter(filterID, filter.getFilterText(), filter.getFilterJSON());
        oUserAccessRequestAuditHistoryMapper.addHistory(filterID, user.getId(), "Edit request to: '" + filter.getFilterText() + "', from: '" + orig.getFilterText() + "'");

        return Response.ok("success").build();
    }

    /**
     * Return a list of access requests made by a user
     * 
     * @param user The current user (Must be logged in)
     * 
     * @return A List of User Access Requests made by a user
     * 
     * @throws IOException 
     * 
     * @response.representation.200.qname List<UserAccessRequest> 
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getRequests(@TokenUser(allowPublic=false) User user) throws IOException {
        return oUserAccessRequestMapper.getUserRequests(user.getId());
    }
    
    /**
     * Return a list of access requests made by a user that have been granted
     * 
     * @param user The current user (Must be logged in)
     * 
     * @return A List of User Access Requests made by a user that have been 
     * granted
     * 
     * @throws IOException 
     * 
     * @response.representation.200.qname List<UserAccessRequest> 
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/granted")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getGrantedRequests(@TokenUser(allowPublic=false) User user) throws IOException {
        return oUserAccessRequestMapper.getGrantedUserRequests(user.getId());
    }

    /**
     * Return a list of access requests made that a user has admin rights over
     * 
     * @param user The current user (Must be logged in)
     * 
     * @return A List of User Access Requests that a user has admin rights over
     * 
     * @response.representation.200.qname List<UserAccessRequest> 
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/admin")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getRequestsForAdmin(@TokenUser(allowPublic=false) User user) {
        return oUserAccessRequestMapper.getAdminableRequests(user.getId());
    }
    
    /**
     * Return a list of access requests made that a user has admin rights over 
     * and are pending
     * 
     * @param user The current user (Must be logged in)
     * 
     * @return A List of User Access Requests that a user has admin rights over 
     * and are still pending
     * 
     * @response.representation.200.qname List<UserAccessRequest> 
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/admin/pending")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getRequestsPendingForAdmin(@TokenUser(allowPublic=false) User user) {
        return oUserAccessRequestMapper.getPendingAdminableRequests(user.getId());
    }

    /**
     * Return a list of access requests made that a user has admin rights over
     * and have been granted
     * 
     * @param user The current user (Must be logged in)
     * 
     * @return A List of User Access Requests that a user has admin rights over 
     * and have been granted
     * 
     * @response.representation.200.qname List<UserAccessRequest>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/admin/granted")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getRequestsGrantedForAdmin(@TokenUser(allowPublic=false) User user) {
        return oUserAccessRequestMapper.getGrantedAdminableRequests(user.getId());
    }

    /**
     * Return a list of access requests made that a user has admin rights over
     * and have been denied
     * 
     * @param user
     * 
     * @return A List of User Access Requests that a user has admin rights over 
     * and have been denied
     * 
     * @response.representation.200.qname List<UserAccessRequest>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/admin/denied")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getRequestsDeniedForAdmin(@TokenUser(allowPublic=false) User user) {
        return oUserAccessRequestMapper.getDeniedAdminableRequests(user.getId());
    }

    /**
     * Return a specific User Access Request as long as the current user has 
     * admin rights over that request, otherwise return a 403 Forbidden error
     * 
     * @param user The current User (Must have admin rights over the specified
     * user access request)
     * @param filterID A filter ID identifying a request
     * 
     * @return A User Access Request that a user has admin rights over
     * 
     * @response.representation.200.qname UserAccessRequest
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserAccessRequest getRequest(@TokenAccessRequestAdminUser(path="id") User user
            , @PathParam("id") int filterID) {
        return oUserAccessRequestMapper.getRequest(filterID);
    }
    
    /**
     * Update a specified User Access Request if the user has admin rights over
     * the request, otherwise return a 403 Forbidden error
     * 
     * @param user The current user (Must have admin rights over the request)
     * @param filterID A filter ID identifying a request
     * @param action What action to take in this update
     * @param reason The reason that this action has been taken
     * @param expires If the action should expire after a certain date
     *
     * @return A Response object detailing the success or failure of the action
     * 
     * @throws ParseException The expires string was in an incorrect format
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @POST
    @Path("/requests/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRequest(@TokenAccessRequestAdminUser(path="id") User user
            , @PathParam("id") int filterID
            , @FormParam("action") String action
            , @FormParam("reason") String reason
            , @FormParam("expires") @DefaultValue("") String expires) throws ParseException {
        if ("grant".equalsIgnoreCase(action)) {
            oUserAccessRequestAuditHistoryMapper.addHistory(filterID, user.getId(), "Accept request");
            return acceptRequest(filterID, reason, expires);
        } else if ("deny".equalsIgnoreCase(action)) {
            oUserAccessRequestAuditHistoryMapper.addHistory(filterID, user.getId(), "Deny request");
            return denyRequest(filterID, reason);
        } else if ("close".equalsIgnoreCase(action)) {
            oUserAccessRequestAuditHistoryMapper.addHistory(filterID, user.getId(), "Close request");
            return closeRequest(filterID, reason);
        } else if ("revoke".equalsIgnoreCase(action)) {
            oUserAccessRequestAuditHistoryMapper.addHistory(filterID, user.getId(), "Revoke action");
            return revokeRequest(filterID, reason);
        } else {
            return Response.serverError().build();
        }
    }
       
        /**
     * Returns an audit history listing all organisational access changes made to a dataset
     * 
     * @param user A dataset administrator
     * @param dataset The dataset being queried
     * 
     * @return A list of history elements
     * 
     * @response.representation.200.qname List<OrganisationAccessRequestAuditHistory>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/history/{dataset}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequestAuditHistory> getHistory(@TokenDatasetAdminUser(path="dataset") User user, @PathParam("dataset") String dataset) {
        return oUserAccessRequestAuditHistoryMapper.getHistory(dataset);
    }

    /**
     * Accept a User Access Request with a given ID
     * 
     * @param filterID A filter ID identifying a request
     * @param reason A reason for accepting the request
     * @param expires An expiry date for the request (Optional)
     * 
     * @return A Response object detailing the result of the operation
     * 
     * @throws ParseException The expires string was in an incorrect format
     */
    private Response acceptRequest(int filterID, String reason, String expires) throws ParseException {
        if (expires.isEmpty()) {
            oUserAccessRequestMapper.acceptRequest(filterID, reason, new Date(new java.util.Date().getTime()));
            return Response.status(Response.Status.OK).entity("{}").build();
        } else {
            DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
            java.util.Date expiresDate = df.parse(expires);
            oUserAccessRequestMapper.acceptRequestWithExpires(filterID, reason, new Date(new java.util.Date().getTime()), new Date(expiresDate.getTime()));
            return Response.status(Response.Status.OK).entity("{}").build();
        }
    }

    /**
     * Deny a User Access Request with a given ID
     * 
     * @param filterID A filter ID identifying a request
     * @param reason A reason for denying the request
     * 
     * @return A Response object detailing the result of the operation
     */
    private Response denyRequest(int filterID, String reason) {
        oUserAccessRequestMapper.denyRequest(filterID, reason, new Date(new java.util.Date().getTime()));
        return Response.status(Response.Status.OK).entity("{}").build();
    }

    /**
     * Close a User Access Request with a given ID
     * 
     * @param filterID A filter ID identifying a request
     * @param reason A reason for closing the request
     * 
     * @return A Response object detailing the result of the operation
     */
    private Response closeRequest(int filterID, String reason) {
        oUserAccessRequestMapper.closeRequest(filterID, reason, new Date(new java.util.Date().getTime()));
        return Response.status(Response.Status.OK).entity("{}").build();
    }
    
    /**
     * Revoke a User Access Request with a given ID
     * 
     * @param filterID A filter ID identifying a request
     * @param reason A reason for revoking the request
     * 
     * @return A Response object detailing the result of the operation
     */
    private Response revokeRequest(int filterID, String reason) {
        oUserAccessRequestMapper.revokeRequest(filterID, reason, new Date(new java.util.Date().getTime()));
        return Response.status(Response.Status.OK).entity("{}").build();
    }

    /**
     * Strip any access given by a User Access Request
     * 
     * @param id A filter ID identifying a request
     * 
     * @return If the operation was successful or not
     * 
     * @throws IOException An Error occurred mapping a JSON string to an object
     */
    private boolean stripAccess(int id) throws IOException {
        UserAccessRequest uar = oUserAccessRequestMapper.getRequest(id);
        AccessRequestJSON accessRequest = parseJSON(uar.getFilter().getFilterJSON());

        List<String> species = accessRequestUtils.createSpeciesList(accessRequest);        
        List<Integer> records = accessRequestUtils.getRecordSet(accessRequest, species, uar.getDatasetKey(), uar.getUser());
        
        for (int i : records) {
            oUserTaxonObservationAccessMapper.RemoveAccess(uar.getUser().getId(), i);
        }

        List<UserAccessRequest> uars = oUserAccessRequestMapper.getGrantedUserRequestsByDataset(uar.getDatasetKey(), uar.getUser().getId());
        
        for (UserAccessRequest req : uars) {
            giveAccess(req);
        }
        return true;
    }

    /**
     * Apply access rules given by a particular User Access Request
     * 
     * @param id A filter ID identifying a request
     * 
     * @return If the operation was a success or not
     * 
     * @throws IOException An Error occurred mapping a JSON string to an object
     */
    private boolean giveAccess(int id) throws IOException {
        UserAccessRequest uar = oUserAccessRequestMapper.getRequest(id);
        return giveAccess(uar);
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
        List<Integer> records = accessRequestUtils.getRecordSet(accessRequest, species, uar.getDatasetKey(), uar.getUser());
        
        for (int i : records) {
            oUserTaxonObservationAccessMapper.AddAccess(uar.getUser().getId(), i);
        }

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

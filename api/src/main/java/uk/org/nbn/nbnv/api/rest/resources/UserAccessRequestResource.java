package uk.org.nbn.nbnv.api.rest.resources;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.codehaus.enunciate.jaxrs.TypeHint;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalUserAccessRequestAuditHistoryMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalUserAccessRequestMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalUserTaxonObservationAccessMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.UserMapper;
import uk.org.nbn.nbnv.api.mail.TemplateMailer;
import uk.org.nbn.nbnv.api.model.DatasetAdministrator;
import uk.org.nbn.nbnv.api.model.TaxonObservationFilter;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.UserAccessRequest;
import uk.org.nbn.nbnv.api.model.UserAccessRequestAuditHistory;
import uk.org.nbn.nbnv.api.model.meta.AccessRequestJSON;
import uk.org.nbn.nbnv.api.model.meta.AccessRequestModifiedTimeJSON;
import uk.org.nbn.nbnv.api.model.meta.EditAccessRequestJSON;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenAccessRequestAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenSystemAdministratorUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.utils.AccessRequestJSONToText;
import uk.org.nbn.nbnv.api.utils.AccessRequestUtils;

/**
 *
 * @author Paul Gilbertson
 */
@Component
@Path("/user/userAccesses")
public class UserAccessRequestResource extends RequestResource {
    @Autowired OperationalTaxonObservationFilterMapper oTaxonObservationFilterMapper;
    @Autowired OperationalUserAccessRequestMapper oUserAccessRequestMapper;
    @Autowired OperationalUserTaxonObservationAccessMapper oUserTaxonObservationAccessMapper;
    @Autowired OperationalUserAccessRequestAuditHistoryMapper oUserAccessRequestAuditHistoryMapper;
    @Autowired DatasetAdministratorMapper datasetAdministratorMapper;
    @Autowired UserMapper userMapper;
    @Autowired AccessRequestUtils accessRequestUtils;
    @Autowired TemplateMailer mailer;
    @Autowired AccessRequestJSONToText accessRequestJSONToText;
    
    private Logger logger = LoggerFactory.getLogger(UserAccessRequestResource.class);
    
    /**
     * Create a Access Request for a user
     * 
     * @param user The current user (Must be logged in) (Injected Token no need 
     * to pass)
     * @param json JSON object containing an access request, please see 
     * <a href="accessRequestJSON.html">Additional Documentation</a> for more
     * details
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
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully created the user access request"),
        @ResponseCode(code = 403, condition = "The current user is not logged in"),
        @ResponseCode(code = 500, condition = "The JSON filter was not correctly formed, please check the filter and try again")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createRequest(@TokenUser(allowPublic=false) User user, String json) throws IOException, TemplateException {
        AccessRequestJSON accessRequest = parseJSON(json);

        // Fail if this is an organisation request
        if (accessRequest.getReason().getOrganisationID() > -1) {
            return Response.serverError().build();
        }
        
        // Basic check for JSON validity
        checkJSONFilterForValidity(accessRequest);
        
        if (!accessRequest.getSpatial().isAll() && !accessRequest.getDataset().isSecret()) {
            accessRequest.setSensitive("ns");
            json = json.replace(":\"sans\"", ":\"ns\"");
        }

        TaxonObservationFilter filter = accessRequestUtils.createFilter(json, accessRequest);
        List<String> species = accessRequestUtils.createSpeciesList(accessRequest);        
        List<String> datasets = accessRequestUtils.createDatasetList(accessRequest, species, user);
        
        for (String datasetKey : datasets) {
            // Check that at least one record would be granted by this access request being granted
            checkForRecordsReturnedSingleDataset(User.PUBLIC_USER, accessRequest, datasetKey);
        }   
        
        if (accessRequest.getDataset().isSecret()) {
            List<String> sensitive = accessRequestUtils.createSensitiveDatasetList(accessRequest, species, user);
            
            for (String datasetKey : datasets) {
                if (sensitive.contains(datasetKey)) {
                    sensitive.remove(datasetKey);
                }
            }
            
            for (String datasetKey : sensitive) {
                oTaxonObservationFilterMapper.createFilter(filter);
                oUserAccessRequestMapper.createRequest(filter.getId(), user.getId(), datasetKey, accessRequest.getReason().getPurpose(), accessRequest.getReason().getDetails(), new Timestamp(new java.util.Date().getTime()), true);
                oUserAccessRequestAuditHistoryMapper.addHistory(filter.getId(), user.getId(), "Created request for: '" + filter.getFilterText() + "'");
                mailRequestCreate(oUserAccessRequestMapper.getRequest(filter.getId()));
            }
        }     
                       
        for (String datasetKey : datasets) {
            try {
                oTaxonObservationFilterMapper.createFilter(filter);

                oUserAccessRequestMapper.createRequest(filter.getId(), user.getId(), datasetKey, accessRequest.getReason().getPurpose(), accessRequest.getReason().getDetails(), new Timestamp(new java.util.Date().getTime()), false);
                oUserAccessRequestAuditHistoryMapper.addHistory(filter.getId(), user.getId(), "Created request for: '" + filter.getFilterText() + "'");

                mailRequestCreate(oUserAccessRequestMapper.getRequest(filter.getId()));
            } catch (IllegalArgumentException ex) {
                logger.info("Could not create request for: '" 
                        + filter.getFilterText() + "' / '" 
                        + filter.getFilterJSON() + "' on dataset "
                        + datasetKey + " :- Zero Records would be granted with this request");
            }            
        }

        return Response.status(Response.Status.OK).entity("{}").build();
    }
    
    /**
     * Create and proactively grant a user access request, if the user is an 
     * admin of the provided dataset
     * 
     * @param user The current user (must be dataset admin) (Injected Token no 
     * need to pass)
     * @param json JSON object containing an access request, please see 
     * <a href="accessRequestJSON.html">Additional Documentation</a> for more
     * details
     * 
     * @return
     * 
     * @throws IOException
     * @throws ParseException
     * @throws TemplateException 
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @PUT
    @Path("/requests/admin/granted")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully created the user access request"),
        @ResponseCode(code = 403, condition = "The current user is not logged in"),
        @ResponseCode(code = 500, condition = "The JSON filter was not correctly formed, please check the filter and try again")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createGrant(@TokenUser(allowPublic=false) User user, String json) throws IOException, ParseException, TemplateException {
        AccessRequestJSON accessRequest = parseJSON(json);

        // Fail if this is an organisation request
        if (accessRequest.getReason().getOrganisationID() > -1) {
            return Response.serverError().build();
        }
        
        // Fail if the dataset request is not admined by the requestor
        if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), accessRequest.getDataset().getDatasets().get(0))) {
            return Response.serverError().build();
        }
        
        // Basic check for JSON validity
        checkJSONFilterForValidity(accessRequest);       
        // Check that at least one record would be granted by this access request being granted
        checkForRecordsReturnedSingleDataset(User.PUBLIC_USER, accessRequest, accessRequest.getDataset().getDatasets().get(0));

        TaxonObservationFilter filter = accessRequestUtils.createFilter(json, accessRequest);
        List<String> datasets = accessRequest.getDataset().getDatasets();
        User reqUser = userMapper.getUserById(accessRequest.getReason().getUserID());
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
        for (String datasetKey : datasets) {
            oTaxonObservationFilterMapper.createFilter(filter);
            oUserAccessRequestMapper.createRequest(filter.getId(), accessRequest.getReason().getUserID(), datasetKey, accessRequest.getReason().getPurpose(), accessRequest.getReason().getDetails(), new Timestamp(new java.util.Date().getTime()), false);
            oUserAccessRequestAuditHistoryMapper.addHistory(filter.getId(), user.getId(), "Created request for " + reqUser.getForename() + ' ' + reqUser.getSurname() + " of: '" + filter.getFilterText() + "'");
            acceptRequest(user, filter.getId(), accessRequest.getReason().getReason(), accessRequest.getTime().isAll() ? "" : df.format(accessRequest.getTime().getDate()), true);
        }

        return Response.status(Response.Status.OK).entity("{}").build();
    }

    /**
     * Edit and then grant an existing User Access Request 
     * 
     * @param user The current user (Must have admin rights over the specified 
     * Access Request) (Injected Token no need to pass)
     * @param filterID A filter ID for this request
     * @param json JSON object containing an access request, please see 
     * <a href="accessRequestJSON.html">Additional Documentation</a> for more
     * details
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
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully created the user access request"),
        @ResponseCode(code = 403, condition = "The current user does not have admin rights over this access request"),
        @ResponseCode(code = 500, condition = "The JSON filter was not correctly formed, please check the filter and try again")
    })        
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response editAndGrantRequest(@TokenAccessRequestAdminUser(path="id") User user, 
        @PathParam("id") int filterID, String json) throws IOException, ParseException, TemplateException {
        
        ObjectMapper mapper = new ObjectMapper();        
        EditAccessRequestJSON editAccessRequest = mapper.readValue(json, EditAccessRequestJSON.class);
        AccessRequestJSON accessRequest = editAccessRequest.getJson();
        
        // Fail if this is an organisation request
        if (accessRequest.getReason().getOrganisationID() > -1) {
            return Response.serverError().build();
        }
               
        // Basic check for JSON validity
        checkJSONFilterForValidity(accessRequest);
        // Check that at least one record would be granted by this access request being granted for the public user
        checkForRecordsReturnedSingleDataset(User.PUBLIC_USER, accessRequest, accessRequest.getDataset().getDatasets().get(0));
        
        TaxonObservationFilter filter = accessRequestUtils.createFilter(editAccessRequest.getRawJSON(), accessRequest);
        TaxonObservationFilter orig = oTaxonObservationFilterMapper.selectById(filterID);
        oTaxonObservationFilterMapper.editFilter(filterID, filter.getFilterText(), filter.getFilterJSON());
        oUserAccessRequestAuditHistoryMapper.addHistory(filterID, user.getId(), "Edit request to: '" + filter.getFilterText() + "', from: '" + orig.getFilterText() + "'");

        
        return acceptRequest(user, filterID, editAccessRequest.getReason(), editAccessRequest.getExpires(), false);
    }

    /**
     * Return a list of access requests made by a user
     * 
     * @param user The current user (Must be logged in) (Injected Token no need 
     * to pass)
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
    @TypeHint(UserAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned all access requests for this user"),
        @ResponseCode(code = 403, condition = "The current user is not logged in")
    })        
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getRequests(@TokenUser(allowPublic=false) User user) throws IOException {
        return oUserAccessRequestMapper.getUserRequests(user.getId());
    }
    
    /**
     * Return a list of access requests made by a user that have been granted
     * 
     * @param user The current user (Must be logged in) (Injected Token no need 
     * to pass)
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
    @TypeHint(UserAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned all granted access requests for this user"),
        @ResponseCode(code = 403, condition = "The current user is not logged in")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getGrantedRequests(@TokenUser(allowPublic=false) User user) throws IOException {
        return oUserAccessRequestMapper.getGrantedUserRequests(user.getId());
    }

    /**
     * Return a list of access requests made by a user that are pending
     * 
     * @param user The current user (Must be logged in) (Injected Token no need 
     * to pass)
     * 
     * @return A List of User Access Requests made by a user that are pending
     * 
     * @throws IOException 
     * 
     * @response.representation.200.qname List<UserAccessRequest> 
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/pending")
    @TypeHint(UserAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned all pending access requests for this user"),
        @ResponseCode(code = 403, condition = "The current user is not logged in")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getPendingRequests(@TokenUser(allowPublic=false) User user) throws IOException {
        return oUserAccessRequestMapper.getPendingUserRequests(user.getId());
    }

    /**
     * Return a list of access requests made that a user has admin rights over
     * 
     * @param user The current user (Must be logged in) (Injected Token no need 
     * to pass)
     * 
     * @return A List of User Access Requests that a user has admin rights over
     * 
     * @response.representation.200.qname List<UserAccessRequest> 
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/admin")
    @TypeHint(UserAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned all adminable access requests for this user"),
        @ResponseCode(code = 403, condition = "The current user is not logged in")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getRequestsForAdmin(@TokenUser(allowPublic=false) User user) {
        return oUserAccessRequestMapper.getAdminableRequests(user.getId());
    }
    
    /**
     * Return a list of access requests made that a user has admin rights over 
     * and are pending
     * 
     * @param user The current user (Must be logged in) (Injected Token no need 
     * to pass)
     * 
     * @return A List of User Access Requests that a user has admin rights over 
     * and are still pending
     * 
     * @response.representation.200.qname List<UserAccessRequest> 
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/admin/pending")
    @TypeHint(UserAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned all pending adminable access requests for this user"),
        @ResponseCode(code = 403, condition = "The current user is not logged in")
    })  
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getRequestsPendingForAdmin(@TokenUser(allowPublic=false) User user) {
        return oUserAccessRequestMapper.getPendingAdminableRequests(user.getId());
    }

    /**
     * Return a list of access requests made that a user has admin rights over
     * and have been granted
     * 
     * @param user The current user (Must be logged in) (Injected Token no need 
     * to pass)
     * 
     * @return A List of User Access Requests that a user has admin rights over 
     * and have been granted
     * 
     * @response.representation.200.qname List<UserAccessRequest>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/admin/granted")
    @TypeHint(UserAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned all granted adminable access requests for this user"),
        @ResponseCode(code = 403, condition = "The current user is not logged in")
    })     
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getRequestsGrantedForAdmin(@TokenUser(allowPublic=false) User user) {
        return oUserAccessRequestMapper.getGrantedAdminableRequests(user.getId());
    }

    /**
     * Return a list of access requests made that a user has admin rights over
     * and have been denied
     * 
     * @param user The current user (Must be logged in) (Injected Token no need 
     * to pass)
     * 
     * @return A List of User Access Requests that a user has admin rights over 
     * and have been denied
     * 
     * @response.representation.200.qname List<UserAccessRequest>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/admin/denied")
    @TypeHint(UserAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned all denied adminable access requests for this user"),
        @ResponseCode(code = 403, condition = "The current user is not logged in")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getRequestsDeniedForAdmin(@TokenUser(allowPublic=false) User user) {
        return oUserAccessRequestMapper.getDeniedAdminableRequests(user.getId());
    }

    /**
     * Return a specific User Access Request as long as the current user has 
     * admin rights over that request, otherwise return a 403 Forbidden error
     * 
     * @param user The current User (Must have admin rights over the specified
     * user access request) (Injected Token no need to pass)
     * @param filterID A filter ID identifying a request
     * 
     * @return A User Access Request that a user has admin rights over
     * 
     * @response.representation.200.qname UserAccessRequest
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/{id}")
    @TypeHint(UserAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the requested access request"),
        @ResponseCode(code = 403, condition = "The current user has no admin rights over this request")
    })  
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
     * (Injected Token no need to pass)
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
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully completed operation"),
        @ResponseCode(code = 403, condition = "The current user has no admin rights over this request")
    })   
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRequest(@TokenAccessRequestAdminUser(path="id") User user
            , @PathParam("id") int filterID
            , @FormParam("action") String action
            , @FormParam("reason") String reason
            , @FormParam("expires") @DefaultValue("") String expires) throws ParseException, IOException, TemplateException {
        if ("grant".equalsIgnoreCase(action)) {
            return acceptRequest(user, filterID, reason, expires, false);
        } else if ("deny".equalsIgnoreCase(action)) {
            return denyRequest(user, filterID, reason);
        } else if ("close".equalsIgnoreCase(action)) {
            return closeRequest(user, filterID, reason);
        } else if ("revoke".equalsIgnoreCase(action)) {
            return revokeRequest(user, filterID, reason);
        } else {
            return Response.serverError().build();
        }
    }
      
    /**
     * SysAdmin function to reset accesses
     * @param user (Injected Token no need to pass)
     * @param dataset
     * @return
     * @throws IOException 
     */
    @GET
    @Path("/reset/{dataset}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetDatasetAccess(@TokenSystemAdministratorUser() User user, @PathParam("dataset") String dataset) throws IOException {
        resetAllAccess(dataset);
        return Response.ok().build();
    }

    /**
     * SysAdmin function to get all expired access requests, used to find expired
     * requests
     * 
     * @param user The system admin user who called this endpoint
     * 
     * @return A List of expired organisation access requests
     */
    @GET
    @Path("/expired")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserAccessRequest> getExpired(@TokenSystemAdministratorUser User user) {
        return oUserAccessRequestMapper.getExpiredRequests();
    }    
    
    /**
     * SysAdmin function to revoke access, typically called for expirations
     * 
     * @param user The system admin who called this endpoint
     * @param filterID The filterID of the request to be expired
     *
     * @return If the revoke action was a success
     */    
    @POST
    @Path("/requests/revoke/{id}")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully completed operation"),
        @ResponseCode(code = 403, condition = "The current user has no admin rights over this request")
    })   
    @Produces(MediaType.APPLICATION_JSON)
    public Response sysAdminRevoke(
            @TokenSystemAdministratorUser User user, 
            @PathParam("id") int filterID) {
        try {
            revokeRequest(user, filterID, "Request Expired");
        } catch (IOException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build();
        } catch (TemplateException ex) {
            return Response.status(Response.Status.ACCEPTED).entity(ex.toString()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build();
        }
        return Response.ok("Successfully Revoked Request").build();
    }
    
    /**
     * Returns an audit history listing all organisational access changes made to a dataset
     * 
     * @param user A dataset administrator (Injected Token no need to pass)
     * @param dataset The dataset being queried
     * 
     * @return A list of history elements
     * 
     * @response.representation.200.qname List<OrganisationAccessRequestAuditHistory>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/history/{dataset}")
    @TypeHint(UserAccessRequestAuditHistory.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the audit history for this dataset"),
        @ResponseCode(code = 403, condition = "The current user has no admin rights over this request")
    }) 
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
     * @param proactive If the request was a proactive from the dataset admin 
     * (true) or was a request made by a user (false)
     * 
     * @return A Response object detailing the result of the operation
     * 
     * @throws ParseException The expires string was in an incorrect format
     */
    private Response acceptRequest(User user, int filterID, String reason, String expires, boolean proactive) throws ParseException, IOException, TemplateException {
        UserAccessRequest uar = oUserAccessRequestMapper.getRequest(filterID);
        
        AccessRequestJSON accessRequest = parseJSON(uar.getFilter().getFilterJSON());
             
        // Basic check for JSON validity
        checkJSONFilterForValidity(accessRequest);
        // Check that at least one record would be granted by this access request being granted
        checkForRecordsReturnedSingleDataset(User.PUBLIC_USER, accessRequest, uar.getDatasetKey());
        
        giveAccess(uar);
        
        if (expires.isEmpty()) {
            oUserAccessRequestMapper.acceptRequest(filterID, reason, new Timestamp(new java.util.Date().getTime()));
        } else {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat db_df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            java.util.Date expiresDate = df.parse(expires);
                        
            Calendar expiresCal = Calendar.getInstance();
            Calendar requestCal = Calendar.getInstance();
            expiresCal.setTime(expiresDate);
            requestCal.setTime(accessRequest.getTime().getDate());
            
            if (!(expiresCal.get(Calendar.YEAR) == requestCal.get(Calendar.YEAR)
                    && expiresCal.get(Calendar.DAY_OF_YEAR) == requestCal.get(Calendar.DAY_OF_YEAR))) {
            
                String origText = uar.getFilter().getFilterText();

                accessRequest.getTime().setAll(false);
                accessRequest.getTime().setDate(expiresDate);

                AccessRequestModifiedTimeJSON json = new AccessRequestModifiedTimeJSON(accessRequest, db_df.format(expiresDate));

                ObjectMapper om = new ObjectMapper();
                // Stop nulls being pushed out to the json output
                om.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
                ObjectWriter ow = om.writer();

                TaxonObservationFilter filter = accessRequestUtils.createFilter(ow.writeValueAsString(json), accessRequest);

                oTaxonObservationFilterMapper.editFilter(filterID, filter.getFilterText(), ow.writeValueAsString(json));
                oUserAccessRequestAuditHistoryMapper.addHistory(filterID, user.getId(), "Edit request to: '" + filter.getFilterText() + "', from: '" + origText + "'");
            }
            oUserAccessRequestMapper.acceptRequestWithExpires(filterID, reason, new Timestamp(new java.util.Date().getTime()), new Date(expiresDate.getTime()));
        }

        oUserAccessRequestAuditHistoryMapper.addHistory(filterID, user.getId(), "Accept request");
        mailRequestGrant(oUserAccessRequestMapper.getRequest(filterID), reason, proactive);
        return Response.status(Response.Status.OK).entity("{}").build();
    }

    /**
     * Deny a User Access Request with a given ID
     * 
     * @param filterID A filter ID identifying a request
     * @param reason A reason for denying the request
     * 
     * @return A Response object detailing the result of the operation
     */
    private Response denyRequest(User user, int filterID, String reason) throws IOException, TemplateException {
        oUserAccessRequestMapper.denyRequest(filterID, reason, new Timestamp(new java.util.Date().getTime()));
        oUserAccessRequestAuditHistoryMapper.addHistory(filterID, user.getId(), "Deny request");
        mailRequestDeny(oUserAccessRequestMapper.getRequest(filterID), reason);
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
    private Response closeRequest(User user, int filterID, String reason) {
        oUserAccessRequestMapper.closeRequest(filterID, reason, new Timestamp(new java.util.Date().getTime()));
        oUserAccessRequestAuditHistoryMapper.addHistory(filterID, user.getId(), "Close request");
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
    private Response revokeRequest(User user, int filterID, String reason) throws IOException, TemplateException {
        stripAccess(filterID, user, reason);
        mailRequestRevoke(oUserAccessRequestMapper.getRequest(filterID), reason);
        return Response.status(Response.Status.OK).entity("{}").build();
    }

    
    private boolean resetAllAccess(String dataset) throws IOException {
        oUserTaxonObservationAccessMapper.removeAllUserAccessForDataset(dataset);
        List<UserAccessRequest> uars = oUserAccessRequestMapper.getGrantedRequestsByDataset(dataset);
        
        for (UserAccessRequest req : uars) {
            giveAccess(req);
        }
        return true;
        
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
    private boolean stripAccess(int id, User user, String reason) throws IOException {
        UserAccessRequest uar = oUserAccessRequestMapper.getRequest(id);
        AccessRequestJSON accessRequest = parseJSON(uar.getFilter().getFilterJSON());

        List<String> species = accessRequestUtils.createSpeciesList(accessRequest);        
        List<String> datasets = new ArrayList<String>();
        datasets.add(uar.getDatasetKey());
        oUserTaxonObservationAccessMapper.removeUserAccess(uar.getUser(), accessRequest.getYear().getStartYear(), accessRequest.getYear().getEndYear(), datasets, species, accessRequest.getSpatial().getMatch(), accessRequest.getSpatial().getFeature(), (accessRequest.getSensitive().equals("sans") ? true : false), accessRequest.getTaxon().getDesignation(), accessRequest.getTaxon().getOutput(), accessRequest.getTaxon().getOrgSuppliedList(), accessRequest.getSpatial().getGridRef(), "");
        
        oUserAccessRequestMapper.revokeRequest(id, reason, new Timestamp(new java.util.Date().getTime()));
        oUserAccessRequestAuditHistoryMapper.addHistory(id, user.getId(), "Revoke action");

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
    
    private void mailRequestCreate(UserAccessRequest request) throws IOException, TemplateException {
        List<DatasetAdministrator> admins = datasetAdministratorMapper.selectByDataset(request.getDatasetKey());
        
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("rName", request.getUser().getForename() + " " + request.getUser().getSurname());
        message.put("reason", request.getRequestReason());
        message.put("details", request.getFilter().getFilterText());
        message.put("orgReq", Boolean.FALSE);
        message.put("dataset", request.getDataset().getTitle());
        message.put("purpose", request.getRequestPurposeLabel());        
        
        for (DatasetAdministrator admin : admins) {
            message.put("daName", admin.getUser().getForename() + " " + admin.getUser().getSurname());
            mailer.send("accessRequestMade.ftl", admin.getUser().getEmail(), "NBN Gateway: New access request", message);
        }
    }

    private void mailRequestDeny(UserAccessRequest request, String reason) throws IOException, TemplateException {
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("rName", request.getUser().getForename() + " " + request.getUser().getSurname());
        message.put("rDate", request.getRequestDate());
        message.put("reason", reason);
        message.put("details", request.getFilter().getFilterText());
        message.put("dataset", request.getDataset().getTitle());
        mailer.send("accessRequestDeny.ftl", request.getUser().getEmail(), "NBN Gateway: Access request declined", message);
    }

    private void mailRequestGrant(UserAccessRequest request, String reason, boolean proactive) throws IOException, TemplateException {
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("rName", request.getUser().getForename() + " " + request.getUser().getSurname());
        message.put("rDate", request.getRequestDate());
        message.put("lDate", request.getAccessExpires());
        message.put("reason", reason);
        message.put("details", request.getFilter().getFilterText());
        message.put("dataset", request.getDataset().getTitle());
        message.put("org", false);

        if (proactive) {    
            mailer.send("accessProactiveGrant.ftl", request.getUser().getEmail(), "NBN Gateway: Access Granted", message);
        } else {
            mailer.send("accessRequestGrant.ftl", request.getUser().getEmail(), "NBN Gateway: Access request approved", message);
        }
    }

    private void mailRequestRevoke(UserAccessRequest request, String reason) throws IOException, TemplateException {
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("rName", request.getUser().getForename() + " " + request.getUser().getSurname());
        message.put("rDate", request.getRequestDate());
        message.put("reason", reason);
        message.put("details", request.getFilter().getFilterText());
        message.put("dataset", request.getDataset().getTitle());
        mailer.send("accessRequestRevoke.ftl", request.getUser().getEmail(), "NBN Gateway: Access request removed", message);
    }

}

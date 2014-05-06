/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.ws.rs.QueryParam;
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
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationAccessRequestAuditHistoryMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationAccessRequestMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationTaxonObservationAccessMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper;
import uk.org.nbn.nbnv.api.mail.TemplateMailer;
import uk.org.nbn.nbnv.api.model.DatasetAdministrator;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.OrganisationAccessRequest;
import uk.org.nbn.nbnv.api.model.OrganisationAccessRequestAuditHistory;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.TaxonObservationFilter;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.meta.AccessRequestJSON;
import uk.org.nbn.nbnv.api.model.meta.AccessRequestModifiedTimeJSON;
import uk.org.nbn.nbnv.api.model.meta.EditAccessRequestJSON;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenOrganisationAccessRequestAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenOrganisationUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenSystemAdministratorUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.utils.AccessRequestUtils;

/**
 *
 * @author Paul Gilbertson
 */
@Component
@Path("/organisation/organisationAccesses")
public class OrganisationAccessRequestResource extends RequestResource {
    @Autowired OperationalTaxonObservationFilterMapper oTaxonObservationFilterMapper;
    @Autowired OperationalOrganisationAccessRequestMapper oOrganisationAccessRequestMapper;
    @Autowired OperationalOrganisationTaxonObservationAccessMapper oOrganisationTaxonObservationAccessMapper;
    @Autowired OperationalOrganisationAccessRequestAuditHistoryMapper oOrganisationAccessRequestAuditHistoryMapper;
    @Autowired OrganisationMapper organisationMapper;
    @Autowired DatasetAdministratorMapper datasetAdministratorMapper;
    @Autowired OperationalOrganisationMembershipMapper oOrganisationMembershipMapper;
    @Autowired AccessRequestUtils accessRequestUtils;
    @Autowired TemplateMailer mailer;
    
    private Logger logger = LoggerFactory.getLogger(OrganisationAccessRequestResource.class);
    
    /**
     * Create an Organisation Access Request for data
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
        @ResponseCode(code = 200, condition = "Organisation Access Request was successfully created"),
        @ResponseCode(code = 500, condition = "There was an issue with the request, please verify access request was correct and try again")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createRequest(@TokenUser(allowPublic=false) User user, String json) throws IOException, TemplateException {
        AccessRequestJSON accessRequest = parseJSON(json);

        // Fail if this is an user request
        if (accessRequest.getReason().getOrganisationID() == -1) {
            return Response.serverError().build();
        }

        // Basic filter validity checks
        checkJSONFilterForValidity(accessRequest);
        
        Organisation org = organisationMapper.selectByID(accessRequest.getReason().getOrganisationID());
        
        if (!accessRequest.getSpatial().isAll() && !accessRequest.getDataset().isSecret()) {
            accessRequest.setSensitive("ns");
            json = json.replace(":\"sans\"", ":\"ns\"");
        }

        TaxonObservationFilter filter = accessRequestUtils.createFilter(json, accessRequest);
        List<String> species = accessRequestUtils.createSpeciesList(accessRequest);        
        List<String> datasets = accessRequestUtils.createDatasetList(accessRequest, species, org);

        for (String datasetKey : datasets) {
            // Check that at least one record would be granted by this access request being granted
            checkForRecordsReturnedSingleDataset(user.PUBLIC_USER, accessRequest, datasetKey);
        }           
        
        if (accessRequest.getDataset().isSecret()) {
            List<String> sensitive = accessRequestUtils.createSensitiveDatasetList(accessRequest, species, org);
            
            for (String datasetKey : datasets) {
                if (sensitive.contains(datasetKey)) {
                    sensitive.remove(datasetKey);
                }
            }

            for (String datasetKey : sensitive) {
                oTaxonObservationFilterMapper.createFilter(filter);
                oOrganisationAccessRequestMapper.createRequest(filter.getId(), org.getId(), datasetKey, accessRequest.getReason().getPurpose(), accessRequest.getReason().getDetails(), new Timestamp(new java.util.Date().getTime()), true);
                oOrganisationAccessRequestAuditHistoryMapper.addHistory(filter.getId(), user.getId(), "Created request for: '" + filter.getFilterText() + "'");
                mailRequestCreate(oOrganisationAccessRequestMapper.getRequest(filter.getId()), user);
            }
        }

        for (String datasetKey : datasets) {
            try {
                oTaxonObservationFilterMapper.createFilter(filter);
                oOrganisationAccessRequestMapper.createRequest(filter.getId(), org.getId(), datasetKey, accessRequest.getReason().getPurpose(), accessRequest.getReason().getDetails(), new Timestamp(new java.util.Date().getTime()), false);
                oOrganisationAccessRequestAuditHistoryMapper.addHistory(filter.getId(), user.getId(), "Created request for: '" + filter.getFilterText() + "'");
                mailRequestCreate(oOrganisationAccessRequestMapper.getRequest(filter.getId()), user);
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
     * Proactively grant an organisation access according to a given json string
     * if the current user administrates the datasets in the request otherwise
     * throw an error
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
     * @throws ParseException
     * @throws TemplateException 
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @PUT
    @Path("/requests/admin/granted")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Organisation Access Request was successfully created and proactively granted"),
        @ResponseCode(code = 500, condition = "There was an issue with the request, please verify access request was correct and try again")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createGrant(@TokenUser(allowPublic=false) User user, String json) throws IOException, ParseException, TemplateException {
        AccessRequestJSON accessRequest = parseJSON(json);

        // Fail if this is not an organisation request
        if (accessRequest.getReason().getOrganisationID() == -1) {
            return Response.serverError().build();
        }
        
        // Fail if the dataset request is not admined by the requestor
        if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), accessRequest.getDataset().getDatasets().get(0))) {
            return Response.serverError().build();
        }
        
        // Basic filter validity checks
        checkJSONFilterForValidity(accessRequest);    
        // Check that at least one record would be granted by this access request being granted
        checkForRecordsReturnedSingleDataset(User.PUBLIC_USER, accessRequest, accessRequest.getDataset().getDatasets().get(0));

        TaxonObservationFilter filter = accessRequestUtils.createFilter(json, accessRequest);
        List<String> datasets = accessRequest.getDataset().getDatasets();
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
        for (String datasetKey : datasets) {
            oTaxonObservationFilterMapper.createFilter(filter);
            oOrganisationAccessRequestMapper.createRequest(filter.getId(), accessRequest.getReason().getOrganisationID(), datasetKey, accessRequest.getReason().getPurpose(), accessRequest.getReason().getDetails(), new Timestamp(new java.util.Date().getTime()), false);
            Organisation org = organisationMapper.selectByID(accessRequest.getReason().getOrganisationID());
            oOrganisationAccessRequestAuditHistoryMapper.addHistory(filter.getId(), user.getId(), "Created request for " + org.getName() + " of: '" + filter.getFilterText() + "'");
            acceptRequest(user, filter.getId(), accessRequest.getReason().getReason(), accessRequest.getTime().isAll() ? "" : df.format(accessRequest.getTime().getDate()), true);
        }

        return Response.status(Response.Status.OK).entity("{}").build();
    }

    /**
     * Edit and grant existing Organisation Access Request for data
     * 
     * @param user The current user (Must be logged in) (Injected Token no need 
     * to pass)
     * @param filterID Filter ID for a TaxonObservationFilter
     * @param json JSON containing an access request
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
        @ResponseCode(code = 200, condition = "Organisation Access Request was successfully edited"),
        @ResponseCode(code = 500, condition = "There was an issue with the request, please verify access request was correct and try again")
    })   
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response editAndGrantRequest(@TokenOrganisationAccessRequestAdminUser(path="id") User user, @PathParam("id") int filterID, String json) throws IOException, ParseException, TemplateException {
        ObjectMapper mapper = new ObjectMapper();        
        EditAccessRequestJSON editAccessRequest = mapper.readValue(json, EditAccessRequestJSON.class);
        AccessRequestJSON accessRequest = editAccessRequest.getJson();

        // Fail if this is an user request
        if (accessRequest.getReason().getOrganisationID() == -1) {
            return Response.serverError().build();
        }
        
        // Basic filter validity checks
        checkJSONFilterForValidity(accessRequest);
        // Check that at least one record would be granted by this access request being granted
        checkForRecordsReturnedSingleDataset(User.PUBLIC_USER, accessRequest, accessRequest.getDataset().getDatasets().get(0));
        
        TaxonObservationFilter filter = accessRequestUtils.createFilter(editAccessRequest.getRawJSON(), accessRequest);
        TaxonObservationFilter orig = oTaxonObservationFilterMapper.selectById(filterID);
        oTaxonObservationFilterMapper.editFilter(filterID, filter.getFilterText(), filter.getFilterJSON());
        oOrganisationAccessRequestAuditHistoryMapper.addHistory(filterID, user.getId(), "Edit request to: '" + filter.getFilterText() + "', from: '" + orig.getFilterText() + "'");

        return acceptRequest(user, filterID, editAccessRequest.getReason(), editAccessRequest.getExpires(), false);
    }
    
    /**
     * Returns a list of all Organisation Access Requests for 
     * organisations that a user is a member of and have been granted
     * 
     * @param user The current user (Must be logged in) (Injected Token no need 
     * to pass)
     * 
     * @return A list of all Organisation Access Requests for a given 
     * organisation that have been granted
     * 
     * @throws IOException 
     * 
     * @response.representation.200.qname List<OrganisationAccessRequest>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/granted")
    @TypeHint(OrganisationAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully returned a list of all granted Organisation Access Requests which were actionable by this user")
    }) 
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationAccessRequest> getGrantedRequests(@TokenUser(allowPublic=false) User user) throws IOException {
        return oOrganisationAccessRequestMapper.getUserGrantedOrganisationRequests(user.getId());
    }

    /**
     * Returns a list of all Organisation Access Requests for a given 
     * organisation
     * 
     * @param user The current user (Must be logged in and an admin of this 
     * organisation) (Injected Token no need to pass)
     * @param orgID An Organisation ID
     * 
     * @return A list of all Organisation Access Requests for a given 
     * organisation
     * 
     * @throws IOException 
     * 
     * @response.representation.200.qname List<OrganisationAccessRequest>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/requests")
    @TypeHint(OrganisationAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully returned a list of all Organisation Access Requests which are actionable by this user for the selected organisation"),
        @ResponseCode(code = 403, condition = "The current user is not an admin of this organisation")
    })        
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationAccessRequest> getRequests(@TokenOrganisationUser(path="id", roles = OrganisationMembership.Role.administrator) User user, @PathParam("id") int orgID) throws IOException {
        return oOrganisationAccessRequestMapper.getOrganisationRequests(orgID);
    }
    
    /**
     * Returns a list of all Organisation Access Requests for a given 
     * organisation that have been granted
     * 
     * @param user The current user (Must be logged in and an admin of this 
     * organisation) (Injected Token no need to pass)
     * @param orgID An Organisation ID
     * 
     * @return A list of all Organisation Access Requests for a given 
     * organisation that have been granted
     * 
     * @throws IOException 
     * 
     * @response.representation.200.qname List<OrganisationAccessRequest>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("{id}/requests/granted")
    @TypeHint(OrganisationAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully returned a list of all granted Organisation Access Requests which are actionable by this user for the selected organisation"),
        @ResponseCode(code = 403, condition = "The current user is not an admin of this organisation")
    })        
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationAccessRequest> getGrantedRequests(@TokenOrganisationUser(path="id", roles = OrganisationMembership.Role.administrator) User user, @PathParam("id") int orgID) throws IOException {
        return oOrganisationAccessRequestMapper.getGrantedOrganisationRequests(orgID);
    }

    /**
     * Returns a list of all Organisation Access Requests for a given 
     * organisation that are pending
     * 
     * @param user The current user (Must be logged in and an admin of this 
     * organisation) (Injected Token no need to pass)
     * @param orgID An Organisation ID
     * 
     * @return A list of all Organisation Access Requests for a given 
     * organisation that are pending
     * 
     * @throws IOException 
     * 
     * @response.representation.200.qname List<OrganisationAccessRequest>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("{id}/requests/pending")
    @TypeHint(OrganisationAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully returned a list of all pending Organisation Access Requests which are actionable by this user for the selected organisation"),
        @ResponseCode(code = 403, condition = "The current user is not an admin of this organisation")
    })     
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationAccessRequest> getPendingRequests(@TokenOrganisationUser(path="id", roles = OrganisationMembership.Role.administrator) User user, @PathParam("id") int orgID) throws IOException {
        return oOrganisationAccessRequestMapper.getPendingOrganisationRequests(orgID);
    }

    /**
     * Returns a list of all pending Organisation Access Requests for 
     * Organisations that a user administers
     * 
     * @param user The current user (Must be logged in) (Injected Token no need 
     * to pass)
     * 
     * @return A list of all pending Organisation Access Requests 
     * 
     * @throws IOException 
     * 
     * @response.representation.200.qname List<OrganisationAccessRequest>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/pending")
    @TypeHint(OrganisationAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully returned a list of all pending Organisation Access Requests which were created by this user"),
        @ResponseCode(code = 403, condition = "The current user is not logged in")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationAccessRequest> getPendingRequests(@TokenUser(allowPublic=false) User user) throws IOException {
        return oOrganisationAccessRequestMapper.getUserPendingOrganisationRequests(user.getId());
    }
    
    /**
     * Returns a List of Organisation Access Requests for which the current user
     * has admin rights
     * 
     * @param user The current user (Must be logged in) (Injected Token no need 
     * to pass)
     * 
     * @return A List of Organisation Access Requests for which the current user
     * has admin rights
     * 
     * @response.representation.200.qname List<OrganisationAccessRequest>
     * @response.representation.200.mediaType application/json
     */
    @GET 
    @Path("/requests/admin")
    @TypeHint(OrganisationAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully returned a list of all Organisation Access Requests which are adminable by this user"),
        @ResponseCode(code = 403, condition = "The current user is not logged in")
    })         
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationAccessRequest> getRequestsForAdmin(@TokenUser(allowPublic=false) User user) {
        return oOrganisationAccessRequestMapper.getAdminableRequests(user.getId());
    }
    
    /**
     * Returns a List of Organisation Access Requests which are pending and the
     * current user has admin rights over
     * 
     * @param user The current user (Must be logged in) (Injected Token no need 
     * to pass)
     * 
     * @return A List of Organisation Access Requests which are pending and the
     * current user has admin rights over
     * 
     * @response.representation.200.qname List<OrganisationAccessRequest>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/admin/pending")
    @TypeHint(OrganisationAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully returned a list of all pending Organisation Access Requests which are adminable by this user"),
        @ResponseCode(code = 403, condition = "The current user is not logged in")
    })      
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationAccessRequest> getRequestsPendingForAdmin(@TokenUser(allowPublic=false) User user) {
        return oOrganisationAccessRequestMapper.getPendingAdminableRequests(user.getId());
    }

    /**
     * Returns a List of Organisation Access Requests which have been granted 
     * and the current user has admin rights over
     * 
     * @param user The current user (Must be logged in) (Injected Token no need 
     * to pass)
     * 
     * @return A List of Organisation Access Requests which have been granted 
     * and the current user has admin rights over
     * 
     * @response.representation.200.qname List<OrganisationAccessRequest>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/admin/granted")
    @TypeHint(OrganisationAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully returned a list of all granted Organisation Access Requests which are adminable by this user"),
        @ResponseCode(code = 403, condition = "The current user is not logged in")
    }) 
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationAccessRequest> getRequestsGrantedForAdmin(@TokenUser(allowPublic=false) User user) {
        return oOrganisationAccessRequestMapper.getGrantedAdminableRequests(user.getId());
    }

    /**
     * Returns a List of Organisation Access Requests which have been denied 
     * and the current user has admin rights over
     * 
     * @param user The current user (Must be logged in) (Injected Token no need 
     * to pass)
     * 
     * @return A List of Organisation Access Requests which have been denied 
     * and the current user has admin rights over
     * 
     * @response.representation.200.qname List<OrganisationAccessRequest>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/admin/denied")
    @TypeHint(OrganisationAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully returned a list of all denied Organisation Access Requests which are adminable by this user"),
        @ResponseCode(code = 403, condition = "The current user is not logged in")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationAccessRequest> getRequestsDeniedForAdmin(@TokenUser(allowPublic=false) User user) {
        return oOrganisationAccessRequestMapper.getDeniedAdminableRequests(user.getId());
    }

    /**
     * Returns an Organisation Access Request with a specific filter ID
     * 
     * @param user The Current User (checked for dataset administration rights 
     * to the dataset within an access request as specified as a path param id
     * (Injected Token no need to pass)
     * @param filterID The filter ID for this request
     * 
     * @return An Organisation Access Request with a specific filter ID
     * 
     * @response.representation.200.qname OrganisationAccessRequest
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/{id}")
    @TypeHint(OrganisationAccessRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully returned the specified Organisation Access Request if the user has access to it"),
        @ResponseCode(code = 403, condition = "Current user does not have rights to this organisation access request")
    })      
    @Produces(MediaType.APPLICATION_JSON)
    public OrganisationAccessRequest getRequest(@TokenOrganisationAccessRequestAdminUser(path="id") User user, @PathParam("id") int filterID) {
        return oOrganisationAccessRequestMapper.getRequest(filterID);
    }
    
    /**
     * Update a specified Organisation Access Request 
     * 
     * @param user The current user (must have admin rights over the request) 
     * (Injected Token no need to pass)
     * @param filterID The filter ID for this request
     * @param action The action to take on this request
     * @param reason The reason this action has been taken
     * @param expires An optional expiry date for the access
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @throws ParseException 
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @POST
    @Path("/requests/{id}")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Operation was successfully completed"),
        @ResponseCode(code = 403, condition = "Current user does not have rights over this request"),
        @ResponseCode(code = 500, condition = "Operation was not successfully compelted, ensure the request was correctly formed")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRequest(@TokenOrganisationAccessRequestAdminUser(path="id") User user
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
            return revokeRequest(user, filterID, reason, false);
        } else {
            return Response.serverError().build();
        }
    }

    /**
     * SysAdmin function to reset accesses
     * @param user
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
    public List<OrganisationAccessRequest> getExpired(@TokenSystemAdministratorUser User user) {
        return oOrganisationAccessRequestMapper.getExpiredRequests();
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
            @PathParam("id") int filterID,
            @QueryParam("silent") @DefaultValue("false") boolean silent) {
        try {
            revokeRequest(user, filterID, "Request Expired", silent);
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
     * @param user Current User (Must be a dataset administrator) (Injected 
     * Token no need to pass)
     * @param dataset The dataset being queried
     * 
     * @return A list of history elements
     * 
     * @response.representation.200.qname List<OrganisationAccessRequestAuditHistory>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests/history/{dataset}")
    @TypeHint(OrganisationAccessRequestAuditHistory.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully returned a list of audit history objects"),
        @ResponseCode(code = 403, condition = "The current user does not have rights over this dataset")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationAccessRequestAuditHistory> getHistory(@TokenDatasetAdminUser(path="dataset") User user, @PathParam("dataset") String dataset) {
        return oOrganisationAccessRequestAuditHistoryMapper.getHistory(dataset);
    }

    /**
     * Accept a given organisation access request 
     * 
     * @param filterID The Filter ID of a request
     * @param reason Any reason given for acceptance of the request
     * @param expires An expiry date for this access request (Optional)
     * @param proactive If the request was a proactive from the dataset admin 
     * (true) or was a request made by a user (false)
     * 
     * @return Response to be passed back to the user
     * @throws ParseException 
     */
    private Response acceptRequest(User user, int filterID, String reason, String expires, boolean proactive) throws ParseException, IOException, TemplateException {
        OrganisationAccessRequest oar = oOrganisationAccessRequestMapper.getRequest(filterID);
        
        AccessRequestJSON accessRequestJSON = parseJSON(oar.getFilter().getFilterJSON());
        // Basic filter validity checks
        checkJSONFilterForValidity(accessRequestJSON);
        // Check that at least one record would be granted by this access request being granted
        checkForRecordsReturnedSingleDataset(User.PUBLIC_USER, accessRequestJSON, oar.getDatasetKey());
        
        giveAccess(oar);
        
        if (expires.isEmpty()) {
            oOrganisationAccessRequestMapper.acceptRequest(filterID, reason, new Timestamp(new java.util.Date().getTime()));
        } else {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat db_df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            java.util.Date expiresDate = df.parse(expires);
                
            Calendar expiresCal = Calendar.getInstance();
            Calendar requestCal = Calendar.getInstance();
            expiresCal.setTime(expiresDate);
            requestCal.setTime(accessRequestJSON.getTime().getDate());
            
            if (!(expiresCal.get(Calendar.YEAR) == requestCal.get(Calendar.YEAR)
                    && expiresCal.get(Calendar.DAY_OF_YEAR) == requestCal.get(Calendar.DAY_OF_YEAR))) {
            
                String origText = oar.getFilter().getFilterText();

                accessRequestJSON.getTime().setAll(false);
                accessRequestJSON.getTime().setDate(expiresDate);

                AccessRequestModifiedTimeJSON json = new AccessRequestModifiedTimeJSON(accessRequestJSON, db_df.format(expiresDate));

                ObjectMapper om = new ObjectMapper();
                // Stop nulls being pushed out to the json output
                om.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
                ObjectWriter ow = om.writer();

                TaxonObservationFilter filter = accessRequestUtils.createFilter(ow.writeValueAsString(json), accessRequestJSON);

                oTaxonObservationFilterMapper.editFilter(filterID, filter.getFilterText(), ow.writeValueAsString(json));
                oOrganisationAccessRequestAuditHistoryMapper.addHistory(filterID, user.getId(), "Edit request to: '" + filter.getFilterText() + "', from: '" + origText + "'");
            }
            oOrganisationAccessRequestMapper.acceptRequestWithExpires(filterID, reason, new Timestamp(new java.util.Date().getTime()), new Date(expiresDate.getTime()));
        }

        oOrganisationAccessRequestAuditHistoryMapper.addHistory(filterID, user.getId(), "Accept request");
        mailRequestGrant(oOrganisationAccessRequestMapper.getRequest(filterID), reason, proactive);
        return Response.status(Response.Status.OK).entity("{}").build();
    }

    /**
     * Deny a given organisation access request 
     * 
     * @param filterID The Filter ID of a request
     * @param reason Any reason given for denial of the request
     * @return Response to be passed back to the user
     * @throws ParseException 
     */
    private Response denyRequest(User user, int filterID, String reason) throws IOException, TemplateException {
        oOrganisationAccessRequestMapper.denyRequest(filterID, reason, new Timestamp(new java.util.Date().getTime()));
        oOrganisationAccessRequestAuditHistoryMapper.addHistory(filterID, user.getId(), "Deny request");            
        mailRequestDeny(oOrganisationAccessRequestMapper.getRequest(filterID), reason);
        return Response.status(Response.Status.OK).entity("{}").build();
    }

    /**
     * Close a given organisation access request
     *
     * @param filterID The Filter ID of a request
     * @param reason Any reason given for closure of the request
     * @return Response to be passed back to the user
     * @throws ParseException
     */
    private Response closeRequest(User user, int filterID, String reason) {
        oOrganisationAccessRequestMapper.closeRequest(filterID, reason, new Timestamp(new java.util.Date().getTime()));
        oOrganisationAccessRequestAuditHistoryMapper.addHistory(filterID, user.getId(), "Close request");            
        return Response.status(Response.Status.OK).entity("{}").build();
    }

    /**
     * Revoke a given organisation access request 
     * 
     * @param filterID The Filter ID of a request
     * @param reason Any reason given for revocation of the request
     * @return Response to be passed back to the user
     * @throws ParseException 
     */
    private Response revokeRequest(User user, int filterID, String reason, boolean silent) throws IOException, TemplateException {
        stripAccess(filterID, user, reason);
        if (!silent) {
            mailRequestRevoke(oOrganisationAccessRequestMapper.getRequest(filterID), reason);
        }
        return Response.status(Response.Status.OK).entity("{}").build();
    }

    private boolean resetAllAccess(String dataset) throws IOException {
        oOrganisationTaxonObservationAccessMapper.removeAllOrganisationAccessForDataset(dataset);
        List<OrganisationAccessRequest> uars = oOrganisationAccessRequestMapper.getGrantedRequestsByDataset(dataset);
        
        for (OrganisationAccessRequest req : uars) {
            giveAccess(req);
        }
        return true;
        
    }
    
    /**
     * Strip access granted by a particular request
     * 
     * @param id The ID of the request to be actioned on
     * @return A boolean denoting the success of this operation
     * @throws IOException 
     */
    private boolean stripAccess(int id, User user, String reason) throws IOException {
        OrganisationAccessRequest uar = oOrganisationAccessRequestMapper.getRequest(id);
        AccessRequestJSON accessRequest = parseJSON(uar.getFilter().getFilterJSON());

        List<String> species = accessRequestUtils.createSpeciesList(accessRequest);        
        List<String> datasets = new ArrayList<String>();
        datasets.add(uar.getDatasetKey());
        oOrganisationTaxonObservationAccessMapper.removeOrganisationAccess(uar.getOrganisation(), accessRequest.getYear().getStartYear(), accessRequest.getYear().getEndYear(), datasets, species, accessRequest.getSpatial().getMatch(), accessRequest.getSpatial().getFeature(), (accessRequest.getSensitive().equals("sans") ? true : false), accessRequest.getTaxon().getDesignation(), accessRequest.getTaxon().getOutput(), accessRequest.getTaxon().getOrgSuppliedList(), accessRequest.getSpatial().getGridRef(), "");

        oOrganisationAccessRequestMapper.revokeRequest(id, reason, new Timestamp(new java.util.Date().getTime()));
        oOrganisationAccessRequestAuditHistoryMapper.addHistory(id, user.getId(), "Revoke action");

        List<OrganisationAccessRequest> uars = oOrganisationAccessRequestMapper.getGrantedOrganisationRequestsByDataset(uar.getDatasetKey(), uar.getOrganisation().getId());
        
        for (OrganisationAccessRequest req : uars) {
            giveAccess(req);
        }
        return true;
    }

    /**
     * Give access according to a given request id
     * 
     * @param id The request ID to be actioned
     * @return A boolean denoting the success of this operation
     * @throws IOException 
     */
    private boolean giveAccess(int id) throws IOException {
        OrganisationAccessRequest uar = oOrganisationAccessRequestMapper.getRequest(id);
        return giveAccess(uar);
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

    private void mailRequestCreate(OrganisationAccessRequest request, User user) throws IOException, TemplateException {
        List<DatasetAdministrator> admins = datasetAdministratorMapper.selectByDataset(request.getDatasetKey());
        
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("rName", user.getForename() + " " + user.getSurname());
        message.put("oName", request.getOrganisation().getName());
        message.put("reason", request.getRequestReason());
        message.put("details", request.getFilter().getFilterText());
        message.put("orgReq", Boolean.TRUE);
        message.put("dataset", request.getDataset().getTitle());
        message.put("purpose", request.getRequestPurposeLabel());        
        
        for (DatasetAdministrator admin : admins) {
            message.put("daName", admin.getUser().getForename() + " " + admin.getUser().getSurname());
            mailer.send("accessRequestMade.ftl", admin.getUser().getEmail(), "NBN Gateway: New access request", message);
        }
    }

    private void mailRequestDeny(OrganisationAccessRequest request, String reason) throws IOException, TemplateException {
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("rName", request.getOrganisation().getName());
        message.put("rDate", request.getRequestDate());
        message.put("reason", reason);
        message.put("details", request.getFilter().getFilterText());
        message.put("dataset", request.getDataset().getTitle());
        
        List<OrganisationMembership> admins = oOrganisationMembershipMapper.selectAdminsByOrganisation(request.getOrganisation().getId());
        for (OrganisationMembership admin : admins) {
            mailer.send("accessRequestDeny.ftl", admin.getUser().getEmail(), "NBN Gateway: Access request declined", message);
        }
    }

    private void mailRequestGrant(OrganisationAccessRequest request, String reason, boolean proactive) throws IOException, TemplateException {
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("rName", request.getOrganisation().getName());
        message.put("rDate", request.getRequestDate());
        message.put("lDate", request.getAccessExpires());
        message.put("reason", reason);
        message.put("details", request.getFilter().getFilterText());
        message.put("dataset", request.getDataset().getTitle());
        message.put("org", true);

        List<OrganisationMembership> admins = oOrganisationMembershipMapper.selectAdminsByOrganisation(request.getOrganisation().getId());
        for (OrganisationMembership admin : admins) {
            if (proactive) {
                mailer.send("accessProactiveGrant.ftl", admin.getUser().getEmail(), "NBN Gateway: Access granted", message);
            } else {
                mailer.send("accessRequestGrant.ftl", admin.getUser().getEmail(), "NBN Gateway: Access request approved", message);
            }
        }
    }

    private void mailRequestRevoke(OrganisationAccessRequest request, String reason) throws IOException, TemplateException {
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("rName", request.getOrganisation().getName());
        message.put("rDate", request.getRequestDate());
        message.put("reason", reason);
        message.put("details", request.getFilter().getFilterText());
        message.put("dataset", request.getDataset().getTitle());
        
        List<OrganisationMembership> admins = oOrganisationMembershipMapper.selectAdminsByOrganisation(request.getOrganisation().getId());
        for (OrganisationMembership admin : admins) {
            mailer.send("accessRequestRevoke.ftl", admin.getUser().getEmail(), "NBN Gateway: Access request removed", message);
        }
    }
}

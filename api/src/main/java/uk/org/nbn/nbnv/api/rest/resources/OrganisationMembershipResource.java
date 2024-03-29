/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationJoinRequestMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.mail.TemplateMailer;
import uk.org.nbn.nbnv.api.model.OrganisationJoinRequest;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.meta.OrganisationAddRemoveUserJSON;
import uk.org.nbn.nbnv.api.model.meta.OrganisationJoinRequestJSON;
import uk.org.nbn.nbnv.api.model.meta.UserRoleChangeJSON;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenAnyDatasetOrOrgAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenOrganisationJoinRequestUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenOrganisationUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;

/**
 *
 * @author Paul Gilbertson
 */
@Component
@Path("/organisationMemberships")
public class OrganisationMembershipResource extends AbstractResource {

    @Autowired OrganisationMembershipMapper organisationMembershipMapper;
    @Autowired OrganisationMapper organisationMapper;
    @Autowired OperationalOrganisationMembershipMapper oOrganisationMembershipMapper;
    @Autowired OperationalOrganisationJoinRequestMapper oOrganisationJoinRequestMapper;
    @Autowired TemplateMailer mailer;

    /**
     * Return a list of all organisation memberships from the data warehouse
     * 
     * @return A list of all organisation memberships from the data warehouse
     * 
     * @response.representation.200.qname List<OrganisationMembership>
     * @response.representation.200.mediaType application/json
     */
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<OrganisationMembership> get() {
//        return organisationMembershipMapper.selectAll();
//    }

    /**
     * Return a list of organisation memberships for a given organisation from 
     * the core database
     * 
     * @param user The current user (must be an admin) (Injected Token no need 
     * to pass)
     * @param id An organisation ID
     * 
     * @return A list of organisation memberships for a given organisation from 
     * the core database
     * 
     * @response.representation.200.qname List<OrganisationMembership>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}")
    @TypeHint(OrganisationMembership.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully returned all organsiation memberships for the specified organisation"),
        @ResponseCode(code = 403, condition = "The user is not an administrator of this organisation")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationMembership> get(@TokenOrganisationUser(path = "id", roles = OrganisationMembership.Role.administrator) User user, @PathParam("id") int id) {
        return oOrganisationMembershipMapper.selectByOrganisation(id);
    }
    
    @GET
    @Path("/{id}/users")
    @TypeHint(User.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned all organisation members for specified organistion"),
        @ResponseCode(code = 403, condition = "The user is not an administrator of any organisation or dataset")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUserList(@TokenAnyDatasetOrOrgAdminUser() User user, @PathParam("id") int id) {
        return organisationMembershipMapper.selectUserListByOrganisation(id);
    }

    /**
     * Return the organisation membership details for a given user and 
     * organisation from the core database
     * 
     * @param user The current user (must be an admin of any dataset or 
     * organisation) (Injected Token no need to pass)
     * @param id An organisation ID
     * @param user A Users ID
     * 
     * @return The organisation membership details for a given user and 
     * organisation from the core database
     * 
     * @response.representation.200.qname OrganisationMembership
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/{user}")
    @TypeHint(OrganisationMembership.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned request user organisation membership"),
        @ResponseCode(code = 403, condition = "The current user is not an administrator of any dataset or organisation")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    public OrganisationMembership getSpecificUser(@TokenAnyDatasetOrOrgAdminUser() User user, @PathParam("id") int id, @PathParam("user") int userID) {
        if (oOrganisationMembershipMapper.isUserMemberOfOrganisation(userID, id)) {
            return oOrganisationMembershipMapper.selectByUserAndOrganisation(userID, id);
        }

        return null;
    }

    /**
     * Return true if a user is a member of a given organisation from the core 
     * database
     * 
     * @param user The current user (Injected Token no need to pass)
     * @param id An organisation ID
     * 
     * @return True if a user is a member of a given organisation from the core 
     * database
     * 
     * @response.representation.200.qname boolean
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/isMember")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Returned if the selected user is a member of this organisation"),
        @ResponseCode(code = 401, condition = "No current user is logged in")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isMemberOfOrganisation(@TokenUser(allowPublic = false) User user, @PathParam("id") int id) {
        if (oOrganisationMembershipMapper.isUserMemberOfOrganisation(user.getId(), id)) {
            return oOrganisationMembershipMapper.isUserMemberOfOrganisation(user.getId(), id);
        }

        return false;
    }

    /**
     * Return true if a user is an admin of a given organisation from the core 
     * database
     * 
     * @param user The current user (Injected Token no need to pass)
     * @param id An organisation ID
     * 
     * @return True if a user is an admin of a given organisation from the core 
     * database
     * 
     * @response.representation.200.qname boolean
     * @response.representation.200.mediaType application/json
     */    
    @GET
    @Path("/{id}/isadmin")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Returned if the selected user is an admin of this organisation"),
        @ResponseCode(code = 401, condition = "No current user is logged in")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isUserOrgAdmin(@TokenUser(allowPublic = false) User user, @PathParam("id") int id) {
        if (oOrganisationMembershipMapper.isUserMemberOfOrganisation(user.getId(), id)) {
            if (oOrganisationMembershipMapper.selectByUserAndOrganisation(user.getId(), id).getRole() == OrganisationMembership.Role.administrator) {
                return true;
            }
        }

        return false;
    }

    /**
     * Add a user directly to the organisation from the admin panel, requires a
     * user who has admin rights over the organisation
     * 
     * @param user The current user (Must be an admin or returns 403 Forbidden)
     * (Injected Token no need to pass)
     * @param orgId An organisation ID
     * @param data The user to be added to the organisation
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @POST
    @Path("/{id}/addUser")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Operation completed successfully"),
        @ResponseCode(code = 403, condition = "The current user is not an organisation administrator")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUserFromAdminPanel(@TokenOrganisationUser(path = "id", roles = OrganisationMembership.Role.administrator) User user, @PathParam("id") int orgId, OrganisationAddRemoveUserJSON data) {
        oOrganisationMembershipMapper.addUser(data.getUserID(), orgId);
        return Response.status(Response.Status.OK).entity(data).build();
    }

    /**
     * Modify an existing users role in an organisation
     *
     * @param user The current user (must be an admin) (Injected Token no need 
     * to pass)
     * @param orgId The organisation ID
     * @param data A JSON data packet containing the user to modified and their
     * new role
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @POST
    @Path("/{id}/modifyUserRole")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully modified user role"),
        @ResponseCode(code = 403, condition = "The current user is not an organisation administrator")
    })    
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyUserRole(@TokenOrganisationUser(path = "id", roles = OrganisationMembership.Role.administrator) User user, @PathParam("id") int orgId, UserRoleChangeJSON data) {
        oOrganisationMembershipMapper.changeUserRole(data.getRole(), data.getUserID(), orgId);
        return Response.status(Response.Status.OK).entity(data).build();
    }

    /**
     * Remove the specified user from a given organisation if the current user
     * is an org admin
     *
     * @param user The current user (must be an admin) (Injected Token no need 
     * to pass)
     * @param orgId The organisation ID
     * @param data A JSON data packet containing the user to removed
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @POST
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully removed user from organistaion"),
        @ResponseCode(code = 403, condition = "The current user is not an organisation administrator")
    })
    @Path("/{id}/removeUser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyUserRole(@TokenOrganisationUser(path = "id", roles = OrganisationMembership.Role.administrator) User user, @PathParam("id") int orgId, OrganisationAddRemoveUserJSON data) {
        oOrganisationMembershipMapper.removeUser(data.getUserID(), orgId);
        return Response.status(Response.Status.OK).entity(data).build();
    }

    /**
     * Get any active request (i.e. responseTypeID is NULL) for the current user
     * of the specified organisation
     *
     * @param user The current user (must be an admin) (Injected Token no need 
     * to pass)
     * @param orgID The organisation id
     * 
     * @return An Organisation Join Request made by the user for the specified
     * organisation or a null response ID if no request has been made
     * 
     * @response.representation.200.qname OrganisationJoinRequest
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/join")
    @TypeHint(OrganisationJoinRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned selected join request for this user and the selected organisation OR may contain error, check returned object")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public OrganisationJoinRequest getJoinRequests(@TokenUser(allowPublic = false) User user, @PathParam("id") int orgID) {
        if (oOrganisationJoinRequestMapper.activeOrganisationJoinRequestByUserExists(user.getId(), orgID)) {
            return oOrganisationJoinRequestMapper.getActiveJoinRequestByUserAndOrganisation(user.getId(), orgID);
        }

        // No request exists, so return a request with -1 requestTypeID
        OrganisationJoinRequest req = new OrganisationJoinRequest();
        req.setError();

        return req;
    }

    /**
     * Create a new organisation join request for a given organisation, return a
     * status code
     *
     * @param user The current user (Injected Token no need to pass)
     * @param orgID The Organisation ID
     * @param data A JSON packet containing a reason for the request
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @PUT
    @Path("/{id}/join")
    @StatusCodes({
        @ResponseCode(code = 201, condition = "Successfully created this join request"),
        @ResponseCode(code = 303, condition = "A request to join this organisation already exists"),
        @ResponseCode(code = 400, condition = "The current user is already a member of this organisation"),
        @ResponseCode(code = 403, condition = "The current user is not logged in")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@TokenUser(allowPublic = false) User user, @PathParam("id") int orgID, OrganisationJoinRequestJSON data) throws IOException, TemplateException {
        if (oOrganisationMembershipMapper.isUserMemberOfOrganisation(user.getId(), orgID)) {
            data.setReason("Already a member of this organisation");
            return Response.status(Response.Status.BAD_REQUEST).entity(data).build();
        }

        if (oOrganisationJoinRequestMapper.activeOrganisationJoinRequestByUserExists(user.getId(), orgID)) {
            data.setReason("A request to join this organistion already exists");
            return Response.status(Response.Status.SEE_OTHER).entity(data).build();
        }

        oOrganisationJoinRequestMapper.createJoinRequest(user.getId(), orgID, data.getReason(), new java.sql.Date(new java.util.Date().getTime()));
        
        List<OrganisationMembership> admins = 
        oOrganisationMembershipMapper.selectAdminsByOrganisation(orgID);
        for (OrganisationMembership admin : admins) {
            OrganisationJoinRequest request = new OrganisationJoinRequest();
            request.setUser(user);
            request.setRequestReason(data.getReason());
            request.setOrganisation(organisationMapper.selectByID(orgID));
            sendJoinRequestToAdmins(request, admin.getUser());           
        }
        
        return Response.status(Response.Status.CREATED).entity(data).build();
    }

    /**
     * Return all active requests to join this organisation (i.e. have
     * responseTypeID of NULL) if the user is the org admin of the specified
     * organisation
     *
     * @param user The Current user (must be an admin) (Injected Token no need 
     * to pass)
     * @param orgId The Organisation ID
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/requests")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned all active join requests for this organisation"),
        @ResponseCode(code = 403, condition = "The current user is not an admin for this organisation")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveJoinRequests(@TokenOrganisationUser(path = "id", roles = OrganisationMembership.Role.administrator) User user, @PathParam("id") int orgId) {
        return Response.status(Response.Status.OK).entity(oOrganisationJoinRequestMapper.getActiveJoinRequestsByOrganisation(orgId)).build();
    }

    /**
     * Return all active requests to join organisations
     *
     * @param user The Current user (Injected Token no need to pass)
     * 
     * @return The list of open organisation requests
     * 
     * @response.representation.200.qname List<OrganisationJoinRequest>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/requests")
    @TypeHint(OrganisationJoinRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the current users active join requests"),
        @ResponseCode(code = 403, condition = "The current user is not logged in")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationJoinRequest> getUserActiveRequest(@TokenUser(allowPublic=false) User user) {
        return oOrganisationJoinRequestMapper.getActiveJoinRequestsByUser(user.getId());
    }
    
    /**
     * Return a specific request by its ID, if the user can access it i.e. org
     * Admin or requesting user otherwise return a 403 Forbidden error
     *
     * @param user The Current User (must be an admin or requesting user) 
     * (Injected Token no need to pass)
     * @param reqId The request id
     * 
     * @return A specific organisation join request by its ID, if the user can 
     * access it
     * 
     * @response.representation.200.qname OrganisationJoinRequest
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/request/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @TypeHint(OrganisationJoinRequest.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the requested join request"),
        @ResponseCode(code = 403, condition = "The current user does not have any rights over this join request (i.e. is neither the requestor or a member of the organisation in question)")
    })
    public OrganisationJoinRequest getJoinRequestByID(@TokenOrganisationJoinRequestUser(path = "id") User user, @PathParam("id") int reqId) {
        return oOrganisationJoinRequestMapper.getJoinRequestByID(reqId);
    }

    /**
     * Post a response to an existing organisation join request, can be either
     * an accept / deny (org admin) or a withdraw (requesting User), if the call
     * has is incorrect (json passed is wrong) return a BAD_REQUEST response. If
     * the request is made by someone who is neither the requesting user or an
     * org admin of the organisation in this request then a FORBIDDEN response
     * should be shown.
     *
     * @param user The current user (Injected Token no need to pass)
     * @param id The id of the request in the database
     * @param data A JSON data packet containing information about the type of
     * response and associated reasons for it
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @throws IOException Errors associated with the Mailer
     * @throws TemplateException Errors associated with the Mailer
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json
     */
    @POST
    @Path("/request/{id}")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Accepted or denied join request successfully"),
        @ResponseCode(code = 202, condition = "Accepted or denied join request successfully, but could not send notification email"),
        @ResponseCode(code = 400, condition = "Request was not well formed, please correct it and try again"),
        @ResponseCode(code = 403, condition = "The current user does not have any rights over this join request (i.e. is neither the requestor or a member of the organisation in question)")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response action(@TokenOrganisationJoinRequestUser(path = "id") User user, @PathParam("id") int id, OrganisationJoinRequestJSON data) throws IOException, TemplateException {
        OrganisationJoinRequest request = oOrganisationJoinRequestMapper.getJoinRequestByID(id);

        if (oOrganisationMembershipMapper.isUserOrganisationAdmin(user.getId(), request.getOrganisation().getId())) {
            if (data.getResponseType() == 1) {
                // Accept the request
                oOrganisationJoinRequestMapper.acceptJoinRequest(data.getId(), data.getReason(), new java.sql.Date(new java.util.Date().getTime()));
                // Add the user to the organisation
                oOrganisationMembershipMapper.addUser(request.getUser().getId(), request.getOrganisation().getId());
                
                // Send email response to the requesting user, saying that the request was accepted
                sendEmail(request, "organisation-join-accept.ftl", request.getUser().getEmail(),
                        "NBN Gateway: You are now a member of " + request.getOrganisation().getName());

                return Response.status(Response.Status.OK).entity(data).build();
            } else if (data.getResponseType() == 2) {
                // Deny the request
                oOrganisationJoinRequestMapper.denyJoinRequest(data.getId(), data.getReason(), new java.sql.Date(new java.util.Date().getTime()));
                try {
                    // Send email response to the requesting user, saying that the request was denied
                    sendEmail(request, "organisation-join-deny.ftl", request.getUser().getEmail(),
                            "NBN Gateway: Your request to join " + request.getOrganisation().getName() + " was denied");
                } catch (Exception ex) {
                    return Response.status(Response.Status.ACCEPTED).entity(ex).build();
                }

                return Response.status(Response.Status.OK).entity(data).build();
            }
        } else if (user.getId() == oOrganisationJoinRequestMapper.getJoinRequestByID(data.getId()).getUser().getId()) {
            if (data.getResponseType() == 3) {
                // Withdraw the request
                oOrganisationJoinRequestMapper.withdrawJoinRequest(data.getId(), new java.sql.Date(new java.util.Date().getTime()));
                return Response.status(Response.Status.OK).entity(data).build();
            }
        }
        
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    // TODO: Add explanation to this to the user feedback
    /**
     * E-mail the user with a response to their request
     * 
     * @param request The join request in question
     * @param template The email template that should be used
     * @param email The users email address
     * @param subject The subject line of the email
     * @throws IOException
     * @throws TemplateException 
     */
    private void sendEmail(OrganisationJoinRequest request, String template, String email, String subject) throws IOException, TemplateException {
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("portal", properties.getProperty("portal_url"));
        message.put("name", request.getUser().getForename());
        message.put("organisation", request.getOrganisation().getName());
        message.put("organisationID", request.getOrganisation().getId());
        message.put("responseReason", request.getRequestReason());

        mailer.send(template, request.getUser().getEmail(), subject, message);
    }
    
    private void sendJoinRequestToAdmins(OrganisationJoinRequest request, User admin) throws IOException, TemplateException {
        Map<String, Object> message = new HashMap<String,Object>();
        message.put("portal", properties.getProperty("portal_url"));
        message.put("name", admin.getForename());
        message.put("requestor", request.getUser().getForename() + " " + request.getUser().getSurname());
        message.put("reason", request.getRequestReason());
        message.put("organisation", request.getOrganisation().getName());
        
        mailer.send("organistaion-join-admin-notify.ftl", admin.getEmail(), "NBN Gateway: A user has requested to join your organisation", message);
    }
}

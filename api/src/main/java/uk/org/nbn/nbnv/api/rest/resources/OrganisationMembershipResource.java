/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.net.SocketException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationJoinRequestMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.mail.TemplateMailer;
import uk.org.nbn.nbnv.api.model.OrganisationJoinRequest;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.meta.OrganisationAddRemoveUserJSON;
import uk.org.nbn.nbnv.api.model.meta.OrganisationJoinRequestJSON;
import uk.org.nbn.nbnv.api.model.meta.UserRoleChangeJSON;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;

/**
 *
 * @author Paul Gilbertson
 */
@Component
@Path("/organisationMemberships")
public class OrganisationMembershipResource extends AbstractResource {

    @Autowired OrganisationMembershipMapper organisationMembershipMapper;
    @Autowired OperationalOrganisationMembershipMapper oOrganisationMembershipMapper;
    @Autowired OperationalOrganisationJoinRequestMapper oOrganisationJoinRequestMapper;
    @Autowired TemplateMailer mailer;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationMembership> get() {
        return organisationMembershipMapper.selectAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationMembership> get(@PathParam("id") int id) {
        return oOrganisationMembershipMapper.selectByOrganisation(id);
    }

    @GET
    @Path("/{id}/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public OrganisationMembership getSpecificUser(@PathParam("id") int id, @PathParam("user") int user) {
        if (oOrganisationMembershipMapper.isUserMemberOfOrganisation(user, id)) {
            return oOrganisationMembershipMapper.selectByUserAndOrganisation(user, id);
        }

        return null;
    }

    @GET
    @Path("/{id}/{user}/isMember")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isMemberOfOrganisation(@PathParam("id") int id, @PathParam("user") int user) {
        if (oOrganisationMembershipMapper.isUserMemberOfOrganisation(user, id)) {
            return oOrganisationMembershipMapper.isUserMemberOfOrganisation(user, id);
        }

        return false;
    }

    @GET
    @Path("/{id}/{user}/isadmin")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isUserOrgAdmin(@PathParam("id") int id, @PathParam("user") int user) {
        if (oOrganisationMembershipMapper.isUserMemberOfOrganisation(user, id)) {
            if (oOrganisationMembershipMapper.selectByUserAndOrganisation(user, id).getRole() == OrganisationMembership.Role.administrator) {
                return true;
            }
        }

        return false;
    }

    @POST
    @Path("/{id}/addUser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUserFromAdminPanel(@TokenUser(allowPublic = false) User user, @PathParam("id") int orgId, OrganisationAddRemoveUserJSON data) {
        if (oOrganisationMembershipMapper.isUserOrganisationAdmin(user.getId(), orgId)) {
            oOrganisationMembershipMapper.addUser(data.getUserID(), orgId);
            return Response.status(Response.Status.ACCEPTED).entity(data).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Modify an existing users role in an organisation
     *
     * @param user The current user (must be an admin)
     * @param orgId The organisation ID
     * @param data A JSON data packet containing the user to modified and their
     * new role
     * @return
     */
    @POST
    @Path("/{id}/modifyUserRole")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyUserRole(@TokenUser(allowPublic = false) User user, @PathParam("id") int orgId, UserRoleChangeJSON data) {
        if (oOrganisationMembershipMapper.isUserOrganisationAdmin(user.getId(), orgId)) {
            oOrganisationMembershipMapper.changeUserRole(data.getRole(), data.getUserID(), orgId);
            return Response.status(Response.Status.ACCEPTED).entity(data).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Remove the specified user from a given organisation if the current user
     * is an org admin
     *
     * @param user The current user (must be an admin)
     * @param orgId The organisation ID
     * @param data A JSON data packet containing the user to removed
     * @return
     */
    @POST
    @Path("/{id}/removeUser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyUserRole(@TokenUser(allowPublic = false) User user, @PathParam("id") int orgId, OrganisationAddRemoveUserJSON data) {
        if (oOrganisationMembershipMapper.isUserOrganisationAdmin(user.getId(), orgId)) {
            oOrganisationMembershipMapper.removeUser(data.getUserID(), orgId);
            return Response.status(Response.Status.ACCEPTED).entity(data).build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }

    /**
     * Get any active request (i.e. responseTypeID is NULL) for the current user
     * of the specified organisation
     *
     * @param user The current user (must be an admin)
     * @param orgID The organisation id
     * @return
     */
    @GET
    @Path("/{id}/join")
    @Produces(MediaType.APPLICATION_JSON)
    public OrganisationJoinRequest get(@TokenUser(allowPublic = false) User user, @PathParam("id") int orgID) {
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
     * @param user The current user
     * @param orgID The Organisation ID
     * @param data A JSON packet containing a reason for the request
     * @return
     */
    @PUT
    @Path("/{id}/join")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@TokenUser(allowPublic = false) User user, @PathParam("id") int orgID, OrganisationJoinRequestJSON data) {
        if (oOrganisationMembershipMapper.isUserMemberOfOrganisation(user.getId(), orgID)) {
            data.setReason("Already a member of this organisation");
            return Response.status(Response.Status.BAD_REQUEST).entity(data).build();
        }

        if (oOrganisationJoinRequestMapper.activeOrganisationJoinRequestByUserExists(user.getId(), orgID)) {
            data.setReason("A request to join this organistion already exists");
            return Response.status(Response.Status.SEE_OTHER).entity(data).build();
        }

        oOrganisationJoinRequestMapper.createJoinRequest(user.getId(), orgID, data.getReason(), new java.sql.Date(new java.util.Date().getTime()));
        return Response.status(Response.Status.CREATED).entity(data).build();
    }

    /**
     * Return all active requests to join this organisation (i.e. have
     * responseTypeID of NULL) if the user is the org admin of the specified
     * organisation
     *
     * @param user The Current user (must be an admin)
     * @param orgId The Organisation ID
     * @return
     */
    @GET
    @Path("/{id}/requests")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveJoinRequests(@TokenUser(allowPublic = false) User user, @PathParam("id") int orgId) {
        if (oOrganisationMembershipMapper.isUserOrganisationAdmin(user.getId(), orgId)) {
            return Response.status(Response.Status.OK).entity(oOrganisationJoinRequestMapper.getActiveJoinRequestsByOrganisation(orgId)).build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }

    /**
     * Return a specific request by its ID, if the user can access it i.e. org
     * Admin or requesting user
     *
     * @param user The Current User (must be an admin or requesting user)
     * @param reqId The request id
     * @return A Response containing the Request or an error code
     */
    @GET
    @Path("/request/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJoinRequestByID(@TokenUser(allowPublic = false) User user, @PathParam("id") int reqId) {
        try {
            OrganisationJoinRequest req = oOrganisationJoinRequestMapper.getActiveJoinRequestByID(reqId);
            if (user.getId() == req.getUser().getId()
                    || oOrganisationMembershipMapper.isUserOrganisationAdmin(user.getId(), req.getOrganisation().getId())) {
                return Response.status(Response.Status.OK).entity(req).build();
            }

            return Response.status(Response.Status.FORBIDDEN).build();
        } catch (NullPointerException ex) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Post a response to an existing organisation join request, can be either
     * an accept / deny (org admin) or a withdraw (requesting User), if the call
     * has is incorrect (json passed is wrong) return a BAD_REQUEST response. If
     * the request is made by someone who is neither the requesting user or an
     * org admin of the organisation in this request then a FORBIDDEN response
     * should be shown.
     *
     * @param user The current user
     * @param id The id of the request in the database
     * @param data A JSON data packet containing information about the type of
     * response and associated reasons for it
     * @return A Response containing any feedback from the REST services, i.e.
     * request status
     * @throws IOException Errors associated with the Mailer
     * @throws TemplateException Errors associated with the Mailer
     */
    @POST
    @Path("/request/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response action(@TokenUser(allowPublic = false) User user, @PathParam("id") int id, OrganisationJoinRequestJSON data) {
        OrganisationJoinRequest request = oOrganisationJoinRequestMapper.getActiveJoinRequestByID(id);


        if (oOrganisationMembershipMapper.isUserOrganisationAdmin(user.getId(), request.getOrganisation().getId())) {
            if (data.getResponseType() == 1) {
                // Accept the request
                oOrganisationJoinRequestMapper.acceptJoinRequest(data.getId(), data.getReason(), new java.sql.Date(new java.util.Date().getTime()));
                // Add the user to the organisation
                oOrganisationMembershipMapper.addUser(request.getUser().getId(), request.getOrganisation().getId());
                try {
                    // Send email response to the requesting user, saying that the request was accepted
                    sendEmail(request, "organisation-join-accept.ftl", request.getUser().getEmail(),
                            "NBN Gateway: You are now a member of " + request.getOrganisation().getName());
                    return Response.status(Response.Status.OK).entity(data).build();
                } catch (Exception ex) {
                    return Response.status(Response.Status.ACCEPTED).entity(ex).build();
                }
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
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } else if (user.getId() == oOrganisationJoinRequestMapper.getActiveJoinRequestByID(data.getId()).getUser().getId()) {
            if (data.getResponseType() == 3) {
                // Withdraw the request
                oOrganisationJoinRequestMapper.withdrawJoinRequest(data.getId(), new java.sql.Date(new java.util.Date().getTime()));
                return Response.status(Response.Status.OK).entity(data).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }

        return Response.status(Response.Status.UNAUTHORIZED).entity(data).build();
    }

    // TODO: Add explanation to this to the user feedback
    private void sendEmail(OrganisationJoinRequest request, String template, String email, String subject) throws IOException, TemplateException {
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("portal", properties.getProperty("portal_url"));
        message.put("name", request.getUser().getForename());
        message.put("organisation", request.getOrganisation().getName());
        message.put("organisationID", request.getOrganisation().getId());
        message.put("responseReason", request.getRequestReason());

        mailer.send(template, request.getUser().getEmail(), subject, message);
    }
}

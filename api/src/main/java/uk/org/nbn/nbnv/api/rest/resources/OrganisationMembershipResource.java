/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
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
    
    @POST
    @Path("/{id}/modifyUserRole")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyUserRole(@TokenUser(allowPublic=false) User user, @PathParam("id") int orgId, UserRoleChangeJSON data) {
        if (oOrganisationMembershipMapper.isUserOrganisationAdmin(user.getId(), orgId)) {
            oOrganisationMembershipMapper.changeUserRole(data.getRole(), data.getUserID(), orgId);
            return Response.status(Response.Status.ACCEPTED).entity(data).build();
        } 
        
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    
    @POST
    @Path("/{id}/removeUser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyUserRole(@TokenUser(allowPublic=false) User user, @PathParam("id") int orgId, OrganisationAddRemoveUserJSON data) {
        if (oOrganisationMembershipMapper.isUserOrganisationAdmin(user.getId(), orgId)) {
            oOrganisationMembershipMapper.removeUser(data.getUserID(), orgId);
            return Response.status(Response.Status.ACCEPTED).entity(data).build();
        } 
        
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/{id}/join")
    @Produces(MediaType.APPLICATION_JSON)
    public OrganisationJoinRequest get(@TokenUser(allowPublic = false) User user, @PathParam("id") int orgID) {
        if (oOrganisationJoinRequestMapper.activeOrganisationJoinRequestByUserExists(user.getId(), orgID))
            return oOrganisationJoinRequestMapper.getActiveJoinRequestByUserAndOrganisation(user.getId(), orgID);
        
        // No request exists, so return a request with -1 requestTypeID
        OrganisationJoinRequest req = new OrganisationJoinRequest();
        req.setError();

        return req;
    }

    @POST
    @Path("/join/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response action(@TokenUser(allowPublic = false) User user, @PathParam("id") int id, OrganisationJoinRequestJSON data) {
        OrganisationJoinRequest request = oOrganisationJoinRequestMapper.getActiveJoinRequestByID(id);
        if (oOrganisationMembershipMapper.isUserOrganisationAdmin(user.getId(), request.getOrganisation().getId())) {
            if (data.getResponseType() == 1) {
                oOrganisationJoinRequestMapper.acceptJoinRequest(data.getId(), data.getReason(), new java.sql.Date(new java.util.Date().getTime()));
                oOrganisationMembershipMapper.addUser(request.getUser().getId(), request.getOrganisation().getId());
                return Response.status(Response.Status.OK).entity(data).build();
            } else if (data.getResponseType() == 2) {
                oOrganisationJoinRequestMapper.denyJoinRequest(data.getId(), data.getReason(), new java.sql.Date(new java.util.Date().getTime()));
                return Response.status(Response.Status.OK).entity(data).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } else if (user.getId() == oOrganisationJoinRequestMapper.getActiveJoinRequestByID(data.getId()).getUser().getId()) {
            if (data.getResponseType() == 3) {
                oOrganisationJoinRequestMapper.withdrawJoinRequest(data.getId(), new java.sql.Date(new java.util.Date().getTime()));
                return Response.status(Response.Status.OK).entity(data).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }

        return Response.status(Response.Status.UNAUTHORIZED).entity(data).build();
    }

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

    @GET
    @Path("/{id}/join/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveJoinRequests(@TokenUser(allowPublic = false) User user, @PathParam("id") int orgId) {
        if (oOrganisationMembershipMapper.isUserOrganisationAdmin(user.getId(), orgId)) {
            return Response.status(Response.Status.OK).entity(oOrganisationJoinRequestMapper.getActiveJoinRequestsByOrganisation(orgId)).build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/join/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJoinRequestByID(@TokenUser(allowPublic = false) User user, @PathParam("id") int reqId) {
        try {
            OrganisationJoinRequest req = oOrganisationJoinRequestMapper.getActiveJoinRequestByID(reqId);
            if (user.getId() == req.getUser().getId() ||
                    oOrganisationMembershipMapper.isUserOrganisationAdmin(user.getId(), req.getOrganisation().getId())) {
                return Response.status(Response.Status.OK).entity(req).build();
            }
            
            return Response.status(Response.Status.FORBIDDEN).build();
        } catch (NullPointerException ex) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}

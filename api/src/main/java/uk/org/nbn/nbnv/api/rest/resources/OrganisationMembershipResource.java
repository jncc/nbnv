/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.meta.OrganisationAddRemoveUserJSON;
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
        return organisationMembershipMapper.selectByOrganisation(id);
    }
    
    @GET
    @Path("/{id}/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public OrganisationMembership getSpecificUser(@PathParam("id") int id, @PathParam("user") int user) {
        if (organisationMembershipMapper.isUserMemberOfOrganisation(user, id)) {
            return organisationMembershipMapper.selectByUserAndOrganisation(user, id);
        }
        
        return null;
    }
    
    @GET
    @Path("/{id}/{user}/isadmin")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isUserOrgAdmin(@PathParam("id") int id, @PathParam("user") int user) {
        if (organisationMembershipMapper.isUserMemberOfOrganisation(user, id)) {
            if (organisationMembershipMapper.selectByUserAndOrganisation(user, id).getRole() == OrganisationMembership.Role.administrator) {
                return true;
            }
        }
        
        return false;
    }

    @POST
    @Path("/{id}/addUser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUserFromAdminPanel(@TokenUser(allowPublic = false) User user, @PathParam("id") int orgId, OrganisationAddRemoveUserJSON data) {
        if (organisationMembershipMapper.isUserOrganisationAdmin(user.getId(), orgId)) {
            oOrganisationMembershipMapper.addUser(data.getUserID(), orgId);
            return Response.status(Response.Status.ACCEPTED).entity(data).build();
        }
        
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    
    @POST
    @Path("/{id}/modifyUserRole")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyUserRole(@TokenUser(allowPublic=false) User user, @PathParam("id") int orgId, UserRoleChangeJSON data) {
        if (organisationMembershipMapper.isUserOrganisationAdmin(user.getId(), orgId)) {
            oOrganisationMembershipMapper.changeUserRole(data.getRole(), data.getUserID(), orgId);
            return Response.status(Response.Status.ACCEPTED).entity(data).build();
        } 
        
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    
    @POST
    @Path("/{id}/removeUser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyUserRole(@TokenUser(allowPublic=false) User user, @PathParam("id") int orgId, OrganisationAddRemoveUserJSON data) {
        if (organisationMembershipMapper.isUserOrganisationAdmin(user.getId(), orgId)) {
            oOrganisationMembershipMapper.removeUser(data.getUserID(), orgId);
            return Response.status(Response.Status.ACCEPTED).entity(data).build();
        } 
        
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}

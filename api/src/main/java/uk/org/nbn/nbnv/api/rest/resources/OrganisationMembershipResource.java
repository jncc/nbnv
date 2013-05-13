/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.io.IOException;
import java.util.Date;
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
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationJoinRequestMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.meta.AccessRequestJSON;
import uk.org.nbn.nbnv.api.model.meta.OrganisationJoinRequestJSON;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;

/**
 *
 * @author Paul Gilbertson
 */
@Component
@Path("/organisationMemberships")
public class OrganisationMembershipResource extends AbstractResource {
    @Autowired OrganisationMembershipMapper organisationMembershipMapper;
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

    @GET
    @Path("/{id}/join")
    @Produces(MediaType.APPLICATION_JSON)
    public OrganisationMembership getJoinRequests(@TokenUser(allowPublic=false) User user, @PathParam("id") int orgId) {
        return null;
    }
    
    @PUT
    @Path("/{id}/join")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createJoinRequest(@TokenUser(allowPublic=false) User user, @PathParam("id") int orgId, String json) throws IOException {
        OrganisationJoinRequestJSON organisationJoinRequestJSON = parseJSON(json);
        
        oOrganisationJoinRequestMapper.createJoinRequest(user.getId(), 
                orgId, 
                organisationJoinRequestJSON.getRequestReason(), 
                new java.sql.Date(new Date().getTime()));
        
        return Response.ok("success").build();
    }
    
    private OrganisationJoinRequestJSON parseJSON(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, OrganisationJoinRequestJSON.class);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.mappers.OrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;

/**
 *
 * @author Paul Gilbertson
 */
@Component
@Path("/organisationMemberships")
public class OrganisationMembershipResource {
    @Autowired OrganisationMembershipMapper organisationMembershipMapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationMembership> get() {
        return organisationMembershipMapper.selectAll();
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.mappers.OrganisationMapper;
import uk.org.nbn.nbnv.api.model.Organisation;

/**
 *
 * @author Paul Gilbertson
 */
@Component
@Path("/organisations")
public class OrganisationResource {
    @Autowired OrganisationMapper organisationMapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Organisation> get() {
        return organisationMapper.selectAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Organisation getByID(@PathParam("id") int id) {
        return organisationMapper.selectByID(id);
    }
}

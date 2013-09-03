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
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationSuppliedListMapper;
import uk.org.nbn.nbnv.api.model.OrganisationSuppliedList;

/**
 *
 * @author Matt Debont
 */
@Component
@Path("/organisationList")
public class OrganisationSuppliedListResource {
    
    @Autowired OrganisationSuppliedListMapper organisationSuppliedListMapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationSuppliedList> getAllOrgSuppliedLists() {
        return organisationSuppliedListMapper.selectAll();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public OrganisationSuppliedList getOrgSuppliedListByID(@PathParam("id") int id) {
        return organisationSuppliedListMapper.selectByID(id);
    }
      
    @GET
    @Path("/code/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public OrganisationSuppliedList getOrgSuppliedListByCode(@PathParam("code") String code) {
        return organisationSuppliedListMapper.selectByCode(code);
    }
}

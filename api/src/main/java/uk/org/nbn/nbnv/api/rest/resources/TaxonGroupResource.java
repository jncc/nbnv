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
import uk.org.nbn.nbnv.api.dao.taxonGroup.TaxonGroupMapper;
import uk.org.nbn.nbnv.api.model.TaxonGroup;

/**
 *
 * @author Administrator
 */
@Component
@Path("/taxonGroup")
public class TaxonGroupResource { 
    @Autowired TaxonGroupMapper mapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonGroup> getTaxonGroups() {
        return mapper.selectAll();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonGroup getTaxonGroup(@PathParam("id") String id) {
        TaxonGroup toReturn = mapper.getTaxonGroup(id);
        toReturn.setChildren(mapper.getChildren(toReturn.getTaxonGroupKey()));
        return toReturn;
    }
    

    
    
}

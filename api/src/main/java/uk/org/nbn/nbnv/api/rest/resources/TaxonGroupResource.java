/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.TaxonGroupMapper;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonGroup;

/**
 *
 * @author Administrator
 */
@Component
@Path("/taxon_groups")
public class TaxonGroupResource { 
    @Autowired TaxonGroupMapper mapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonGroup> getTaxonGroups() {
        return mapper.selectAll();
    }
    
    @GET
    @Path("/top_levels")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonGroup> getTopLevelTaxonGroups() {
        return mapper.getTopLevels();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonGroup getTaxonGroup(@PathParam("id") String id) {
        TaxonGroup toReturn = mapper.getTaxonGroup(id);
        toReturn.setChildren(mapper.getChildren(id));
        return toReturn;
    }
    
    @GET
    @Path("/{id}/taxa")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Taxon> getTaxa(
        @PathParam("id") String taxonGroup, 
        @QueryParam("limit") @DefaultValue("20") int limit, 
        @QueryParam("offset") @DefaultValue("1") int offset
    ) {
        return mapper.getTaxa(taxonGroup, new RowBounds(offset, limit));
    }
}

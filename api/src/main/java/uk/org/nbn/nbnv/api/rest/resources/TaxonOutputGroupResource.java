package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonOutputGroupMapper;
import uk.org.nbn.nbnv.api.model.TaxonOutputGroup;

@Component
@Path("/taxonOutputGroups")
public class TaxonOutputGroupResource extends AbstractResource {

    @Autowired TaxonOutputGroupMapper taxonOutputGroupMapper;

    /**
     * Returns a list of all TaxonOutputGroups from the data warehouse
     * 
     * @return A List of all TaxonOutputGroups
     * 
     * @response.representation.200.qname List<TaxonOutputGroup>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonOutputGroup> getTaxonOutputGroups() {
        return taxonOutputGroupMapper.selectAll();
    }

    /**
     * Return a specific TaxonOutputGroup from the data warehouse
     * 
     * @param id An ID for a Taxon Output Group
     * 
     * @return A specific Taxon Output Group
     * 
     * @response.representation.200.qname TaxonOutputGroup
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonOutputGroup getTaxonOutputGroup(@PathParam("id") String id) {
        return taxonOutputGroupMapper.getById(id);
    }
}

package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.TaxonMapper;
import uk.org.nbn.nbnv.api.model.Taxon;

@Component
@Path("/species")
public class TaxonResource {
    
    @Autowired TaxonMapper taxonMapper;
    
    @GET
    @Path("/taxon_group/{taxonGroupId}/designation/{designationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Taxon> getSpeciesByDesignationAndTaxonGroup(@PathParam("designationId") int designationId, @PathParam("taxonGroupId") String taxonGroupId) {
            return taxonMapper.selectByDesignationAndTaxonNavigationGroup(designationId, taxonGroupId);
    }
    
}

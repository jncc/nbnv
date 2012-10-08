package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.mappers.TaxonOutputGroupMapper;
import uk.org.nbn.nbnv.api.model.TaxonOutputGroup;

@Component
@Path("/taxonOutputGroups")
public class TaxonOutputGroupResource {

    @Autowired TaxonOutputGroupMapper taxonOutputGroupMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonOutputGroup> getTaxonNavigationGroups() {
        return taxonOutputGroupMapper.selectAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonOutputGroup getTaxonOutputGroup(@PathParam("id") String id) {
        return taxonOutputGroupMapper.getById(id);
    }
    
}

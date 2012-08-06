package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.mappers.TaxonGroupMapper;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonGroup;

@Component
@Path("/taxonGroups")
public class TaxonGroupResource {

    @Autowired
    TaxonGroupMapper mapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonGroup> getTaxonGroups() {
        return mapper.selectAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonGroup getTaxonGroup(@PathParam("id") String id) {
        return mapper.getTaxonGroup(id);
    }

    @GET
    @Path("/topLevels")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonGroup> getTopLevelTaxonGroups(@QueryParam("designationId") int designationId) {
        if (designationId != 0) {
            return mapper.getTopLevelssByDesignationID(designationId);
        } else {
            return mapper.getTopLevels();
        }
    }

    @GET
    @Path("/topLevels/designations/{designationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonGroup> getTopLevelTaxonNavigationGroupsByDesignation(@PathParam("designationId") int id) {
        return mapper.getTopLevelssByDesignationID(id);
    }

    @GET
    @Path("/{id}/species")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Taxon> getTaxa(
            @PathParam("id") String taxonGroup,
            @QueryParam("limit") @DefaultValue("20") int limit,
            @QueryParam("offset") @DefaultValue("1") int offset) {
        return mapper.getTaxa(taxonGroup, new RowBounds(offset, limit));
    }
    
}

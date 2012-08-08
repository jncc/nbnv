package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.mappers.TaxonGroupMapper;
import uk.org.nbn.nbnv.api.model.TaxonGroup;
import uk.org.nbn.nbnv.api.solr.SolrResponse;

@Component
@Path("/taxonGroups")
public class TaxonGroupResource {

    @Autowired TaxonGroupMapper mapper;

    @Autowired SolrServer solrServer;
    
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
    public SolrResponse getTaxa(
            @PathParam("id") String taxonGroup,
            @QueryParam("rows") @DefaultValue("20") int rows,
            @QueryParam("start") @DefaultValue("0") int start) throws SolrServerException {
        
        SolrQuery query = new SolrQuery();
        query.setQuery("navigationGroupKey:" + taxonGroup);
        query.setRows(rows);
        query.setStart(start);
        return new SolrResponse(solrServer.query(query));
    }
    
}

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
import uk.org.nbn.nbnv.api.dao.mappers.TaxonNavigationGroupMapper;
import uk.org.nbn.nbnv.api.model.TaxonNavigationGroup;
import uk.org.nbn.nbnv.api.solr.SolrResponse;

@Component
@Path("/taxonNavigationGroups")
public class TaxonNavigationGroupResource {

    @Autowired TaxonNavigationGroupMapper mapper;

    @Autowired SolrServer solrServer;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonNavigationGroup> getTaxonNavigationGroups() {
        return mapper.selectAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonNavigationGroup getTaxonNavigationGroup(@PathParam("id") String id) {
        return mapper.getTaxonNavigationGroup(id);
    }

    @GET
    @Path("/topLevels")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonNavigationGroup> getTopLevelTaxonNavigationGroups(@QueryParam("designationId") int designationId) {
        if (designationId != 0) {
            return mapper.getTopLevelssByDesignationID(designationId);
        } else {
            return mapper.getTopLevels();
        }
    }

    @GET
    @Path("/topLevels/designations/{designationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonNavigationGroup> getTopLevelTaxonNavigationGroupsByDesignation(@PathParam("designationId") int id) {
        return mapper.getTopLevelssByDesignationID(id);
    }

    @GET
    @Path("/{id}/species")
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse getTaxa(
            @PathParam("id") String taxonGroup,
            @QueryParam("limit") @DefaultValue("20") int limit,
            @QueryParam("offset") @DefaultValue("0") int offset) throws SolrServerException {
        
        SolrQuery query = new SolrQuery();
        query.setQuery("navigationGroupKey:" + taxonGroup);
        query.setRows(limit);
        query.setStart(offset);
        return new SolrResponse(solrServer.query(query));
    }
    
}

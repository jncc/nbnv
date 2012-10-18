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
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonNavigationGroupMapper;
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
    public List<TaxonNavigationGroup> getTopLevelTaxonNavigationGroups(@QueryParam("designationId") String designationId) {
        if (designationId != null) {
            return mapper.getTopLevelsByDesignationID(designationId);
        } else {
            return mapper.getTopLevels();
        }
    }

    @GET
    @Path("/topLevels/designations/{designationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonNavigationGroup> getTopLevelTaxonNavigationGroupsByDesignation(@PathParam("designationId") String id) {
        return mapper.getTopLevelsByDesignationID(id);
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

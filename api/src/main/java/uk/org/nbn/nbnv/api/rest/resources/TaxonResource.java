package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.mappers.TaxonMapper;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.solr.SolrResponse;

@Component
@Path("/species")
public class TaxonResource {
    
    @Autowired TaxonMapper taxonMapper;
    @Autowired SolrServer solrServer;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse getTaxa(
            @PathParam("id") String taxonGroup,
            @QueryParam("rows") @DefaultValue("20") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("q") String q
            ) throws SolrServerException {
        
        SolrQuery query = new SolrQuery();
        query.setQuery(q);
        query.setFacet(true);
        query.addFacetField("category");
        query.setRows(rows);
        query.setStart(start);
        return new SolrResponse(solrServer.query(query));
    }
    
}

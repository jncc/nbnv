/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.solr.SolrResponse;

@Component
@Path("/search")
public class SearchResource {
    @Autowired SolrServer solrServer;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse getTaxa(
            @QueryParam("rows") @DefaultValue("20") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("category") List<String> categories,
            @QueryParam("lang") List<String> languages,
            @QueryParam("sort") String sort,
            @QueryParam("q") String q
            ) throws SolrServerException {
        
        SolrQuery query = new SolrQuery();
        
        if(q!=null && !q.isEmpty()) {
            query.setQuery(q);
            query.setParam("defType", "dismax");
        }
        else {
            query.setQuery("*:*");
        }
        query.setFacet(true);

        if(sort!=null) {
            query.setSortField(sort, SolrQuery.ORDER.asc);
        }
        query.setRows(rows);
        query.setStart(start);
        return new SolrResponse(solrServer.query(query));
    }
}

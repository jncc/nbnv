package uk.org.nbn.nbnv.api.rest.resources;

import java.util.Iterator;
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
@Path("/species")
public class TaxonResource {
    @Autowired SolrServer solrServer;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse getTaxa(
            @PathParam("id") String taxonGroup,
            @QueryParam("rows") @DefaultValue("20") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("category") List<String> categories,
            @QueryParam("lang") List<String> languages,
            @QueryParam("sort") String sort,
            @QueryParam("q") String q
            ) throws SolrServerException {
        
        SolrQuery query = new SolrQuery();
        query.setQuery((q==null) ? "*:*" : q);
        query.setFacet(true);

        if(!categories.isEmpty()) query.addFilterQuery(getOrFilter("category", categories));
        if(!languages.isEmpty()) query.addFilterQuery(getOrFilter("lang", languages));
        
        query.addFacetField("category", "lang");
        if(sort!=null) {
            query.setSortField(sort, SolrQuery.ORDER.asc);
        }
        query.setRows(rows);
        query.setStart(start);
        System.out.println(languages);
        System.out.println(query);
        return new SolrResponse(solrServer.query(query));
    }
    
    private static String getOrFilter(String parameter, List<String> values) {
        if(values.isEmpty()) 
            throw new IllegalArgumentException("I need some valomes in order to filter");
        StringBuilder toReturn = new StringBuilder(parameter);
        toReturn.append(":(");
        Iterator<String> iterator = values.iterator();
        
        toReturn.append(iterator.next());
        for(; iterator.hasNext();) {
            toReturn.append(" OR ");
            toReturn.append(iterator.next());
        }
        toReturn.append(")");
        return toReturn.toString();
    }
}

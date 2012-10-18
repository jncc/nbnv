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
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.solr.SolrResponse;

@Component
@Path("/taxa")
public class TaxonResource {
    @Autowired SolrServer solrServer;
    @Autowired TaxonMapper taxonMapper;
    @Autowired DatasetMapper datasetMapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON) 
    @Path("{taxonVersionKey}")
    public Taxon getTaxon(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return taxonMapper.getTaxon(taxonVersionKey);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON) 
    @Path("/{taxonVersionKey}/datasets")
    public List<Dataset> getDatasetListForTaxonViewableByUser(@TokenUser User user, @PathParam("taxonVersionKey") String taxonVersionKey) {
        return datasetMapper.selectDatasetsForTaxonViewableByUser(user, taxonVersionKey);
    }
    
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

        query.addFilterQuery("record_type:taxon");
        if(!categories.isEmpty()) query.addFilterQuery(getOrFilter("category", categories));
        if(!languages.isEmpty()) query.addFilterQuery(getOrFilter("lang", languages));
        
        query.addFacetField("category", "lang");
        if(sort!=null) {
            query.setSortField(sort, SolrQuery.ORDER.asc);
        }
        query.setRows(rows);
        query.setStart(start);
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

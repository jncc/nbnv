package uk.org.nbn.nbnv.api.solr;

import java.util.Iterator;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;

/**
 * The following class is a builder for solr queries
 * @author Developer
 */
public class SolrHelper {
    private SolrQuery query;
    
    public SolrHelper() {
        this.query = new SolrQuery();
    }
    
    public SolrHelper query(String q) {
        if(q!=null && !q.isEmpty()) {
            query.setQuery(q);
            query.setParam("defType", "dismax");
        }
        else {
            query.setQuery("*:*");
        }
        return this;
    }
    
    public SolrHelper rows(int rows) {
        query.setRows(rows);
        return this;
    }
    
    public SolrHelper start(int offset) {
        query.setStart(offset);
        return this;
    }
    
    public SolrHelper filterQuery(String fq) {
        query.addFilterQuery(fq);
        return this;
    }
    
    public SolrHelper facetOn(String... facets) {
        query.setFacet(true);
        query.addFacetField(facets);
        return this;
    }
    
    public SolrHelper sort(String field, SolrQuery.ORDER order) {
        if(field != null) {
            query.setSortField(field, order);
        }
        return this;
    }
    
    public SolrHelper addOrFilter(String key, List<String> values) {
        if(shouldFilter(values)) {
            query.addFilterQuery(getOrFilter(key, values));
        }
        return this;
    }
    
    public SolrResponse response(SolrServer solrServer) throws SolrServerException {
        return new SolrResponse(solrServer.query(query));
    }
    
    private static boolean shouldFilter(List<String> collection) {
        return !collection.isEmpty() && !collection.get(0).isEmpty();
    }
    
    private static String getOrFilter(String parameter, List<String> values) {
        if(values.isEmpty()) {
            throw new IllegalArgumentException("I need some values in order to filter");
        }
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

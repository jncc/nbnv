package uk.org.nbn.nbnv.api.solr;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletContext;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The following class is a builder for solr queries
 * @author Developer
 */
@Component
public class Solr {
    @Autowired SolrServer solrServer;
    @Autowired Properties properties;
    @Autowired(required=false) ServletContext context;
    
    public SolrBuilder create() {
        return new SolrBuilder();
    }
    
    public class SolrBuilder {
        private SolrQuery query;
        
        public SolrBuilder() {
            this.query = new SolrQuery();
        }
         
        public SolrBuilder query(String q) {
            if(q!=null && !q.isEmpty()) {
                query.setQuery(q);
                query.setParam("defType", "dismax");
            }
            else {
                query.setQuery("*:*");
            }
            return this;
        }
        
        public SolrBuilder rows(int rows) {
            query.setRows(rows);
            return this;
        }

        public SolrBuilder start(int offset) {
            query.setStart(offset);
            return this;
        }

        public SolrBuilder filterQuery(String fq) {
            query.addFilterQuery(fq);
            return this;
        }

        public SolrBuilder facetOn(String... facets) {
            query.setFacet(true);
            query.addFacetField(facets);
            return this;
        }

        public SolrBuilder sort(String field, SolrQuery.ORDER order) {
            if(field != null) {
                query.setSortField(field, order);
            }
            return this;
        }

        public SolrBuilder addOrFilter(String key, List<String> values) {
            if(shouldFilter(values)) {
                query.addFilterQuery(getOrFilter(key, values));
            }
            return this;
        }

        public SolrResponse response() throws SolrServerException {
            return new SolrResponse(solrServer.query(query), 
                                    properties.getProperty("portal_url"),
                                    (context != null) ? context.getContextPath() : "");
        }
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

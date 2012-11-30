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
import uk.org.nbn.nbnv.api.rest.resources.*;

/**
 * The following class is a builder for solr queries
 * @author Developer
 */
@Component
public class Solr {
    @Autowired SolrServer solrServer;
    @Autowired Properties properties;
    @Autowired(required=false) ServletContext context;
    
    enum ResultType { DESIGNATION, DATASET, FEATURE, TAXON, ORGANISATION}
    
    @Autowired DatasetResource datasetResource;
    @Autowired DesignationResource designationResource;
    @Autowired FeatureResource featureResource;
    @Autowired TaxonResource taxonResource;
    @Autowired OrganisationResource organisationResource;
    
    protected Object resolveSolrResult(String record_id) {
        String[] record_idParts = record_id.split("-", 2);
        String id = record_idParts[1];
        switch(ResultType.valueOf(record_idParts[0])) {
            case DESIGNATION:   return designationResource.getDesignation(id);
            case DATASET:       return datasetResource.getDatasetByID(id);
            case FEATURE:       return featureResource.getFeature(id);
            case ORGANISATION:  return organisationResource.getByID(Integer.parseInt(id));
            case TAXON:         return taxonResource.getTaxon(id);
            default :           throw new IllegalArgumentException("The solr response contained results on an unkown type");
        }
    }
    
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
            return new SolrResponse(solrServer.query(query),Solr.this);
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

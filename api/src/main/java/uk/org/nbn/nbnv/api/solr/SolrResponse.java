package uk.org.nbn.nbnv.api.solr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.util.NamedList;

/**
 *
 * @author Administrator
 */
public class SolrResponse {
    
    private final QueryResponse toWrap;
    private final List<SolrResponseDocument<?>> results;
    
    public SolrResponse(QueryResponse toWrap, Solr solrHelper) {
        this.toWrap = toWrap;
        this.results = new ArrayList<SolrResponseDocument<?>>();
        for(SolrDocument currDocument : toWrap.getResults()) {
            results.add(new SolrResponseDocument<Object>(
                solrHelper.solrFactory.resolve(currDocument),
                (String)currDocument.get("record_type"),
                (String)currDocument.get("name")
            ));
        }
    }
    
    /**
     * The following method will process the values of api_href and portal_href
     * @return 
     */
    public List<SolrResponseDocument<?>> getResults() {
        return results;
    }
    
    public Map getFacetFields() {
        List<FacetField> facetFields = toWrap.getFacetFields();
        if(facetFields != null) {
            return processFacetFields(facetFields);
        }
        else {
            return null;
        }
    }
    
    private static Map processFacetFields(List<FacetField> facetFields) {
        Map toReturn = new HashMap();
        for(FacetField currField : facetFields) {
            String category = currField.getName();
            toReturn.put(category, processCategoriesdFacet(currField.getValues()));
        }
        return toReturn;
    }
    
    private static Map processCategoriesdFacet(List<Count> facetCount) {
        Map<String, Long> toReturn = new HashMap<String, Long>();
        for(Count currCatFacet: facetCount) {
            toReturn.put(currCatFacet.getName(), currCatFacet.getCount());
        }
        return toReturn;
    }
    
    public SolrResponseHeader getHeader() {
        return new SolrResponseHeader();
    }
    
    public class SolrResponseHeader {
        public long getNumFound() {
            return toWrap.getResults().getNumFound();
        }  
        
        public long getStart() {    
            return toWrap.getResults().getStart();
        }
        
        public Long getRows() {
            NamedList requestParameters = (NamedList)toWrap.getResponseHeader().get("params");
            String rowsStr = (String)requestParameters.get("rows");
            if(rowsStr!=null) {
                return Long.parseLong(rowsStr);
            }
            else {
                return null;
            }
        }
    }
}

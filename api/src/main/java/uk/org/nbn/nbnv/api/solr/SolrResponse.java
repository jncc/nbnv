package uk.org.nbn.nbnv.api.solr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
    private static final String PORTAL_LINK_KEY = "portal_href";
    private static final String API_LINK_KEY = "api_href";
    
    private final QueryResponse toWrap;
    private final String portalPath, apiPath;
    
    public SolrResponse(QueryResponse toWrap, String portalPath, String apiPath) {
        this.toWrap = toWrap;
        this.portalPath = portalPath;
        this.apiPath = apiPath;
    }
    
    /**
     * The following method will process the values of api_href and portal_href
     * @return 
     */
    public List<SolrDocument> getResults() {
        List<SolrDocument> toReturn = toWrap.getResults();
        for(SolrDocument currDocument : toReturn) {
            currDocument.put(API_LINK_KEY, apiPath + currDocument.get(API_LINK_KEY));
            currDocument.put(PORTAL_LINK_KEY, portalPath + currDocument.get(PORTAL_LINK_KEY));
        }
        return toReturn;
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

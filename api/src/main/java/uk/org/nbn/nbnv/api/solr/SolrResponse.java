/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.solr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.util.NamedList;

/**
 *
 * @author Administrator
 */
public class SolrResponse {
    private final QueryResponse toWrap;
    public SolrResponse(QueryResponse toWrap) {
        this.toWrap = toWrap;
    }
    
    public List<SolrDocument> getResults() {
        return toWrap.getResults();
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

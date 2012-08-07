/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.solr;

import java.util.List;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

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
    
    public long getNumFound() {
        return toWrap.getResults().getNumFound();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.solr;

import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author Administrator
 */
@Service
public class SolrDataImporterTask {
    @Autowired SolrServer solrServer;
    
    @Scheduled(fixedDelay=10800000)
    public void executeFullDataImport() throws SolrServerException {
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("qt", "/dataimport");
        params.set("command", "full-import");

        solrServer.query(params, METHOD.POST);
    }
}

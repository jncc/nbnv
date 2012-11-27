package uk.org.nbn.nbnv.api.services;

import java.io.IOException;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * The following service handles the importing of data into solr at specified
 * intervals
 * @author Christopher Johnson
 */
@Service
public class SolrDataImportService {
    @Autowired SolrServer solrServer;
    
    @Scheduled(cron="0 0 * * *")
    public void performFullDataImport() throws SolrServerException, IOException {
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("command", "full-import");
        QueryRequest request = new QueryRequest(params);
        request.setPath("/dataimport");
        solrServer.request(request);
    }
}

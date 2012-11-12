/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.solr.SolrHelper;
import uk.org.nbn.nbnv.api.solr.SolrResponse;

@Component
@Path("/search")
public class SearchResource {
    @Autowired SolrServer solrServer;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse searchAll(
            @QueryParam("rows") @DefaultValue("20") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("sort") String sort,
            @QueryParam("q") String q
            ) throws SolrServerException {
        return new SolrHelper()
                .query(q)
                .sort(sort, SolrQuery.ORDER.asc)
                .start(start)
                .rows(rows)
                .response(solrServer);
    }
    
    @GET
    @Path("/designations")
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse searchDesignations(
            @QueryParam("rows") @DefaultValue("20") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("sort") String sort,
            @QueryParam("q") String q
            ) throws SolrServerException {
        return new SolrHelper()
                .query(q)
                .filterQuery("record_type:designation")
                .sort(sort, SolrQuery.ORDER.asc)
                .start(start)
                .rows(rows)
                .response(solrServer);
    }
    
    @GET
    @Path("/taxonDatasets")
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse searchDatasets(
            @QueryParam("rows") @DefaultValue("20") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("sort") String sort,
            @QueryParam("q") String q
            ) throws SolrServerException {
        return new SolrHelper()
                .query(q)
                .filterQuery("record_type:taxondataset")
                .sort(sort, SolrQuery.ORDER.asc)
                .start(start)
                .rows(rows)
                .response(solrServer);
    }
}

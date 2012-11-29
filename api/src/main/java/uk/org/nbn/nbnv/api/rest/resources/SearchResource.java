/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.solr.Solr;
import uk.org.nbn.nbnv.api.solr.SolrResponse;

@Component
@Path("/search")
public class SearchResource extends AbstractResource {
    @Autowired Solr solr;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse searchAll(
            @QueryParam("rows") @DefaultValue("20") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("sort") String sort,
            @QueryParam("q") String q
            ) throws SolrServerException {
        return solr
                .create()
                .query(q)
                .sort(sort, SolrQuery.ORDER.asc)
                .start(start)
                .rows(rows)
                .response();
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
        return solr
                .create()
                .query(q)
                .filterQuery("record_type:designation")
                .sort(sort, SolrQuery.ORDER.asc)
                .start(start)
                .rows(rows)
                .response();
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
        return solr
                .create()
                .query(q)
                .filterQuery("record_type:taxondataset")
                .sort(sort, SolrQuery.ORDER.asc)
                .start(start)
                .rows(rows)
                .response();
    }
    
    @GET
    @Path("/taxa")
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse searchTaxa(
            @QueryParam("rows") @DefaultValue("20") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("category") List<String> categories,
            @QueryParam("languageKey") List<String> languages,
            @QueryParam("sort") String sort,
            @QueryParam("q") String q
            ) throws SolrServerException {
        return solr
                .create()
                .query(q)
                .filterQuery("record_type:taxon")
                .facetOn("category", "languageKey")
                .addOrFilter("category", categories)
                .addOrFilter("languageKey", languages)
                .sort(sort, SolrQuery.ORDER.asc)
                .start(start)
                .rows(rows)
                .response();
    }
}

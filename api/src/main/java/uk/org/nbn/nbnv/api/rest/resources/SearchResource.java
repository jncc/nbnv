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
            @QueryParam("order") @DefaultValue("asc") SolrQuery.ORDER order,
            @QueryParam("q") String q
            ) throws SolrServerException {
        return solr
                .create()
                .query(q)
                .sort(sort, order)
                .start(start)
                .rows(rows)
                .response();
    }
    
    @GET
    @Path("/siteDatasets/{datasetKey}")
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse searchSiteDataset(
            @PathParam("datasetKey") String datasetKey,
            @QueryParam("rows") @DefaultValue("20") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("bbox") String bbox,
            @QueryParam("q") String q) throws SolrServerException {
        String[] bboxParts = bbox.split(",");
        return solr
                .create()
                .query(q)
                .start(start)
                .rows(rows)
                .filterQuery("record_type:siteboundaryfeature")
                .filterQuery(String.format("location:[%s,%s TO %s,%s]", (Object[]) bboxParts))
                .response();
    }
    
    @GET
    @Path("/designations")
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse searchDesignations(
            @QueryParam("rows") @DefaultValue("20") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("sort") String sort,
            @QueryParam("order") @DefaultValue("asc") SolrQuery.ORDER order,
            @QueryParam("q") String q
            ) throws SolrServerException {
        return solr
                .create()
                .query(q)
                .filterQuery("record_type:designation")
                .sort(sort, order)
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
            @QueryParam("order") @DefaultValue("asc") SolrQuery.ORDER order,
            @QueryParam("q") String q
            ) throws SolrServerException {
        return solr
                .create()
                .query(q)
                .filterQuery("record_type:taxondataset")
                .sort(sort, order)
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
            @QueryParam("taxonOutputGroupKey") List<String> outputGroups,
            @QueryParam("sort") String sort,
            @QueryParam("order") @DefaultValue("asc") SolrQuery.ORDER order,
            @QueryParam("q") String q
            ) throws SolrServerException {
        return solr
                .create()
                .query(q)
                .filterQuery("record_type:taxon")
                .addOrFilter("taxonOutputGroupKey", outputGroups)
                .sort(sort, order)
                .boostFunction("ord(gatewayRecordCount)")
                .start(start)
                .rows(rows)
                .response();
    }
}

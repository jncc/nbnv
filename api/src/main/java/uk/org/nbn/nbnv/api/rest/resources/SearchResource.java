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
import org.apache.solr.client.solrj.util.ClientUtils;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.solr.Solr;
import uk.org.nbn.nbnv.api.solr.SolrResponse;

@Component
@Path("/search")
public class SearchResource extends AbstractResource {
    @Autowired Solr solr;
    
    /**
     * Unrestricted search, searches for any reference to a given search term in
     * all searchable objects in the database
     * 
     * @param rows The number of rows to show in each page of search results
     * @param start The page that the user wants to start displaying the results 
     * at
     * @param sort If we should sort the results or not
     * @param order The order in which we should sort the results
     * @param q The search term
     * @param exclude a record type to exclude from the search results (eg the 
     * IMT requires the exclusion of records that are of type 'organisation')
     * 
     * @return Search Results for this search term
     * 
     * @throws SolrServerException 
     * 
     * @response.representation.200.qname SolrResponse
     * @response.representation.200.mediaType application/json
     */
    @GET
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned SOLR results across all searchable objects for a given term")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse searchAll(
            @QueryParam("rows") @DefaultValue("25") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("sort") String sort,
            @QueryParam("order") @DefaultValue("asc") SolrQuery.ORDER order,
            @QueryParam("q") String q,
            @QueryParam("exclude") String exclude
            ) throws SolrServerException {
        if (q != null) {
            q = ClientUtils.escapeQueryChars(q.trim());
        }
        return solr
                .create()
                .query(q)
                .excludeByFieldValue("record_type", exclude)
                .sort(sort, order)
                .start(start)
                .rows(rows)
                .response();
    }
    
    /**
     * Search inside the site dataset records and a specific dataset for a given 
     * search term
     * 
     * @param datasetKey The dataset to be searched
     * @param start The page that the user wants to start displaying the results 
     * at
     * @param sort If we should sort the results or not
     * @param bbox A Bounding Box to search within
     * @param q The search term
     * 
     * @return Search Results for this search term, restricted to site datasets
     * 
     * @throws SolrServerException 
     * 
     * @response.representation.200.qname SolrResponse
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/siteDatasets/{datasetKey}")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned SOLR results across all searchable site datasets for a given term")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse searchSiteDataset(
            @PathParam("datasetKey") String datasetKey,
            @QueryParam("rows") @DefaultValue("10") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("bbox") @DefaultValue("-180,-90,180,90") String bbox,
            @QueryParam("q") String q) throws SolrServerException {
        String[] bboxParts = bbox.split(",");
        if (q != null) {
            q = ClientUtils.escapeQueryChars(q.trim());
        }        
        return solr
                .create()
                .query(q)
                .start(start)
                .rows(rows)
                .filterQuery("record_type:siteboundaryfeature")
                .filterQuery(String.format("datasetKey:%s",datasetKey))
                .filterQuery(String.format("location:[%s,%s TO %s,%s]", (Object[]) bboxParts))
                .response();
    }

    /**
     * Search within site boundaries records for a given term 
     * 
     * @param rows The number of rows to show in each page of search results
     * @param start The page that the user wants to start displaying the results 
     * at
     * @param q The search term
     * 
     * @throws SolrServerException 
     * 
     * @return Search Results for this search term restricted to site boundaries
     * 
     * @response.representation.200.qname SolrResponse
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/siteBoundaries")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned SOLR results across all searchable site boundaries for a given term")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse searchSiteBoundaries(
            @QueryParam("rows") @DefaultValue("10") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("q") String q) throws SolrServerException {
        if (q != null) {
            q = ClientUtils.escapeQueryChars(q.trim());
        }    
        return solr
                .create()
                .query(q)
                .start(start)
                .rows(rows)
                .filterQuery("record_type:siteboundaryfeature")
                .response();
    }

    /**
     * Search designation records for a given search term
     * 
     * @param rows The number of rows to show in each page of search results
     * @param start The page that the user wants to start displaying the results 
     * at
     * @param sort If we should sort the results or not
     * @param order The order in which we should sort the results
     * @param q The search term
     * 
     * @return Search Results for this search term restricted to designation 
     * records
     * 
     * @throws SolrServerException 
     * 
     * @response.representation.200.qname SolrResponse
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/designations")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned SOLR results across all searchable designations for a given term")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse searchDesignations(
            @QueryParam("rows") @DefaultValue("25") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("sort") String sort,
            @QueryParam("order") @DefaultValue("asc") SolrQuery.ORDER order,
            @QueryParam("q") String q
            ) throws SolrServerException {
        if (q != null) {
            q = ClientUtils.escapeQueryChars(q.trim());
        }    
        return solr
                .create()
                .query(q)
                .filterQuery("record_type:designation")
                .sort(sort, order)
                .start(start)
                .rows(rows)
                .response();
    }
    
    /**
     * Search taxon dataset records for a given search term
     * 
     * @param rows The number of rows to show in each page of search results
     * @param start The page that the user wants to start displaying the results 
     * at
     * @param sort If we should sort the results or not
     * @param order The order in which we should sort the results
     * @param q The search term
     * 
     * @return Search Results for this search term restricted to taxon datasets
     *
     * @throws SolrServerException 
     * 
     * @response.representation.200.qname SolrResponse
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/taxonDatasets")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned SOLR results across all searchable taxon datasets for a given term")
    })  
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse searchDatasets(
            @QueryParam("rows") @DefaultValue("25") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("sort") String sort,
            @QueryParam("order") @DefaultValue("asc") SolrQuery.ORDER order,
            @QueryParam("q") String q
            ) throws SolrServerException {
        if (q != null) {
            q = ClientUtils.escapeQueryChars(q.trim());
        }    
        return solr
                .create()
                .query(q)
                .filterQuery("record_type:taxondataset")
                .sort(sort, order)
                .start(start)
                .rows(rows)
                .response();
    }
    
    /**
     * Search taxon records with a given term
     * 
     * @param rows The number of rows to show in each page of search results
     * @param start The page that the user wants to start displaying the results 
     * at
     * @param outputGroups List of taxon output groups
     * @param sort If we should sort the results or not
     * @param order The order in which we should sort the results
     * @param prefered Restrict search to preferred or any
     * @param q The search term
     * 
     * @return Search Results for this search term restricted to taxon records
     * 
     * @throws SolrServerException 
     * 
     * @response.representation.200.qname SolrResponse
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/taxa")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned SOLR results across all searchable taxa for a given term")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse searchTaxa(
            @QueryParam("rows") @DefaultValue("25") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("taxonOutputGroupKey") List<String> outputGroups,
            @QueryParam("sort") String sort,
            @QueryParam("order") @DefaultValue("asc") SolrQuery.ORDER order,
            @QueryParam("prefered") @DefaultValue("false") boolean prefered,
            @QueryParam("q") String q
            ) throws SolrServerException {
        if (q != null) {
            q = ClientUtils.escapeQueryChars(q.trim());
        }    
        return solr
                .create()
                .query(q)
                .filterQuery("record_type:taxon" + (prefered ? " AND prefered:true" : ""))
                .addOrFilter("taxonOutputGroupKey", outputGroups)
                .sort(sort, order)
                //.boostFunction("ord(gatewayRecordCount)")
                .start(start)
                .rows(rows)
                .response();
    }

}

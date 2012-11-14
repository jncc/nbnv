package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DesignationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonDesignation;
import uk.org.nbn.nbnv.api.model.TaxonWebLink;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.solr.SolrResponse;

@Component
@Path("/taxa")
public class TaxonResource {
    @Autowired SearchResource searchResource;
    @Autowired TaxonMapper taxonMapper;
    @Autowired DatasetMapper datasetMapper;
    @Autowired DesignationMapper designationMapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8") 
    @Path("{taxonVersionKey}")
    public Taxon getTaxon(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return taxonMapper.getTaxon(taxonVersionKey);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8") 
    @Path("{taxonVersionKey}/synonyms")
    public List<Taxon> getTaxonSynonyms(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return taxonMapper.selectSynonymsByTVK(taxonVersionKey);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8") 
    @Path("{taxonVersionKey}/parent")
    public Taxon getTaxonParent(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return taxonMapper.getParentTaxon(taxonVersionKey);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8") 
    @Path("{taxonVersionKey}/children")
    public List<Taxon> getTaxonChildren(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return taxonMapper.selectChildrenByTVK(taxonVersionKey);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8") 
    @Path("{taxonVersionKey}/taxonomy")
    public List<Taxon> getTaxonAncestry(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return taxonMapper.selectAncestryByTVK(taxonVersionKey);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("{taxonVersionKey}/designations")
    public List<TaxonDesignation> getTaxonDesignations(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return designationMapper.selectByTaxonVersionKey(taxonVersionKey);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8") 
    @Path("/{taxonVersionKey}/datasets")
    public List<Dataset> getDatasetListForTaxonViewableByUser(@TokenUser User user, @PathParam("taxonVersionKey") String taxonVersionKey) {
        return datasetMapper.selectDatasetsForTaxonViewableByUser(user, taxonVersionKey);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8") 
    @Path("/{taxonVersionKey}/weblinks")
    public List<TaxonWebLink> getTaxonWebLinks(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return taxonMapper.getActiveWebLinksByTVK(taxonVersionKey);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public SolrResponse getTaxa(
            @QueryParam("rows") @DefaultValue("20") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("category") List<String> categories,
            @QueryParam("languageKey") List<String> languages,
            @QueryParam("sort") String sort,
            @QueryParam("q") String q
            ) throws SolrServerException {
        return searchResource.searchTaxa(rows, start, categories, languages, sort, q);
    }
}

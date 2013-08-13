package uk.org.nbn.nbnv.api.rest.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.providers.ProviderHelper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DesignationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonObservationMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.Designation;
import uk.org.nbn.nbnv.api.model.SiteBoundary;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonDatasetWithQueryStats;
import uk.org.nbn.nbnv.api.model.TaxonDesignation;
import uk.org.nbn.nbnv.api.model.TaxonWebLink;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.rest.resources.utils.DownloadHelper;
import uk.org.nbn.nbnv.api.solr.SolrResolver;
import uk.org.nbn.nbnv.api.solr.SolrResponse;

@Component
@Path("/taxa")
public class TaxonResource extends AbstractResource {

    @Autowired SearchResource searchResource;
    @Autowired TaxonMapper taxonMapper;
    @Autowired DatasetMapper datasetMapper;
    @Autowired DesignationMapper designationMapper;
    @Autowired SiteBoundaryMapper siteBoundaryMapper;
    @Autowired DownloadHelper downloadHelper;
    @Autowired TaxonObservationMapper observationMapper;

    /**
     * Return a specific Taxon record from the data warehouse
     * 
     * @param taxonVersionKey A Taxon Version Key denoting a Taxon Record
     * 
     * @return A Taxon Record
     * 
     * @response.representation.200.qname Taxon
     * @response.representation.200.mediaType application/json;charset=utf-8
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("{taxonVersionKey}")
    @SolrResolver("TAXON")
    public Taxon getTaxon(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return taxonMapper.getTaxon(taxonVersionKey);
    }

    /**
     * Return a list of Taxon Records which are synonymous with the specified 
     * Taxon Record
     * 
     * @param taxonVersionKey A Taxon Version Key denoting a Taxon Record
     * 
     * @return A List of Synonyms for a given Taxon Version Key
     * 
     * @response.representation.200.qname List<Taxon>
     * @response.representation.200.mediaType application/json;charset=utf-8
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("{taxonVersionKey}/synonyms")
    public List<Taxon> getTaxonSynonyms(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return taxonMapper.selectSynonymsByTVK(taxonVersionKey);
    }

    /**
     * Return the parent Taxon Record of a specific Taxon Record
     * 
     * @param taxonVersionKey A Taxon Version Key denoting a Taxon Record
     * 
     * @return The parent of the specified Taxon Record
     * 
     * @response.representation.200.qname List<Taxon>
     * @response.representation.200.mediaType application/json;charset=utf-8
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("{taxonVersionKey}/parent")
    public Taxon getTaxonParent(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return taxonMapper.getParentTaxon(taxonVersionKey);
    }

    /**
     * Return a list of all Taxon that are children of a specific Taxon Record
     * 
     * @param taxonVersionKey A Taxon Version Key denoting a Taxon Record
     *
     * @return The children of the specified Taxon Record
     * 
     * @response.representation.200.qname List<Taxon>
     * @response.representation.200.mediaType application/json;charset=utf-8
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("{taxonVersionKey}/children")
    public List<Taxon> getTaxonChildren(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return taxonMapper.selectChildrenByTVK(taxonVersionKey);
    }

    /**
     * Return a list of Taxon Records denoting the Ancestry of a specified 
     * Taxon Record
     * 
     * @param taxonVersionKey A Taxon Version Key denoting a Taxon Record
     * 
     * @return The Ancestry of a given Taxon Record
     * 
     * @response.representation.200.qname List<Taxon>
     * @response.representation.200.mediaType application/json;charset=utf-8
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("{taxonVersionKey}/taxonomy")
    public List<Taxon> getTaxonAncestry(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return taxonMapper.selectAncestryByTVK(taxonVersionKey);
    }

    /**
     * Return a list of Taxon Designations that are applicable to a specified
     * Taxon Record
     * 
     * @param taxonVersionKey A Taxon Version Key denoting a Taxon Record
     * 
     * @return A List of Taxon Designations that apply to a specified record
     * 
     * @response.representation.200.qname List<TaxonDesignation>
     * @response.representation.200.mediaType application/json;charset=utf-8
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("{taxonVersionKey}/designations")
    public List<TaxonDesignation> getTaxonDesignations(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return designationMapper.selectByTaxonVersionKey(taxonVersionKey);
    }

    /**
     * Return a list of Archive Taxon Designations that are applicable to a 
     * specified Taxon Record
     * 
     * @param taxonVersionKey A Taxon Version Key denoting a Taxon Record
     * 
     * @return A List of Archive Taxon Designations that apply to a specified 
     * record
     * 
     * @response.representation.200.qname List<TaxonDesignation> 
     * @response.representation.200.mediaType application/json;charset=utf-8
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("{taxonVersionKey}/designations/archive")
    public List<TaxonDesignation> getArchiveTaxonDesignations(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return designationMapper.selectArchiveByTaxonVersionKey(taxonVersionKey);
    }

    /**
     * Return a list of datasets that are viewable by the user and contain 
     * records with a specified Taxon Version Key
     * 
     * @param user The current user
     * @param taxonVersionKey A Taxon Version Key denoting a Taxon Record
     * 
     * @return A List of Datasets containing records with a specified Taxon 
     * Version Key
     * 
     * @response.representation.200.qname List<Dataset>
     * @response.representation.200.mediaType application/json;charset=utf-8
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/{taxonVersionKey}/datasets")
    public List<Dataset> getDatasetListForTaxonViewableByUser(@TokenUser User user, @PathParam("taxonVersionKey") String taxonVersionKey) {
        return datasetMapper.selectDatasetsForTaxonViewableByUser(user, taxonVersionKey);
    }

    /**
     * Return a list of Active Taxon Web Links for a specific Taxon Record
     * 
     * @param taxonVersionKey A Taxon Version Key denoting a Taxon Record
     * 
     * @return A list of web links for a specific taxon record
     * 
     * @response.representation.200.qname List<TaxonWebLink>
     * @response.representation.200.mediaType application/json;charset=utf-8
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/{taxonVersionKey}/weblinks")
    public List<TaxonWebLink> getTaxonWebLinks(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return taxonMapper.getActiveWebLinksByTVK(taxonVersionKey);
    }
    
    /**
     * Search for a Taxon Record given a search term, this is a SOLR search
     * function
     * 
     * @param rows The number of rows to display per page
     * @param start The starting page
     * @param taxonOutputGroups A List of Taxon Output Groups
     * @param sort Whether we should sort the results
     * @param order The order in which we should sort the results
     * @param prefered If we should restrict the results to preferred taxon 
     * records
     * @param q A given search term
     * 
     * @return Search results for a given search term
     * 
     * @throws SolrServerException 
     * 
     * @response.representation.200.qname SolrResponse
     * @response.representation.200.mediaType application/json;charset=utf-8
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public SolrResponse getTaxa(
            @QueryParam("rows") @DefaultValue("20") int rows,
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("taxonOutputGroupKey") List<String> taxonOutputGroups,
            @QueryParam("sort") String sort,
            @DefaultValue("asc") @QueryParam("order") SolrQuery.ORDER order,
            @QueryParam("prefered") @DefaultValue("false") boolean prefered,
            @QueryParam("q") String q) throws SolrServerException {
        
        if (q == null || q.isEmpty()) {
            return searchResource.searchTaxa(rows, start, taxonOutputGroups, "gatewayRecordCount", SolrQuery.ORDER.desc, true, q);
        }
        
        return searchResource.searchTaxa(rows, start, taxonOutputGroups, sort, order, prefered, q);
    }

    /**
     * Return a list of Site Boundaries containing Taxon records defined by the 
     * given search parameters
     * 
     * @param user The current user
     * @param startYear The start year of the desired search range
     * @param endYear The end year of the desired search range
     * @param datasetKeys Datasets to search in
     * @param taxa Taxon Version Keys to search for
     * @param spatialRelationship Any spatial relationship requirements
     * @param featureID Any feature ID's required
     * @param sensitive If the records should be sensitive or not
     * @param designation Any required designations
     * @param taxonOutputGroup Any Taxon Output Group required
     * @param gridRef A Grid Reference to search within
     * 
     * @return A List of Site Boundaries matching the given parameters
     * 
     * @response.representation.200.qname  List<SiteBoundary>
     * @response.representation.200.mediaType application/json;charset=utf-8
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/{taxonVersionKey}/siteBoundaries")
    public List<SiteBoundary> getSiteBoundaries(
            @TokenUser() User user,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) int startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) int endYear,
            @QueryParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) List<String> datasetKeys,
            @PathParam("taxonVersionKey") List<String> taxa,
            @QueryParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) String spatialRelationship,
            @QueryParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) String featureID,
            @QueryParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) Boolean sensitive,
            @QueryParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) String designation,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) String taxonOutputGroup,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef) {
        return siteBoundaryMapper.getByTaxonVersionKey(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, 0);
    }

    /**
     * Return a zipped copy of a list of Site Boundaries containing Taxon 
     * records defined by the given search parameters
     * 
     * @param user The current user
     * @param startYear The start year of the desired search range
     * @param endYear The end year of the desired search range
     * @param datasetKeys Datasets to search in
     * @param taxa Taxon Version Keys to search for
     * @param spatialRelationship Any spatial relationship requirements
     * @param featureID Any feature ID's required
     * @param sensitive If the records should be sensitive or not
     * @param designation Any required designations
     * @param taxonOutputGroup Any Taxon Output Group required
     * @param gridRef A Grid Reference to search within
     * 
     * @return A zipped download containing list of Site Boundaries matching the 
     * given parameters
     * 
     * @response.representation.200.qname StreamingOutput
     * @response.representation.200.mediaType application/x-zip-compressed
     */
    @GET
    @Produces("application/x-zip-compressed")
    @Path("/{taxonVersionKey}/siteBoundaries/download")
    public Response getSiteBoundariesDownload(
            @TokenUser() final User user,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) final int startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) final int endYear,
            @QueryParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) final List<String> datasetKeys,
            @PathParam("taxonVersionKey") final List<String> taxa,
            @QueryParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) final String spatialRelationship,
            @QueryParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) final String featureID,
            @QueryParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) final Boolean sensitive,
            @QueryParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) final String designation,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) final String taxonOutputGroup,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) final String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) final String polygon,
            @Context HttpServletResponse response) throws IOException {
        try {
            response.setHeader("Content-Disposition", "attachement; filename=\"siteList.zip\"");
            ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
            String title = "Site list download";
            addSites(zip, user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
            addReadMe(zip, title, user, startYear, endYear, datasetKeys, spatialRelationship, sensitive, designation, taxa);
            addDatasetMetadata(zip, user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon);
            zip.flush();
            zip.close();            
        } catch (IOException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok().build();
//        return new StreamingOutput() {
//
//            @Override
//            public void write(OutputStream out) throws IOException, WebApplicationException {
//                ZipOutputStream zip = new ZipOutputStream(out);
//                String title = "Site list download";
//                addSites(zip, user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
//                addReadMe(zip, title, user, startYear, endYear, datasetKeys, spatialRelationship, sensitive, designation, taxa);
//                addDatasetMetadata(zip, user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon);
//                zip.flush();
//                zip.close();
//            }
//        };
    }

    /**
     * Adds dataset metadata to zip file before returning it
     * 
     * @param zip A zip file to be returned to the user
     * @param user The current user
     * @param startYear The start year of the desired search range
     * @param endYear The end year of the desired search range
     * @param datasetKeys Datasets to search in
     * @param taxa Taxon Version Keys to search for
     * @param spatialRelationship Any spatial relationship requirements
     * @param featureID Any feature ID's required
     * @param sensitive If the records should be sensitive or not
     * @param designation Any required designations
     * @param taxonOutputGroup Any Taxon Output Group required
     * @param gridRef A Grid Reference to search within
     * @param polygon WKT polygon filter
     * 
     * @throws IOException 
     */
    private void addDatasetMetadata(ZipOutputStream zip, User user, int startYear, int endYear, List<String> datasetKeys, List<String> taxa, String spatialRelationship, String featureID, boolean sensitive, String designation, String taxonOutputGroup, String gridRef, String polygon) throws IOException {
        List<TaxonDatasetWithQueryStats> datasetsWithQueryStats = observationMapper.selectObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon);
        downloadHelper.addDatasetWithQueryStatsMetadata(zip, user.getId(), datasetsWithQueryStats);
    }

    /**
     * Add a README document to a given zip file before returning it to the user
     * 
     * @param zip
     * @param title
     * @param user The current user
     * @param startYear The start year of the desired search range
     * @param endYear The end year of the desired search range
     * @param datasetKeys Datasets to search in
     * @param spatialRelationship Any spatial relationship requirements
     * @param sensitive If the records should be sensitive or not
     * @param designation Any required designations
     * @param taxa Taxon Version Keys to search for
     * 
     * @throws IOException 
     */
    private void addReadMe(ZipOutputStream zip, String title, User user, int startYear, int endYear, List<String> datasetKeys, String spatialRelationship, boolean sensitive, String designation, List<String> taxa) throws IOException {
        HashMap<String, String> filters = new HashMap<String, String>();
        if (taxa != null && taxa.size() > 0 && !((String) taxa.get(0)).equals("")) {
            filters.put("Taxa", ProviderHelper.taxaListToCommaList(taxa));
        }

        if (!(new Integer(startYear).toString().equals(ObservationResourceDefaults.defaultStartYear))) {
            filters.put("Start year", new Integer(startYear).toString());
        }
        if (!(new Integer(endYear).toString().equals(ObservationResourceDefaults.defaultEndYear))) {
            filters.put("End year", new Integer(endYear).toString());
        }
        filters.put("Spatial relationship", spatialRelationship);
        filters.put("Include only sensitive records", Boolean.toString(sensitive));
        if (designation != null && !designation.equals(ObservationResourceDefaults.defaultDesignation)) {
            Designation desig = designationMapper.selectByID(designation);
            filters.put("Designation key", designation);
            filters.put("Designation name", desig.getName());
        }
        if (datasetKeys != null && datasetKeys.size() > 0 && !((String) datasetKeys.get(0)).equals("")) {
            filters.put("Dataset keys", ProviderHelper.datasetListToCommaList(datasetKeys));
        }
        downloadHelper.addReadMe(zip, user, title, filters);
    }
    
    /**
     * Add sites data to a given zip file before returning it to the user
     * 
     * @param zip A zip file to be returned to the user
     * @param user The current user
     * @param startYear The start year of the desired search range
     * @param endYear The end year of the desired search range
     * @param datasetKeys Datasets to search in
     * @param taxa Taxon Version Keys to search for
     * @param spatialRelationship Any spatial relationship requirements
     * @param featureID Any feature ID's required
     * @param sensitive If the records should be sensitive or not
     * @param designation Any required designations
     * @param taxonOutputGroup Any Taxon Output Group required
     * @param gridRef A Grid Reference to search within
     * 
     * @throws IOException 
     */
    private void addSites(ZipOutputStream zip, User user, int startYear, int endYear, List<String> datasetKeys, List<String> taxa, String spatialRelationship, String featureID, boolean sensitive, String designation, String taxonOutputGroup, String gridRef) throws IOException {
        List<SiteBoundary> sites = siteBoundaryMapper.getByTaxonVersionKey(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, 0);
        zip.putNextEntry(new ZipEntry("SiteList.csv"));
        ArrayList<String> values = new ArrayList<String>();
        values.add("Site Name");
        values.add("Type");
        values.add("NBN Site Key");
        downloadHelper.writelnCsv(zip, values);
        for (SiteBoundary site : sites) {
            values = new ArrayList<String>();
            values.add(site.getName());
            values.add(site.getSiteBoundaryDataset().getTitle());
            values.add(site.getIdentifier());
            downloadHelper.writelnCsv(zip, values);
        }
    }
}

package uk.org.nbn.nbnv.api.rest.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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

    @Autowired
    SearchResource searchResource;
    @Autowired
    TaxonMapper taxonMapper;
    @Autowired
    DatasetMapper datasetMapper;
    @Autowired
    DesignationMapper designationMapper;
    @Autowired
    SiteBoundaryMapper siteBoundaryMapper;
    @Autowired
    DownloadHelper downloadHelper;
    @Autowired
    TaxonObservationMapper observationMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("{taxonVersionKey}")
    @SolrResolver("TAXON")
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
    @Path("{taxonVersionKey}/designations/archive")
    public List<TaxonDesignation> getArchiveTaxonDesignations(@PathParam("taxonVersionKey") String taxonVersionKey) {
        return designationMapper.selectArchiveByTaxonVersionKey(taxonVersionKey);
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
            @QueryParam("taxonOutputGroupKey") List<String> taxonOutputGroups,
            @QueryParam("sort") String sort,
            @DefaultValue("asc") @QueryParam("order") SolrQuery.ORDER order,
            @QueryParam("q") String q) throws SolrServerException {
        
        if (q == null || q.isEmpty()) {
            return searchResource.searchTaxa(rows, start, taxonOutputGroups, "gatewayRecordCount", SolrQuery.ORDER.desc, q);
        }
        
        return searchResource.searchTaxa(rows, start, taxonOutputGroups, sort, order, q);
    }

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
        return siteBoundaryMapper.getByTaxonVersionKey(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
    }

    @GET
    @Produces("application/x-zip-compressed")
    @Path("/{taxonVersionKey}/siteBoundaries/download")
    public StreamingOutput getSiteBoundariesDownload(
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
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) final String gridRef) {
        return new StreamingOutput() {
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
                ZipOutputStream zip = new ZipOutputStream(out);
                String title = "Site list download";
                addSites(zip, user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
                addReadMe(zip, title, user, startYear, endYear, datasetKeys, spatialRelationship, sensitive, designation, taxa);
                addDatasetMetadata(zip, user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
                zip.flush();
                zip.close();
            }
        };


    }

    private void addDatasetMetadata(ZipOutputStream zip, User user, int startYear, int endYear, List<String> datasetKeys, List<String> taxa, String spatialRelationship, String featureID, boolean sensitive, String designation, String taxonOutputGroup, String gridRef) throws IOException {
        List<TaxonDatasetWithQueryStats> datasetsWithQueryStats = observationMapper.selectObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
        downloadHelper.addDatasetWithQueryStatsMetadata(zip, user.getId(), datasetsWithQueryStats);
    }

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
    
    private void addSites(ZipOutputStream zip, User user, int startYear, int endYear, List<String> datasetKeys, List<String> taxa, String spatialRelationship, String featureID, boolean sensitive, String designation, String taxonOutputGroup, String gridRef) throws IOException {
        List<SiteBoundary> sites = siteBoundaryMapper.getByTaxonVersionKey(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
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

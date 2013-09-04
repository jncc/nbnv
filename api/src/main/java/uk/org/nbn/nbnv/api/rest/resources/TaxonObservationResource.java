package uk.org.nbn.nbnv.api.rest.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper;
import uk.org.nbn.nbnv.api.dao.providers.ProviderHelper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DesignationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.FeatureMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationSuppliedListMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonObservationAttributeMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonObservationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonOutputGroupMapper;
import uk.org.nbn.nbnv.api.model.*;
import uk.org.nbn.nbnv.api.model.meta.DownloadFilterJSON;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetOrOrgAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.rest.resources.utils.DownloadHelper;
import uk.org.nbn.nbnv.api.utils.DownloadUtils;

@Component
@Path("/taxonObservations")
public class TaxonObservationResource extends AbstractResource {

    @Autowired TaxonObservationMapper observationMapper;
    @Autowired OrganisationMapper organisationMapper;
    @Autowired DatasetMapper datasetMapper;
    @Autowired FeatureMapper featureMapper;
    @Autowired TaxonOutputGroupMapper taxonOutputGroupMapper;
    @Autowired DownloadHelper downloadHelper;
    @Autowired DesignationMapper designationMapper;
    @Autowired SiteBoundaryMapper siteBoundaryMapper;
    @Autowired TaxonObservationAttributeMapper taxonObservationAttributeMapper;
    @Autowired OperationalTaxonObservationFilterMapper oTaxonObservationFilterMapper;
    @Autowired OrganisationSuppliedListMapper organisationSuppliedListMapper;
    @Autowired DownloadUtils downloadUtils;

    /**
     * Return a Taxon Observation Record with a specified numerical ID, as long
     * as the user is authorised to view this record
     * 
     * @param user The current User
     * @param id A numerical ID for an observation record
     * 
     * @return The requested Taxon Observation Record
     * 
     * @response.representation.200.qname List<TaxonObservation>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id : \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonObservation getObservation(@TokenUser() User user, @PathParam("id") int id) {
        return observationMapper.selectById(id, user.getId());
    }

    /**
     * Returns a List of Taxon Observation Records in a specified dataset as 
     * long as the user is authorised to view them
     * 
     * @param user The current user
     * @param datasetKey A dataset key
     * 
     * @return A list of Taxon Observation Records in the specified dataset
     * 
     * @response.representation.200.qname List<TaxonObservation>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{datasetKey : [A-Z][A-Z0-9]{7}}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonObservation> getObservationsByDataset(@TokenUser() User user, @PathParam("datasetKey") String datasetKey) {
        return observationMapper.selectByDataset(datasetKey, user.getId());
    }

    /**
     * Returns a list of Taxon Observations about a specific Taxon Version Key 
     * that the current user has access to
     * 
     * @param user The current user
     * @param ptvk The Taxon Version Key to search for
     * 
     * @return A List of Taxon Observations containing the specified Taxon 
     * Version Key
     * 
     * @response.representation.200.qname List<TaxonObservation>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{ptvk : [A-Z]{3}SYS[0-9]{10}}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonObservation> getObservationsByTaxon(@TokenUser() User user, @PathParam("ptvk") String ptvk) {
        return observationMapper.selectByPTVK(ptvk, user.getId());
    }
    
    /**
     * Returns a JSONObject which states if a TVK has presence,absence and polygon records associated 
     * with it which are accessible by the current user
     * 
     * @param user The current user
     * @param taxonVersionKey The TVK to be searched
     * @return If any presence records exist
     * 
     * @response.representation.200.qname boolean
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{ptvk : [A-Z]{3}SYS[0-9]{10}}/types")
    public JSONObject taxonHasPresence(@TokenUser() User user, @PathParam("ptvk") String taxonVersionKey) throws JSONException {
        return new JSONObject()
                .put("hasGridAbsence", observationMapper.pTVKHasGridAbsence(taxonVersionKey, user.getId(), 1) != null)
                .put("hasGridPresence", observationMapper.pTVKHasGridAbsence(taxonVersionKey, user.getId(), 0) != null)
                .put("hasPolygonAbsence", observationMapper.pTVKHasPolygonAbsence(taxonVersionKey, user.getId(), 1) != null)
                .put("hasPolygonPresence", observationMapper.pTVKHasPolygonAbsence(taxonVersionKey, user.getId(), 0) != null);
    }

    /*
     * Needs InjectorProvider to work
     *
     * @GET public List<TaxonObservation> getObservationsByFilter(@TokenUser()
     * User user, TaxonObservationFilter filter) { return
     * observationMapper.selectObservationRecordsByFilter(user.getId(), filter.getStartYear(),
     * filter.getEndYear()); }
     */
    /**
     * Returns a list of Taxon Observations matching the given serach parameters
     * 
     * @param user The current user, determines what datasets they have access 
     * to
     * @param startYear The start year of the desired range
     * @param endYear The end year of the desired range
     * @param datasetKeys Datasets to search in
     * @param taxa Taxon Version Keys to search for
     * @param spatialRelationship Any spatial relationship information required
     * @param featureID Any required feature ID
     * @param sensitive If the results should include sensitive records or not
     * @param designation Any required designations
     * @param taxonOutputGroup Any required taxon output groups
     * @param gridRef Any grid references to search within
     * @param polygon WKT WGS-84 polygon filter
     * 
     * @return A list of Taxon Observations conforming to the provided search
     * parameters
     * 
     * @response.representation.200.qname List<TaxonObservation>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonObservation> getObservationsByFilter(
            @TokenUser() User user,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) int startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) int endYear,
            @QueryParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) List<String> datasetKeys,
            @QueryParam("ptvk") @DefaultValue(ObservationResourceDefaults.defaultTaxa) List<String> taxa,
            @QueryParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) String spatialRelationship,
            @QueryParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) String featureID,
            @QueryParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) Boolean sensitive,
            @QueryParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) String designation,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) String taxonOutputGroup,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon) {
        //TODO: squareBlurring(?)
        return observationMapper.selectObservationRecordsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon);
    }
    
    /**
     * Return a list of Taxon Observation Attribute Value records that conform
     * to the provided search parameters
     * 
     * @param user The current user, determines what datasets they have access 
     * to
     * @param datasetKey The Dataset to search in
     * @param attributeID An attribute that the records should have
     * @param startYear The start year of the desired range
     * @param endYear The end year of the desired range
     * @param taxa Taxon Version Keys to search for
     * @param spatialRelationship Any spatial relationship information required
     * @param featureID Any required feature ID
     * @param sensitive If the results should include sensitive records or not
     * @param designation Any required designations
     * @param taxonOutputGroup Any required taxon output groups
     * @param gridRef Any grid references to search within
     * @param polygon WKT WGS-84 polygon filter
     * 
     * @return A list of TaxonObservationAttributeValue Records conforming to 
     * the provided search parameters
     * 
     * @response.representation.200.qname List<TaxonObservationAttributeValue>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{datasetKey : [A-Z][A-Z0-9]{7}}/attributes/{attributeID: [0-9]{1,10}}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonObservationAttributeValue> getOneObservationAttributeByFilter(
            @TokenUser() User user,
            @PathParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) String datasetKey,
            @PathParam("attributeID") @DefaultValue(ObservationResourceDefaults.defaultAttributeID) int attributeID,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) int startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) int endYear,
            @QueryParam("ptvk") @DefaultValue(ObservationResourceDefaults.defaultTaxa) List<String> taxa,
            @QueryParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) String spatialRelationship,
            @QueryParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) String featureID,
            @QueryParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) Boolean sensitive,
            @QueryParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) String designation,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) String taxonOutputGroup,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon) {
        //TODO: squareBlurring(?)
        return observationMapper.selectObservationAttributeByFilter(user, datasetKey, attributeID, startYear, endYear, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon);
    }

    /**
     * Returns a list of Taxon With Query Stats, essentially Taxon Observations
     * by species, conforming to the provided search parameters
     * 
     * @param user The current user, determines what datasets they have access 
     * to
     * @param startYear The start year of the desired range
     * @param endYear The end year of the desired range
     * @param datasetKeys Datasets to search in
     * @param taxa Taxon Version Keys to search for
     * @param spatialRelationship Any spatial relationship information required
     * @param featureID Any required feature ID
     * @param sensitive If the results should include sensitive records or not
     * @param designation Any required designations
     * @param taxonOutputGroup Any required taxon output groups
     * @param gridRef Any grid references to search within
     * @param polygon WKT WGS-84 polygon filter
     * 
     * @return A List of TaxonWithQueryStats conforming to the provided search
     * parameters
     * 
     * @response.representation.200.qname List<TaxonWithQueryStats>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/species")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonWithQueryStats> getObservationSpeciesByFilter(
            @TokenUser() User user,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) int startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) int endYear,
            @QueryParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) List<String> datasetKeys,
            @QueryParam("ptvk") @DefaultValue(ObservationResourceDefaults.defaultTaxa) List<String> taxa,
            @QueryParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) String spatialRelationship,
            @QueryParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) String featureID,
            @QueryParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) Boolean sensitive,
            @QueryParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) String designation,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) String taxonOutputGroup,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon) {
        //TODO: squareBlurring(?)
        List<TaxonWithQueryStats> toReturn = observationMapper.selectObservationSpeciesByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon, 0);
        Collections.sort(toReturn);
        return toReturn;
    }

    /**
     * Returns a zip file containing a list of Taxon With Query Stats, 
     * essentially Taxon Observations by species, conforming to the provided 
     * search parameters
     * 
     * @param user The current user, determines what datasets they have access 
     * to
     * @param startYear The start year of the desired range
     * @param endYear The end year of the desired range
     * @param datasetKeys Datasets to search in
     * @param taxa Taxon Version Keys to search for
     * @param spatialRelationship Any spatial relationship information required
     * @param featureID Any required feature ID
     * @param sensitive If the results should include sensitive records or not
     * @param designation Any required designations
     * @param taxonOutputGroup Any required taxon output groups
     * @param gridRef Any grid references to search within
     * @param polygon WKT WGS-84 polygon filter
     * 
     * @return A Zip file containing a list of TaxonWithQueryStats conforming to 
     * the provided search parameters
     * 
     * @response.representation.200.qname StreamingOutput
     * @response.representation.200.mediaType application/x-zip-compressed
     */
    @GET
    @Path("/species/download")
    @Produces("application/x-zip-compressed")
    public StreamingOutput getSpeciesDownloadByFilter(
            @TokenUser() final User user,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) final int startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) final int endYear,
            @QueryParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) final List<String> datasetKeys,
            @QueryParam("ptvk") @DefaultValue(ObservationResourceDefaults.defaultTaxa) final List<String> taxa,
            @QueryParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) final String spatialRelationship,
            @QueryParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) final String featureID,
            @QueryParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) final Boolean sensitive,
            @QueryParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) final String designation,
            @QueryParam("organisationList") final int organisationList,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) final String taxonOutputGroup,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) final String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) final String polygon) {
        //TODO: squareBlurring(?)
        return new StreamingOutput() {
            public void write(OutputStream out) throws IOException, WebApplicationException {
                ZipOutputStream zip = new ZipOutputStream(out);
                String title = "Species list download";
                addSpecies(zip, user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon);
                addReadMe(zip, title, user, startYear, endYear, datasetKeys, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, organisationList);
                addDatasetMetadata(zip, user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, organisationList, gridRef, polygon);
                zip.flush();
                zip.close();
            }
        };
    }

    /**
     * Return a list of TaxonOutputGroupWithQueryStats based on the provided
     * parameters
     * 
     * @param user The current user, determines what datasets they have access 
     * to
     * @param startYear The start year of the desired range
     * @param endYear The end year of the desired range
     * @param datasetKeys Datasets to search in
     * @param taxa Taxon Version Keys to search for
     * @param spatialRelationship Any spatial relationship information required
     * @param featureID Any required feature ID
     * @param sensitive If the results should include sensitive records or not
     * @param designation Any required designations
     * @param taxonOutputGroup Any required taxon output groups
     * @param gridRef Any grid references to search within
     * @param polygon WKT WGS-84 polygon filter

     * @return A list of TaxonOutputGroupWithQueryStats based on the provided
     * parameters
     * 
     * @response.representation.200.qname List<TaxonOutputGroupWithQueryStats>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/groups")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonOutputGroupWithQueryStats> getObservationGroupsByFilter(
            @TokenUser() User user,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) int startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) int endYear,
            @QueryParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) List<String> datasetKeys,
            @QueryParam("ptvk") @DefaultValue(ObservationResourceDefaults.defaultTaxa) List<String> taxa,
            @QueryParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) String spatialRelationship,
            @QueryParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) String featureID,
            @QueryParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) Boolean sensitive,
            @QueryParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) String designation,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) String taxonOutputGroup,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon) {
        //TODO: squareBlurring(?)
        return observationMapper.selectObservationGroupsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon, 0);
    }

    /**
     * Return a list of TaxonDatasetWithQueryStats constrained by the given 
     * parameters
     * 
     * @param user The current user, determines what datasets they have access 
     * to
     * @param startYear The start year of the desired range
     * @param endYear The end year of the desired range
     * @param datasetKeys Datasets to search in
     * @param taxa Taxon Version Keys to search for
     * @param spatialRelationship Any spatial relationship information required
     * @param featureID Any required feature ID
     * @param sensitive If the results should include sensitive records or not
     * @param designation Any required designations
     * @param taxonOutputGroup Any required taxon output groups
     * @param gridRef Any grid references to search within
     * @param polygon WKT WGS-84 polygon filter
     * 
     * @return Return a list of TaxonDatasetWithQueryStats constrained by the given 
     * parameters
     * 
     * @response.representation.200.qname List<TaxonDatasetWithQueryStats>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/datasets")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonDatasetWithQueryStats> getObservationDatasetsByFilter(
            @TokenUser() User user,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) int startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) int endYear,
            @QueryParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) List<String> datasetKeys,
            @QueryParam("ptvk") @DefaultValue(ObservationResourceDefaults.defaultTaxa) List<String> taxa,
            @QueryParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) String spatialRelationship,
            @QueryParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) String featureID,
            @QueryParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) Boolean sensitive,
            @QueryParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) String designation,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) String taxonOutputGroup,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon) {
        //TODO: squareBlurring(?)
        return observationMapper.selectObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon);
    }

    /**
     * Return a list of all TaxonDatasetWithQueryStats constrained by the given
     * parameters
     * 
     * @param user The current user, determines what datasets they have access 
     * to
     * @param startYear The start year of the desired range
     * @param endYear The end year of the desired range
     * @param datasetKeys Datasets to search in
     * @param taxa Taxon Version Keys to search for
     * @param spatialRelationship Any spatial relationship information required
     * @param featureID Any required feature ID
     * @param sensitive If the results should include sensitive records or not
     * @param designation Any required designations
     * @param taxonOutputGroup Any required taxon output groups
     * @param gridRef Any grid references to search within
     * @param polygon WKT WGS-84 polygon filter
     * 
     * @return A list of all TaxonDatasetWithQueryStats constrained by the given
     * parameters
     * 
     * @response.representation.200.qname List<TaxonDatasetWithQueryStats>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/datasets/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonDatasetWithQueryStats> getAllObservationDatasetsByFilter(
            @TokenUser() User user,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) int startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) int endYear,
            @QueryParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) List<String> datasetKeys,
            @QueryParam("ptvk") @DefaultValue(ObservationResourceDefaults.defaultTaxa) List<String> taxa,
            @QueryParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) String spatialRelationship,
            @QueryParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) String featureID,
            @QueryParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) Boolean sensitive,
            @QueryParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) String designation,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) String taxonOutputGroup,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon) {
        //TODO: squareBlurring(?)
        return observationMapper.selectAllObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon);
    }

    /**
     * Returns a list of all requestable TaxonDatasetWithQueryStats constrained
     * by the given parameters
     * 
     * @param user The current user, determines what datasets they have access 
     * to
     * @param startYear The start year of the desired range
     * @param endYear The end year of the desired range
     * @param datasetKeys Datasets to search in
     * @param taxa Taxon Version Keys to search for
     * @param spatialRelationship Any spatial relationship information required
     * @param featureID Any required feature ID
     * @param sensitive If the results should include sensitive records or not
     * @param designation Any required designations
     * @param taxonOutputGroup Any required taxon output groups
     * @param gridRef Any grid references to search within
     * @param polygon WKT WGS-84 polygon filter
     * 
     * @return A list of all requestable TaxonDatasetWithQueryStats constrained
     * by the given parameters
     * 
     * @response.representation.200.qname List<TaxonDatasetWithQueryStats>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/datasets/requestable")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonDatasetWithQueryStats> getRequestableObservationDatasetsByFilter(
            @TokenUser() User user,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) int startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) int endYear,
            @QueryParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) List<String> datasetKeys,
            @QueryParam("ptvk") @DefaultValue(ObservationResourceDefaults.defaultTaxa) List<String> taxa,
            @QueryParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) String spatialRelationship,
            @QueryParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) String featureID,
            @QueryParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) Boolean sensitive,
            @QueryParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) String designation,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) String taxonOutputGroup,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon) {
        //TODO: squareBlurring(?)
        
        // Stop leaking dataset info when spatial filter applied. Fixes issue species info fishing
        if (sensitive && !featureID.equals(ObservationResourceDefaults.defaultFeatureID)) {
            sensitive = Boolean.FALSE;
        }
        
        return observationMapper.selectRequestableObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon);
    }
    
    /**
     * Returns a list of all requestable TaxonDatasetWithQueryStats constrained
     * by the given parameters and a single dataset
     * 
     * @param user The current user, determines what datasets they have access 
     * to
     * @param startYear The start year of the desired range
     * @param endYear The end year of the desired range
     * @param datasetKey Dataset to search in
     * @param taxa Taxon Version Keys to search for
     * @param spatialRelationship Any spatial relationship information required
     * @param featureID Any required feature ID
     * @param sensitive If the results should include sensitive records or not
     * @param designation Any required designations
     * @param taxonOutputGroup Any required taxon output groups
     * @param gridRef Any grid references to search within
     * @param polygon WKT WGS-84 polygon filter
     * 
     * @return A list of all requestable TaxonDatasetWithQueryStats constrained
     * by the given parameters
     * 
     * @response.representation.200.qname List<TaxonDatasetWithQueryStats>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/datasets/{datasetKey : [A-Z][A-Z0-9]{7}}/requestable")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonDatasetWithQueryStats> getRequestableObservationDetailsForDatasetByFilter(
            @TokenDatasetAdminUser(path="datasetKey") User user,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) int startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) int endYear,
            @PathParam("datasetKey") String datasetKey,
            @QueryParam("ptvk") @DefaultValue(ObservationResourceDefaults.defaultTaxa) List<String> taxa,
            @QueryParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) String spatialRelationship,
            @QueryParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) String featureID,
            @QueryParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) Boolean sensitive,
            @QueryParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) String designation,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) String taxonOutputGroup,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon) {
        //TODO: squareBlurring(?)
                
        List<String> datasetKeys = new ArrayList<String>();
        datasetKeys.add(datasetKey);
        return observationMapper.selectRequestableObservationDatasetsByFilter(User.PUBLIC_USER, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon);
    }
    
    /**
     * Returns a list of all TaxonDatasetWithQueryStats constrained by the given 
     * parameters in unavailable datasets
     * 
     * @param user The current user, determines what datasets they have access 
     * to
     * @param startYear The start year of the desired range
     * @param endYear The end year of the desired range
     * @param datasetKeys Datasets to search in
     * @param taxa Taxon Version Keys to search for
     * @param spatialRelationship Any spatial relationship information required
     * @param featureID Any required feature ID
     * @param sensitive If the results should include sensitive records or not
     * @param designation Any required designations
     * @param taxonOutputGroup Any required taxon output groups
     * @param gridRef Any grid references to search within
     * @param polygon WKT WGS-84 polygon filter
     * 
     * @return A list of all TaxonDatasetWithQueryStats constrained by the given 
     * parameters in unavailable datasets
     * 
     * @response.representation.200.qname List<TaxonDatasetWithQueryStats>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/unavailableDatasets")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonDatasetWithQueryStats> getUnavailableDatasetsByFilter(
            @TokenUser() User user,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) int startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) int endYear,
            @QueryParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) List<String> datasetKeys,
            @QueryParam("ptvk") @DefaultValue(ObservationResourceDefaults.defaultTaxa) List<String> taxa,
            @QueryParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) String spatialRelationship,
            @QueryParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) String featureID,
            @QueryParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) Boolean sensitive,
            @QueryParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) String designation,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) String taxonOutputGroup,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon) {
        //TODO: squareBlurring(?)
        return observationMapper.selectUnavailableDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon);
    }

    /**
     * Returns a list of TaxonDatasets (Observations) constrained by the given
     * parameters
     * 
     * @param user The current user, determines what datasets they have access 
     * to
     * @param startYear The start year of the desired range
     * @param endYear The end year of the desired range
     * @param datasetKeys Datasets to search in
     * @param taxa Taxon Version Keys to search for
     * @param spatialRelationship Any spatial relationship information required
     * @param featureID Any required feature ID
     * @param sensitive If the results should include sensitive records or not
     * @param designation Any required designations
     * @param taxonOutputGroup Any required taxon output groups
     * @param gridRef Any grid references to search within
     * @param polygon WKT WGS-84 polygon filter
     * 
     * @return A list of TaxonDatasets (Observations) constrained by the given
     * parameters
     * 
     * @response.representation.200.qname List<TaxonDataset>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/datasets/observations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonDataset> getDatasetsWithObservationsByFilter(
            @TokenUser(allowPublic=false) User user,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) int startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) int endYear,
            @QueryParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) List<String> datasetKeys,
            @QueryParam("ptvk") @DefaultValue(ObservationResourceDefaults.defaultTaxa) List<String> taxa,
            @QueryParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) String spatialRelationship,
            @QueryParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) String featureID,
            @QueryParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) Boolean sensitive,
            @QueryParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) String designation,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) String taxonOutputGroup,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon) {
        //TODO: squareBlurring(?)
        List<TaxonObservation> taxonObservationsOrderedByDataset = observationMapper.selectObservationsByFilterOrderedByDataset(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon);
        return getDatasetsWithObservations(taxonObservationsOrderedByDataset);
    }

    /**
     * Return a list of ProviderWithQueryStats constrained by the given 
     * parameters
     * 
     * @param user The current user, determines what datasets they have access 
     * to
     * @param startYear The start year of the desired range
     * @param endYear The end year of the desired range
     * @param datasetKeys Datasets to search in
     * @param taxa Taxon Version Keys to search for
     * @param spatialRelationship Any spatial relationship information required
     * @param featureID Any required feature ID
     * @param sensitive If the results should include sensitive records or not
     * @param designation Any required designations
     * @param taxonOutputGroup Any required taxon output groups
     * @param gridRef Any grid references to search within
     * @param polygon WKT WGS-84 polygon filter
     * @param getPerm true / false determines if to not use or use the 
     * groupDatasetsByProvider method to generate output
     * 
     * @return A list of ProviderWithQueryStats constrained by the given 
     * parameters
     * 
     * @response.representation.200.qname List<ProviderWithQueryStats>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/providers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProviderWithQueryStats> getObservationProvidersByFilter(
            @TokenUser() User user,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) int startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) int endYear,
            @QueryParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) List<String> datasetKeys,
            @QueryParam("ptvk") @DefaultValue(ObservationResourceDefaults.defaultTaxa) List<String> taxa,
            @QueryParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) String spatialRelationship,
            @QueryParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) String featureID,
            @QueryParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) Boolean sensitive,
            @QueryParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) String designation,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) String taxonOutputGroup,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon,
            @QueryParam("returnAccessPositions") @DefaultValue("") String getPerm) {
        //TODO: squareBlurring(?)
        List<TaxonDatasetWithQueryStats> datasetsWithQueryStats = observationMapper.selectObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon);

        if (getPerm.equalsIgnoreCase("true")) {
            for (TaxonDatasetWithQueryStats d : datasetsWithQueryStats) {
                d.setAccessPositions(datasetMapper.getDatasetAccessPositions(d.getDatasetKey(), user.getId()));
            }
        }
        return groupDatasetsByProvider(datasetsWithQueryStats);
    }
    
    /***************************************************************************
     * Download Functions
     **************************************************************************/
    
    /**
     * Return a list of taxon observations that match a given filter in the JSON
     * string supplied, the list is returned as a ZIP file with appropriate 
     * ReadMes attached and logs the download in the database for stats
     * 
     * @param user The current user (must be logged in)
     * @param json A JSON string representing the filter
     * @return A ZIP file containing the list of observations and ReadMes
     * @throws IOException 
     */
    @GET
    @Path("/download")
    @Produces("application/x-zip-compressed")
    public StreamingOutput getObservationsByFilterZip(            
            @TokenUser(allowPublic = false) final User user,
            @QueryParam("json") String json) throws IOException {        
        
        final DownloadFilterJSON dFilter = parseJSON(json);
        TaxonObservationFilter filter = downloadUtils.createFilter(json, dFilter);
        
        oTaxonObservationFilterMapper.createFilter(filter);
        if (dFilter.getReason().getOrganisationID() > -1) {
            // If we are doing the download on behalf of an organisation
            oTaxonObservationFilterMapper.createDownloadLogAsOrg(filter.getId(), 
                    dFilter.getReason().getPurpose(), dFilter.getReason().getDetails(), 
                    user.getId(), organisationMapper.selectByID(
                        dFilter.getReason().getOrganisationID()).getName(), 
                    dFilter.getReason().getOrganisationID());
        } else {
            // If we are doing the download for personal reasons
            oTaxonObservationFilterMapper.createDownloadLog(filter.getId(), dFilter.getReason().getPurpose(), dFilter.getReason().getDetails(), user.getId());
        }
        
        final int filterID = filter.getId();
        return new StreamingOutput() {
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
                ZipOutputStream zip = new ZipOutputStream(out);
                String title = "Taxon Observation Download";
                
                List<String> taxaList = new ArrayList<String>();
                taxaList.add(dFilter.getTaxon().getTvk());
                // Add the list of observations to the download
                addObservations(zip, user, 
                        dFilter.getYear().getStartYear(), 
                        dFilter.getYear().getEndYear(), 
                        dFilter.getDataset().getDatasets(), 
                        taxaList, 
                        dFilter.getSpatial().getMatch(),
                        dFilter.getSpatial().getFeature(), 
                        dFilter.getSensitive().equals("sans"), 
                        dFilter.getTaxon().getDesignation(), 
                        dFilter.getTaxon().getOutput(), 
                        dFilter.getTaxon().getOrgSuppliedList(),
                        dFilter.getSpatial().getGridRef(), 
                        dFilter.getPolygon(), 
                        dFilter.getReason().getIncludeAttributes().equals("true"), 
                        filterID);
                // Add ReadMe to the download
                addReadMe(zip, title, user,  
                        dFilter.getYear().getStartYear(), 
                        dFilter.getYear().getEndYear(), 
                        dFilter.getDataset().getDatasets(), 
                        dFilter.getSpatial().getMatch(),
                        dFilter.getSpatial().getFeature(), 
                        Boolean.getBoolean(dFilter.getSensitive()), 
                        dFilter.getTaxon().getDesignation(), 
                        dFilter.getTaxon().getOutput(),
                        dFilter.getTaxon().getOrgSuppliedList());
                // Add Dataset Metadata to the download
                addDatasetMetadata(zip, user, 
                        dFilter.getYear().getStartYear(), 
                        dFilter.getYear().getEndYear(), 
                        dFilter.getDataset().getDatasets(), 
                        taxaList, 
                        dFilter.getSpatial().getMatch(),
                        dFilter.getSpatial().getFeature(), 
                        Boolean.getBoolean(dFilter.getSensitive()), 
                        dFilter.getTaxon().getDesignation(), 
                        dFilter.getTaxon().getOutput(), 
                        dFilter.getTaxon().getOrgSuppliedList(),
                        dFilter.getSpatial().getGridRef(), 
                        dFilter.getPolygon());
                zip.flush();
                zip.close();
            }
        };
    }
    
    @GET
    @Path("/download/report/{datasetKey : [A-Z][A-Z0-9]{7}}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DownloadReport> getDownloadReportsByDataset(
            @TokenDatasetOrOrgAdminUser(path = "datasetKey") User user, 
            @PathParam("datasetKey") String datasetKey,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) String startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) String endYear,
            @QueryParam("filterID") @DefaultValue(DownloadResourceDefaults.filterID) int filterID,
            @QueryParam("userID") @DefaultValue(DownloadResourceDefaults.userID) int userID,
            @QueryParam("organisationID") @DefaultValue(DownloadResourceDefaults.organistionID) int organisationID,
            @QueryParam("purposeID") @DefaultValue(DownloadResourceDefaults.purposeID) int purposeID) {
        return observationMapper.selectDownloadReportsByDataset(datasetKey, startYear, endYear, filterID, userID, organisationID, purposeID);
    }

    /**
     * 
     * @param datasetsWithQueryStats
     * @return 
     */
    private List<ProviderWithQueryStats> groupDatasetsByProvider(List<TaxonDatasetWithQueryStats> datasetsWithQueryStats) {
        HashMap<Integer, ProviderWithQueryStats> providers = new HashMap<Integer, ProviderWithQueryStats>();
        for (TaxonDatasetWithQueryStats datasetWithQueryStats : datasetsWithQueryStats) {
            Integer providerKey = datasetWithQueryStats.getTaxonDataset().getOrganisationID();
            if (providers.containsKey(providerKey)) {
                appendDatasetToProvider(providers, datasetWithQueryStats);
            } else {
                providers.put(providerKey, getNewProviderWithQueryStats(datasetWithQueryStats));
            }
        }
        List<ProviderWithQueryStats> toReturn = new ArrayList<ProviderWithQueryStats>(providers.values());
        sortByProviderAndDataset(toReturn);
        return toReturn;
    }

    /**
     * 
     * @param datasetWithQueryStats
     * @return 
     */
    private ProviderWithQueryStats getNewProviderWithQueryStats(TaxonDatasetWithQueryStats datasetWithQueryStats) {
        int organisationID = datasetWithQueryStats.getTaxonDataset().getOrganisationID();
        List<TaxonDatasetWithQueryStats> datasets = new ArrayList<TaxonDatasetWithQueryStats>();
        datasets.add(datasetWithQueryStats);

        ProviderWithQueryStats toReturn = new ProviderWithQueryStats();
        toReturn.setOrganisationID(organisationID);
        toReturn.setQuerySpecificObservationCount(datasetWithQueryStats.getQuerySpecificObservationCount());
        toReturn.setOrganisation(organisationMapper.selectByID(organisationID));
        toReturn.setDatasetsWithQueryStats(datasets);
        return toReturn;
    }

    /**
     * 
     * @param providers
     * @param datasetWithQueryStats 
     */
    private void appendDatasetToProvider(HashMap<Integer, ProviderWithQueryStats> providers, TaxonDatasetWithQueryStats datasetWithQueryStats) {
        ProviderWithQueryStats provider = providers.get(datasetWithQueryStats.getTaxonDataset().getOrganisationID());
        provider.setQuerySpecificObservationCount(provider.getQuerySpecificObservationCount() + datasetWithQueryStats.getQuerySpecificObservationCount());
        provider.getDatasetsWithQueryStats().add(datasetWithQueryStats);
    }

    /**
     * 
     * @param providersToSort 
     */
    private void sortByProviderAndDataset(List<ProviderWithQueryStats> providersToSort) {
        Collections.sort(providersToSort, Collections.reverseOrder());
        for (ProviderWithQueryStats providerWithQueryStats : providersToSort) {
            Collections.sort(providerWithQueryStats.getDatasetsWithQueryStats());
        }
    }

    /*
     * This takes a list of observations in dataset order and returns a list
     * of datasets with their observations
     */
    /**
     * 
     * @param taxonObservationsOrderedByDataset
     * @return 
     */
    private List<TaxonDataset> getDatasetsWithObservations(List<TaxonObservation> taxonObservationsOrderedByDataset) {
        List<TaxonDataset> toReturn = new ArrayList<TaxonDataset>();
        if (taxonObservationsOrderedByDataset.size() > 0) {
            List<TaxonObservation> taxonObservationsForDataset = null;
            String currentDatasetKey = "";
            String previousDatasetKey = "";
            for (TaxonObservation taxonObservation : taxonObservationsOrderedByDataset) {
                currentDatasetKey = taxonObservation.getDatasetKey();
                if (currentDatasetKey.equals(previousDatasetKey)) {
                    taxonObservationsForDataset.add(taxonObservation);
                } else {
                    if (!"".equals(previousDatasetKey)) {
                        appendTaxonDataset(taxonObservationsForDataset, previousDatasetKey, toReturn);
                    }
                    taxonObservationsForDataset = new ArrayList<TaxonObservation>();
                    taxonObservationsForDataset.add(taxonObservation);
                }
                previousDatasetKey = currentDatasetKey;
            }
            appendTaxonDataset(taxonObservationsForDataset, currentDatasetKey, toReturn);
        }
        return toReturn;
    }

    /**
     * 
     * @param taxonObservations
     * @param taxonDatasetKey
     * @param taxonDatasets 
     */
    private void appendTaxonDataset(List<TaxonObservation> taxonObservations, String taxonDatasetKey, List<TaxonDataset> taxonDatasets) {
        TaxonDataset taxonDataset = datasetMapper.selectTaxonDatasetByID(taxonDatasetKey);
        taxonDataset.setObservations(taxonObservations);
        taxonDatasets.add(taxonDataset);
    }

    /**
     * 
     * @param zip
     * @param user
     * @param startYear
     * @param endYear
     * @param datasetKeys
     * @param taxa
     * @param spatialRelationship
     * @param featureID
     * @param sensitive
     * @param designation
     * @param taxonOutputGroup
     * @param gridRef
     * @throws IOException 
     */
    private void addSpecies(ZipOutputStream zip, User user, int startYear, int endYear, List<String> datasetKeys, List<String> taxa, String spatialRelationship, String featureID, boolean sensitive, String designation, String taxonOutputGroup, String gridRef, String polygon) throws IOException {
        List<TaxonWithQueryStats> taxaWithStats = observationMapper.selectObservationSpeciesByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon, 0);
        zip.putNextEntry(new ZipEntry("TaxonList.csv"));
        ArrayList<String> values = new ArrayList<String>();
        values.add("TaxonName");
        values.add("Authority");
        values.add("CommonName");
        values.add("PreferredTaxonVersionKey");
        downloadHelper.writelnCsv(zip, values);
        for (TaxonWithQueryStats taxonWithStats : taxaWithStats) {
            Taxon taxon = taxonWithStats.getTaxon();
            values = new ArrayList<String>();
            values.add(taxon.getName());
            values.add(taxon.getAuthority());
            if (taxon.getCommonName() != null && !taxon.getCommonName().isEmpty()) {
                values.add(taxon.getCommonName());
            } else {
                values.add("");
            }
            values.add(taxon.getPTaxonVersionKey());
            downloadHelper.writelnCsv(zip, values);
        }
    }
    
    private void addObservations(ZipOutputStream zip, User user, 
            int startYear, int endYear, List<String> datasetKeys, 
            List<String> taxa, String spatialRelationship, String featureID, 
            boolean sensitive, String designation, String taxonOutputGroup, 
            int orgSuppliedList, String gridRef, String polygon, 
            boolean includeAttributes, int filterID) throws IOException {
        
        List<Attribute> attributes = new ArrayList<Attribute>();
        List<TaxonObservationAttribute> observationAttributes = new ArrayList<TaxonObservationAttribute>();
        Map<Integer, Map<Integer, String>> atts = new HashMap<Integer, Map<Integer, String>>();
        
        // Get observations for download
        List<TaxonObservationDownload> observations = 
                observationMapper.selectDownloadableRecords(user, startYear, 
                endYear, datasetKeys, taxa, spatialRelationship, featureID, 
                sensitive, designation, taxonOutputGroup, orgSuppliedList,
                gridRef, polygon);
        
        // Push in standard header fields for download
        zip.putNextEntry(new ZipEntry("Observations.csv"));
        ArrayList<String> values = new ArrayList<String>();
        values.add("observationID");
        values.add("recordKey");
        values.add("organisationName");
        values.add("datasetKey");
        values.add("surveyKey");
        values.add("sampleKey");
        values.add("gridReference");
        values.add("precision");
        values.add("siteKey");
        values.add("siteName");
        values.add("featureKey");
        values.add("startDate");
        values.add("endDate");
        values.add("dateType");
        values.add("recorder");
        values.add("determiner");
        values.add("pTaxonVersionKey");
        values.add("taxonName");
        values.add("authority");
        values.add("commonName");
        values.add("taxonGroup");
        values.add("sensitive");
        values.add("zeroAbundance");
        values.add("fullVersion");
        values.add("useConstraints");
        
        // If including attributes then push in the appropriate fields
        if (includeAttributes) {
            // Find attributes
            attributes = taxonObservationAttributeMapper.getAttributeListForObservations(
                    user, startYear, endYear, datasetKeys, taxa, 
                    spatialRelationship, featureID, sensitive, designation, 
                    taxonOutputGroup, orgSuppliedList, gridRef, polygon);
            
            for (Attribute attrib : attributes) {
                values.add(attrib.getLabel());
            }
            
            // Grab all attributes that match full version records in the list
            observationAttributes = taxonObservationAttributeMapper.getAttributesForObservations(
                    user, startYear, endYear, datasetKeys, taxa, spatialRelationship,
                    featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon);

            // Put attributes into a map structure for retrieval later
            // Map --> obsID --> Map --> attID --> attVal
            for (TaxonObservationAttribute att : observationAttributes) {
                Map<Integer, String> temp = atts.get(att.getObservationID());

                if (temp == null) {
                    temp = new HashMap<Integer, String>();
                    atts.put(att.getObservationID(), temp);
                }

                temp.put(att.getAttributeID(), att.getTextValue());
            }
        }
        
        // Write headers out
        downloadHelper.writelnCsv(zip, values);
        
        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
        HashMap<String, Integer> datasetRecordCounts = new HashMap<String, Integer>();

        for(TaxonObservationDownload observation : observations) {
            values = new ArrayList<String>();
            values.add(Integer.toString(observation.getObservationID()));
            values.add(observation.getObservationKey());
            values.add(observation.getOrganisationName());
            values.add(observation.getDatasetKey());
            values.add(StringUtils.hasText(observation.getSurveyKey()) ? observation.getSurveyKey() : "");
            values.add(StringUtils.hasText(observation.getSampleKey()) ? observation.getSampleKey() : "");
            values.add(StringUtils.hasText(observation.getGridReference()) ? observation.getGridReference() : "");
            values.add(StringUtils.hasText(observation.getPrecision()) ? observation.getPrecision() : "");
            values.add(StringUtils.hasText(observation.getSiteKey()) ? observation.getSiteKey() : "");
            values.add(StringUtils.hasText(observation.getSiteName()) ? observation.getSiteName() : "");
            values.add(StringUtils.hasText(observation.getFeatureKey()) ? observation.getFeatureKey() : "");
            values.add(observation.getStartDate() != null ? df.format(observation.getStartDate()) : "");
            values.add(observation.getEndDate() != null ? df.format(observation.getEndDate()) : "");
            values.add(observation.getDateType());
            values.add(StringUtils.hasText(observation.getRecorder()) ? observation.getRecorder() : "");
            values.add(StringUtils.hasText(observation.getDeterminer()) ? observation.getDeterminer() : "");
            values.add(observation.getpTaxonVersionKey());
            values.add(observation.getpTaxonName());
            values.add(observation.getAuthority());
            values.add(StringUtils.hasText(observation.getCommonName()) ? observation.getCommonName() : "");
            values.add(observation.getTaxonGroup());
            values.add(observation.isSensitive() ? "1" : "0");
            values.add(observation.isZeroAbundance() ? "1" : "0");
            values.add(observation.isFullVersion()? "1" : "0");
            values.add(StringUtils.hasText(observation.getUseConstraints()) ? observation.getUseConstraints() : "");

            if (includeAttributes) {
                if (observation.isFullVersion()) {
                    for (Attribute att : attributes) {
                        if (atts.get(observation.getObservationID()).containsKey(att.getAttributeID())) {
                            values.add(atts.get(observation.getObservationID()).get(att.getAttributeID()));
                        } else {
                            values.add("");
                        }
                    }
                } else {
                    for (int i = 0; i < observationAttributes.size(); i++) {
                        values.add("");
                    }
                }
            }
            
            downloadHelper.writelnCsv(zip, values);
            
            if (datasetRecordCounts.containsKey(observation.getDatasetKey()))
                datasetRecordCounts.put(observation.getDatasetKey(), datasetRecordCounts.get(observation.getDatasetKey()) + 1);
            else 
                datasetRecordCounts.put(observation.getDatasetKey(), 1);
        }
        
        for (String key : datasetRecordCounts.keySet()) {
            oTaxonObservationFilterMapper.createDatasetDownloadStats(filterID, key, datasetRecordCounts.get(key));
        }
    }

    /**
     * 
     * @param zip
     * @param user
     * @param startYear
     * @param endYear
     * @param datasetKeys
     * @param taxa
     * @param spatialRelationship
     * @param featureID
     * @param sensitive
     * @param designation
     * @param taxonOutputGroup
     * @param gridRef
     * @throws IOException 
     */
    private void addDatasetMetadata(ZipOutputStream zip, User user, 
            int startYear, int endYear, List<String> datasetKeys, 
            List<String> taxa, String spatialRelationship, String featureID, 
            boolean sensitive, String designation, String taxonOutputGroup, 
            int orgSuppliedList, String gridRef, String polygon) 
            throws IOException {
        List<TaxonDatasetWithQueryStats> datasetsWithQueryStats = observationMapper.selectObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon);
        downloadHelper.addDatasetWithQueryStatsMetadata(zip, user.getId(), datasetsWithQueryStats);
    }
    
    /**
     * 
     * @param zip
     * @param title
     * @param user
     * @param startYear
     * @param endYear
     * @param datasetKeys
     * @param spatialRelationship
     * @param featureID
     * @param sensitive
     * @param designation
     * @param taxonOutputGroupKey
     * @throws IOException 
     */
    private void addReadMe(ZipOutputStream zip, String title, User user, 
            int startYear, int endYear, List<String> datasetKeys, 
            String spatialRelationship, String featureID, boolean sensitive, 
            String designation, String taxonOutputGroupKey, int orgSuppliedList) 
            throws IOException{
        
        HashMap<String, String> filters = new HashMap<String, String>();
        if(featureID != null && !featureID.equals(ObservationResourceDefaults.defaultFeatureID)){
            Feature feature = featureMapper.getFeature(featureID);
            if(feature.getType().equals("GridSquare")){
                filters.put("Grid reference:", feature.getLabel());
            }else if(feature.getType().equals("SiteBoundary")){
                filters.put("Site name", feature.getLabel());
                filters.put("Site key", feature.getIdentifier());
            }
        }
        if(!(new Integer(startYear).toString().equals(ObservationResourceDefaults.defaultStartYear))){
            filters.put("Start year", new Integer(startYear).toString());
        }
        if(!(new Integer(endYear).toString().equals(ObservationResourceDefaults.defaultEndYear))){
            filters.put("End year", new Integer(endYear).toString());
        }
        filters.put("Spatial relationship", spatialRelationship);
        filters.put("Include only sensitive records", Boolean.toString(sensitive));
        if(designation != null && !designation.equals(ObservationResourceDefaults.defaultDesignation)){
            Designation desig = designationMapper.selectByID(designation);
            filters.put("Designation key", designation);
            filters.put("Designation name", desig.getName());
        }
        if(datasetKeys != null && datasetKeys.size() > 0 && !((String)datasetKeys.get(0)).equals("")) {
            filters.put("Datset keys", ProviderHelper.datasetListToCommaList(datasetKeys));
        }
        if(taxonOutputGroupKey != null && !taxonOutputGroupKey.equals(ObservationResourceDefaults.defaultTaxonOutputGroup)){
            TaxonOutputGroup taxonOutputGroup = taxonOutputGroupMapper.getById(taxonOutputGroupKey);
            filters.put("Taxon group", taxonOutputGroup.getName());
            filters.put("Taxon group key", taxonOutputGroupKey);
        }
        if (orgSuppliedList > 0) {
            OrganisationSuppliedList organisationSuppliedList = organisationSuppliedListMapper.selectByID(orgSuppliedList);
            filters.put("Organisation Supplied List Code", organisationSuppliedList.getCode());
            filters.put("Organisation Supplied List", organisationSuppliedList.getName());
        }
        
        downloadHelper.addReadMe(zip, user, title, filters);
    }
    
    /**
     * Attempt to parse a DownloadFilterJSON object out from an input JSON
     * string
     * 
     * @param json The string containing the download filter
     * @return An object representation of the download filter
     * @throws IOException If the JSON string does not match the input object
     * throw and error
     */
    private DownloadFilterJSON parseJSON(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, DownloadFilterJSON.class);
    }
}



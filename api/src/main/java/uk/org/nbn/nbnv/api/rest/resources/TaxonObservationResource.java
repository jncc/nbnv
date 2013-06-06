package uk.org.nbn.nbnv.api.rest.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.providers.ProviderHelper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DesignationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.FeatureMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonObservationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonOutputGroupMapper;
import uk.org.nbn.nbnv.api.model.*;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.rest.resources.utils.DownloadHelper;

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
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef) {
        //TODO: squareBlurring(?)
        return observationMapper.selectObservationRecordsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
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
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef) {
        //TODO: squareBlurring(?)
        return observationMapper.selectObservationAttributeByFilter(user, datasetKey, attributeID, startYear, endYear, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
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
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef) {
        //TODO: squareBlurring(?)
        List<TaxonWithQueryStats> toReturn = observationMapper.selectObservationSpeciesByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
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
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) final String taxonOutputGroup,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) final String gridRef) {
        //TODO: squareBlurring(?)
        return new StreamingOutput() {
            public void write(OutputStream out) throws IOException, WebApplicationException {
                ZipOutputStream zip = new ZipOutputStream(out);
                String title = "Species list download";
                addSpecies(zip, user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
                addReadMe(zip, title, user, startYear, endYear, datasetKeys, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup);
                addDatasetMetadata(zip, user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
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
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef) {
        //TODO: squareBlurring(?)
        return observationMapper.selectObservationGroupsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
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
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef) {
        //TODO: squareBlurring(?)
        return observationMapper.selectObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
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
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef) {
        //TODO: squareBlurring(?)
        return observationMapper.selectAllObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
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
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef) {
        //TODO: squareBlurring(?)
        
        // Stop leaking dataset info when spatial filter applied. Fixes issue species info fishing
        if (sensitive && !featureID.equals(ObservationResourceDefaults.defaultFeatureID)) {
            sensitive = Boolean.FALSE;
        }
        
        return observationMapper.selectRequestableObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
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
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef) {
        //TODO: squareBlurring(?)
                
        List<String> datasetKeys = new ArrayList<String>();
        datasetKeys.add(datasetKey);
        return observationMapper.selectRequestableObservationDatasetsByFilter(User.PUBLIC_USER, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
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
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef) {
        //TODO: squareBlurring(?)
        return observationMapper.selectUnavailableDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
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
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef) {
        //TODO: squareBlurring(?)
        List<TaxonObservation> taxonObservationsOrderedByDataset = observationMapper.selectObservationsByFilterOrderedByDataset(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
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
            @QueryParam("returnAccessPositions") @DefaultValue("") String getPerm) {
        //TODO: squareBlurring(?)
        List<TaxonDatasetWithQueryStats> datasetsWithQueryStats = observationMapper.selectObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);

        if (getPerm.equalsIgnoreCase("true")) {
            for (TaxonDatasetWithQueryStats d : datasetsWithQueryStats) {
                d.setAccessPositions(datasetMapper.getDatasetAccessPositions(d.getDatasetKey(), user.getId()));
            }
        }
        return groupDatasetsByProvider(datasetsWithQueryStats);
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
    private void addSpecies(ZipOutputStream zip, User user, int startYear, int endYear, List<String> datasetKeys, List<String> taxa, String spatialRelationship, String featureID, boolean sensitive, String designation, String taxonOutputGroup, String gridRef) throws IOException {
        List<TaxonWithQueryStats> taxaWithStats = observationMapper.selectObservationSpeciesByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
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
    private void addDatasetMetadata(ZipOutputStream zip, User user, int startYear, int endYear, List<String> datasetKeys, List<String> taxa, String spatialRelationship, String featureID, boolean sensitive, String designation, String taxonOutputGroup, String gridRef) throws IOException {
        List<TaxonDatasetWithQueryStats> datasetsWithQueryStats = observationMapper.selectObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef);
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
    private void addReadMe(ZipOutputStream zip, String title, User user, int startYear, int endYear, List<String> datasetKeys, String spatialRelationship, String featureID, boolean sensitive, String designation, String taxonOutputGroupKey) throws IOException{
        
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
        downloadHelper.addReadMe(zip, user, title, filters);
    }
}

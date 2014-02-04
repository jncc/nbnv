package uk.org.nbn.nbnv.api.rest.resources;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.org.nbn.nbnv.api.dao.core.OperationalApiObservationViewMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper;
import uk.org.nbn.nbnv.api.dao.providers.ProviderHelper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DesignationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DownloadMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.FeatureMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationSuppliedListMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.PolygonUtilsMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonObservationAttributeMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonObservationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonOutputGroupMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.UserMapper;
import uk.org.nbn.nbnv.api.mail.TemplateMailer;
import uk.org.nbn.nbnv.api.model.*;
import uk.org.nbn.nbnv.api.model.meta.DatasetRecordCount;
import uk.org.nbn.nbnv.api.model.meta.DownloadFilterJSON;
import uk.org.nbn.nbnv.api.model.meta.DownloadStatsJSON;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.rest.resources.utils.DownloadHelper;
import uk.org.nbn.nbnv.api.utils.DownloadUtils;
import uk.org.nbn.nbnv.api.utils.FilterToText;

@Component
@Path("/taxonObservations")
public class TaxonObservationResource extends RequestResource {

    @Autowired TaxonObservationMapper observationMapper;
    @Autowired OrganisationMapper organisationMapper;
    @Autowired OrganisationMembershipMapper organisationMembershipMapper;
    @Autowired DatasetMapper datasetMapper;
    @Autowired DatasetAdministratorMapper datasetAdministratorMapper;
    @Autowired FeatureMapper featureMapper;
    @Autowired TaxonOutputGroupMapper taxonOutputGroupMapper;
    @Autowired DownloadHelper downloadHelper;
    @Autowired DesignationMapper designationMapper;
    @Autowired SiteBoundaryMapper siteBoundaryMapper;
    @Autowired TaxonObservationAttributeMapper taxonObservationAttributeMapper;
    @Autowired OperationalTaxonObservationFilterMapper oTaxonObservationFilterMapper;
    @Autowired OrganisationSuppliedListMapper organisationSuppliedListMapper;
    @Autowired DownloadUtils downloadUtils;
    @Autowired TemplateMailer templateMailer;
    @Autowired DownloadMapper downloadMapper;
    @Autowired UserMapper userMapper;
    @Autowired PolygonUtilsMapper polygonUtilsMapper;
    @Autowired OperationalApiObservationViewMapper oApiObservationViewMapper;
    @Autowired FilterToText filterToText;
    
    private Logger logger = LoggerFactory.getLogger(TaxonObservationResource.class);

    /**
     * Return a Taxon Observation Record with a specified numerical ID, as long
     * as the user is authorised to view this record
     * 
     * @param user The current User
     * @param request The incoming HTTP request (Auto-injected no need to pass)
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
    public TaxonObservation getObservation(
            @TokenUser(allowPublic = false) User user, 
            @Context HttpServletRequest request, 
            @PathParam("id") int id) {
        TaxonObservation obs = observationMapper.selectById(id, user.getId());
        if (obs != null) {
            ApiObservationView view = new ApiObservationView(user.getId(), request.getRemoteAddr(), "Single Record with the ID " + id, 1);
            oApiObservationViewMapper.addAPIObservationView(view);
            oApiObservationViewMapper.addAPIObservationViewStats(view.getId(), obs.getDatasetKey(), 1);
        }
        return obs;
    }

    /**
     * Returns a List of Taxon Observation Records in a specified dataset as 
     * long as the user is authorised to view them
     * 
     * @param user The current user
     * @param request The incoming HTTP request (Auto-injected no need to pass)
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
    public List<TaxonObservation> getObservationsByDataset(
            @TokenUser(allowPublic = false) User user, 
            @Context HttpServletRequest request,
            @PathParam("datasetKey") String datasetKey) {
        List<String> datasetKeys = new ArrayList<String>();
        datasetKeys.add(datasetKey);

        writeAPIViewRecordToDatabase(user, request.getRemoteAddr(),
                Integer.parseInt(ObservationResourceDefaults.defaultStartYear), 
                Integer.parseInt(ObservationResourceDefaults.defaultEndYear), 
                datasetKeys, 
                new ArrayList<String>(), 
                ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT, 
                ObservationResourceDefaults.defaultFeatureID, 
                Boolean.parseBoolean(ObservationResourceDefaults.defaultSensitive), 
                ObservationResourceDefaults.defaultDesignation,
                ObservationResourceDefaults.defaultTaxonOutputGroup,
                Integer.parseInt(ObservationResourceDefaults.defaultOrgSuppliedList),
                ObservationResourceDefaults.defaultGridRef, 
                ObservationResourceDefaults.defaultPolygon, 
                false);        
                
        return observationMapper.selectByDataset(datasetKey, user.getId());
    }

    /**
     * Returns a list of Taxon Observations about a specific Taxon Version Key 
     * that the current user has access to
     * 
     * @param user The current user
     * @param request The incoming HTTP request (Auto-injected no need to pass)
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
    public List<TaxonObservation> getObservationsByTaxon(
            @TokenUser(allowPublic = false) User user, 
            @Context HttpServletRequest request, 
            @PathParam("ptvk") String ptvk) {
        List<String> ptvks = new ArrayList<String>();
        ptvks.add(ptvk);
        
        writeAPIViewRecordToDatabase(user, request.getRemoteAddr(),
                Integer.parseInt(ObservationResourceDefaults.defaultStartYear), 
                Integer.parseInt(ObservationResourceDefaults.defaultEndYear), 
                new ArrayList<String>(), 
                ptvks, 
                ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT, 
                ObservationResourceDefaults.defaultFeatureID, 
                Boolean.parseBoolean(ObservationResourceDefaults.defaultSensitive), 
                ObservationResourceDefaults.defaultDesignation,
                ObservationResourceDefaults.defaultTaxonOutputGroup,
                Integer.parseInt(ObservationResourceDefaults.defaultOrgSuppliedList),
                ObservationResourceDefaults.defaultGridRef, 
                ObservationResourceDefaults.defaultPolygon, 
                false);
        
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
                .put("hasGridAbsence", observationMapper.pTVKHasGridAbsence(taxonVersionKey, user.getId(), true) != null)
                .put("hasGridPresence", observationMapper.pTVKHasGridAbsence(taxonVersionKey, user.getId(), false) != null)
                .put("hasPolygonAbsence", observationMapper.pTVKHasPolygonAbsence(taxonVersionKey, user.getId(), true) != null)
                .put("hasPolygonPresence", observationMapper.pTVKHasPolygonAbsence(taxonVersionKey, user.getId(), false) != null);
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
     * @param request The incoming HTTP request (Auto-injected no need to pass)
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
     * @param absence Whether the results should be limited to just absence records (true) 
     * or just presence records (false).  If this parameter is missing then both absence 
     * and presence records are returned.  Valid values are 'true' and 'false'
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
            @TokenUser(allowPublic = false) User user,
            @Context HttpServletRequest request,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) int startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) int endYear,
            @QueryParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) List<String> datasetKeys,
            @QueryParam("ptvk") @DefaultValue(ObservationResourceDefaults.defaultTaxa) List<String> taxa,
            @QueryParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) String spatialRelationship,
            @QueryParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) String featureID,
            @QueryParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) Boolean sensitive,
            @QueryParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) String designation,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) String taxonOutputGroup,
            @QueryParam("orgSuppliedList") @DefaultValue(ObservationResourceDefaults.defaultOrgSuppliedList) int orgSuppliedList,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon,
            @QueryParam("absence") @DefaultValue(ObservationResourceDefaults.defaultAbsence) Boolean absence) throws IllegalArgumentException {        
        return retreiveObservationsRecordsByFilter(user, request.getRemoteAddr(), startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, orgSuppliedList, gridRef, polygon, absence);
    }
    
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
     * @param absence Whether the results should be limited to just absence records (true) 
     * or just presence records (false).  If this parameter is missing then both absence 
     * and presence records are returned.  Valid values are 'true' and 'false'
     * 
     * @return A list of Taxon Observations conforming to the provided search
     * parameters
     * 
     * @response.representation.200.qname List<TaxonObservation>
     * @response.representation.200.mediaType application/json
     */    
    @POST 
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonObservation> getObservationsByFilterViaPOST(
            @TokenUser(allowPublic = false) User user,
            @Context HttpServletRequest request,
            @FormParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) int startYear,
            @FormParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) int endYear,
            @FormParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) List<String> datasetKeys,
            @FormParam("ptvk") @DefaultValue(ObservationResourceDefaults.defaultTaxa) List<String> taxa,
            @FormParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) String spatialRelationship,
            @FormParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) String featureID,
            @FormParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) Boolean sensitive,
            @FormParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) String designation,
            @FormParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) String taxonOutputGroup,
            @FormParam("orgSuppliedList") @DefaultValue(ObservationResourceDefaults.defaultOrgSuppliedList) int orgSuppliedList,
            @FormParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @FormParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon,
            @FormParam("absence") @DefaultValue(ObservationResourceDefaults.defaultAbsence) Boolean absence) throws IllegalArgumentException {
        return retreiveObservationsRecordsByFilter(user, request.getRemoteAddr(), startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, orgSuppliedList, gridRef, polygon, absence);
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
            @TokenUser(allowPublic = false) User user,
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
            @QueryParam("orgSuppliedList") @DefaultValue(ObservationResourceDefaults.defaultOrgSuppliedList) int orgSuppliedList,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon) {
        //TODO: squareBlurring(?)
        
//        // Check size of polygon
//        if (StringUtils.hasText(polygon)) {
//            checkPolygonMaxSize(polygon, taxa, designation, taxonOutputGroup, orgSuppliedList, datasetKeys);
//        }
        
        List<TaxonWithQueryStats> toReturn = observationMapper.selectObservationSpeciesByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, orgSuppliedList, gridRef, polygon, false);
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
            @QueryParam("orgSuppliedList") @DefaultValue(ObservationResourceDefaults.defaultOrgSuppliedList) final int orgSuppliedList,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) final String taxonOutputGroup,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) final String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) final String polygon) {
        //TODO: squareBlurring(?)
        return new StreamingOutput() {
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
                ZipOutputStream zip = new ZipOutputStream(out);
                String title = "Species list download";
                addSpecies(zip, user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, orgSuppliedList, gridRef, polygon);
                addReadMe(zip, title, user, startYear, endYear, datasetKeys, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, orgSuppliedList);
                addDatasetMetadata(zip, user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, orgSuppliedList, gridRef, polygon);
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
        return observationMapper.selectObservationGroupsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon, false);
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
            @QueryParam("orgSuppliedList") @DefaultValue(ObservationResourceDefaults.defaultOrgSuppliedList) int orgSuppliedList,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon) {
        //TODO: squareBlurring(?)
        return observationMapper.selectObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon, orgSuppliedList);
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
        if (sensitive && (!featureID.equals(ObservationResourceDefaults.defaultFeatureID) || !gridRef.equals(ObservationResourceDefaults.defaultGridRef))) {
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
            @Context HttpServletRequest request,
            @QueryParam("startYear") @DefaultValue(ObservationResourceDefaults.defaultStartYear) int startYear,
            @QueryParam("endYear") @DefaultValue(ObservationResourceDefaults.defaultEndYear) int endYear,
            @QueryParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) List<String> datasetKeys,
            @QueryParam("ptvk") @DefaultValue(ObservationResourceDefaults.defaultTaxa) List<String> taxa,
            @QueryParam("spatialRelationship") @DefaultValue(ObservationResourceDefaults.SPATIAL_RELATIONSHIP_DEFAULT) String spatialRelationship,
            @QueryParam("featureID") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) String featureID,
            @QueryParam("sensitive") @DefaultValue(ObservationResourceDefaults.defaultSensitive) Boolean sensitive,
            @QueryParam("designation") @DefaultValue(ObservationResourceDefaults.defaultDesignation) String designation,
            @QueryParam("taxonOutputGroup") @DefaultValue(ObservationResourceDefaults.defaultTaxonOutputGroup) String taxonOutputGroup,
            @QueryParam("orgSuppliedList") @DefaultValue(ObservationResourceDefaults.defaultOrgSuppliedList) int orgSuppliedList,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon) {
        //TODO: squareBlurring(?)
        List<TaxonObservation> taxonObservationsOrderedByDataset = observationMapper.selectObservationsByFilterOrderedByDataset(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, orgSuppliedList, gridRef, polygon);
        writeAPIViewRecordToDatabase(user, request.getRemoteAddr(), startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, orgSuppliedList, gridRef, polygon, false);
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
            @QueryParam("orgSuppliedList") @DefaultValue(ObservationResourceDefaults.defaultOrgSuppliedList) int orgSuppliedList,
            @QueryParam("gridRef") @DefaultValue(ObservationResourceDefaults.defaultGridRef) String gridRef,
            @QueryParam("polygon") @DefaultValue(ObservationResourceDefaults.defaultPolygon) String polygon,
            @QueryParam("returnAccessPositions") @DefaultValue("") String getPerm) {
        //TODO: squareBlurring(?)
        List<TaxonDatasetWithQueryStats> datasetsWithQueryStats = observationMapper.selectObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon, orgSuppliedList);

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
            @QueryParam("json") String json,
            @Context HttpServletResponse response) throws IOException, TemplateException {    
        
        logger.info("Taxon Observation Download Started");
        
        // Allows the fileDownload javascript to close the waiting window when
        // the download is finished, required cookie, but doesn't stick around
        // long
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        
        // Fix to prevent someone injecting somone elses user id into 
        // the download request, doesn't give them that persons access but it
        // would report the download as being done by that user
        DownloadFilterJSON rawFilter = parseJSON(json);        
        rawFilter.getReason().setUserID(user.getId());
        ObjectWriter ow = new ObjectMapper().writer();
        
        final DownloadFilterJSON dFilter = rawFilter;
        
        // Check the filter for validity (Basic)
        checkJSONFilterForValidity(dFilter);
        
        final TaxonObservationFilter filter = downloadUtils.createFilter(ow.writeValueAsString(dFilter), dFilter);
        
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
        
        //mailDatasetDownloadNotifications(filter, dFilter, user);
        
        final int filterID = filter.getId();
        return new StreamingOutput() {
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
                ZipOutputStream zip = new ZipOutputStream(out);
                String title = "Taxon Observation Download";
                
                List<String> taxaList = new ArrayList<String>();
                taxaList.add(dFilter.getTaxon().getTvk());
                // Add the list of observations to the download
                try {
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
                            filterID, filter, dFilter);
                } catch (TemplateException ex) {
                    throw new IOException(ex);
                }
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
    
    /**
     * Returns a list of download reports for the specified dataset
     * 
     * @param user The current user must be an admin of the dataset (Injected token no need to pass)
     * @param datasetKey A dataset key
     * @param json A JSON object containing a set of filters to run the query on
     * @return
     * @throws IOException 
     */
    @GET
    @Path("/download/report/{datasetKey : [A-Z][A-Z0-9]{7}}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DownloadReport> getDownloadReportsByDataset (
            @TokenDatasetAdminUser(path = "datasetKey") User user, 
            @PathParam("datasetKey") String datasetKey,
            @QueryParam("json") String json) throws IOException {
        DownloadStatsJSON stats = parseJSONStats(json);
        
        List<String> dList = new ArrayList<String>();
        dList.add(datasetKey);
        
        return observationMapper.selectDownloadReportsByDataset(dList, 
                stats.getStartDate(), stats.getEndDate(), stats.getFilterID(), 
                stats.getUserID(), stats.getOrganisationID(), 
                stats.getPurposeID());
    }
    
    /**
     * Returns a list of download reports for the specified dataset as a CSV
     * 
     * @param user The current user must be an admin of the dataset (Injected token no need to pass)
     * @param datasetKey A dataset key
     * @param json A JSON object containing a set of filters to run the query on
     * @return
     * @throws IOException 
     */
    @GET
    @Path("/download/report/{datasetKey : [A-Z][A-Z0-9]{7}}/csv")
    @Produces("application/x-zip-compressed")
    public StreamingOutput getDownloadReportsByDatasetAsCSV (
            @TokenDatasetAdminUser(path = "datasetKey") final User user, 
            @PathParam("datasetKey") final String datasetKey,
            @QueryParam("json") final String json) throws IOException {

        return new StreamingOutput() {
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
                DownloadStatsJSON stats = parseJSONStats(json);

                List<String> dList = new ArrayList<String>();
                dList.add(datasetKey);

                List<DownloadReport> reports = observationMapper.selectDownloadReportsByDataset(dList, 
                        stats.getStartDate(), stats.getEndDate(), stats.getFilterID(), 
                        stats.getUserID(), stats.getOrganisationID(), 
                        stats.getPurposeID());
                
                ZipOutputStream zip = new ZipOutputStream(out);

                zip.putNextEntry(new ZipEntry("DownloadReportsFor" + datasetKey + ".csv"));
                
                List<String> values = new ArrayList<String>();              
                values.add("filterID");
                values.add("datasetKey");
                values.add("forename");
                values.add("surname");
                values.add("email");
                values.add("organisationName");                
                values.add("purpose");
                values.add("reason");
                values.add("download");
                values.add("downloadTime");
                values.add("totalDownloadedInThisDataset");
                values.add("totalRecordsInDataset");
                values.add("totalDownloadedInThisDownload");
                
                downloadHelper.writelnCsv(zip, values);
                
                for (DownloadReport report : reports) {
                    values = new ArrayList<String>();
                    
                    values.add(report.getFilterID());
                    values.add(report.getDatasetKey());
                    values.add(report.getForename());
                    values.add(report.getSurname());
                    values.add(report.getEmail());
                    values.add(StringUtils.hasText(report.getOrganisationName()) ? report.getOrganisationName() : "");
                    values.add(report.getPurpose());
                    values.add(report.getReason());
                    values.add(report.getFilterText());
                    values.add(report.getDownloadTimeString());
                    values.add(Integer.toString(report.getRecordCount()));
                    values.add(Integer.toString(report.getTotalRecords()));
                    values.add(Integer.toString(report.getTotalDownloaded()));
                    
                    downloadHelper.writelnCsv(zip, values);
                }
                
                zip.flush();
                zip.close();                
            }
        };
    }    
    
    /**
     * 
     * @param user
     * @param json
     * @return
     * @throws IOException 
     */
    @GET
    @Path("/download/report/downloadStats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDownloadStats(
        @TokenUser(allowPublic = false) User user, 
        @QueryParam("json") String json) throws IOException {
        DownloadStatsJSON stats = parseJSONStats(json);
        
        if (userIsDatasetAdminForDatsets(user, stats.getDataset().getDatasets())) {
            return Response.status(Response.Status.OK).entity(
                    observationMapper.selectDownloadStats(
                    stats.getDataset().getDatasets(),
                    stats.getStartDate(), stats.getEndDate(),
                    stats.getFilterID(), stats.getUserID(),
                    stats.getOrganisationID(), stats.getPurposeID())).build();
        }

        return Response.status(Response.Status.FORBIDDEN).entity("You do no have administration access to one or more of the selected datasets").build();
    }

    /**
     * 
     * @param user
     * @param json
     * @return
     * @throws IOException 
     */
    @GET
    @Path("/download/report/downloadUserStats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDownloadUserStats(
            @TokenUser(allowPublic = false) User user,
            @QueryParam("json") String json) throws IOException {

        DownloadStatsJSON stats = parseJSONStats(json);
        if (userIsDatasetAdminForDatsets(user, stats.getDataset().getDatasets())) {
            return Response.status(Response.Status.OK).entity(
                    observationMapper.selectUserDownloadStats(
                    stats.getDataset().getDatasets(),
                    stats.getStartDate(), stats.getEndDate(),
                    stats.getFilterID(), stats.getUserID(),
                    stats.getOrganisationID(), stats.getPurposeID())).build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }

    /**
     * 
     * @param user
     * @param json
     * @return
     * @throws IOException 
     */
    @GET
    @Path("/download/report/downloadOrganisationStats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDownloadOrganisationStats(
            @TokenUser(allowPublic = false) User user,
            @QueryParam("json") String json) throws IOException {

        DownloadStatsJSON stats = parseJSONStats(json);

        if (userIsDatasetAdminForDatsets(user, stats.getDataset().getDatasets())) {
            return Response.status(Response.Status.OK).entity(
                    observationMapper.selectOrganisationDownloadStats(
                    stats.getDataset().getDatasets(),
                    stats.getStartDate(), stats.getEndDate(),
                    stats.getFilterID(), stats.getUserID(),
                    stats.getOrganisationID(), stats.getPurposeID())).build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }
    
    /**
     * Returns a set of combined statistics about requested datasets specified
     * in the json parameter along with other filters, these include top 5 
     * downloaders (organisation and user) and other statistics that help create
     * the download reports pages
     * 
     * @param user The current user (must be admin of all datasets specified in the JSON) (Injected token no need to pass)
     * @param json The JSON object determining the datasets and filters used
     * @return A list of combined stats (general statistics, user stats and organisation stats)
     * @throws IOException 
     */
    @GET
    @Path("/download/report/combinedStats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCombinedDownloadStats(
            @TokenUser(allowPublic = false) User user,
            @QueryParam("json") String json) throws IOException {

        DownloadStatsJSON stats = parseJSONStats(json);

        if (userIsDatasetAdminForDatsets(user, stats.getDataset().getDatasets())) {
            List<List<DownloadStat>> output = new ArrayList();
            output.add(observationMapper.selectDownloadStats(
                    stats.getDataset().getDatasets(),
                    stats.getStartDate(), stats.getEndDate(),
                    stats.getFilterID(), stats.getUserID(),
                    stats.getOrganisationID(), stats.getPurposeID()));
            output.add(observationMapper.selectUserDownloadStats(
                    stats.getDataset().getDatasets(),
                    stats.getStartDate(), stats.getEndDate(),
                    stats.getFilterID(), stats.getUserID(),
                    stats.getOrganisationID(), stats.getPurposeID()));
            output.add(observationMapper.selectOrganisationDownloadStats(
                    stats.getDataset().getDatasets(),
                    stats.getStartDate(), stats.getEndDate(),
                    stats.getFilterID(), stats.getUserID(),
                    stats.getOrganisationID(), stats.getPurposeID()));
            
            return Response.status(Response.Status.OK).entity(output).build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }

    private boolean userIsDatasetAdminForDatsets(User user, List<String> datasetKeys) {
        if (datasetKeys != null && !datasetKeys.isEmpty() && user != null) {
            for (String dataset : datasetKeys) {
                if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), dataset)) {
                    // We are not a dataset admin of this dataset return false
                    return false;
                }
            }
            // User is a dataset administrator for all given datasets
            return true;
        }
        // No datasets supplied or user is null
        return false;
    }
    
//    private boolean userIsDatasetOrOrganisationAdminForDatasets(User user, List<String> datasetKeys) {
//        if (datasetKeys != null && !datasetKeys.isEmpty() && user != null) {
//            
//            for (String dataset : datasetKeys) {
//                if (datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), dataset)) {
//                    return true;
//                } else {
//                    Dataset d = datasetMapper.selectByDatasetKey(dataset);
//                    if (organisationMembershipMapper.isUserOrganisationAdmin(user.getId(), d.getOrganisationID())) {
//                        return true;
//                    }
//                }
//            }
//        }
//        
//        return false;
//    }
    
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
    private void addSpecies(ZipOutputStream zip, User user, int startYear, int endYear, List<String> datasetKeys, List<String> taxa, String spatialRelationship, String featureID, boolean sensitive, String designation, String taxonOutputGroup, int orgSuppliedList, String gridRef, String polygon) throws IOException {
        List<TaxonWithQueryStats> taxaWithStats = observationMapper.selectObservationSpeciesByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, orgSuppliedList, gridRef, polygon, false);
        zip.putNextEntry(new ZipEntry("TaxonList.csv"));
        ArrayList<String> values = new ArrayList<String>();
        values.add("TaxonName");
        values.add("Authority");
        values.add("CommonName");
        values.add("PreferredTaxonVersionKey");
        values.add("TaxonGroupKey");
        values.add("TaxonGroup");
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
            values.add(taxon.getTaxonOutputGroupKey());
            values.add(taxon.getTaxonOutputGroupName());
            downloadHelper.writelnCsv(zip, values);
        }
    }
    
    private void addObservations(ZipOutputStream zip, User user, 
            int startYear, int endYear, List<String> datasetKeys, 
            List<String> taxa, String spatialRelationship, String featureID, 
            boolean sensitive, String designation, String taxonOutputGroup, 
            int orgSuppliedList, String gridRef, String polygon, 
            boolean includeAttributes, int filterID, TaxonObservationFilter filter, DownloadFilterJSON dFilter) throws IOException, TemplateException {
        
        
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
        
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
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
            values.add(observation.isSensitive() ? "true" : "false");
            values.add(observation.isZeroAbundance() ? "true" : "false");
            values.add(observation.isFullVersion()? "true" : "false");
            values.add(StringUtils.hasText(observation.getUseConstraints()) ? observation.getUseConstraints() : "");

            if (includeAttributes) {
                if (observation.isFullVersion() || observation.isPublicAttribute()) {
                    for (Attribute att : attributes) {
                        if (atts.containsKey(observation.getObservationID()) 
                                && atts.get(observation.getObservationID()).containsKey(att.getAttributeID())) {
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
            logger.info("Taxon Observation Download Sending email for dataset " + key);
            mailDatasetDownloadNotification(filter, dFilter, key, user);
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
        List<TaxonDatasetWithQueryStats> datasetsWithQueryStats = observationMapper.selectObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon, orgSuppliedList);
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
    
    private DownloadStatsJSON parseJSONStats(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, DownloadStatsJSON.class);
    }
    
    private void mailDatasetDownloadNotification(TaxonObservationFilter filter, DownloadFilterJSON dFilter, String dataset, User user) throws IOException, TemplateException {
        Map<Integer, String> purposes = new HashMap<Integer, String>();
        purposes.put(1, "Personal interest");
        purposes.put(2, "Educational purposes");
        purposes.put(3, "Research and scientific analysis");
        purposes.put(4, "Media publication");
        purposes.put(5, "Conservation NGO work");
        purposes.put(6, "Professional land management");
        purposes.put(7, "Data provision and interpretation (commercial)");
        purposes.put(8, "Data provision and interpretation (non-profit)");
        purposes.put(9, "Statutory work");
        
        List<UserDownloadNotification> users = downloadMapper.getUsersToNotifyForDatasetDownload(dataset);
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("portal", properties.getProperty("portal_url"));

        message.put("downloader", user.getForename() + " " + user.getSurname() + "(" + user.getEmail() + ")");

        if (dFilter.getReason().getOrganisationID() > -1) {
            Organisation org = organisationMapper.selectByID(dFilter.getReason().getOrganisationID());
            message.put("dorg", org.getName());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        message.put("downloadTime", sdf.format(new Date()));

        message.put("purpose", purposes.get(dFilter.getReason().getPurpose()));
        message.put("downloadReason", dFilter.getReason().getDetails());
        message.put("filterText", filter.getFilterText());

        message.put("dataset", dataset);
        message.put("datasetName", datasetMapper.selectByDatasetKey(dataset).getTitle());

        for (UserDownloadNotification nuser : users) {                
            message.put("name", nuser.getForename());
            templateMailer.send(
                    "dataset_download_notification.ftl", 
                    nuser.getEmail(), 
                    "NBN Gateway: User downloaded records", 
                    message);
        }
    }
    
    private void mailDatasetDownloadNotifications(TaxonObservationFilter filter, DownloadFilterJSON dFilter, User user) throws IOException, TemplateException {
        List<String> datasets = null;
        
        if (dFilter.getDataset().isAll()) {
            List<String> species = null;
            datasets = new ArrayList<String>();
            if (dFilter.getTaxon().getTvk() != null && !dFilter.getTaxon().getTvk().isEmpty()) {
                species = new ArrayList<String>();
                species.add(dFilter.getTaxon().getTvk());
            }

            List<TaxonDatasetWithQueryStats> selectObservationDatasetsByFilter = observationMapper.selectObservationDatasetsByFilter(user, dFilter.getYear().getStartYear(), dFilter.getYear().getEndYear(), new ArrayList<String>(), species, dFilter.getSpatial().getMatch(), dFilter.getSpatial().getFeature(), (dFilter.getSensitive().equals("sans") ? true : false), dFilter.getTaxon().getDesignation(), dFilter.getTaxon().getOutput(), "", "", dFilter.getTaxon().getOrgSuppliedList());

            for (TaxonDatasetWithQueryStats tdwqs : selectObservationDatasetsByFilter) {
                datasets.add(tdwqs.getDatasetKey());
            }

        } else {
            datasets = dFilter.getDataset().getDatasets();
        }

        Map<Integer, String> purposes = new HashMap<Integer, String>();
        purposes.put(1, "Personal interest");
        purposes.put(2, "Educational purposes");
        purposes.put(3, "Research and scientific analysis");
        purposes.put(4, "Media publication");
        purposes.put(5, "Conservation NGO work");
        purposes.put(6, "Professional land management");
        purposes.put(7, "Data provision and interpretation (commercial)");
        purposes.put(8, "Data provision and interpretation (non-profit)");
        purposes.put(9, "Statutory work");
        
        for (String dataset : datasets) {
            List<UserDownloadNotification> users = downloadMapper.getUsersToNotifyForDatasetDownload(dataset);
            Map<String, Object> message = new HashMap<String, Object>();
            message.put("portal", properties.getProperty("portal_url"));
            
            message.put("downloader", user.getForename() + " " + user.getSurname() + "(" + user.getEmail() + ")");
            
            if (dFilter.getReason().getOrganisationID() > -1) {
                Organisation org = organisationMapper.selectByID(dFilter.getReason().getOrganisationID());
                message.put("dorg", org.getName());
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            message.put("downloadTime", sdf.format(new Date()));
            
            message.put("purpose", purposes.get(dFilter.getReason().getPurpose()));
            message.put("downloadReason", dFilter.getReason().getDetails());
            message.put("filterText", filter.getFilterText());
            
            message.put("dataset", dataset);
            message.put("datasetName", datasetMapper.selectByDatasetKey(dataset).getTitle());
            
            for (UserDownloadNotification nuser : users) {                
                message.put("name", nuser.getForename());
                templateMailer.send(
                        "dataset_download_notification.ftl", 
                        nuser.getEmail(), 
                        "NBN Gateway: User downloaded records", 
                        message);
            }
        }
    }
    
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
     * @param absence Whether the results should be limited to just absence records (true) 
     * or just presence records (false).  If this parameter is missing then both absence 
     * and presence records are returned.  Valid values are 'true' and 'false'
     * 
     * @return A list of Taxon Observations conforming to the provided search
     * parameters
     * 
     * @response.representation.200.qname List<TaxonObservation>
     * @response.representation.200.mediaType application/json
     */    
    private List<TaxonObservation> retreiveObservationsRecordsByFilter(
            User user, String ip, int startYear, int endYear, List<String> datasetKeys, 
            List<String> taxa, String spatialRelationship, String featureID, 
            Boolean sensitive, String designation, String taxonOutputGroup,
            int orgSuppliedList, String gridRef, String polygon, 
            Boolean absence) throws IllegalArgumentException {
        //TODO: squareBlurring(?)
        
        if (StringUtils.hasText(polygon)) {
           checkPolygonMaxSize(polygon, taxa, designation, taxonOutputGroup, orgSuppliedList, datasetKeys);
        }
        
        // Stop users being able to request all records that they have access to at the same time
        if (!listHasAtLeastOneText(taxa)
                && !StringUtils.hasText(designation)
                && !StringUtils.hasText(taxonOutputGroup)
                && orgSuppliedList < 1
                && !listHasAtLeastOneText(datasetKeys)
                && !StringUtils.hasText(featureID) 
                && !StringUtils.hasText(gridRef)
                && !StringUtils.hasText(polygon)) {
            throw new IllegalArgumentException("Must Supply at least one type of filter; dataset (key list), spatial(featureID, gridRef or polygon) or taxon (PTVK list, Output Group, Designation or Organisation Supplied List)");    
        }
        
        if (datasetKeys.size() > 1 
                && !listHasAtLeastOneText(taxa)
                && !StringUtils.hasText(designation)
                && !StringUtils.hasText(taxonOutputGroup)
                && orgSuppliedList < 1
                && !StringUtils.hasText(featureID) 
                && !StringUtils.hasText(gridRef)
                && !StringUtils.hasText(polygon)) {
            throw new IllegalArgumentException("Must supply a spatial or taxon filter with more than one dataset");
        }
        
        writeAPIViewRecordToDatabase(user, ip, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, orgSuppliedList, gridRef, polygon, absence);
        
        return observationMapper.selectObservationRecordsByFilter(user, startYear, endYear, datasetKeys, taxa, spatialRelationship, featureID, sensitive, designation, taxonOutputGroup, gridRef, polygon, absence);        
    }
    
    private void checkPolygonMaxSize(String polygon, List<String> taxa, String designation, String taxonOutputGroup, int orgSuppliedList, List<String> datasetKeys) {
        try {
            double polygonArea = polygonUtilsMapper.getAreaFromWKT(polygon);
            double maxPolygonArea;
            String message = "";
            if (!listHasAtLeastOneText(taxa)
                    && !StringUtils.hasText(designation)
                    && !StringUtils.hasText(taxonOutputGroup)
                    && orgSuppliedList < 1
                    && !listHasAtLeastOneText(datasetKeys)) {
                maxPolygonArea = Double.parseDouble(properties.getProperty("max_polygon_no_filter"));
                message = " Please include a taxonomic or dataset filter to increase the maximum search area.";
            } else {
                maxPolygonArea = Double.parseDouble(properties.getProperty("max_polygon_with_filter"));
            }
            if (polygonArea > maxPolygonArea) {
                throw new IllegalArgumentException("Supplied polygon's area is greater than the maximum allowed area; was " + Double.toString(polygonArea) + "m^2 but must be less than " + Double.toString(maxPolygonArea) + "m^2." + message);
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Supplied polygon could not be correctly interpreted as a Geography data type;\n" + ex.getLocalizedMessage());
        }
    }
    
    /**
     * Writes a record of any taxon observation records being viewed through the 
     * API 
     * 
     * @param user The user who initiated this request
     * @param ip The IP of the user who initiated this request
     * @param startYear The start year filter used in this request
     * @param endYear The end year filter used in this request
     * @param datasetKeys The datasetKeys filter used in this request
     * @param taxa The taxa filter used in this request
     * @param spatialRelationship The spatialRelationship filter used in this request
     * @param featureID The featureID filter used in this request
     * @param sensitive The sensitive records filter used in this request
     * @param designation The designation filter used in this request
     * @param taxonOutputGroup The taxon output group filter used in this request
     * @param orgSuppliedList The organisation supplied list filter used in this request
     * @param gridRef The Grid Reference filter used in this request
     * @param polygon The Polygon filter used in this request
     * @param absence The Absence Record filter used in this request 
     */
    private void writeAPIViewRecordToDatabase(
            User user, String ip, int startYear, int endYear, List<String> datasetKeys, 
            List<String> taxa, String spatialRelationship, String featureID, 
            Boolean sensitive, String designation, String taxonOutputGroup,
            int orgSuppliedList, String gridRef, String polygon, 
            Boolean absence) {
        
        if (taxa.size() == 1 && !StringUtils.hasText(taxa.get(0))) {
            taxa = new ArrayList<String>();
        }
        
        if (datasetKeys.size() == 1 && !StringUtils.hasText(datasetKeys.get(0))) {
            datasetKeys = new ArrayList<String>();
        }        
        
        String filterText = filterToText.convert(startYear, endYear, 
                datasetKeys, taxa, spatialRelationship, featureID, sensitive, 
                designation, taxonOutputGroup, orgSuppliedList, gridRef, 
                polygon, absence);
        
        List<DatasetRecordCount> counts = observationMapper.getRecordCountsForFilterByDataset(
                user, startYear, endYear, datasetKeys, taxa, 
                spatialRelationship, featureID, sensitive, designation, 
                taxonOutputGroup, orgSuppliedList, gridRef, polygon, absence); 
        
        int total = 0;
        for (DatasetRecordCount count : counts) {
            total += count.getCount();
        }
        
        ApiObservationView view = new ApiObservationView(user.getId(), ip, filterText, total);
        oApiObservationViewMapper.addAPIObservationView(view);
        for (DatasetRecordCount count : counts) {
            oApiObservationViewMapper.addAPIObservationViewStats(view.getId(), count.getDatasetKey(), count.getCount());
        }
    }    
}
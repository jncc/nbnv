package uk.org.nbn.nbnv.api.rest.resources;

import java.io.IOException;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.codehaus.enunciate.jaxrs.TypeHint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.core.OperationalDatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalDatasetContributingOrganisationMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalDownloadMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalSurveyMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DownloadMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper;
import uk.org.nbn.nbnv.api.model.AccessPosition;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.DatasetAdministrator;
import uk.org.nbn.nbnv.api.model.DatasetImportStatus;
import uk.org.nbn.nbnv.api.model.DatasetResolutionRecordCount;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.Survey;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.UserDownloadNotification;
import uk.org.nbn.nbnv.api.model.meta.ContributingOrganisation;
import uk.org.nbn.nbnv.api.model.meta.DatasetAdminMembershipJSON;
import uk.org.nbn.nbnv.api.model.meta.OpResult;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetSurveyAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.services.DatasetImporterService;
import uk.org.nbn.nbnv.api.solr.SolrResolver;

@Component
@Path("/datasets")
public class DatasetResource extends AbstractResource {
    @Autowired OperationalDatasetMapper oDatasetMapper;
    @Autowired DatasetAdministratorMapper datasetAdministratorMapper;
    @Autowired OperationalDatasetAdministratorMapper oDatasetAdministratorMapper;
    @Autowired DatasetMapper datasetMapper;
    @Autowired OperationalSurveyMapper oSurveyMapper;
    @Autowired OrganisationMapper organisationMapper;
    @Autowired OperationalDatasetContributingOrganisationMapper oDatasetContributingOrganisationMapper;
    @Autowired DownloadMapper downloadMapper;
    @Autowired OperationalDownloadMapper oDownloadMapper;
    @Autowired DatasetImporterService importerService;
    
    /**
     * Returns a list of all datasets from the data warehouse
     * 
     * @return List of Datasets
     * 
     * @response.representation.200.qname List<Dataset>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @TypeHint(Dataset.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned list of datasets")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dataset> getDatasetList(){
        return datasetMapper.selectAll();
    }
    
    /**
     * Returns a specific dataset from the data warehouse 
     * 
     * @param id ID of a dataset i.e. GA000466
     * 
     * @return A specific dataset from the data warehouse
     * 
     * @response.representation.200.qname Dataset
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}")
    @TypeHint(Dataset.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Found and returned dataset"),
        @ResponseCode(code = 204, condition = "No dataset with this key exists")    
    })    
    @Produces(MediaType.APPLICATION_JSON)
    @SolrResolver("DATASET")
    public Dataset getDatasetByID(@PathParam("id") String id){
        return datasetMapper.selectByDatasetKey(id);
    }
    
    /**
     * Returns a selected dataset from the core database if the current user is
     * an admin of the specified dataset otherwise returns a 403 Forbidden error
     * 
     * @param admin The Current User if they are a dataset admin for this 
     * dataset or throw a 403 Forbidden error (Injected Token no need to pass)
     * @param id ID of a dataset i.e. GA000466
     * 
     * @return A selected dataset from the core database if the current user is
     * an admin of the specified dataset
     * 
     * @response.representation.200.qname Dataset
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/edit")
    @TypeHint(Dataset.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Found and returned dataset"),
        @ResponseCode(code = 204, condition = "No dataset with this key exists"),
        @ResponseCode(code = 403, condition = "You do not have admin rights over this dataset")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Dataset getEditDatasetByID(@TokenDatasetAdminUser(path="id") User admin, @PathParam("id") String id){
        return oDatasetMapper.selectByDatasetKey(id);
    }
    
    /**
     * Query the nbn importer for information relating the the given dataset key.
     * The information we will get is:
     *  - if an import is queued up for the given dataset key
     *  - if an import for the dataset key is currently being processed
     *  - any known history relating the previously completed imports (e.g. validation errors)
     * 
     * @param admin The Current User if they are a dataset admin for this 
     * dataset or throw a 403 Forbidden error (Injected Token no need to pass)
     * @param id ID of a dataset i.e. GA000466
     * 
     * @return The current state of the importer with regards to the given dataset
     * 
     * @response.representation.200.qname DatasetImportStatus
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/import")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "The status of the importer for the given dataset"),
        @ResponseCode(code = 403, condition = "You do not have admin rights over this dataset")}
    )
    @Produces(MediaType.APPLICATION_JSON)
    public DatasetImportStatus getImportStatus(@TokenDatasetAdminUser(path="id") User admin, @PathParam("id") String id) throws IOException {
        return new DatasetImportStatus(
                importerService.isQueued(id),
                id.equalsIgnoreCase(importerService.getCurrentlyProcessedDataset()),
                importerService.getImportHistory(id)
        );
    }
    
    /**
     * Returns a list of the last 10 most recently uploaded datasets
     * 
     * @return a list of the last 10 most recently uploaded datasets
     * 
     * @response.representation.200.qname List<Dataset>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/latest")
    @TypeHint(Dataset.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned list of 10 most recently uploaded datasets")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dataset> getLastUpdatedDatasets() {
        return datasetMapper.getLatestUploaded();
    }
    
    /**
     * Returns an atom+xml feed of the latest 10 most recently uploaded datasets
     * 
     * @return an atom+xml feed of the latest 10 most recently uploaded datasets
     * 
     * @response.representation.200.qname Feed
     * @response.representation.200.mediaType application/atom+xml
     */
    @GET
    @Path("/latest.rss")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned rss feed")
    })    
    @Produces("application/atom+xml")  
    public Feed getLastUpdatedDatasetsFeed() {
        Feed toReturn = new Abdera().getFactory().newFeed();
        for(Dataset currDataset :datasetMapper.getLatestUploaded()) {
            Entry entry = toReturn.addEntry();
            entry.setTitle(currDataset.getOrganisationName());
            entry.setContent(currDataset.getTitle());
            entry.setPublished(currDataset.getDateUploaded());
        }
        return toReturn;
    }
    
    /**
     * If the current user is an admin of the specified dataset update the 
     * dataset metadata with the values contained in the POSTed form otherwise
     * returns a 403 Forbidden error
     * 
     * @param admin The Current User if they are a dataset admin for this 
     * dataset or throw a 403 Forbidden error (Injected Token no need to pass)
     * @param datasetKey Key of a dataset i.e. GA000466
     * @param title The dataset title
     * @param description The dataset description
     * @param captureMethod The dataset Capture Method
     * @param purpose The datasets Purpose
     * @param geographicalCoverage The Geographical Coverage of the dataset
     * @param quality The quality of recording in this dataset
     * @param additionalInformation Any Additional Information about this dataset
     * @param accessConstraints Any Access Constraints applicable to this dataset
     * @param useConstraints Any Use Constraints applicable to this dataset
     * @param temporalCoverage The Temporal Coverage of this dataset
     * 
     * @return An OpResult detailing the success or failure of the action
     * 
     * @response.representation.200.qname OpResult
     * @response.representation.200.mediaType application/json
     */
    @POST
    @Path("/{id}")
    @TypeHint(OpResult.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Operation completed but may have failed, details in OpResult"),
        @ResponseCode(code = 403, condition = "You do not have administration rights over this dataset")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public OpResult updateDataset(@TokenDatasetAdminUser(path="id") User admin, @PathParam("id") String datasetKey
            , @FormParam("title") String title
            , @FormParam("description") String description
            , @FormParam("captureMethod") String captureMethod
            , @FormParam("purpose") String purpose
            , @FormParam("geographicalCoverage") String geographicalCoverage
            , @FormParam("quality") String quality
            , @FormParam("additionalInformation") String additionalInformation
            , @FormParam("accessConstraints") String accessConstraints
            , @FormParam("useConstraints") String useConstraints
            , @FormParam("temporalCoverage") String temporalCoverage) {
        int result = oDatasetMapper.updateDataset(new Dataset(datasetKey, title, description, captureMethod, purpose, geographicalCoverage, quality, additionalInformation, accessConstraints, useConstraints, temporalCoverage));
        
        if (result == 1 ) {
            return new OpResult();
        } else if (result == 0) {
            return new OpResult("No matching dataset found!");
        } else {
            return new OpResult("Unknown issue");
        }
    }
        
    /**
     * Returns a list of attributes relating to the specified dataset
     * 
     * @param user The Current user (Injected Token no need to pass)
     * @param datasetKey Key of a dataset i.e. GA000466
     * 
     * @result A list of attributes relating to the specified dataset
     * 
     * @response.representation.200.qname List<String>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{datasetKey}/accessPositions")
    @TypeHint(AccessPosition.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned list of access positions that the current user has over this dataset")
    })     
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccessPosition> getAttributesByDatasetKey(@TokenUser() User user, @PathParam("datasetKey") String datasetKey){
        return datasetMapper.getDatasetAccessPositions(datasetKey, user.getId());
    }
    
    /**
     * Returns a list of DatasetResolutionRecordCount giving the resolution data
     * for the specified dataset
     * 
     * @param datasetKey Key of a dataset i.e. GA000466
     * 
     * @return A list of DatasetResolutionRecordCount giving the resolution data
     * for the specified dataset
     * 
     * @response.representation.200.qname List<DatasetResolutionRecordCount>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{datasetKey}/resolutionData")
    @TypeHint(DatasetResolutionRecordCount.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a count of records at each resolution in this dataset"),
        @ResponseCode(code = 204, condition = "No dataset with this key exists")
    })      
    @Produces(MediaType.APPLICATION_JSON)
    public List<DatasetResolutionRecordCount> getDatasetResolution(@PathParam("datasetKey") String datasetKey) {
        return datasetMapper.getResolutionData(datasetKey);
    }
    
    /**
     * Return boolean stating if the user is an administrator of this dataset
     * used in the freemarker templates to optionally include links to edit
     * dataset / survey metadata
     *  
     * @param user The Current User (Injected Token no need to pass)
     * @param datasetKey Key of a dataset i.e. GA000466
     * 
     * @return True if the user is admin of this dataset
     * 
     * @response.representation.200.qname boolean
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{datasetKey}/isAdmin")
    @TypeHint(Boolean.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "The user is an admin of the selected dataset"),
        @ResponseCode(code = 403, condition = "The user is not an admin of the selected dataset")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isDatasetAdmin(@TokenUser User user, @PathParam("datasetKey") String datasetKey) {
        return datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey);
    }
    
    /**
     * Return a list of admin users for the specified dataset if the requesting
     * user is an admin themselves
     * 
     * @param user The Current User (Injected Token no need to pass)
     * @param datasetKey Key of a dataset i.e. GA000466
     * 
     * @return True if the user is admin of this dataset
     * 
     * @response.representation.200.qname List<DatasetAdministrator>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{datasetKey}/admins")
    @TypeHint(DatasetAdministrator.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of admins for this dataset"),
        @ResponseCode(code = 403, condition = "The user is not an admin of the selected dataset")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    public List<DatasetAdministrator> getDatasetAdmins(@TokenDatasetAdminUser(path = "datasetKey") User user, @PathParam("datasetKey") String datasetKey) {
        return datasetAdministratorMapper.selectByDataset(datasetKey);
    }
    
    /**
     * Add a selected user as  dataset admin, the current user must be a 
     * dataset admin to do this
     * 
     * @param user The current user, must be admin of this dataset (Injected Token no need to pass)
     * @param datasetKey Key of a dataset i.e. GA000466
     * @param data A DatasetAdminMembershipJSON object detailing the user to be made an admin
     *
     * @return An OpResult detailing the success or failure of the operation
     * 
     * @response.representation.200.qname OpResult
     * @response.representation.200.mediaType application/json
     */
    @POST 
    @Path("/{datasetKey}/addAdmin")
    @TypeHint(OpResult.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Operation has been carried out, may have failed however, check the contents of the OpResult"),
        @ResponseCode(code = 403, condition = "Current user is not an admin of this dataset")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public OpResult addDatasetAdmin(@TokenDatasetAdminUser(path = "datasetKey") User user, @PathParam("datasetKey") String datasetKey, DatasetAdminMembershipJSON data) {
        int result = oDatasetAdministratorMapper.insertNewDatasetAdministrator(data.getUserID(), datasetKey);
        
        if (result == 1) {
            return new OpResult();
        }
        
        return new OpResult("Could not give the selected user admin rights, possibly an admin already");
    };
    
    /**
     * Remove the selected user as  dataset admin, the current user must be a 
     * dataset admin to do this, you can remove your own admin rights
     * 
     * @param user The current user, must be admin of this dataset (Injected Token no need to pass)
     * @param datasetKey Key of a dataset i.e. GA000466
     * @param data A DatasetAdminMembershipJSON object detailing the user to be have admin rights revoked
     *
     * @return An OpResult detailing the success or failure of the operation
     * 
     * @response.representation.200.qname OpResult
     * @response.representation.200.mediaType application/json
     */    
    @POST
    @Path("/{datasetKey}/removeAdmin")
    @TypeHint(OpResult.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Operation has been carried out, may have failed however, check the contents of the OpResult"),
        @ResponseCode(code = 403, condition = "Current user is not an admin of this dataset")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public OpResult removeDatasetAdmin(@TokenDatasetAdminUser(path = "datasetKey") User user, @PathParam("datasetKey") String datasetKey, DatasetAdminMembershipJSON data) {        
        int result = oDatasetAdministratorMapper.removeDatasetAdministrator(data.getUserID(), datasetKey);
        
        if (result == 1) {
            if (downloadMapper.doesUserHaveDownloadNotificationsForDataset(data.getUserID(), datasetKey)) {
                oDownloadMapper.removeUserNotificationForDownload(data.getUserID(), datasetKey);
            }
            return new OpResult();
        }
        
        return new OpResult("Could not revoke the user's dataset admin rights");        
    }
    
    /**
     * Returns a list of datasets the user is administrator of
     * 
     * @param user The current user (Injected Token no need to pass)
     *
     * @return List of datasets
     * 
     * @response.representation.200.qname List<Dataset>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/adminable")
    @TypeHint(Dataset.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of the current users adminable datasets")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dataset> adminableDatasets(@TokenUser(allowPublic=false) User user) {
        return datasetAdministratorMapper.selectDatasetsByUser(user.getId());
    }   
    /***********************************************
     * Survey API calls
     ***********************************************/
    
    /**
     * Get survey metadata from the core database if the user is an admin of the
     * specified parent dataset otherwise return a 403 Forbidden error
     * 
     * @param user The Current User if dataset admin, or return a 403 Forbidden (Injected Token no need to pass)
     * @param datasetKey Key of a dataset i.e. GA000466
     * @param survey Key of a survey belonging to that dataset
     * 
     * @return The specified surveys metadata from the core database
     * 
     * @response.representation.200.qname Survey
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{datasetKey}/surveys/{survey}")
    @TypeHint(Survey.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the survey metadata"),
        @ResponseCode(code = 403, condition = "Current user is not an admin for the dataset containing this survey or the survey itself")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Survey getJson(@TokenDatasetSurveyAdminUser(dataset = "datasetKey", survey = "survey") User user, 
            @PathParam("datasetKey") String datasetKey, @PathParam("survey") int survey) {
        return oSurveyMapper.getSurveyById(survey);
    }

    /**
     * If the current user is an admin of the specified dataset update the 
     * specified surveys metadata with the values contained in the POSTed form
     * 
     * @param admin The Current User if they are a dataset admin for this 
     * dataset or throw a 403 Forbidden error (Injected Token no need to pass)
     * @param datasetKey Key of the parent dataset i.e. GA000466
     * @param survey Key of the survey to have its metadata edited
     * @param title The survey title
     * @param description The survey description
     * @param geographicalCoverage The Geographical Coverage of the survey
     * @param temporalCoverage The Temporal Coverage of this survey
     * @param dataQuality The quality of recording in this survey
     * @param dataCaptureMethod The survey Capture Method
     * @param purpose The surveys purpose
     * @param additionalInformation Any Additional Information about this survey
     * 
     * @return A Response object detailing the success or failure of the action
     * 
     * @response.representation.200.qname Response
     * @response.representation.200.mediaType application/json  
     */
    @POST
    @Path("/{datasetKey}/surveys/{survey}")
    @TypeHint(Survey.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Operation completed successfully"),
        @ResponseCode(code = 403, condition = "Current user is not an admin for the dataset containing this survey or the survey itself")
    })
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putJson(@TokenDatasetSurveyAdminUser(dataset = "datasetKey", survey = "survey") User user,
            @PathParam("datasetKey") String datasetKey, @PathParam("survey") int survey,
            @FormParam("title") String title,
            @FormParam("description") String description,
            @FormParam("geographicalCoverage") String geographicalCoverage,
            @FormParam("temporalCoverage") String temporalCoverage,
            @FormParam("dataQuality") String dataQuality,
            @FormParam("dataCaptureMethod") String dataCaptureMethod,
            @FormParam("purpose") String purpose,
            @FormParam("additionalInformation") String additionalInformation) {

        Survey updated = new Survey(survey, null, title, 
                description, geographicalCoverage, temporalCoverage, 
                dataQuality, dataCaptureMethod, purpose, 
                additionalInformation);
        
        oSurveyMapper.updateSurveyById(updated);

        return Response.status(Response.Status.OK).entity(updated).build();
    }
    
    /**
     * Adds a new contributing organisation to a dataset, if the current user is
     * and admin of that dataset.
     * 
     * @param user The current user, must be a dataset admin (Injected Token no need to pass)
     * @param datasetKey The selected dataset to add a contributing org to
     * @param data JSON datapacket containing the organisation id, ie. 
     * {orgID: 1}
     * @return The success or failure of the action
     * 
     * @response.representation.200.qname OpResult
     * @response.representation.200.mediaType application/json  
     */
    @PUT
    @Path("/{datasetKey}/contributing")
    @TypeHint(OpResult.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Operation returned, may have failed, check the OpResult"),
        @ResponseCode(code = 403, condition = "Current user is not an admin of this dataset")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public OpResult addNewContributingOrganisation(
            @TokenDatasetAdminUser(path = "datasetKey") User user,
            @PathParam("datasetKey") String datasetKey,
            ContributingOrganisation data) {
        // Needs most up to date data so use core otherwise we can get 
        // inconsistencies
        Dataset dataset = oDatasetMapper.selectByDatasetKey(datasetKey);
        Organisation org = organisationMapper.selectByID(data.getOrgID());
        
        if (org == null)
            return new OpResult("No such organisation exists");
        for (Organisation contrib : dataset.getContributingOrganisations()) {
            if (contrib.getId() == org.getId()) 
                return new OpResult("This organisation already contributes to this dataset");
        }            
        if (dataset.getOrganisation().getId() == data.getOrgID())
            return new OpResult("This organisation owns this dataset");
        
        if (oDatasetContributingOrganisationMapper.addNewDatasetContirbutor(datasetKey, data.getOrgID()) == 1)
            return new OpResult();
        
        return new OpResult("Could not add the contributing organisation, please try again later");
    }
    
    /**
     * Removes an existing contributing organisation to a dataset, if the 
     * current user is a dataset admin
     * 
     * @param user The current user, must be a dataset admin (Injected Token no need to pass)
     * @param datasetKey The selected dataset
     * @param id The organisation ID to be removed from contributing org list
     * @return The success or failure of this operation
     * 
     * @response.representation.200.qname OpResult
     * @response.representation.200.mediaType application/json  
     */
    @DELETE
    @Path("/{datasetKey}/contributing/{id}")
    @TypeHint(OpResult.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Operation returned, may have failed, check the OpResult"),
        @ResponseCode(code = 403, condition = "Current user is not an admin of this dataset")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public OpResult removeContributingOrganisation(
            @TokenDatasetAdminUser(path = "datasetKey") User user,
            @PathParam("datasetKey") String datasetKey,
            @PathParam("id") int id) {
        // Needs most up to date data so use core otherwise we can get 
        // inconsistencies
        Dataset dataset = oDatasetMapper.selectByDatasetKey(datasetKey);
        Organisation org = organisationMapper.selectByID(id);
        
        if (org == null)
            return new OpResult("No such organisation exists");
        boolean exists = false;
        for (Organisation contrib : dataset.getContributingOrganisations()) {
            if (contrib.getId() == org.getId()) 
                exists = true;
        }                    
        if (!exists)
            return new OpResult("This organisation does not currently contribute to this dataset");
        if (dataset.getOrganisation().getId() == id)
            return new OpResult("This organisation owns this dataset, cannot remove it");
        
        if (oDatasetContributingOrganisationMapper.removeExistingContributer(datasetKey, id) == 1)
            return new OpResult();
        
        return new OpResult("Could not remove the contributing organisation, please try again later");
    }
    
//    /**
//     * Return a list of datasets which are adminable by this user as either a
//     * dataset admin or an organisation admin
//     * 
//     * @param user The current user (Injected Token no need to pass)
//     * @return A List of datasets that the current user can administer
//     * 
//     * @response.representation.200.qname List<Dataset>
//     * @response.representation.200.mediaType application/json  
//     */
//    @GET 
//    @Path("/adminableDatasetsByUserAndOrg")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<Dataset> getAdminableDatasetsByUserAndOrg(@TokenUser(allowPublic = false) User user) {
//        return datasetAdministratorMapper.getAdminableDatasetsByUserAndOrgs(user.getId());
//    }  
      
    /**
     * Return a list of users to notify when a dataset has been downloaded
     * 
     * @param user The current user, must have admin rights over the dataset (Injected Token no need to pass)
     * @param datasetKey The dataset key being downloaded
     * @return A list of users to notify
     * 
     * @response.representation.200.qname List<UserDownloadNotification>
     * @response.representation.200.mediaType application/json  
     */
    @GET
    @Path("/{datasetKey}/downloadNotifications")
    @TypeHint(UserDownloadNotification.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of users to notify"),
        @ResponseCode(code = 403, condition = "Current user is not an admin for this dataset")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserDownloadNotification> getDownloadNotifcationsForDataset (
            @TokenDatasetAdminUser(path = "datasetKey") User user,
            @PathParam("datasetKey") String datasetKey) {
        return downloadMapper.getUsersToNotifyForDatasetDownload(datasetKey);
    }
    
    /**
     * Return a list of datasets that the user currently has download 
     * notifications switched on for
     * 
     * @param user The current user (Injected Token no need to pass)
     * @return A list of datasets that user wants notifications for
     * 
     * @response.representation.200.qname List<UserDownloadNotification>
     * @response.representation.200.mediaType application/json  
     */
    @GET
    @Path("/myDownloadNotifications")
    @TypeHint(UserDownloadNotification.class)
    @StatusCodes({
       @ResponseCode(code = 200, condition = "Successfully returned a list of datasets to send notifcations to, for the current user"),
       @ResponseCode(code = 403, condition = "Current user is not logged in")
    }) 
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserDownloadNotification> getDownloadNotificationsForUser (
            @TokenUser(allowPublic = false) User user) {
        return downloadMapper.getNotifyingDatasetsForUser(user.getId());
    }
    
    /**
     * Return if the current user has download notifications enabled or not for
     * the specified dataset
     * 
     * @param user The current user
     * @param datasetKey A datasetKey
     * @return If the current user has download notifications for this dataset
     * 
     * @response.representation.200.qname boolean
     * @response.representation.200.mediaType application/json  
     */
    @GET
    @Path("/{datasetKey}/userDownloadNotification")
    @TypeHint(Boolean.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a boolean value denoting if the current user has download notifcations turned on for this dataset"),
        @ResponseCode(code = 403, condition = "Current user is not an admin of the dataset")
    })  
    @Produces(MediaType.APPLICATION_JSON)
    public boolean getDownloadNotificationSetting(
            @TokenDatasetAdminUser(path = "datasetKey") User user,
            @PathParam("datasetKey") String datasetKey) {
        return oDownloadMapper.checkUserNotificationForDatasetDownload(user.getId(), datasetKey);
    }
    
    /**
     * Add or remove download notifications for the current user if they are
     * a dataset admin or org admin for this dataset
     * 
     * @param user The current user (Injected Token no need to pass)
     * @param datasetKey A datasetKey
     * @param add True to add notifications and false to remove them
     * @return Success or failure of the operation
     * 
     * @response.representation.200.qname boolean
     * @response.representation.200.mediaType application/json  
     */
    @POST
    @Path("/{datasetKey}/userDownloadNotification")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully updated download notifications setting"),
        @ResponseCode(code = 403, condition = "Current user is not an admin of the dataset")
    })   
    @Produces(MediaType.APPLICATION_JSON)
    public Response setDownloadNotificationSetting(
            @TokenDatasetAdminUser(path = "datasetKey") User user,
            @PathParam("datasetKey") String datasetKey,
            @QueryParam("add") String add) {       
        if (add.equals("true")) {
            if (oDownloadMapper.checkUserNotificationForDatasetDownload(user.getId(), datasetKey))
                return Response.ok().build();
            if (oDownloadMapper.addUserNotificationForDatasetDownload(user.getId(), datasetKey) == 1) 
                return Response.ok().build();                   
        } else {
            if (oDownloadMapper.checkUserNotificationForDatasetDownload(user.getId(), datasetKey)) {
                oDownloadMapper.removeUserNotificationForDownload(user.getId(), datasetKey);
            }
        }
        
        return Response.ok().build();
    }
}

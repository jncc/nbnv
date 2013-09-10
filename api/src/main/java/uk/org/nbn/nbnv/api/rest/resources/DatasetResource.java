package uk.org.nbn.nbnv.api.rest.resources;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.core.OperationalDatasetContributingOrganisationMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalDownloadMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalSurveyMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DownloadMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.DatasetAdministrator;
import uk.org.nbn.nbnv.api.model.DatasetResolutionRecordCount;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.Survey;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.UserDownloadNotification;
import uk.org.nbn.nbnv.api.model.meta.ContributingOrganisation;
import uk.org.nbn.nbnv.api.model.meta.DatasetAdminMembershipJSON;
import uk.org.nbn.nbnv.api.model.meta.OpResult;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetOrOrgAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetSurveyAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.solr.SolrResolver;

@Component
@Path("/datasets")
public class DatasetResource extends AbstractResource {
    @Autowired OperationalDatasetMapper oDatasetMapper;
    @Autowired DatasetAdministratorMapper datasetAdministratorMapper;
    @Autowired DatasetMapper datasetMapper;
    @Autowired OperationalSurveyMapper oSurveyMapper;
    @Autowired OrganisationMapper organisationMapper;
    @Autowired OperationalDatasetContributingOrganisationMapper oDatasetContributingOrganisationMapper;
    @Autowired DownloadMapper downloadMapper;
    @Autowired OperationalDownloadMapper oDownloadMapper;
    
    /**
     * Returns a list of all datasets from the data warehouse
     * 
     * @return List of Datasets
     * 
     * @response.representation.200.qname List<Dataset>
     * @response.representation.200.mediaType application/json
     */
    @GET
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
     * dataset or throw a 403 Forbidden error
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
    @Produces(MediaType.APPLICATION_JSON)
    public Dataset getEditDatasetByID(@TokenDatasetAdminUser(path="id") User admin, @PathParam("id") String id){
        return oDatasetMapper.selectByDatasetKey(id);
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
     * dataset or throw a 403 Forbidden error
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
     * @param user The Current user
     * @param datasetKey Key of a dataset i.e. GA000466
     * 
     * @result A list of attributes relating to the specified dataset
     * 
     * @response.representation.200.qname List<String>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{datasetKey}/accessPositions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAttributesByDatasetKey(@TokenUser() User user, @PathParam("datasetKey") String datasetKey){
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
    @Produces(MediaType.APPLICATION_JSON)
    public List<DatasetResolutionRecordCount> getDatasetResolution(@PathParam("datasetKey") String datasetKey) {
        return datasetMapper.getResolutionData(datasetKey);
    }
    
    /**
     * Return boolean stating if the user is an administrator of this dataset
     * used in the freemarker templates to optionally include links to edit
     * dataset / survey metadata
     * 
     * @param user The Current User
     * @param datasetKey Key of a dataset i.e. GA000466
     * 
     * @return True if the user is admin of this dataset
     * 
     * @response.representation.200.qname boolean
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{datasetKey}/isAdmin")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isDatasetAdmin(@TokenUser User user, @PathParam("datasetKey") String datasetKey) {
        return datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey);
    }
    
    /**
     * Return a list of admin users for the specified dataset if the requesting
     * user is an admin themselves
     * 
     * @param user The Current User
     * @param datasetKey Key of a dataset i.e. GA000466
     * 
     * @return True if the user is admin of this dataset
     * 
     * @response.representation.200.qname List<DatasetAdministrator>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{datasetKey}/admins")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DatasetAdministrator> getDatasetAdmins(@TokenDatasetAdminUser(path = "datasetKey") User user, @PathParam("datasetKey") String datasetKey) {
        return datasetAdministratorMapper.selectByDataset(datasetKey);
    }
    
    @POST 
    @Path("/{datasetKey}/addAdmin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public OpResult addDatasetAdmin(@TokenDatasetAdminUser(path = "datasetKey") User user, @PathParam("datasetKey") String datasetKey, DatasetAdminMembershipJSON data) {
        int result = datasetAdministratorMapper.insertNewDatasetAdministrator(data.getUserID(), datasetKey);
        
        if (result == 1) {
            return new OpResult();
        }
        
        return new OpResult("Could not give the selected user admin rights, possibly an admin already");
    };
    
    @POST
    @Path("/{datasetKey}/removeAdmin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public OpResult removeDatasetAdmin(@TokenDatasetAdminUser(path = "datasetKey") User user, @PathParam("datasetKey") String datasetKey, DatasetAdminMembershipJSON data) {
        int result = datasetAdministratorMapper.removeDatasetAdministrator(data.getUserID(), datasetKey);
        
        if (result == 1) {
            return new OpResult();
        }
        
        return new OpResult("Could not revoke the user's dataset admin rights");        
    }
    
    /**
     * Returns a list of datasets the user can admin
     * 
     * @param user The current user
     *
     * @return List of datasets
     * 
     * @response.representation.200.qname List<Dataset>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/adminable")
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
     * @param user The Current User if dataset admin, or return a 403 Forbidden
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
    @Produces("application/json")
    public Survey getJson(@TokenDatasetSurveyAdminUser(dataset = "datasetKey", survey = "survey") User user, 
            @PathParam("datasetKey") String datasetKey, @PathParam("survey") int survey) {
        return oSurveyMapper.getSurveyById(survey);
    }

    /**
     * If the current user is an admin of the specified dataset update the 
     * specified surveys metadata with the values contained in the POSTed form
     * 
     * @param admin The Current User if they are a dataset admin for this 
     * dataset or throw a 403 Forbidden error
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
     * @param user The current user, must be a dataset admin
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
     * @param user The current user, must be a dataset admin
     * @param datasetKey The selected dataset
     * @param id The organisation ID to be removed from contributing org list
     * @return The success or failure of this operation
     * 
     * @response.representation.200.qname OpResult
     * @response.representation.200.mediaType application/json  
     */
    @DELETE
    @Path("/{datasetKey}/contributing/{id}")
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
//     * @param user The current user
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
     * @param user The current user, must have admin rights over the dataset
     * @param datasetKey The dataset key being downloaded
     * @return A list of users to notify
     * 
     * @response.representation.200.qname List<UserDownloadNotification>
     * @response.representation.200.mediaType application/json  
     */
    @GET
    @Path("/{datasetKey}/downloadNotifications")
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
     * @param user The current user
     * @return A list of datasets that user wants notifications for
     * 
     * @response.representation.200.qname List<UserDownloadNotification>
     * @response.representation.200.mediaType application/json  
     */
    @GET
    @Path("/myDownloadNotifications")
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
    @Produces(MediaType.APPLICATION_JSON)
    public boolean getDownloadNotificationSetting(
            @TokenDatasetAdminUser(path = "datasetKey") User user,
            @PathParam("datasetKey") String datasetKey) {
        return downloadMapper.doesUserHaveDownloadNotificationsForDataset(user.getId(), datasetKey);
    }
    
    /**
     * Add or remove download notifications for the current user if they are
     * a dataset admin or org admin for this dataset
     * 
     * @param user The current user
     * @param datasetKey A datasetKey
     * @param add True to add notifications and false to remove them
     * @return Success or failure of the operation
     * 
     * @response.representation.200.qname boolean
     * @response.representation.200.mediaType application/json  
     */
    @POST
    @Path("/{datasetKey}/userDownloadNotification")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean setDownloadNotificationSetting(
            @TokenDatasetAdminUser(path = "datasetKey") User user,
            @PathParam("datasetKey") String datasetKey,
            @QueryParam("add") boolean add) {
        
        if (add) 
            return oDownloadMapper.addUserNotificationForDatasetDownload(user.getId(), datasetKey);
        
        return oDownloadMapper.removeUserNotificationForDownload(user.getId(), datasetKey);
    }
}

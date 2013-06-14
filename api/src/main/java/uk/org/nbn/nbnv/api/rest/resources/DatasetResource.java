package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalSurveyMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.model.Attribute;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.DatasetResolutionRecordCount;
import uk.org.nbn.nbnv.api.model.Survey;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.meta.OpResult;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.solr.SolrResolver;

@Component
@Path("/datasets")
public class DatasetResource extends AbstractResource {
    @Autowired OperationalDatasetMapper oDatasetMapper;
    @Autowired DatasetAdministratorMapper datasetAdministratorMapper;
    @Autowired DatasetMapper datasetMapper;
    @Autowired OperationalSurveyMapper oSurveyMapper;
    
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
     * @response.representation.200.qname List<DatasetResolutionRecordCount>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{datasetKey}/isAdmin")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isDatasetAdmin(@TokenUser User user, @PathParam("datasetKey") String datasetKey) {
        return datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey);
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
    public Survey getJson(@TokenDatasetAdminUser(path = "datasetKey") User user, 
            @PathParam("datasetKey") String datasetKey, @PathParam("survey") int survey) {
        return oSurveyMapper.getSurveyById(survey, datasetKey);
    }

    /**
     * If the current user is an admin of the specified dataset update the 
     * specified surveys metadata with the values contained in the POSTed form
     * 
     * @param admin The Current User if they are a dataset admin for this 
     * dataset or throw a 403 Forbidden error
     * @param datasetKey Key of the parent dataset i.e. GA000466
     * @param survey Key of the survey to have its metadata edited
     * @param providerKey Key provided by the owner of the survey (external id)
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
    public Response putJson(@TokenDatasetAdminUser(path = "datasetKey") User user,
            @PathParam("datasetKey") String datasetKey, @PathParam("survey") int survey,
            @FormParam("providerKey") String providerKey,
            @FormParam("title") String title,
            @FormParam("description") String description,
            @FormParam("geographicalCoverage") String geographicalCoverage,
            @FormParam("temporalCoverage") String temporalCoverage,
            @FormParam("dataQuality") String dataQuality,
            @FormParam("dataCaptureMethod") String dataCaptureMethod,
            @FormParam("purpose") String purpose,
            @FormParam("additionalInformation") String additionalInformation) {

        Survey updated = new Survey(survey, providerKey, title, 
                description, geographicalCoverage, temporalCoverage, 
                dataQuality, dataCaptureMethod, purpose, 
                additionalInformation);
        
        oSurveyMapper.updateSurveyById(updated);

        return Response.status(Response.Status.OK).entity(updated).build();
    }
}

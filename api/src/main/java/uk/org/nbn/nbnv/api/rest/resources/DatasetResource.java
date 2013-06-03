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
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
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
    @Autowired DatasetMapper datasetMapper;
    @Autowired OperationalSurveyMapper oSurveyMapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dataset> getDatasetList(){
        return datasetMapper.selectAll();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @SolrResolver("DATASET")
    public Dataset getDatasetByID(@PathParam("id") String id){
        return datasetMapper.selectByDatasetKey(id);
    }
    
    @GET
    @Path("/{id}/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public Dataset getEditDatasetByID(@TokenDatasetAdminUser(path="id") User admin, @PathParam("id") String id){
        return oDatasetMapper.selectByDatasetKey(id);
    }
    
    @GET
    @Path("/latest")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dataset> getLastUpdatedDatasets() {
        return datasetMapper.getLatestUploaded();
    }
    
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
        
    @GET
    @Path("/{datasetKey}/accessPositions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAttributesByDatasetKey(@TokenUser() User user, @PathParam("datasetKey") String datasetKey){
        return datasetMapper.getDatasetAccessPositions(datasetKey, user.getId());
    }

    @GET
    @Path("/{datasetKey}/resolutionData")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DatasetResolutionRecordCount> getDatasetResolution(@PathParam("datasetKey") String datasetKey) {
        return datasetMapper.getResolutionData(datasetKey);
    }
    
    /***********************************************
     * Survey API calls
     ***********************************************/
    
    @GET
    @Path("/{datasetKey}/surveys/{survey}")
    @Produces("application/json")
    public Survey getJson(@TokenDatasetAdminUser(path = "datasetKey") User user, 
            @PathParam("datasetKey") String datasetKey, @PathParam("survey") int survey) {
        return oSurveyMapper.getSurveyById(survey);
    }

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

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
import uk.org.nbn.nbnv.api.dao.warehouse.SampleMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.SurveyMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonObservationMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.DatasetAdministrator;
import uk.org.nbn.nbnv.api.model.DatasetResolutionRecordCount;
import uk.org.nbn.nbnv.api.model.Sample;
import uk.org.nbn.nbnv.api.model.Survey;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.meta.DatasetAdminMembershipJSON;
import uk.org.nbn.nbnv.api.model.meta.OpResult;
import uk.org.nbn.nbnv.api.rest.exceptions.UnauthorisedException;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetSurveyAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.solr.SolrResolver;

@Component
@Path("/data")
public class DataResource extends AbstractResource {
//    @Autowired OperationalDatasetMapper oDatasetMapper;
    @Autowired DatasetAdministratorMapper datasetAdministratorMapper;
    @Autowired DatasetMapper datasetMapper;
    @Autowired SurveyMapper surveyMapper;
    @Autowired SampleMapper sampleMapper;
    @Autowired TaxonObservationMapper taxonObservationMapper;
    
    @GET
    @Path("/taxondatasets")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonDataset> getTaxonDatasetsForUser(@TokenUser(allowPublic=false) User user){
        return datasetMapper.getTaxonDatasetsForUser(user.getId());
    }
    
    @GET
    @Path("/taxondatasets/{datasetKey}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonDataset getTaxonDatasetForUser(@TokenUser(allowPublic=false) User user, @PathParam("datasetKey") String datasetKey){
        return datasetMapper.getTaxonDatasetForUser(user.getId(), datasetKey);
    }
    
        
    @GET
    @Path("/taxondatasets/{datasetKey}/surveys")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Survey> getDatasetSurveys(@TokenUser(allowPublic=false) User user, @PathParam("datasetKey") String datasetKey){
        if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey)) {
           throw new UnauthorisedException();
        }
        
        return surveyMapper.selectSurveysByDatasetKey(datasetKey);
    }
    
//    @GET
//    @Path("/taxondatasets/{datasetKey}/surveys/{surveyId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<Survey> getDatasetSurveys(@TokenUser(allowPublic=false) User user
//            , @PathParam("datasetKey") String datasetKey
//            , @PathParam("surveyId") int surveyId){
//        if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey)) {
//           throw new UnauthorisedException();
//        }
//        
//        return surveyMapper.selectSurveyByIdAndDatasetKey(datasetKey, surveyId);
//    }
    
    @GET
    @Path("/taxondatasets/{datasetKey}/surveys/{surveyProviderKey}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Survey> getDatasetSurveys(@TokenUser(allowPublic=false) User user
            , @PathParam("datasetKey") String datasetKey
            , @PathParam("surveyProviderKey") String providerKey){
        if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey)) {
           throw new UnauthorisedException();
        }
        
        return surveyMapper.selectSurveyByProviderKeyAndDatasetKey(datasetKey, providerKey);
    }
    
//    @GET
//    @Path("/taxondatasets/{datasetKey}/surveys/{surveyId}/samples")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<Sample> getSurveySamples(@TokenUser(allowPublic=false) User user
//            , @PathParam("datasetKey") String datasetKey
//            , @PathParam("surveyId") int surveyId){
//        if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey)) {
//           throw new UnauthorisedException();
//        }
//        
//        return sampleMapper.selectSamplesBySurveyID(surveyId);
//    }
    
    @GET
    @Path("/taxondatasets/{datasetKey}/surveys/{surveyProviderKey}/samples")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sample> getSurveySamples(@TokenUser(allowPublic=false) User user
            , @PathParam("datasetKey") String datasetKey
            , @PathParam("surveyProviderKey") String providerKey){
        if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey)) {
           throw new UnauthorisedException();
        }
        
        return sampleMapper.selectSamplesBySurveyProviderKey(datasetKey, providerKey);
    }
        
    @GET
    @Path("/taxondatasets/{datasetKey}/surveys/{surveyProviderKey}/samples/{sampleProviderKey}")
    @Produces(MediaType.APPLICATION_JSON)
    public Sample getSurveySample(@TokenUser(allowPublic=false) User user
            , @PathParam("datasetKey") String datasetKey
            , @PathParam("surveyProviderKey") String surveyProviderKey
            , @PathParam("sampleProviderKey") String sampleProviderKey){
        if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey)) {
           throw new UnauthorisedException();
        }
        
        return sampleMapper.selectSampleBySampleProviderKey(datasetKey, surveyProviderKey, sampleProviderKey);
    }
    
//    @GET
//    @Path("/taxondatasets/{datasetKey}/surveys/{surveyProviderKey}/samples/{sampleProviderKey}/taxonObservations")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Sample getSampleTaxonObservations(@TokenUser(allowPublic=false) User user
//            , @PathParam("datasetKey") String datasetKey
//            , @PathParam("surveyProviderKey") String surveyProviderKey
//            , @PathParam("sampleProviderKey") String sampleProviderKey){
//        if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey)) {
//           throw new UnauthorisedException();
//        }
//        
//        return taxonObservationMapper.
//    }
 }
  


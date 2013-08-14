package uk.org.nbn.nbnv.api.rest.resources;

import java.util.ArrayList;
import java.util.Arrays;
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
import uk.org.nbn.nbnv.api.model.TaxonObservation;
import uk.org.nbn.nbnv.api.model.TaxonObservationAttributeValue;
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

    
    @GET
    @Path("/taxondatasets/{datasetKey}/surveys/{surveyKey}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Survey> getDatasetSurveys(@TokenUser(allowPublic=false) User user
            , @PathParam("datasetKey") String datasetKey
            , @PathParam("surveyKey") String surveyKey){
        if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey)) {
           throw new UnauthorisedException();
        }
        
        return surveyMapper.selectSurveyByProviderKeyAndDatasetKey(datasetKey, surveyKey);
    }
   
    
    @GET
    @Path("/taxondatasets/{datasetKey}/surveys/{surveyKey}/samples")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sample> getSurveySamples(@TokenUser(allowPublic=false) User user
            , @PathParam("datasetKey") String datasetKey
            , @PathParam("surveyKey") String surveyKey){
        if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey)) {
           throw new UnauthorisedException();
        }
        
        return sampleMapper.selectSamplesBySurveyProviderKey(datasetKey, surveyKey);
    }
        
    @GET
    @Path("/taxondatasets/{datasetKey}/surveys/{surveyKey}/samples/{sampleKey}")
    @Produces(MediaType.APPLICATION_JSON)
    public Sample getSurveySample(@TokenUser(allowPublic=false) User user
            , @PathParam("datasetKey") String datasetKey
            , @PathParam("surveyKey") String surveyKey
            , @PathParam("sampleKey") String sampleKey){
        if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey)) {
           throw new UnauthorisedException();
        }
        
        return sampleMapper.selectSampleBySampleProviderKey(datasetKey, surveyKey, sampleKey);
    }
    
    @GET
    @Path("/taxondatasets/{datasetKey}/surveys/{surveyKey}/samples/{sampleKey}/taxonObservations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonObservation> getSampleTaxonObservations(@TokenUser(allowPublic=false) User user
            , @PathParam("datasetKey") String datasetKey
            , @PathParam("surveyKey") String surveyKey
            , @PathParam("sampleKey") String sampleKey){
        if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey)) {
           throw new UnauthorisedException();
        }
        
        List<String> datasetKeyList = Arrays.asList(datasetKey);
        
        return taxonObservationMapper.selectObservationsByHierachy(datasetKeyList, surveyKey, sampleKey);
    }
    
    @GET
    @Path("/taxondatasets/{datasetKey}/surveys/{surveyKey}/samples/{sampleKey}/taxonObservations/{taxonObservationKey}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonObservation> getTaxonObservation(@TokenUser(allowPublic=false) User user
            , @PathParam("datasetKey") String datasetKey
            , @PathParam("surveyKey") String surveyKey
            , @PathParam("sampleKey") String sampleKey
            , @PathParam("observationKey") String taxonObservationKey){
        if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey)) {
           throw new UnauthorisedException();
        }
        
        List<String> datasetKeyList = Arrays.asList(datasetKey);
        
        return taxonObservationMapper.selectObservationsByHierachyAndKey(datasetKeyList, surveyKey, sampleKey, taxonObservationKey);
    }
    
    @GET
    @Path("/taxondatasets/{datasetKey}/surveys/{surveyKey}/samples/{sampleKey}/taxonObservations/{taxonObservationKey}/attributes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonObservationAttributeValue> getTaxonObservationAttributes(@TokenUser(allowPublic=false) User user
            , @PathParam("datasetKey") String datasetKey
            , @PathParam("surveyKey") String surveyKey
            , @PathParam("sampleKey") String sampleKey
            , @PathParam("taxonObservationKey") String taxonObservationKey){
        if (!datasetAdministratorMapper.isUserDatasetAdministrator(user.getId(), datasetKey)) {
           throw new UnauthorisedException();
        }
        
        List<String> datasetKeyList = Arrays.asList(datasetKey);
        
        return taxonObservationMapper.selectObservationAttributeByHierchy(datasetKeyList, surveyKey, sampleKey, taxonObservationKey);
    }
    
 }
  


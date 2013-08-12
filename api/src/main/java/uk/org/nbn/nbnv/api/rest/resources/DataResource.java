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
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.DatasetAdministrator;
import uk.org.nbn.nbnv.api.model.DatasetResolutionRecordCount;
import uk.org.nbn.nbnv.api.model.Survey;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.meta.DatasetAdminMembershipJSON;
import uk.org.nbn.nbnv.api.model.meta.OpResult;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetSurveyAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.solr.SolrResolver;

@Component
@Path("/data")
public class DataResource extends AbstractResource {
//    @Autowired OperationalDatasetMapper oDatasetMapper;
//    @Autowired DatasetAdministratorMapper datasetAdministratorMapper;
    @Autowired DatasetMapper datasetMapper;
//    @Autowired OperationalSurveyMapper oSurveyMapper;
    
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
    public List<TaxonDataset> getTaxonDatasetsForUser(@TokenUser(allowPublic=false) User user){
        return datasetMapper.getTaxonDatasetsForUser(user.getId());
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonDataset> getTaxonDatasetsForUserId(int userID){
        return datasetMapper.getTaxonDatasetsForUser(userID);
    }
  
}

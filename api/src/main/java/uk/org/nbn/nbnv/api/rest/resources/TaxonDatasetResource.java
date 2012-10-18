package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.AttributeMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.SurveyMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonMapper;
import uk.org.nbn.nbnv.api.model.*;

@Component
@Path("/taxonDatasets")
public class TaxonDatasetResource {
    
    @Autowired DatasetMapper datasetMapper;
    @Autowired TaxonMapper taxonMapper;
    @Autowired SurveyMapper surveyMapper;
    @Autowired AttributeMapper attributeMapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonDataset> getDatasetList(){
        return datasetMapper.selectAllTaxonDatasets();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonDataset getTaxonDatasetByID(@PathParam("id") String id){
        TaxonDataset toReturn = datasetMapper.selectTaxonDatasetByID(id);
        return toReturn;
    }
    
    @GET
    @Path("/{id}/taxa")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonWithDatasetStats> getTaxaByDatasetKey(@PathParam("id") String id){
        return taxonMapper.selectByDatasetKey(id);
    }
    
    @GET
    @Path("/{id}/recordsPerYear")
    @Produces(MediaType.APPLICATION_JSON)
    public List<YearStats> getRecordsPerYearByDatasetKey(@PathParam("id") String id){
        return datasetMapper.selectRecordsPerYear(id);
    }
    
    @GET
    @Path("/{id}/recordsPerDateType")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DateTypeStats> getDateTypeRecordCountsByDatasetKey(@PathParam("id") String id){
        return datasetMapper.selectRecordCountPerDateTypeByDatasetKey(id);
    }
    
    @GET
    @Path("/{id}/surveys")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Survey> getSurveysByDatasetKey(@PathParam("id") String id){
        return surveyMapper.selectSurveysByDatasetKey(id);
    }
    
    @GET
    @Path("/{id}/attributes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Attribute> getAttributesByDatasetKey(@PathParam("id") String id){
        return attributeMapper.selectAttributesByDatasetKey(id);
    }
    
}

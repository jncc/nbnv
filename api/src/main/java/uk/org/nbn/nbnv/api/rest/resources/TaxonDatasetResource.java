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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.core.OperationalAttributeMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.AttributeMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.SurveyMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonMapper;
import uk.org.nbn.nbnv.api.model.*;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenTaxonObservationAttributeAdminUser;

@Component
@Path("/taxonDatasets")
public class TaxonDatasetResource extends AbstractResource {

    @Autowired DatasetMapper datasetMapper;
    @Autowired TaxonMapper taxonMapper;
    @Autowired SurveyMapper surveyMapper;
    @Autowired AttributeMapper attributeMapper;
    @Autowired OperationalAttributeMapper oAttributeMapper;
    
    /**
     * Return a list of all Taxon Datasets from the data warehouse
     * 
     * @return A list of all Taxon Datasets from the data warehouse
     * 
     * @response.representation.200.qname List<TaxonDataset>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonDataset> getDatasetList(){
        return datasetMapper.selectAllTaxonDatasets();
    }
    
    /**
     * Return a specific Taxon Dataset from the data warehouse
     * 
     * @param id A Taxon Dataset ID
     * 
     * @return A specific Taxon Dataset from the data warehouse
     * 
     * @response.representation.200.qname TaxonDataset
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonDataset getTaxonDatasetByID(@PathParam("id") String id){
        TaxonDataset toReturn = datasetMapper.selectTaxonDatasetByID(id);
        return toReturn;
    }
    
    /**
     * Return a list of Dataset Stats of the Taxon within a Taxon Dataset from 
     * the data warehouse
     * 
     * @param id A Taxon Dataset ID
     * 
     * @return A list of Dataset Stats of the Taxon within a Taxon Dataset from 
     * the data warehouse
     * 
     * @response.representation.200.qname List<TaxonWithDatasetStats>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/taxa")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonWithDatasetStats> getTaxaByDatasetKey(@PathParam("id") String id){
        return taxonMapper.selectByDatasetKey(id);
    }
    
    /**
     * Return a list of yearly stats for specific dataset
     * 
     * @param id A dataset key
     * 
     * @return A list of yearly stats for specific dataset
     * 
     * @response.representation.200.qname JSONObject
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/recordsPerYear")
    @Produces(MediaType.APPLICATION_JSON)
    public List<YearStats> getRecordsPerYearByDatasetKey(@PathParam("id") String id){
        return datasetMapper.selectRecordsPerYear(id);
    }
    
    /**
     * Return a list of records per date for a specific dataset
     * 
     * @param id A dataset key
     * 
     * @return A list of records per date for a specific dataset
     * 
     * @response.representation.200.qname JSONObject
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/recordsPerDateType")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DateTypeStats> getDateTypeRecordCountsByDatasetKey(@PathParam("id") String id){
        return datasetMapper.selectRecordCountPerDateTypeByDatasetKey(id);
    }
    
    /**
     * Return a list of surveys in a dataset
     * 
     * @param id A dataset key
     * 
     * @return A list of surveys in a dataset
     * 
     * @response.representation.200.qname JSONObject
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/surveys")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Survey> getSurveysByDatasetKey(@PathParam("id") String id){
        return surveyMapper.selectSurveysByDatasetKey(id);
    }
    
    /**
     * Return a list of attributes in a specific dataset
     * 
     * @param id A dataset key
     * 
     * @return A list of attributes in a specific dataset
     * 
     * @response.representation.200.qname JSONObject
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/attributes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Attribute> getAttributesByDatasetKey(@PathParam("id") String id){
        return attributeMapper.selectAttributesByDatasetKey(id);
    }
    
    /**
     * Return a specific attribute if the user is the admin of the dataset that
     * contains this attribute (attributes in this case are related to Taxon
     * Observations)
     * 
     * @param user The current user (Must be an admin of this dataset)
     * @param id A dataset Key
     * @param attributeID An attribute that resides in this dataset
     * @return 
     */
    @GET
    @Path("/{id}/attributes/{attribute}")
    @Produces(MediaType.APPLICATION_JSON)
    public Attribute getAttributeByID(@TokenTaxonObservationAttributeAdminUser(dataset = "id", attribute = "attribute") User user, @PathParam("id") String id, @PathParam("attribute") int attributeID) {
        return oAttributeMapper.getDatasetAttribute(attributeID);
    }
    
    @POST
    @Path("/{id}/attributes/{attribute}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public int updateAttributeByID(@TokenTaxonObservationAttributeAdminUser(dataset = "id", attribute = "attribute") User user, 
            @PathParam("id") String id, 
            @PathParam("attribute") int attributeID,
            @FormParam("description") String description) {
        return oAttributeMapper.updateDatasetAttribute(attributeID, description);
    }
    
}
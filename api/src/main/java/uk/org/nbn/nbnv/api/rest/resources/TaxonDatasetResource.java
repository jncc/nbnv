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
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.codehaus.enunciate.jaxrs.TypeHint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.core.OperationalAttributeMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.AttributeMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.SurveyMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonMapper;
import uk.org.nbn.nbnv.api.model.*;
import uk.org.nbn.nbnv.api.model.meta.DatasetAccessPositionsJSON;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenTaxonObservationAttributeAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;

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
    @TypeHint(TaxonDataset.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of all Taxon Datasets")
    })
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
    @TypeHint(TaxonDataset.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the requested Taxon Dataset")
    })    
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
    @TypeHint(TaxonWithDatasetStats.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of all Taxon Dataset statistics")
    })   
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
    @TypeHint(YearStats.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of yearly statistics for a taxon dataset")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    public List<YearStats> getRecordsPerYearByDatasetKey(@PathParam("id") String id){
        return datasetMapper.selectRecordsPerYear(id);
    }
    
    /**
     * Return a list of statistics per date type for a specific dataset
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
    @TypeHint(DateTypeStats.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of statistics per date type for a taxon dataset")
    })  
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
    @TypeHint(Survey.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of surveys in a taxon dataset")
    })   
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
     * @response.representation.200.qname List<Attribute>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/attributes")
    @TypeHint(Attribute.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of attributes for a taxon dataset")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    public List<Attribute> getAttributesByDatasetKey(@PathParam("id") String id){
        return attributeMapper.selectAttributesByDatasetKey(id);
    }
    
    /**
     * Return a specific attribute if the user is the admin of the dataset that
     * contains this attribute (attributes in this case are related to Taxon
     * Observations)
     * 
     * @param user The current user (Must be an admin of this dataset) (Injected 
     * Token no need to pass)
     * @param id A dataset Key
     * @param attributeID An attribute that resides in this dataset
     * @return 
     * 
     * @response.representation.200.qname Attribute
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/attributes/{attribute}")
    @TypeHint(Attribute.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the requested attribute from a taxon dataset"),
        @ResponseCode(code = 403, condition = "The current user does not have admin rights over the dataset")
    })  
    @Produces(MediaType.APPLICATION_JSON)
    public Attribute getAttributeByID(@TokenTaxonObservationAttributeAdminUser(dataset = "id", attribute = "attribute") User user, @PathParam("id") String id, @PathParam("attribute") int attributeID) {
        return oAttributeMapper.getDatasetAttribute(attributeID);
    }
    
    /**
     * Update an attribute for a given dataset, if the user has permissions to
     * update this dataset
     * 
     * @param user The current user (Must be an admin of this dataset) (Injected 
     * Token no need to pass)
     * @param id The dataset key of the attribute you want to update
     * @param attributeID The attribute you wish to update
     * @param description The new description of that attribute
     * @return The success or failure of this action
     * 
     * @response.representation.200.qname int
     * @response.representation.200.mediaType application/json
     */
    @POST
    @Path("/{id}/attributes/{attribute}")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully updated attribute"),
        @ResponseCode(code = 403, condition = "The current user does not have admin rights over this attribute")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public int updateAttributeByID(@TokenTaxonObservationAttributeAdminUser(dataset = "id", attribute = "attribute") User user, 
            @PathParam("id") String id, 
            @PathParam("attribute") int attributeID,
            @FormParam("description") String description) {
        return oAttributeMapper.updateDatasetAttribute(attributeID, description);
    }
    
    /**
     * Returns a json object detailing the current users access permissions for
     * a given dataset key
     * 
     * @param user The current user (Injected Token no need to pass)
     * @param datasetKey The dataset you want your access positions for
     * @return The access positions the current user has over this dataset
     * 
     * @response.representation.200.qname DatasetAccessPositionsJSON
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{datasetKey}/accessPositions")
    @TypeHint(DatasetAccessPositionsJSON.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the current users dataset access positions for the selcted dataset")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public DatasetAccessPositionsJSON getAccessPositions(@TokenUser() User user, @PathParam("datasetKey") String datasetKey){
        DatasetAccessPositionsJSON ret = new DatasetAccessPositionsJSON();
        ret.setPublicAccess(datasetMapper.selectTaxonDatasetByID(datasetKey).getPublicResolution());
        ret.setEnhanced(datasetMapper.getDatasetAccessPositions(datasetKey, user.getId()));
        
        return ret;
    }
}
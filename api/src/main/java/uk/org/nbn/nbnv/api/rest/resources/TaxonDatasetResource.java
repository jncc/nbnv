package uk.org.nbn.nbnv.api.rest.resources;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.codehaus.enunciate.jaxrs.TypeHint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.core.OperationalAttributeMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.AttributeMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.SurveyMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonMapper;
import uk.org.nbn.nbnv.api.model.*;
import uk.org.nbn.nbnv.api.model.meta.DatasetAccessPositionsJSON;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenTaxonObservationAttributeAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.services.TaxonDatasetImporterService;
import uk.org.nbn.nbnv.api.utils.NXFReader;

@Component
@Path("/taxonDatasets")
public class TaxonDatasetResource extends AbstractResource {

    @Autowired DatasetMapper datasetMapper;
    @Autowired TaxonMapper taxonMapper;
    @Autowired SurveyMapper surveyMapper;
    @Autowired AttributeMapper attributeMapper;
    @Autowired DatasetAdministratorMapper datasetAdministratorMapper;
    @Autowired OperationalAttributeMapper oAttributeMapper;
    @Autowired TaxonDatasetImporterService importerService;
    
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
     * Returns a list of taxon datasets the user is administrator of
     * 
     * @param user The current user (Injected Token no need to pass)
     *
     * @return List of taxon datasets which the current user can administer
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
    public List<TaxonDataset> adminableDatasets(@TokenUser(allowPublic=false) User user) throws IOException {
        return datasetAdministratorMapper.selectTaxonDatasetsByUser(user.getId());
    }
    
    /**
     * Returns the import status information for the current users adminable
     * datasets which have some import status attributed to them. That is:
     *  - the dataset is queued for import
     *  - the dataset is being processed by the importer
     *  - the dataset has some import results
     * @param user The current user (Injected Token no need to pass)
     * @return a list of the import statuses for datasets this user can administrate
     * @throws IOException 
     */
    @GET
    @Path("/adminable/import")
    @TypeHint(Dataset.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of the current users adminable datasets")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonDatasetWithImportStatus> getImportStatusForAdminableDatasets(@TokenUser(allowPublic=false) User user) throws IOException {
        List<TaxonDatasetWithImportStatus> toReturn = new ArrayList<>();
        for(TaxonDataset dataset: adminableDatasets(user)) {
            DatasetImportStatus status = getImportStatus(user, dataset.getKey());
            if(status.isIsOnQueue() || status.isIsProcessing() || !status.getHistory().isEmpty()) {
                toReturn.add(new TaxonDatasetWithImportStatus(dataset, status));
            }
        }
        return toReturn;
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
     * Removes the dataset which is queued for import from the dataset importer
     * @param admin The Current User if they are a dataset admin for this 
     * @param id of the dataset to remove from the importer queue
     * @return an http response
     * @throws IOException if the system failed to remove the dataset from the queue
     */
    @DELETE
    @Path("/{id}/import")
    @StatusCodes({
        @ResponseCode(code = 204, condition = "Removed the dataset queued for import"),
        @ResponseCode(code = 404, condition = "Dataset was not queued")
    })
    public Response removeDatasetFromQueue(@TokenDatasetAdminUser(path="id") User admin, @PathParam("id") String id) throws IOException {
        if(importerService.removeFromQueue(id)) {
            return Response.noContent().build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    /**
     * The following takes an nbn exchange format stream and queues it up ready
     * to REPLACE the existing dataset.
     * @param admin The Current User if they are a dataset admin for this 
     * @param id of the dataset to replace
     * @param request containing a nbn exchange format file in the body
     * @return the status of the dataset if successful
     * @throws TemplateException 
     */
    @PUT
    @Path("/{id}/import")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Dataset queued for replacement"),
        @ResponseCode(code = 400, condition = "Failed to upload dataset"),
        @ResponseCode(code = 404, condition = "No dataset found"),
        @ResponseCode(code = 409, condition = "Already queued for import")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response queueReplacementDataset(@TokenDatasetAdminUser(path="id") User admin, @PathParam("id") String id, @Context HttpServletRequest request) throws TemplateException {
        return uploadDataset(admin, id, request, true);
    }
    
    /**
     * The following takes an nbn exchange format stream and queues it up ready
     * to APPEND to an existing dataset.
     * @param admin The Current User if they are a dataset admin for this 
     * @param id of the dataset to replace
     * @param request containing a nbn exchange format file in the body
     * @return the status of the dataset if successful
     * @throws TemplateException 
     */
    @POST
    @Path("/{id}/import")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Dataset queued for appending"),
        @ResponseCode(code = 400, condition = "Failed to upload dataset"),
        @ResponseCode(code = 404, condition = "No dataset found"),
        @ResponseCode(code = 409, condition = "Already queued for import")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response queueAppendDataset(@TokenDatasetAdminUser(path="id") User admin, @PathParam("id") String id, @Context HttpServletRequest request) throws TemplateException {
        return uploadDataset(admin, id, request, false);
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
    
    private Response uploadDataset(User admin, String id, HttpServletRequest request, boolean upsert) throws TemplateException {
        TaxonDataset dataset = datasetMapper.selectTaxonDatasetByID(id);
        if(dataset != null) {
            if(importerService.isQueued(id)) {
                return Response.status(CONFLICT)
                                .entity(createErrorResponse("This dataset already has an import queued. You will have to delete this first"))
                                .build();
            }
            
            try {
                NXFReader nxf = new NXFReader(new LineNumberReader(new InputStreamReader(request.getInputStream())));
                importerService.importDataset(nxf, dataset, upsert);
                return Response.ok(getImportStatus(admin,id)).build();
            }
            catch(IOException io) {
                return Response.status(BAD_REQUEST)
                        .entity(createErrorResponse(io.getMessage())).build();
            }
        }
        else {
            return Response.status(NOT_FOUND).build();
        }
    }
    
    private Map<String,Object> createErrorResponse(String message) {
        Map<String,Object> toReturn = new HashMap<>();
        toReturn.put("success", false);
        toReturn.put("status", message);
        return toReturn;
    }
}
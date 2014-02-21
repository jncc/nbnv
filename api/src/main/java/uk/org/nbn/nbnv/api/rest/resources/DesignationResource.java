package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.codehaus.enunciate.jaxrs.TypeHint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DesignationCategoryMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DesignationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonNavigationGroupMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.Designation;
import uk.org.nbn.nbnv.api.model.DesignationCategory;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonNavigationGroup;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.solr.SolrResolver;

@Component
@Path("/designations")
public class DesignationResource extends AbstractResource {
    @Autowired DesignationCategoryMapper designationCategoryMapper;
    @Autowired DesignationMapper designationMapper;
    @Autowired TaxonMapper taxonMapper;
    @Autowired DatasetMapper datasetMapper;
    @Autowired TaxonNavigationGroupMapper taxonNavigationGroupMapper;

    /**
     * Return a list of all Designations from the data warehouse
     * 
     * @return A list of all Designations from the data warehouse
     * 
     * @response.representation.200.qname List<Designation>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @TypeHint(Designation.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of all designations")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<Designation> getDesignationList() {
        return designationMapper.selectAll();
    }

    /**
     * Returns a specific designation from the data warehouse
     * 
     * @param id A Designation ID
     * 
     * @return A specific designation from the data warehouse
     * 
     * @response.representation.200.qname Designation
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}")
    @TypeHint(Designation.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully returned selected designation"),
        @ResponseCode(code = 204, condition = "Could not find the requested designation")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    @SolrResolver("DESIGNATION")
    public Designation getDesignation(@PathParam("id") String id) {
        return designationMapper.selectByID(id); 
    }

    /**
     * Returns all associated designation categories associated with a specified 
     * designation
     * 
     * @param id A Designation ID
     * 
     * @return All associated designation categories associated with a specified 
     * designation
     * 
     * @response.representation.200.qname DesignationCategory
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/designationCategories")
    @TypeHint(DesignationCategory.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of designation categories that this designation belongs to"),
        @ResponseCode(code = 204, condition = "Could not find the requested designation")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public DesignationCategory getDesignationCategory(@PathParam("id") String id) {
        return designationCategoryMapper.selectByDesignationID(id);
    }
    
    /**
     * Returns a list of datasets in a specified designation which are viewable
     * by the current user
     * 
     * @param user The current user
     * @param designation A Designation ID
     * 
     * @return A list of datasets in a specified designation which are viewable
     * by the current user
     * 
     * @response.representation.200.qname List<Dataset>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/datasets")
    @TypeHint(Designation.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of datasets that contain records in this designation"),
        @ResponseCode(code = 204, condition = "Could not find the requested designation")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dataset> getDesignationDatasets(@TokenUser User user, @PathParam("id") String designation) {
        return datasetMapper.selectDatasetsInDesignationViewableByUser(user, designation);
    }
    
    /**
     * Returns a list of Taxons specified by designation and a taxon navigation
     * group (or just by a designation id if no taxon navigation group id is 
     * supplied)
     * 
     * @param id A Designation ID
     * @param taxonNavigationGroupId A Taxon Navigation Group ID (Optional) 
     * supplied via querystring
     * 
     * @return A list of Taxons specified by designation and a taxon navigation
     * group (or just by a designation id if no taxon navigation group id is 
     * supplied)
     * 
     * @response.representation.200.qname List<Taxon>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/species")
    @TypeHint(Taxon.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of taxa that this designation contains"),
        @ResponseCode(code = 204, condition = "Could not find the requested designation")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<Taxon> getSpeciesByDesignationAndTaxonNavigationGroup(@PathParam("id") String id, @QueryParam("taxonNavigationGroupId") String taxonNavigationGroupId) {
        if(taxonNavigationGroupId != null){
            return taxonMapper.selectByDesignationAndTaxonNavigationGroup(id, taxonNavigationGroupId);
        }else{
            return taxonMapper.selectByDesignationID(id);
        }
    }

    /**
     * Returns a Taxon Navigation Group specified by a designation and taxon 
     * navigation group id
     * 
     * @param id A Designation ID
     * @param taxonNavigationGroupId A Taxon Navigation Group ID
     * 
     * @return A Taxon Navigation Group specified by a designation and taxon 
     * navigation group ID
     * 
     * @response.representation.200.qname TaxonNavigationGroup
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("{id}/taxonNavigationGroups/{taxonNavigationGroupId}")
    @TypeHint(TaxonNavigationGroup.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a taxon navigation group within this designation"),
        @ResponseCode(code = 204, condition = "Could not find the requested designation or taxon navigation group id")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonNavigationGroup getTaxonNavigationGroupByDesignation(@PathParam("id") String id, @PathParam("taxonNavigationGroupId") String taxonNavigationGroupId){
        TaxonNavigationGroup toReturn = taxonNavigationGroupMapper.getTaxonNavigationGroup(taxonNavigationGroupId);
        toReturn.setChildren(taxonNavigationGroupMapper.getChildrenByDesignation(taxonNavigationGroupId, id));
        return toReturn;
    }
}

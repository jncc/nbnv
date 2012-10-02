package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.mappers.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.mappers.DesignationCategoryMapper;
import uk.org.nbn.nbnv.api.dao.mappers.DesignationMapper;
import uk.org.nbn.nbnv.api.dao.mappers.TaxonMapper;
import uk.org.nbn.nbnv.api.dao.mappers.TaxonNavigationGroupMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.Designation;
import uk.org.nbn.nbnv.api.model.DesignationCategory;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonNavigationGroup;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;

@Component
@Path("/designations")
public class DesignationResource {
    @Autowired DesignationCategoryMapper designationCategoryMapper;
    @Autowired DesignationMapper designationMapper;
    @Autowired TaxonMapper taxonMapper;
    @Autowired DatasetMapper datasetMapper;
    @Autowired TaxonNavigationGroupMapper taxonNavigationGroupMapper;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Designation> getDesignationList() {
        return designationMapper.selectAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Designation getDesignation(@PathParam("id") String id) {
        return designationMapper.selectByID(id); 
    }

    @GET
    @Path("/{id}/designationCategories")
    @Produces(MediaType.APPLICATION_JSON)
    public DesignationCategory getDesignationCategory(@PathParam("id") String id) {
        return designationCategoryMapper.selectByDesignationID(id);
    }
    
    @GET
    @Path("/{id}/datasets")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dataset> getDesignationDatasets(@TokenUser User user, @PathParam("id") String designation) {
        return datasetMapper.selectDatasetsInDesignationViewableByUser(user, designation);
    }
    
    @GET
    @Path("/{id}/species")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Taxon> getSpeciesByDesignationAndTaxonNavigationGroup(@PathParam("id") String id, @QueryParam("taxonNavigationGroupId") String taxonNavigationGroupId) {
        if(taxonNavigationGroupId != null){
            return taxonMapper.selectByDesignationAndTaxonNavigationGroup(id, taxonNavigationGroupId);
        }else{
            return taxonMapper.selectByDesignationID(id);
        }
    }

    @GET
    @Path("{id}/taxonNavigationGroups/{taxonNavigationGroupId}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonNavigationGroup getTaxonNavigationGroupByDesignation(@PathParam("id") String id, @PathParam("taxonNavigationGroupId") String taxonNavigationGroupId){
        TaxonNavigationGroup toReturn = taxonNavigationGroupMapper.getTaxonNavigationGroup(taxonNavigationGroupId);
        toReturn.setChildren(taxonNavigationGroupMapper.getChildrenByDesignation(taxonNavigationGroupId, id));
        return toReturn;
    }

}

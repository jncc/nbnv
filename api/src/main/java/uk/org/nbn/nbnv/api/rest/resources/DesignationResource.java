package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.mappers.DesignationCategoryMapper;
import uk.org.nbn.nbnv.api.dao.mappers.DesignationMapper;
import uk.org.nbn.nbnv.api.dao.mappers.TaxonMapper;
import uk.org.nbn.nbnv.api.dao.mappers.TaxonNavigationGroupMapper;
import uk.org.nbn.nbnv.api.model.Designation;
import uk.org.nbn.nbnv.api.model.DesignationCategory;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonNavigationGroup;

@Component
@Path("/designations")
public class DesignationResource {
    @Autowired DesignationCategoryMapper designationCategoryMapper;
    @Autowired DesignationMapper designationMapper;
    @Autowired TaxonMapper taxonMapper;
    @Autowired TaxonNavigationGroupMapper taxonNavigationGroupMapper;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Designation> getDesignationList() {
        return designationMapper.selectAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Designation getDesignation(@PathParam("id") int id) {
        return designationMapper.selectByID(id); 
    }

    @GET
    @Path("/{id}/designationCategories")
    @Produces(MediaType.APPLICATION_JSON)
    public DesignationCategory getDesignationCategory(@PathParam("id") int id) {
        return designationCategoryMapper.selectByDesignationID(id);
    }
    @GET
    @Path("/{id}/species")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Taxon> getSpeciesByDesignationAndTaxonNavigationGroup(@PathParam("id") int id, @QueryParam("taxonNavigationGroupId") String taxonNavigationGroupId) {
        if(taxonNavigationGroupId != null){
            return taxonMapper.selectByDesignationAndTaxonNavigationGroup(id, taxonNavigationGroupId);
        }else{
            return taxonMapper.selectByDesignationID(id);
        }
    }

    @GET
    @Path("{id}/taxonNavigationGroups/{taxonNavigationGroupId}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonNavigationGroup getTaxonNavigationGroupByDesignation(@PathParam("id") int id, @PathParam("taxonNavigationGroupId") String taxonNavigationGroupId){
        TaxonNavigationGroup toReturn = taxonNavigationGroupMapper.getTaxonNavigationGroup(taxonNavigationGroupId);
        toReturn.setChildren(taxonNavigationGroupMapper.getChildrenByDesignation(taxonNavigationGroupId, id));
        return toReturn;
    }

}

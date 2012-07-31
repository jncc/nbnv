/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.designation.DesignationCategoryMapper;
import uk.org.nbn.nbnv.api.dao.designation.DesignationMapper;
import uk.org.nbn.nbnv.api.dao.designation.TaxonMapper;
import uk.org.nbn.nbnv.api.dao.taxonGroup.TaxonGroupMapper;
import uk.org.nbn.nbnv.api.model.Designation;
import uk.org.nbn.nbnv.api.model.DesignationCategory;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonGroup;

/**
 *
 * @author Administrator
 */
@Component
@Path("/designations")
public class DesignationResource {
    @Autowired DesignationCategoryMapper designationCategoryMapper;
    @Autowired DesignationMapper designationMapper;
    @Autowired TaxonMapper taxonMapper;
    @Autowired TaxonGroupMapper taxonGroupMapper;
	
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
    @Path("/{id}/designationCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public DesignationCategory getDesignationCategory(@PathParam("id") int id) {
        return designationCategoryMapper.selectByDesignationID(id);
    }

    @GET
    @Path("/{id}/taxa")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Taxon> getSpeciesByDesignation(@PathParam("id") int id) {
            return taxonMapper.selectByDesignationID(id);
    }

    @GET
    @Path("/{id}/topLevelTaxonNavigationGroups")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonGroup> getTopLevelTaxonNavigationGroupsByDesignation(@PathParam("id") int id) {
            return taxonGroupMapper.selectTopLevelTaxonNavigationGroupsByDesignationID(id);
    }
    
    @GET
    @Path("{designationId}/taxonGroup/{taxonGroupId}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonGroup getTaxonGroupByDesignation(@PathParam("designationId") int designationId, @PathParam("taxonGroupId") String taxonGroupId){
        TaxonGroup toReturn = taxonGroupMapper.getTaxonGroup(taxonGroupId);
        toReturn.setChildren(taxonGroupMapper.getChildrenByDesignation(taxonGroupId, designationId));
        return toReturn;
    }

}

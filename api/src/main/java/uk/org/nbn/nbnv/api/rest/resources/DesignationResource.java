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
import uk.org.nbn.nbnv.api.dao.designation.TaxonGroupMapper;
import uk.org.nbn.nbnv.api.dao.designation.TaxonMapper;
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
    @Autowired DesignationCategoryMapper desigCat;
    @Autowired DesignationMapper mapper;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Designation> getDesignationList() {
        return mapper.selectAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Designation getDesignation(@PathParam("id") int id) {
        return mapper.selectByID(id); 
    }

    @GET
    @Path("/{id}/designationCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public DesignationCategory getDesignationCategory(@PathParam("id") int id) {
        return desigCat.selectByDesignationID(id);
    }

    @GET
    @Path("/{id}/taxa")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Taxon> getSpeciesByDesignation(@PathParam("id") int id) {
        SqlSessionFactory fact = MyBatisConnectionFactory.getFactory();
        SqlSession session = fact.openSession();
        try{
            TaxonMapper taxonMapper = session.getMapper(TaxonMapper.class);
            return taxonMapper.selectByDesignationID(id);
        } finally {
            session.close();
        }
    }

    @GET
    @Path("/{id}/topLevelTaxonNavigationGroups")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonGroup> getTopLevelTaxonNavigationGroupsByDesignation(@PathParam("id") int id) {
        SqlSessionFactory fact = MyBatisConnectionFactory.getFactory();
        SqlSession session = fact.openSession();
        try{
            TaxonGroupMapper taxonCategoryMapper = session.getMapper(TaxonGroupMapper.class);
            return taxonCategoryMapper.selectTopLevelTaxonNavigationGroupsByDesignationID(id);
        } finally {
            session.close();
        }
    }

    @GET
    @Path("/{designationId}/childTaxonNavigationGroups/{parentGroupId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonGroup> getChildGroupsByParentGroupAndDesignation(@PathParam("designationId") int designationId, @PathParam("parentGroupId") String parentGroupId) {
        SqlSessionFactory fact = MyBatisConnectionFactory.getFactory();
        SqlSession session = fact.openSession();
        try{
            TaxonGroupMapper taxonCategoryMapper = session.getMapper(TaxonGroupMapper.class);
            return taxonCategoryMapper.selectChildTaxonNavigationGroupsByDesignationID(designationId, parentGroupId);
        } finally {
            session.close();
        }
    }

}

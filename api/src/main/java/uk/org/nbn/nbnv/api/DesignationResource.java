/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import uk.org.nbn.nbnv.api.dao.MyBatisConnectionFactory;
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
@Path("/designations")
public class DesignationResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Designation> getDesignationList() {
        SqlSessionFactory fact = MyBatisConnectionFactory.getFactory();
        SqlSession session = fact.openSession();
        
        try {
            DesignationMapper m = session.getMapper(DesignationMapper.class);
            return m.selectAll();
        } finally {
            session.close();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Designation getDesignation(@PathParam("id") int id) {
        SqlSessionFactory fact = MyBatisConnectionFactory.getFactory();
        SqlSession session = fact.openSession();
        
        try {
            DesignationMapper m = session.getMapper(DesignationMapper.class);
            return m.selectByID(id);
        } finally {
            session.close();
        }
    }

    @GET
    @Path("/{id}/designationCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public DesignationCategory getDesignationCategory(@PathParam("id") int id) {
        SqlSessionFactory fact = MyBatisConnectionFactory.getFactory();
        SqlSession session = fact.openSession();
        
        try {
            DesignationCategoryMapper m = session.getMapper(DesignationCategoryMapper.class);
            return m.selectByDesignationID(id);
        } finally {
            session.close();
        }
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

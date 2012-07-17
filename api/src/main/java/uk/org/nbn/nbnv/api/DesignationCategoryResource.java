/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import uk.org.nbn.nbnv.api.dao.MyBatisConnectionFactory;
import uk.org.nbn.nbnv.api.dao.designation.DesignationCategoryMapper;
import uk.org.nbn.nbnv.api.dao.designation.DesignationMapper;
import uk.org.nbn.nbnv.api.model.Designation;
import uk.org.nbn.nbnv.api.model.DesignationCategory;

/**
 *
 * @author Administrator
 */
@Path("/designationCategories")
public class DesignationCategoryResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DesignationCategory> getDesignationCategory() { 
        SqlSessionFactory fact = MyBatisConnectionFactory.getFactory();
        SqlSession session = fact.openSession();
        
        try {
            DesignationCategoryMapper m = session.getMapper(DesignationCategoryMapper.class);
            return m.selectAll();
        } finally {
            session.close();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DesignationCategory getDesignationCategoryByID(@PathParam("id") int id) { 
        SqlSessionFactory fact = MyBatisConnectionFactory.getFactory();
        SqlSession session = fact.openSession();
        
        try {
            DesignationCategoryMapper m = session.getMapper(DesignationCategoryMapper.class);
            return m.selectByID(id);
        } finally {
            session.close();
        }
    }

    @GET
    @Path("/{id}/designations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Designation> getDesignationByCategoryID(@PathParam("id") int id) { 
        SqlSessionFactory fact = MyBatisConnectionFactory.getFactory();
        SqlSession session = fact.openSession();
        
        try {
            DesignationMapper m = session.getMapper(DesignationMapper.class);
            return m.selectByCategoryID(id);
        } finally {
            session.close();
        }
    }

    @GET
    @Path("/{id}/designations/{desigID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Designation getDesignationByCategoryIDAndID(@PathParam("id") int id, @PathParam("desigID") int desigID) { 
        SqlSessionFactory fact = MyBatisConnectionFactory.getFactory();
        SqlSession session = fact.openSession();
        
        try {
            DesignationMapper m = session.getMapper(DesignationMapper.class);
            return m.selectByIDAndCategoryID(desigID, id);
        } finally {
            session.close();
        }
    }
}

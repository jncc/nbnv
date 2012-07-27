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
import uk.org.nbn.nbnv.api.model.Designation;
import uk.org.nbn.nbnv.api.model.DesignationCategory;

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

/*    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Designation> getDesignationListByCategoryID(@QueryParam("category") int categoryID) {
        SqlSessionFactory fact = MyBatisConnectionFactory.getFactory();
        SqlSession session = fact.openSession();
        
        try {
            DesignationMapper m = session.getMapper(DesignationMapper.class);
            return m.selectByCategoryID(categoryID);
        } finally {
            session.close();
        }
    }*/
}

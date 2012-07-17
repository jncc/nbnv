/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import uk.org.nbn.nbnv.api.dao.MyBatisConnectionFactory;
import uk.org.nbn.nbnv.api.dao.designation.DesignationMapper;
import uk.org.nbn.nbnv.api.model.Designation;

/**
 *
 * @author Administrator
 */
@Path("/designation/{id}")
public class DesignationResource {
    @GET
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
}

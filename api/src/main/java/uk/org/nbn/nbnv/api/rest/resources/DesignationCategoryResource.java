package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.mappers.DesignationCategoryMapper;
import uk.org.nbn.nbnv.api.dao.mappers.DesignationMapper;
import uk.org.nbn.nbnv.api.model.Designation;
import uk.org.nbn.nbnv.api.model.DesignationCategory;

@Component
@Path("/designationCategories")
public class DesignationCategoryResource {
    @Autowired DesignationCategoryMapper desigCat;
    @Autowired DesignationMapper desig;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DesignationCategory> getDesignationCategory() { 
        return desigCat.selectAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DesignationCategory getDesignationCategoryByID(@PathParam("id") int id) { 
        return desigCat.selectByID(id);
    }

    @GET
    @Path("/{id}/designations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Designation> getDesignationByCategoryID(@PathParam("id") int id) { 
        return desig.selectByCategoryID(id);
    }

    @GET
    @Path("/{id}/designations/{designationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Designation getDesignationByCategoryIDAndID(@PathParam("id") int id, @PathParam("designationId") String designationId) { 
        return desig.selectByIDAndCategoryID(designationId, id);
    }
}

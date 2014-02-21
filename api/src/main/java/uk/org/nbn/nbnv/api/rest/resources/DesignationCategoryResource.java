package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.codehaus.enunciate.jaxrs.TypeHint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.DesignationCategoryMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DesignationMapper;
import uk.org.nbn.nbnv.api.model.Designation;
import uk.org.nbn.nbnv.api.model.DesignationCategory;

@Component
@Path("/designationCategories")
public class DesignationCategoryResource extends AbstractResource {
    @Autowired DesignationCategoryMapper desigCat;
    @Autowired DesignationMapper desig;
    
    /**
     * Returns all DesignationCategory items from the data warehouse
     * 
     * @return All DesignationCategory items from the data warehouse
     * 
     * @response.representation.200.qname List<DesignationCategory>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @TypeHint(DesignationCategory.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesdfully returned a list of all designation categories")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<DesignationCategory> getDesignationCategory() { 
        return desigCat.selectAll();
    }

    /**
     * Returns a specified designation category from the data warehouse
     * 
     * @param id ID of a designation category
     * 
     * @return A specific designation category from the data warehouse
     * 
     * @response.representation.200.qname DesignationCategory
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}")
    @TypeHint(DesignationCategory.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the selected designation category"),
        @ResponseCode(code = 204, condition = "Could not find the requested designation category")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public DesignationCategory getDesignationCategoryByID(@PathParam("id") int id) { 
        return desigCat.selectByID(id);
    }

    /**
     * Returns a list of Designations associated with a specified category
     * 
     * @param id ID of a designation category
     * 
     * @return A list of Designations associated with a specified category
     * 
     * @response.representation.200.qname List<Designation>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/designations")
    @TypeHint(Designation.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned all designations in the selected designation category"),
        @ResponseCode(code = 204, condition = "Could not find the requested designation category")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<Designation> getDesignationByCategoryID(@PathParam("id") int id) { 
        return desig.selectByCategoryID(id);
    }

    /**
     * Returns a Designation associated with a specified category
     * 
     * @param id ID of a designation category
     * @param designationId ID of a designation
     * 
     * @return A Designation associated with a specified category
     * 
     * @response.representation.200.qname Designation
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/designations/{designationId}")
    @TypeHint(Designation.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully returned selected designation in the selected designation category"),
        @ResponseCode(code = 204, condition = "Could not find the requested designation in this designation category")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Designation getDesignationByCategoryIDAndID(@PathParam("id") int id, @PathParam("designationId") String designationId) { 
        return desig.selectByIDAndCategoryID(designationId, id);
    }
}

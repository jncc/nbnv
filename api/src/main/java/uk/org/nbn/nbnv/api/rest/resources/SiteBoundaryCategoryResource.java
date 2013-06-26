package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryCategoryMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryDatasetMapper;
import uk.org.nbn.nbnv.api.model.SiteBoundaryCategory;

@Component
@Path("/siteBoundaryCategories")
public class SiteBoundaryCategoryResource extends AbstractResource {
    
    @Autowired SiteBoundaryCategoryMapper siteBoundaryCategoryMapper;
    
    /**
     * Return a list of all site boundary categories from the data warehouse
     * 
     * @return A list of all site boundary categories from the data warehouse
     * 
     * @response.representation.200.qname List<SiteBoundaryCategory>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SiteBoundaryCategory> get(){
        List<SiteBoundaryCategory> toReturn = siteBoundaryCategoryMapper.get();
        return toReturn;
    }
    
    /**
     * Return a specific site boundary category
     * 
     * @param id A Site Boundary Category ID
     * 
     * @return A specific site boundary category
     * 
     * @response.representation.200.qname SiteBoundaryCategory
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SiteBoundaryCategory getByID(@PathParam("id") int id){
        return siteBoundaryCategoryMapper.getByID(id);
    }
    
}

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
public class SiteBoundaryCategoryResource {
    
    @Autowired SiteBoundaryCategoryMapper siteBoundaryCategoryMapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SiteBoundaryCategory> get(){
        List<SiteBoundaryCategory> toReturn = siteBoundaryCategoryMapper.get();
        return toReturn;
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SiteBoundaryCategory getByID(@PathParam("id") int id){
        return siteBoundaryCategoryMapper.getByID(id);
    }
    
}

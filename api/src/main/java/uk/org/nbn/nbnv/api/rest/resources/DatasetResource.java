package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.model.Dataset;

@Component
@Path("/datasets")
public class DatasetResource {
    
    @Autowired DatasetMapper datasetMapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dataset> getDatasetList(){
        return datasetMapper.selectAll();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Dataset getDatasetByID(@PathParam("id") String id){
        return datasetMapper.selectByDatasetKey(id);
    }
    
}

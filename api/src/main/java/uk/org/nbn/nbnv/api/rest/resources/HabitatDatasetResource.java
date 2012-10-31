package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.HabitatDatasetMapper;
import uk.org.nbn.nbnv.api.model.HabitatDataset;

@Component
@Path("/habitatDatasets")
public class HabitatDatasetResource {
    
    @Autowired HabitatDatasetMapper habitatDatasetMapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<HabitatDataset> get(){
        return habitatDatasetMapper.get();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public HabitatDataset getByDatasetKey(@PathParam("id") String id){
        return habitatDatasetMapper.getByDatasetKey(id);
    }
}

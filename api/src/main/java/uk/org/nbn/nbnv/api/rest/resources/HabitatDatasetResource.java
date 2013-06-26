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
public class HabitatDatasetResource extends AbstractResource {
    
    @Autowired HabitatDatasetMapper habitatDatasetMapper;
    
    /**
     * Returns a list of all habitat datasets from the data warehouse
     * 
     * @return A list of all habitat datasets from the data warehouse
     * 
     * @response.representation.200.qname List<HabitatDataset>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<HabitatDataset> get(){
        return habitatDatasetMapper.get();
    }
    
    /**
     * Returns a specific habitat dataset from the data warehouse
     * 
     * @param id A Habitat Dataset Key
     * 
     * @return A specific habitat dataset from the data warehouse
     * 
     * @response.representation.200.qname HabitatDataset
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public HabitatDataset getByDatasetKey(@PathParam("id") String id){
        return habitatDatasetMapper.getByDatasetKey(id);
    }
}

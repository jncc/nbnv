package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.ContextLayerMapper;
import uk.org.nbn.nbnv.api.model.ContextLayer;

@Component
@Path("/contextLayers")
public class ContextLayerResource {
    
    @Autowired ContextLayerMapper contextLayerMapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContextLayer> getDatasetList(){
        return contextLayerMapper.getAllContextLayers();
    }
}

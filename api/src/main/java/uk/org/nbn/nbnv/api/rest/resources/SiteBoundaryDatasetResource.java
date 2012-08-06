package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.mappers.SiteBoundaryDatasetMapper;
import uk.org.nbn.nbnv.api.model.SiteBoundaryDataset;

@Component
@Path("/siteDatasets")
public class SiteBoundaryDatasetResource {
    
    @Autowired SiteBoundaryDatasetMapper mapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SiteBoundaryDataset> get(){
        return mapper.get();
    }
    
}

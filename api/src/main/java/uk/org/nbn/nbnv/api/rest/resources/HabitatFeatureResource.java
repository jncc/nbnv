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
import uk.org.nbn.nbnv.api.dao.warehouse.HabitatFeatureMapper;
import uk.org.nbn.nbnv.api.model.HabitatDataset;
import uk.org.nbn.nbnv.api.model.HabitatFeature;

@Component
@Path("/habitatFeatures")
public class HabitatFeatureResource extends AbstractResource {

    @Autowired HabitatFeatureMapper habitatFeatureMapper;

    /**
     * Returns a specific habitat feature from the data warehouse
     * 
     * @param identifier A habitat feature identifier
     * 
     * @return A specific habitat feature from the data warehouse
     * 
     * @response.representation.200.qname HabitatFeature
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{identifier}")
    @Produces(MediaType.APPLICATION_JSON)
    public HabitatFeature getByIdentifier(@PathParam("identifier") String identifier){
        return habitatFeatureMapper.getByIdentifier(identifier);
    }


}

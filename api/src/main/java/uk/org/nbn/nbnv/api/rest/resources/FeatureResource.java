package uk.org.nbn.nbnv.api.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.FeatureMapper;
import uk.org.nbn.nbnv.api.model.Feature;

/**
 * A jersey resource which provides bounding boxes for certain features on the 
 * gateway
 * @author Christopher Johnson
 */
@Component
@Path("/features")
public class FeatureResource {
    @Autowired FeatureMapper featureMapper;
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Feature getFeature(@PathParam("id") int featureId) {
        return featureMapper.getFeature(featureId);
    }
}

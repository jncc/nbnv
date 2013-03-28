package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.FeatureMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryMapper;
import uk.org.nbn.nbnv.api.model.Feature;
import uk.org.nbn.nbnv.api.model.SiteBoundary;
import uk.org.nbn.nbnv.api.solr.SolrResolver;

/**
 * A jersey resource which provides bounding boxes for certain features on the 
 * gateway
 * @author Christopher Johnson
 */
@Component
@Path("/features")
public class FeatureResource extends AbstractResource {
    @Autowired FeatureMapper featureMapper;
    @Autowired SiteBoundaryMapper siteBoundaryMapper;
    
    @GET
    @Path("/{id: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Feature getFeatureId(@PathParam("id") int featureId) {
        return featureMapper.getFeatureID(featureId);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @SolrResolver("FEATURE")
    public Feature getFeature(@PathParam("id") String featureId) {
        return featureMapper.getFeature(featureId);
    }
    
    @GET
    @Path("/siteBoundaries")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SiteBoundary> getSiteBoundaries() {
        return siteBoundaryMapper.getAll();
    }
}

package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.codehaus.enunciate.jaxrs.TypeHint;
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
    
    /**
     * Return a specified feature with a numerical ID
     * 
     * @param featureId A Numerical Feature ID
     * 
     * @return A specified feature with a numerical ID
     * 
     * @response.representation.200.qname Feature
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id: [0-9]+}")
    @TypeHint(Feature.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the requested feature"),
        @ResponseCode(code = 204, condition = "Could not find the requested feature")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Feature getFeatureId(@PathParam("id") int featureId) {
        return featureMapper.getFeatureID(featureId);
    }

    /**
     * Return a specified feature with a string feature id
     * 
     * @param featureId A String Based Feature ID
     * 
     * @return A specified feature with a string feature id
     * 
     * @response.representation.200.qname Feature
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}")
    @TypeHint(Feature.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the requested feature"),
        @ResponseCode(code = 204, condition = "Could not find the requested feature")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @SolrResolver("FEATURE")
    public Feature getFeature(@PathParam("id") String featureId) {
        return featureMapper.getFeature(featureId);
    }
    
    /**
     * Return a list of all Site Boundaries from the data warehouse 
     * 
     * @return A list of all Site Boundaries from the data warehouse
     * 
     * @response.representation.200.qname List<SiteBoundary>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/siteBoundaries")
    @TypeHint(Feature.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the list of all site boundaries")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<SiteBoundary> getSiteBoundaries() {
        return siteBoundaryMapper.getAll();
    }
}

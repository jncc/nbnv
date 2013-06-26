package uk.org.nbn.nbnv.api.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
@Path("/statistics")
public class StatisticsResource extends AbstractResource {
    
    /**
     * Return a set of statistics about the current status of the NBN records
     * 
     * TODO populate this from the database
     * 
     * @return A set of statistics about the current status of the NBN records
     * 
     * @throws JSONException 
     * 
     * @response.representation.200.qname JSONObject
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject getDatasetList() throws JSONException {
        //TODO populate this from the database
        return new JSONObject()
                .put("Datasets", 799)
                .put("Species records", 80682782)
                .put("Updated Dictionary", "2nd May 2012");
    }
}

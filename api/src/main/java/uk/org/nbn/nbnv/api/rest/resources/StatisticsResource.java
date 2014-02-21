package uk.org.nbn.nbnv.api.rest.resources;

import java.util.Properties;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.StatisticsMapper;

@Component
@Path("/statistics")
public class StatisticsResource extends AbstractResource {
    
    @Autowired StatisticsMapper statisticsMapper;
    @Autowired Properties properties;
    
    /**
     * Return a set of statistics about the current status of the NBN records
     * 
     * @return A set of statistics about the current status of the NBN records
     * 
     * @throws JSONException 
     * 
     * @response.representation.200.qname JSONObject
     * @response.representation.200.mediaType application/json
     */
    @GET
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully returned statistics for site")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject getDatasetList() throws JSONException {
        //TODO populate this from the database
        return new JSONObject()
                .put("Datasets", statisticsMapper.getRowCount("TaxonDatasetData"))
                .put("Species records", statisticsMapper.getRowCount("TaxonObservationDataEnhanced"))
                .put("Updated Dictionary", properties.get("species_dictionary_updated"));
    }
}

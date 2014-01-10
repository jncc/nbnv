/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.core.OperationalApiObservationViewMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.ApiObservationViewMapper;
import uk.org.nbn.nbnv.api.model.ApiObservationViewStatistic;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;

/**
 * @author Matt Debont
 */
@Component
@Path("/apiViews")
public class ApiViewResource extends AbstractResource {
    
    @Autowired ApiObservationViewMapper apiObservationViewMapper;
    @Autowired OperationalApiObservationViewMapper oApiObservationViewMapper;
    
    @GET
    @Path("/{datasetKey : [A-Z][A-Z0-9]{7}}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ApiObservationViewStatistic> getApiObserationViewStatisticsByDataset (
            @TokenDatasetAdminUser(path = "datasetKey") User user, 
            @PathParam("datasetKey") String datasetKey,
            @QueryParam("startDate") @DefaultValue("") String startDate,
            @QueryParam("endDate") @DefaultValue("") String endDate) {
        return apiObservationViewMapper.getApiObservationViewStatisticsForDataset(datasetKey, startDate, endDate);
    }
}

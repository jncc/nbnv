/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import uk.org.nbn.nbnv.api.dao.core.OperationalPortalDownloadMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.PortalDownloadMapper;
import uk.org.nbn.nbnv.api.model.PortalDownloadStatistic;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;

/**
 * @author Matt Debont
 */
public class PortalDownloadResource extends AbstractResource {
    
    @Autowired PortalDownloadMapper portalDownloadMapper;
    @Autowired OperationalPortalDownloadMapper oPortalDownloadMapper;
    
    @GET
    @Path("/Download/Portal/{datasetKey}")
    public List<PortalDownloadStatistic> getPortalDownloadStatisticsByDataset (
            @TokenDatasetAdminUser(path = "datasetKey") User user, 
            @PathParam("datasetKey") String datasetKey,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate) {
        return portalDownloadMapper.getPortalDownloadStatisticForDataset(datasetKey, startDate, endDate);
    }
}

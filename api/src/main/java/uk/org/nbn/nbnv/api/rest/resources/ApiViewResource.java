/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.core.OperationalApiObservationViewMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.ApiObservationViewMapper;
import uk.org.nbn.nbnv.api.model.ApiObservationView;
import uk.org.nbn.nbnv.api.model.ApiObservationViewStatistic;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenDatasetAdminUser;
import uk.org.nbn.nbnv.api.rest.resources.utils.DownloadHelper;

/**
 * @author Matt Debont
 */
@Component
@Path("/apiViews")
public class ApiViewResource extends AbstractResource {

    @Autowired ApiObservationViewMapper apiObservationViewMapper;
    @Autowired OperationalApiObservationViewMapper oApiObservationViewMapper;
    @Autowired DownloadHelper downloadHelper;

    @GET
    @Path("/{datasetKey : [A-Z][A-Z0-9]{7}}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ApiObservationViewStatistic> getApiObserationViewStatisticsByDataset(
            @TokenDatasetAdminUser(path = "datasetKey") User user,
            @PathParam("datasetKey") String datasetKey,
            @QueryParam("startDate") @DefaultValue("") String startDate,
            @QueryParam("endDate") @DefaultValue("") String endDate) {
        return apiObservationViewMapper.getApiObservationViewStatisticsForDataset(datasetKey, startDate, endDate);
    }

    @GET
    @Path("/{datasetKey : [A-Z][A-Z0-9]{7}}/csv")
    @Produces("application/x-zip-compressed")
    public StreamingOutput getApiObserationViewStatisticsByDatasetAsCSV(
            @TokenDatasetAdminUser(path = "datasetKey") final User user,
            @PathParam("datasetKey") final String datasetKey,
            @QueryParam("startDate") @DefaultValue("") final String startDate,
            @QueryParam("endDate") @DefaultValue("") final String endDate,
            @Context HttpServletResponse response) {
        
        // Allows the fileDownload javascript to close the waiting window when
        // the download is finished, required cookie, but doesn't stick around
        // long
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");

        return new StreamingOutput() {
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
                ZipOutputStream zip = new ZipOutputStream(out);

                zip.putNextEntry(new ZipEntry("ApiObservationViews" + datasetKey + ".csv"));
                
                List<String> values = new ArrayList<String>();
                
                values.add("viewID");
                values.add("userID");
                values.add("forename");
                values.add("surname");
                values.add("email");
                values.add("ip");
                values.add("datasetKey");
                values.add("filterText");
                values.add("viewTime");
                values.add("viewedRecordsInDataset");
                values.add("totalViewedRecordsInThisView");
                values.add("totalDatasetRecords");
                
                downloadHelper.writelnCsv(zip, values);
                
                List<ApiObservationViewStatistic> views = apiObservationViewMapper.getApiObservationViewStatisticsForDataset(datasetKey, startDate, endDate);
                
                for (ApiObservationViewStatistic view : views) {
                    values = new ArrayList<String>();
                    values.add(Integer.toString(view.getViewID()));
                    values.add(Integer.toString(view.getUserID()));
                    values.add(view.getForename());
                    values.add(view.getSurname());
                    values.add(view.getEmail());
                    values.add(view.getIp());
                    values.add(view.getDatasetKey());
                    values.add(view.getFilterText());
                    values.add(view.getViewTimeString());
                    values.add(Integer.toString(view.getViewed()));
                    values.add(Integer.toString(view.getRecordCount()));
                    values.add(Integer.toString(view.getTotalDatasetRecords()));
                    
                    downloadHelper.writelnCsv(zip, values);
                }
                
                zip.flush();
                zip.close();
            }
        };
    }
}

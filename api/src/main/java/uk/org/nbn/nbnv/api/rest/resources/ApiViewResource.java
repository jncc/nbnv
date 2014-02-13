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

    /**
     * Return a list of api observation views which give the filters used and 
     * the number of records viewed at via the API, includes portal operations
     * which return full records to the user.
     * 
     * @param user The current user, must be admin of the dataset (Injected token no need to pass)
     * @param datasetKey The dataset key of the dataset
     * @param startDate (Optional) A start date to search for views from (must have an end date)
     * @param endDate (Optional) An end date to search for views until (must have a start date)
     * @return A list of ApiObservationViewStatistic's for this dataset and any date filters
     */
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
    
    /**
     * Return a zip file containing a CSV list of api observation views which 
     * give the filters used and the number of records viewed at via the API, 
     * includes portal operations which return full records to the user.
     * 
     * @param user The current user, must be admin of the dataset (Injected token no need to pass)
     * @param datasetKey The dataset key of the dataset
     * @param startDate (Optional) A start date to search for views from (must have an end date)
     * @param endDate (Optional) An end date to search for views until (must have a start date)
     * @return A ZIP file containing a CSV list of ApiObservationViewStatistic's for this dataset and any date filters
     */    
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
        // Set the filename to get around a bug with Firefox not adding the extension properly
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s_view_stats.zip\"", datasetKey));

        return new StreamingOutput() {
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
                ZipOutputStream zip = new ZipOutputStream(out);

                zip.putNextEntry(new ZipEntry("ApiObservationViews" + datasetKey + ".csv"));
                
                List<String> values = new ArrayList<String>();
                
                values.add("viewID");
                values.add("datasetKey");
                values.add("forename");
                values.add("surname");
                values.add("email");
                values.add("ip");
                values.add("View");
                values.add("viewTime");
                values.add("totalviewedRecordsInThisDataset");
                values.add("totalRecordsInDataset");
                values.add("totalViewedRecordsInThisView");
                
                
                downloadHelper.writelnCsv(zip, values);
                
                List<ApiObservationViewStatistic> views = apiObservationViewMapper.getApiObservationViewStatisticsForDataset(datasetKey, startDate, endDate);
                
                for (ApiObservationViewStatistic view : views) {
                    values = new ArrayList<String>();
                    values.add(Integer.toString(view.getViewID()));
                    values.add(view.getDatasetKey());
                    values.add(view.getForename());
                    values.add(view.getSurname());
                    values.add(view.getEmail());
                    values.add(view.getIp());
                    values.add(view.getFilterText());
                    values.add(view.getViewTimeString());
                    values.add(Integer.toString(view.getRecordCount()));
                    values.add(Integer.toString(view.getTotalDatasetRecords()));
                    values.add(Integer.toString(view.getViewed()));
                    
                    
                    downloadHelper.writelnCsv(zip, values);
                }
                
                zip.flush();
                zip.close();
            }
        };
    }
}
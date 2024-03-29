package uk.org.nbn.nbnv.api.rest.resources;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DownloadMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonObservationMapper;
import uk.org.nbn.nbnv.api.mail.TemplateMailer;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.DownloadStat;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.UserDownloadNotification;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenSystemAdministratorUser;

@Component
@Path("/reporting")
public class ReportingResource extends AbstractResource {
    
    @Autowired DownloadMapper downloadMapper;
    @Autowired DatasetMapper datasetMapper;
    @Autowired TemplateMailer templateMailer;
    @Autowired TaxonObservationMapper observationMapper;
    
    @GET
    @Path("/monthlyDownload")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully sent the monthly download report out to all dataset admins"),
        @ResponseCode(code = 403, condition = "The current user is not a system administrator")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public String sendMonthlyDownload(
            @TokenSystemAdministratorUser User user,
            @QueryParam("year") int year,
            @QueryParam("month") int month) throws IOException, TemplateException {
        List<Dataset> datasets = datasetMapper.selectAll();
        
        // Get start and end of selected month
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String start = sdf.format(new Date(cal.getTimeInMillis()));
        
        cal.add(Calendar.MONTH, 1);       
        String end = sdf.format(new Date(cal.getTimeInMillis()));
        
        for (Dataset dataset : datasets) {
            List<String> d = new ArrayList<String>();
            d.add(dataset.getKey());
            
            List<UserDownloadNotification> toNotify = downloadMapper.getUsersToNotifyForDatasetDownload(dataset.getKey());
            List<DownloadStat> stats = observationMapper.selectDownloadStats(d, start, end, null, null, null, null);
            List<DownloadStat> userStats = observationMapper.selectUserDownloadStats(d, start, end, null, null, null, null);
            List<DownloadStat> orgStats = observationMapper.selectOrganisationDownloadStats(d, start, end, null, null, null, null);
            
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("portal", properties.getProperty("portal_url"));
                                   
            data.put("dataset", dataset.getKey());
            data.put("datasetName", dataset.getTitle());
            
            int totalDownloads = 0;
            int totalRecordsDownloaded = 0;
            
            for(DownloadStat stat : stats) {
                totalDownloads += stat.getTotalAlt();
                totalRecordsDownloaded += stat.getTotal();
                data.put("R" + stat.getId(), stat.getTotal());
            }
            
            for(DownloadStat stat : stats) {
                data.put("P" + stat.getId(), (totalRecordsDownloaded / stat.getTotal()) * 100);
            }
            
            data.put("totalDownloads", totalDownloads);
            data.put("totalRecordsDownloaded", totalRecordsDownloaded);
            
            data.put("users", userStats);
            data.put("orgs", orgStats);
            
            if (totalDownloads > 0) {
                for (UserDownloadNotification recipient : toNotify) {
                    data.put("name", recipient.getForename());
                    templateMailer.send("dataset_download_monthly.ftl", recipient.getEmail(), "NBN Gateway: Monthly Dataset Download Statistics", data);
                }
            }
        }
        
        return "";
    }
}

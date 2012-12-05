package uk.org.nbn.nbnv.api.rest.resources.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.lang.StringEscapeUtils;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.DatasetWithQueryStats;
import uk.org.nbn.nbnv.api.model.User;

public class DownloadHelper {
    
    public static void addDatasetMetadata(ZipOutputStream zip, List<Dataset> datasets) throws IOException{
        zip.putNextEntry(new ZipEntry("DatasetMetadata.txt"));
        writeln(zip, "Datasets that contributed to this download");
        for (Dataset dataset : datasets) {
            writeln(zip, "------------------------------------------");
            writeln(zip, "");
            writeln(zip, "Title: " + dataset.getTitle());
            writeln(zip, "");
            writeln(zip, "Dataset key: " + dataset.getKey());
            writeln(zip, "");
            writeln(zip, "Description: " + dataset.getDescription());
            writeln(zip, "");
            writeln(zip, "Dataset owner: " + dataset.getOrganisationName());
            writeln(zip, "");
            if (dataset.getUseConstraints() != null && !"".equals(dataset.getUseConstraints().trim())) {
                writeln(zip, "Use constraints: " + dataset.getUseConstraints());
                writeln(zip, "");
            }
            if (dataset.getAccessConstraints() != null && !"".equals(dataset.getAccessConstraints().trim())) {
                writeln(zip, "Access constraints: " + dataset.getAccessConstraints());
                writeln(zip, "");
            }
        }
        zip.flush();
    }
    
    public static void addDatasetWithQueryStatsMetadata(ZipOutputStream zip, List<DatasetWithQueryStats> datasetsWithQueryStats) throws IOException{
        List<Dataset> datasets = new ArrayList<Dataset>();
        for(DatasetWithQueryStats datasetWithStats : datasetsWithQueryStats){
            datasets.add(datasetWithStats.getDataset());
        }
        addDatasetMetadata(zip, datasets);
    }
    
    public static void addReadMe(ZipOutputStream zip, User user, String title, Map<String, String> filters) throws IOException {
        zip.putNextEntry(new ZipEntry("ReadMe.txt"));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        String userName = "anonymous user (not logged in)";
        if(user.getId() != 1){
            userName = user.getForename() + " " + user.getSurname();
        }
        DownloadHelper.writeln(zip, title);
        DownloadHelper.writeln(zip, "---------------------------------------------");
        DownloadHelper.writeln(zip, "Downloaded by: " + userName);
        DownloadHelper.writeln(zip, "Date and time of download: " + dateFormat.format(new Date()));
        if(filters.size() > 0){
            DownloadHelper.writeln(zip, "");
            DownloadHelper.writeln(zip, "Filters supplied by user:");
            for(String key : filters.keySet()){
                writeln(zip, "    " + key + ": " + filters.get(key));
            }
        }
        zip.flush();
    }

    public static void writelnCsv(ZipOutputStream zip, List<String> values) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String value : values) {
            if(first){
                first = false;
            }else{
                sb.append(",");
            }
            sb.append(StringEscapeUtils.escapeCsv(value));
        }
        writeln(zip, sb.toString());
        zip.flush();
    }
    
    public static void writeln(ZipOutputStream zip, String value) throws IOException{
        zip.write((value + "\r\n").getBytes());
    }

}

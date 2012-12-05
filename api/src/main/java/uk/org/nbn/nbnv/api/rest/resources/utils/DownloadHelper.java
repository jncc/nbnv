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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.DatasetWithQueryStats;
import uk.org.nbn.nbnv.api.model.User;

@Component
public class DownloadHelper {

    @Autowired
    DatasetMapper datasetMapper;

    public void addDatasetMetadata(ZipOutputStream zip, int userID, List<Dataset> datasets) throws IOException {
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
            addAccessPositions(zip, userID, dataset.getKey());
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

    public void addDatasetWithQueryStatsMetadata(ZipOutputStream zip, int userID, List<DatasetWithQueryStats> datasetsWithQueryStats) throws IOException {
        List<Dataset> datasets = new ArrayList<Dataset>();
        for (DatasetWithQueryStats datasetWithStats : datasetsWithQueryStats) {
            datasets.add(datasetWithStats.getDataset());
        }
        addDatasetMetadata(zip, userID, datasets);
    }

    public void addReadMe(ZipOutputStream zip, User user, String title, Map<String, String> filters) throws IOException {
        zip.putNextEntry(new ZipEntry("ReadMe.txt"));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        String userName = "anonymous user (not logged in)";
        if (user.getId() != 1) {
            userName = user.getForename() + " " + user.getSurname();
        }
        writeln(zip, title);
        writeln(zip, "---------------------------------------------");
        writeln(zip, "Downloaded by: " + userName);
        writeln(zip, "Date and time of download: " + dateFormat.format(new Date()));
        if (filters.size() > 0) {
            writeln(zip, "");
            writeln(zip, "Filters supplied by user:");
            for (String key : filters.keySet()) {
                writeln(zip, "    " + key + ": " + filters.get(key));
            }
        }
        zip.flush();
    }

    public void writelnCsv(ZipOutputStream zip, List<String> values) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String value : values) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            sb.append(StringEscapeUtils.escapeCsv(value));
        }
        writeln(zip, sb.toString());
        zip.flush();
    }

    public void writeln(ZipOutputStream zip, String value) throws IOException {
        zip.write((value + "\r\n").getBytes());
    }

    private void addAccessPositions(ZipOutputStream zip, int userID, String datasetKey) throws IOException {
        List<String> accessPositions = datasetMapper.getDatasetAccessPositions(datasetKey, userID);
        boolean first = true;
        int counter = 1;
        if (accessPositions != null && accessPositions.size() > 0) {
            for (String accessPosition : accessPositions) {
                if (first) {
                    writeln(zip, "");
                    writeln(zip, "You have been granted the following access to this dataset");
                    first = false;
                }
                writeln(zip, "    Access " + counter++ + ": " + accessPosition);
            }
        } else {
            writeln(zip, "You have public access to this dataset");
        }
    }

}

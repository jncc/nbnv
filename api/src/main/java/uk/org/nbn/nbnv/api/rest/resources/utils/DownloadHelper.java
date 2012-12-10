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
import uk.org.nbn.nbnv.api.model.TaxonDatasetWithQueryStats;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.User;

@Component
public class DownloadHelper {

    @Autowired
    DatasetMapper datasetMapper;

    public void addDatasetMetadata(ZipOutputStream zip, int userID, List<TaxonDataset> taxonDatasets) throws IOException {
        zip.putNextEntry(new ZipEntry("DatasetMetadata.txt"));
        writeln(zip, "Datasets that contributed to this download");
        for (TaxonDataset taxonDataset : taxonDatasets) {
            writeln(zip, "------------------------------------------");
            writeln(zip, "");
            writeln(zip, "Title: " + taxonDataset.getTitle());
            writeln(zip, "");
            writeln(zip, "Dataset key: " + taxonDataset.getKey());
            writeln(zip, "");
            writeln(zip, "Description: " + taxonDataset.getDescription());
            writeln(zip, "");
            writeln(zip, "Dataset owner: " + taxonDataset.getOrganisationName());
            writeln(zip, "");
            addAccessPositions(zip, userID, taxonDataset);
            if (taxonDataset.getUseConstraints() != null && !"".equals(taxonDataset.getUseConstraints().trim())) {
                writeln(zip, "");
                writeln(zip, "Use constraints: " + taxonDataset.getUseConstraints());
            }
            if (taxonDataset.getAccessConstraints() != null && !"".equals(taxonDataset.getAccessConstraints().trim())) {
                writeln(zip, "");
                writeln(zip, "Access constraints: " + taxonDataset.getAccessConstraints());
            }
        }
        zip.flush();
    }

    public void addDatasetWithQueryStatsMetadata(ZipOutputStream zip, int userID, List<TaxonDatasetWithQueryStats> datasetsWithQueryStats) throws IOException {
        List<TaxonDataset> taxonDatasets = new ArrayList<TaxonDataset>();
        for (TaxonDatasetWithQueryStats datasetWithStats : datasetsWithQueryStats) {
            taxonDatasets.add(datasetWithStats.getTaxonDataset());
        }
        addDatasetMetadata(zip, userID, taxonDatasets);
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

    private void addAccessPositions(ZipOutputStream zip, int userID, TaxonDataset taxonDataset) throws IOException {
        List<String> accessPositions = datasetMapper.getDatasetAccessPositions(taxonDataset.getDatasetKey(), userID);
        boolean first = true;
        int counter = 1;
        writeln(zip, "Public access to this data is:");
        writeln(zip, "    Resolution: " + taxonDataset.getPublicResolution());
        writeln(zip, "    View attributes: " + taxonDataset.isPublicAttribute());
        if (accessPositions != null && accessPositions.size() > 0) {
            for (String accessPosition : accessPositions) {
                if (first) {
                    writeln(zip, "You have been granted the following access to this dataset:");
                    first = false;
                }
                writeln(zip, "    " + counter++ + ": " + accessPosition);
            }
        } else {
        }
    }

}

package uk.org.nbn.nbnv.api.model;

import java.util.List;


/**
 *
 * @author Matt Debont
 */
public class TaxonObservationDownloadsByDataset {
    private String datasetKey;
    private List<TaxonObseravtionDownload> downloads;

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public List<TaxonObseravtionDownload> getDownloads() {
        return downloads;
    }

    public void setDownloads(List<TaxonObseravtionDownload> downloads) {
        this.downloads = downloads;
    }
}

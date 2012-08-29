package uk.org.nbn.nbnv.api.model;

import java.util.List;

public class TaxonDataset extends Dataset{
    private String datasetKey, maxResolution, publicResolution;
    private boolean allowRecordValidation;
    private int recordCount;
    private List<Taxon> taxa;
    
    public TaxonDataset(){}
    
    public TaxonDataset(String datasetKey, String maxResolution, String publicResolution, boolean allowRecordValidation, int recordCount, List<Taxon> taxa){
        this.datasetKey = datasetKey;
        this.maxResolution = maxResolution;
        this.publicResolution = publicResolution;
        this.allowRecordValidation = allowRecordValidation;
        this.recordCount = recordCount;
        this.taxa = taxa;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public String getMaxResolution() {
        return maxResolution;
    }

    public void setMaxResolution(String maxResolution) {
        this.maxResolution = maxResolution;
    }

    public String getPublicResolution() {
        return publicResolution;
    }

    public void setPublicResolution(String publicResolution) {
        this.publicResolution = publicResolution;
    }

    public boolean isAllowRecordValidation() {
        return allowRecordValidation;
    }

    public void setAllowRecordValidation(boolean allowRecordValidation) {
        this.allowRecordValidation = allowRecordValidation;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public List<Taxon> getTaxa() {
        return taxa;
    }

    public void setTaxa(List<Taxon> taxa) {
        this.taxa = taxa;
    }
}

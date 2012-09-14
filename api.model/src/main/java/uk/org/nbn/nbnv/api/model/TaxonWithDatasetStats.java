package uk.org.nbn.nbnv.api.model;

public class TaxonWithDatasetStats extends Taxon {
    
    private int observationCount;
    private String datasetKey;
    
    public TaxonWithDatasetStats(){}
    
    public TaxonWithDatasetStats(int observationCount, String datasetKey) {
        this.observationCount = observationCount;
        this.datasetKey = datasetKey;
    }

    public int getObservationCount() {
        return observationCount;
    }

    public void setObservationCount(int observationCount) {
        this.observationCount = observationCount;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }
    
}

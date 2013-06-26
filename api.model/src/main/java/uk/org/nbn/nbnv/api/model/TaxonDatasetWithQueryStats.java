package uk.org.nbn.nbnv.api.model;

import java.util.List;

public class TaxonDatasetWithQueryStats implements Comparable<TaxonDatasetWithQueryStats>{
    
    private String datasetKey;
    private int querySpecificObservationCount;
    private int querySpecificSensitiveObservationCount;
    private List<String> accessPositions;
    private TaxonDataset taxonDataset;
    
    public TaxonDatasetWithQueryStats(){}
    
    public TaxonDatasetWithQueryStats(String datasetKey, int querySpecificObservationCount, TaxonDataset taxonDataset){
        this.datasetKey = datasetKey;
        this.querySpecificObservationCount = querySpecificObservationCount;
        this.taxonDataset = taxonDataset;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public int getQuerySpecificObservationCount() {
        return querySpecificObservationCount;
    }

    public void setQuerySpecificObservationCount(int querySpecificObservationCount) {
        this.querySpecificObservationCount = querySpecificObservationCount;
    }

    public TaxonDataset getTaxonDataset() {
        return taxonDataset;
    }

    public void setTaxonDataset(TaxonDataset taxonDataset) {
        this.taxonDataset = taxonDataset;
    }
    
    @Override
    public int compareTo(TaxonDatasetWithQueryStats that) {
        return this.taxonDataset.getTitle().compareTo(that.taxonDataset.getTitle());
    }

    /**
     * @return the accessPositions
     */
    public List<String> getAccessPositions() {
        return accessPositions;
    }

    /**
     * @param accessPositions the accessPositions to set
     */
    public void setAccessPositions(List<String> accessPositions) {
        this.accessPositions = accessPositions;
    }

    /**
     * @return the querySpecificSensitiveObservationCount
     */
    public int getQuerySpecificSensitiveObservationCount() {
        return querySpecificSensitiveObservationCount;
    }

    /**
     * @param querySpecificSensitiveObservationCount the querySpecificSensitiveObservationCount to set
     */
    public void setQuerySpecificSensitiveObservationCount(int querySpecificSensitiveObservationCount) {
        this.querySpecificSensitiveObservationCount = querySpecificSensitiveObservationCount;
    }

    
}

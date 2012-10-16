package uk.org.nbn.nbnv.api.model;

public class DatasetWithQueryStats implements Comparable<DatasetWithQueryStats>{
    
    private String datasetKey;
    private int querySpecificObservationCount;
    private Dataset dataset;
    
    public DatasetWithQueryStats(){}
    
    public DatasetWithQueryStats(String datasetKey, int querySpecificObservationCount, Dataset dataset){
        this.datasetKey = datasetKey;
        this.querySpecificObservationCount = querySpecificObservationCount;
        this.dataset = dataset;
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

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }
    
    @Override
    public int compareTo(DatasetWithQueryStats that) {
        return this.dataset.getTitle().compareTo(that.dataset.getTitle());
    }

    
}

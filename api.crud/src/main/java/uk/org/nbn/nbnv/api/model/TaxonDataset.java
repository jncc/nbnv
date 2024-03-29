package uk.org.nbn.nbnv.api.model;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaxonDataset extends Dataset{
    private String datasetKey, maxResolution, publicResolution;
    private boolean allowRecordValidation, publicAttribute, publicRecorder;
    private int recordCount, speciesCount;
    private List<TaxonWithDatasetStats> taxa;
    private List<YearStats> recordsPerYear;
    private List<DateTypeStats> dateTypeStats;
    private List<Survey> surveys;
    private List<TaxonObservation> observations;
    
    public TaxonDataset(){}
    
    public TaxonDataset(String datasetKey, String maxResolution, String publicResolution, boolean allowRecordValidation, int recordCount, List<TaxonWithDatasetStats> taxa, List<YearStats> recordsPerYear, List<DateTypeStats> dateTypeStats, List<Survey> surveys, int speciesCount){
        this.datasetKey = datasetKey;
        this.maxResolution = maxResolution;
        this.publicResolution = publicResolution;
        this.allowRecordValidation = allowRecordValidation;
        this.speciesCount = speciesCount;
        this.recordCount = recordCount;
        this.taxa = taxa;
        this.recordsPerYear = recordsPerYear;
        this.dateTypeStats = dateTypeStats;
        this.surveys = surveys;
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

    public List<TaxonWithDatasetStats> getTaxa() {
        return taxa;
    }

    public void setTaxa(List<TaxonWithDatasetStats> taxa) {
        this.taxa = taxa;
    }

    public List<YearStats> getRecordsPerYear() {
        return recordsPerYear;
    }

    public void setRecordsPerYear(List<YearStats> recordsPerYear) {
        this.recordsPerYear = recordsPerYear;
    }

    public List<DateTypeStats> getDateTypeStats() {
        return dateTypeStats;
    }

    public void setDateTypeStats(List<DateTypeStats> dateTypeStats) {
        this.dateTypeStats = dateTypeStats;
    }

    public List<Survey> getSurveys() {
        return surveys;
    }

    public void setSurveys(List<Survey> surveys) {
        this.surveys = surveys;
    }

    public int getSpeciesCount() {
        return speciesCount;
    }

    public void setSpeciesCount(int speciesCount) {
        this.speciesCount = speciesCount;
    }

    public List<TaxonObservation> getObservations() {
        return observations;
    }

    public void setObservations(List<TaxonObservation> observations) {
        this.observations = observations;
    }

    public boolean isPublicAttribute() {
        return publicAttribute;
    }

    public void setPublicAttribute(boolean publicAttribute) {
        this.publicAttribute = publicAttribute;
    }

    public boolean isPublicRecorder() {
        return publicRecorder;
    }

    public void setPublicRecorder(boolean publicRecorder) {
        this.publicRecorder = publicRecorder;
    }
}

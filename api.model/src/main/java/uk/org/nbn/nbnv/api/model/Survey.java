package uk.org.nbn.nbnv.api.model;

import java.util.List;

public class Survey {
    
    private String datasetKey, surveyKey, title, description, geographicalCoverage, temporalCoverage;
    private int surveyID, speciesCount, sampleCount, recordCount;
    
    public Survey(){}
    
    public Survey(String datasetKey, String surveyKey, String title, String description, String geographicalCoverage, String temoralCoverage, int surveyID, int speciesCount, int sampleCount, int recordCount){
        this.datasetKey = datasetKey;
        this.surveyKey = surveyKey;
        this.title = title;
        this.description = description;
        this.geographicalCoverage = geographicalCoverage;
        this.temporalCoverage = temoralCoverage;
        this.surveyID = surveyID;
        this.speciesCount = speciesCount;
        this.sampleCount = sampleCount;
        this.recordCount = recordCount;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public String getSurveyKey() {
        return surveyKey;
    }

    public void setSurveyKey(String surveyKey) {
        this.surveyKey = surveyKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeographicalCoverage() {
        return geographicalCoverage;
    }

    public void setGeographicalCoverage(String geographicalCoverage) {
        this.geographicalCoverage = geographicalCoverage;
    }

    public String getTemporalCoverage() {
        return temporalCoverage;
    }

    public void setTemporalCoverage(String temporalCoverage) {
        this.temporalCoverage = temporalCoverage;
    }

    public int getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(int surveyID) {
        this.surveyID = surveyID;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(int sampleCount) {
        this.sampleCount = sampleCount;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getSpeciesCount() {
        return speciesCount;
    }

    public void setSpeciesCount(int speciesCount) {
        this.speciesCount = speciesCount;
    }
    
}

package uk.org.nbn.nbnv.api.model;

public class Sample {
    
    private String sampleKey, description, geographicalCoverage, temporalCoverage;
    private int sampleID, surveyID;
    
    public Sample(){}
    
    public Sample(int sampleID, int surveyID, String sampleKey, String description, String geographicalCoverage, String temporalCoverage){
        this.sampleID = sampleID;
        this.surveyID = surveyID;
        this.sampleKey = sampleKey;
        this.description = description;
        this.geographicalCoverage = geographicalCoverage;
        this.temporalCoverage = temporalCoverage;
    }

    public String getSampleKey() {
        return sampleKey;
    }

    public void setSampleKey(String sampleKey) {
        this.sampleKey = sampleKey;
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

    public int getSampleID() {
        return sampleID;
    }

    public void setSampleID(int sampleID) {
        this.sampleID = sampleID;
    }

    public int getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(int surveyID) {
        this.surveyID = surveyID;
    }
    
}

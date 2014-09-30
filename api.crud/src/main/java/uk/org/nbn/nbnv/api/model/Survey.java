package uk.org.nbn.nbnv.api.model;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Survey {

    private String datasetKey, surveyKey, description,
            geographicalCoverage, temporalCoverage, dataCaptureMethod, purpose,
            dataQuality, additionalInformation;
    
    @Size(max = 100)
    private String providerKey;
    
    @Size(max = 200)
    private String title;
    
    private int id, speciesCount, sampleCount, recordCount;

    public Survey() {
    }

    public Survey(String datasetKey, String surveyKey, String title, String description, String geographicalCoverage, String temoralCoverage, int id, int speciesCount, int sampleCount, int recordCount) {
        this.datasetKey = datasetKey;
        this.surveyKey = surveyKey;
        this.title = title;
        this.description = description;
        this.geographicalCoverage = geographicalCoverage;
        this.temporalCoverage = temoralCoverage;
        this.id = id;
        this.speciesCount = speciesCount;
        this.sampleCount = sampleCount;
        this.recordCount = recordCount;
    }
    
    public Survey(int id, String providerKey, String title, String description, String geographicalCoverage, String temporalCoverage, String dataQuality, String dataCaptureMethod, String purpose, String additionalInformation) {
        this.id = id;
        this.providerKey = providerKey;
        this.title = title;
        this.description = description;
        this.geographicalCoverage = geographicalCoverage;
        this.temporalCoverage = temporalCoverage;
        this.dataQuality = dataQuality;
        this.dataCaptureMethod = dataCaptureMethod;
        this.purpose = purpose;
        this.additionalInformation = additionalInformation;
    }

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    public String getDataCaptureMethod() {
        return dataCaptureMethod;
    }

    public void setDataCaptureMethod(String dataCaptureMethod) {
        this.dataCaptureMethod = dataCaptureMethod;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDataQuality() {
        return dataQuality;
    }

    public void setDataQuality(String dataQuality) {
        this.dataQuality = dataQuality;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

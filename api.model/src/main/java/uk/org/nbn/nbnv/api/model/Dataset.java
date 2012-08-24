package uk.org.nbn.nbnv.api.model;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dataset {
    
    private String datasetKey, name, description, typeName, organisationName;
    private String captureMethod, purpose, geographicalCoverage, quality;
    private String additionalInformation, accessConstraints, useConstraints;
    private String temporalCoverage, updateFrequency;
    private Date dateUploaded, metadataLastEdited;
    private int organisationID;
    private boolean conditionsAccepted;
    
    public Dataset(){}
    
    public Dataset(String datasetKey, String name, String description, String typeName, String organisationName,
                        String captureMethod, String purpose, String geographicalCoverage, String quality,
                        String additionalInformation, String accessConstraints, String useConstraints,
                        Date dateUploaded, int organisationID, boolean conditionsAccepted, Date metadataLastEdited,
                        String temporalCoverage, String updateFrequency){
        this.datasetKey = datasetKey;
        this.name = name;
        this.description = description;
        this.typeName = typeName;
        this.organisationName = organisationName;
        this.organisationID = organisationID;
        this.captureMethod = captureMethod;
        this.purpose = purpose;
        this.geographicalCoverage = geographicalCoverage;
        this.quality = quality;
        this.additionalInformation = additionalInformation;
        this.accessConstraints = accessConstraints;
        this.useConstraints = useConstraints;
        this.dateUploaded = dateUploaded;
        this.conditionsAccepted = conditionsAccepted;
        this.metadataLastEdited = metadataLastEdited;
        this.temporalCoverage = temporalCoverage;
        this.updateFrequency = updateFrequency;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setType(String typeName) {
        this.setTypeName(typeName);
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getCaptureMethod() {
        return captureMethod;
    }

    public void setCaptureMethod(String CaptureMethod) {
        this.captureMethod = captureMethod;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getGeographicalCoverage() {
        return geographicalCoverage;
    }

    public void setGeographicalCoverage(String geographicalCoverage) {
        this.geographicalCoverage = geographicalCoverage;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getAccessConstraints() {
        return accessConstraints;
    }

    public void setAccessConstraints(String accessConstraints) {
        this.accessConstraints = accessConstraints;
    }

    public String getUseConstraints() {
        return useConstraints;
    }

    public void setUseConstraints(String useConstraints) {
        this.useConstraints = useConstraints;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }
    
    public String getFormattedDateUploaded(){
        return (new SimpleDateFormat("dd-MMM-yyyy")).format(dateUploaded);
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public int getOrganisationID() {
        return organisationID;
    }

    public void setOrganisationID(int organisationID) {
        this.organisationID = organisationID;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTemporalCoverage() {
        return temporalCoverage;
    }

    public void setTemporalCoverage(String temporalCoverage) {
        this.temporalCoverage = temporalCoverage;
    }

    public String getUpdateFrequency() {
        return updateFrequency;
    }

    public void setUpdateFrequency(String updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    public Date getMetadataLastEdited() {
        return metadataLastEdited;
    }

    public String getFormattedMetadataLastEdited(){
        return (new SimpleDateFormat("dd-MMM-yyyy")).format(metadataLastEdited);
    }

    public void setMetadataLastEdited(Date metadataLastEdited) {
        this.metadataLastEdited = metadataLastEdited;
    }

    public boolean isConditionsAccepted() {
        return conditionsAccepted;
    }

    public void setConditionsAccepted(boolean conditionsAccepted) {
        this.conditionsAccepted = conditionsAccepted;
    }
}

package uk.org.nbn.nbnv.api.model;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dataset {
    
    private String datasetKey, name, description, typeName, organisationName;
    private String dataCaptureMethod, purpose, geographicalCoverage, quality;
    private String additionalInformation, accessConstraints, useConstraints;
    private Date dateUploaded;
    private int organisationID;
    
    public Dataset(){}
    
    public Dataset(String datasetKey, String name, String description, String typeName, String organisationName,
                        String dataCaptureMethod, String purpose, String geographicalCoverage, String quality,
                        String additionalInformation, String accessConstraints, String useConstraints,
                        Date dateUploaded, int organisationID){
        this.datasetKey = datasetKey;
        this.name = name;
        this.description = description;
        this.typeName = typeName;
        this.organisationName = organisationName;
        this.dataCaptureMethod = dataCaptureMethod;
        this.purpose = purpose;
        this.geographicalCoverage = geographicalCoverage;
        this.quality = quality;
        this.additionalInformation = additionalInformation;
        this.accessConstraints = accessConstraints;
        this.useConstraints = useConstraints;
        this.dateUploaded = dateUploaded;
        this.organisationID = organisationID;
        
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
        this.typeName = typeName;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
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
}

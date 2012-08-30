
package nbn.common.dataset;

import nbn.common.organisation.Organisation;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 08-Sep-2010
* @description	    :-
*/
public class HabitatDataset extends GISLayeredDataset {
    private String layerName;
    private String abstractStr;
    private String language;
    private String dataCaptureMethod;
    private String purpose;
    private String geographicalCoverage;
    private String temporalCoverage;
    private String dataQuality;
    private String additionalInformation;
    private String updateFrequency;
    private String uploadedBy;
    private String accessConstraint;
    private String useConstraint;

    private String dateLoaded;
    private String metadataLastUpdated;

    HabitatDataset(String key, String title, Organisation provider) {
	super(key,title,provider);
    }

    void setAbstract(String abstractStr) {
	this.abstractStr = abstractStr;
    }

    void setAccessConstraint(String accessConstraint) {
	this.accessConstraint = accessConstraint;
    }

    void setAdditionalInformation(String additionalInformation) {
	this.additionalInformation = additionalInformation;
    }

    void setDataCaptureMethod(String dataCaptureMethod) {
	this.dataCaptureMethod = dataCaptureMethod;
    }

    void setDataQuality(String dataQuality) {
	this.dataQuality = dataQuality;
    }

    void setDateLoaded(String dateLoaded) {
	this.dateLoaded = dateLoaded;
    }

    void setGeographicalCoverage(String geographicalCoverage) {
	this.geographicalCoverage = geographicalCoverage;
    }

    void setLanguage(String language) {
	this.language = language;
    }

    void setMetadataLastUpdated(String metadataLastUpdated) {
	this.metadataLastUpdated = metadataLastUpdated;
    }

    void setPurpose(String purpose) {
	this.purpose = purpose;
    }

    void setTemporalCoverage(String temporalCoverage) {
	this.temporalCoverage = temporalCoverage;
    }

    void setUpdateFrequency(String updateFrequency) {
	this.updateFrequency = updateFrequency;
    }

    void setUploadedBy(String uploadedBy) {
	this.uploadedBy = uploadedBy;
    }

    void setUseConstraint(String useConstraint) {
	this.useConstraint = useConstraint;
    }

    void setLayerName(String layerName) {
	this.layerName = layerName;
    }
    
    public String getAccessConstraint() {
	return accessConstraint;
    }

    public String getAdditionalInformation() {
	return additionalInformation;
    }

    public String getDataCaptureMethod() {
	return dataCaptureMethod;
    }

    public String getDataQuality() {
	return dataQuality;
    }

    public String getDateLoaded() {
	return dateLoaded;
    }

    public String getGeographicalCoverage() {
	return geographicalCoverage;
    }

    public String getLanguage() {
	return language;
    }

    public String getMetadataLastUpdated() {
	return metadataLastUpdated;
    }

    public String getPurpose() {
	return purpose;
    }

    public String getTemporalCoverage() {
	return temporalCoverage;
    }

    public String getUpdateFrequency() {
	return updateFrequency;
    }

    public String getUploadedBy() {
	return uploadedBy;
    }

    public String getUseConstraint() {
	return useConstraint;
    }

    public String getAbstract() {
	return abstractStr;
    }

    public String getLayerName() {
	return layerName;
    }
}

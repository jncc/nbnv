/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.model;

/**
 *
 * @author Paul Gilbertson
 */
public class Metadata {
    private String title = "";
    private String description = "";
    private String methods = "";
    private String purpose = "";
    private String geographic = "";
    private String temporal = "";
    private String quality = "";
    private String info = "";
    private String use = "";
    private String access = "";
    private String datasetAdminName = "";
    private String datasetAdminPhone = "";
    private String datasetAdminEmail = "";
    private String geographicalRes = "10km^2";
    private String recordAtts = "N/A";
    private String recorderNames = "N/A";
    private int datasetAdminID = -1;
    private int organisationID = -1;

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the methods
     */
    public String getMethods() {
        return methods;
    }

    /**
     * @param methods the methods to set
     */
    public void setMethods(String methods) {
        this.methods = methods;
    }

    /**
     * @return the purpose
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * @param purpose the purpose to set
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     * @return the geographic
     */
    public String getGeographic() {
        return geographic;
    }

    /**
     * @param geographic the geographic to set
     */
    public void setGeographic(String geographic) {
        this.geographic = geographic;
    }

    /**
     * @return the temporal
     */
    public String getTemporal() {
        return temporal;
    }

    /**
     * @param temporal the temporal to set
     */
    public void setTemporal(String temporal) {
        this.temporal = temporal;
    }

    /**
     * @return the quality
     */
    public String getQuality() {
        return quality;
    }

    /**
     * @param quality the quality to set
     */
    public void setQuality(String quality) {
        this.quality = quality;
    }

    /**
     * @return the info
     */
    public String getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * @return the use
     */
    public String getUse() {
        return use;
    }

    /**
     * @param use the use to set
     */
    public void setUse(String use) {
        this.use = use;
    }

    /**
     * @return the access
     */
    public String getAccess() {
        return access;
    }

    /**
     * @param access the access to set
     */
    public void setAccess(String access) {
        this.access = access;
    }

    /**
     * @return the organisationID
     */
    public int getOrganisationID() {
        return organisationID;
    }

    /**
     * @param organisationID the organisationID to set
     */
    public void setOrganisationID(int organisationID) {
        this.organisationID = organisationID;
    }

    public String getDatasetAdminName() {
        return datasetAdminName;
    }

    public void setDatasetAdminName(String datasetAdminName) {
        this.datasetAdminName = datasetAdminName;
    }

    public String getDatasetAdminPhone() {
        return datasetAdminPhone;
    }

    public void setDatasetAdminPhone(String datasetAdminPhone) {
        this.datasetAdminPhone = datasetAdminPhone;
    }

    public String getDatasetAdminEmail() {
        return datasetAdminEmail;
    }

    public void setDatasetAdminEmail(String datasetAdminEmail) {
        this.datasetAdminEmail = datasetAdminEmail;
    }

    public int getDatasetAdminID() {
        return datasetAdminID;
    }

    public void setDatasetAdminID(int datasetAdminID) {
        this.datasetAdminID = datasetAdminID;
    }

    public String getGeographicalRes() {
        return geographicalRes;
    }

    public void setGeographicalRes(String geographicalRes) {
        this.geographicalRes = geographicalRes;
    }

    public String getRecordAtts() {
        return recordAtts;
    }

    public void setRecordAtts(String recordAtts) {
        this.recordAtts = recordAtts;
    }

    public String getRecorderNames() {
        return recorderNames;
    }

    public void setRecorderNames(String recorderNames) {
        this.recorderNames = recorderNames;
    }
}

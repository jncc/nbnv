/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.dataset;

import java.sql.SQLException;
import java.util.HashMap;
import nbn.common.dataset.privileges.Privileges;
import nbn.common.dataset.privileges.PrivilegesDAO;
import nbn.common.feature.Resolution;
import nbn.common.organisation.Organisation;
import nbn.common.taxon.Taxon;
import nbn.common.user.User;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Administrator
 */
public class TaxonDataset extends Dataset {
    /** The dataset description.*/
    private String description = "";
    /** The dataset capture method. */
    private String dataCaptureMethod = "";
    /** The purpose of the dataset. */
    private String purpose = "";
    /** The Geographic coverage of the dataset. */
    private String geographicalCoverage = "";
    /** The Temporal coverage of the dataset. */
    private String temporalCoverage = "";
    /** The confidence of the data datasetProvider in the dataset. */
    private String dataQuality = "";
    /** The upload mechanism. */
    private String updateFrequency = "";
    /** Any access constraints placed on the dataset. */
    private String accessConstraint = "";
    /** Any use constraints placed on the dataset. */
    private String useConstraint = "";
    /** The date the metadata was last edited. */
    private String lastEdited = "";
    /** Any additional information for the dataset. */
    private String additionalInformation = "";
    /** The date the dataset was loaded onto the Gateway .*/
    private String dateUploaded = "";
    /** Indicates the maximum resolution records are recorded to for the datasets. */
    private Resolution maxResolution;

    private int amountOfSpecies,amountOfSites,amountOfSamples,amountOfRecords;
    private short captureStartYear, captureEndYear;

    private HashMap<User, Privileges> _privs;

    public Privileges getPrivileges(User user) throws SQLException {
        if (this._privs.containsKey(user))
            return this._privs.get(user);

        PrivilegesDAO dao = new PrivilegesDAO();
        Privileges privileges = dao.getPrivileges(user, this);
        this._privs.put(user, privileges);
        dao.dispose();

        return privileges;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the dataCaptureMethod
     */
    public String getDataCaptureMethod() {
        return dataCaptureMethod;
    }

    /**
     * @return the purpose
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * @return the geographicalCoverage
     */
    public String getGeographicalCoverage() {
        return geographicalCoverage;
    }

    /**
     * @return the temporalCoverage
     */
    public String getTemporalCoverage() {
        return temporalCoverage;
    }

    /**
     * @return the dataQuality
     */
    public String getDataQuality() {
        return dataQuality;
    }

    /**
     * @return the updateFrequency
     */
    public String getUpdateFrequency() {
        return updateFrequency;
    }

    /**
     * @return the accessConstraint
     */
    public String getAccessConstraint() {
        return accessConstraint;
    }

    /**
     * @return the useConstraint
     */
    public String getUseConstraint() {
        return useConstraint;
    }

    /**
     * @return the lastEdited
     */
    public String getLastEdited() {
        return lastEdited;
    }

    /**
     * @return the additionalInformation
     */
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * @return the dateUploaded
     */
    public String getDateUploaded() {
        return dateUploaded;
    }

    /**
     * @return the maxResolution
     */
    public Resolution getMaxResolution() {
        return maxResolution;
    }

    /**
     * This method will return the amount of records that exist in this dataset for a particular taxon
     */
    public int getAmountOfRecords(Taxon t) {
        return 0;
    }

    public int getAmountOfRecords() {
        return amountOfRecords;
    }

    public int getAmountOfSamples() {
        return amountOfSamples;
    }

    public int getAmountOfSites() {
        return amountOfSites;
    }

    public int getAmountOfSpecies() {
        return amountOfSpecies;
    }

    public short getCaptureEndYear() {
        return captureEndYear;
    }

    public short getCaptureStartYear() {
        return captureStartYear;
    }
    /**
     * @param description the description to set
     */
    void setDescription(String description) {
        this.description = StringEscapeUtils.escapeXml(description);
    }

    /**
     * @param dataCaptureMethod the dataCaptureMethod to set
     */
    void setDataCaptureMethod(String dataCaptureMethod) {
        this.dataCaptureMethod = StringEscapeUtils.escapeXml(dataCaptureMethod);
    }

    /**
     * @param purpose the purpose to set
     */
    void setPurpose(String purpose) {
        this.purpose = StringEscapeUtils.escapeXml(purpose);
    }

    /**
     * @param geographicalCoverage the geographicalCoverage to set
     */
    void setGeographicalCoverage(String geographicalCoverage) {
        this.geographicalCoverage = StringEscapeUtils.escapeXml(geographicalCoverage);
    }

    /**
     * @param temporalCoverage the temporalCoverage to set
     */
    void setTemporalCoverage(String temporalCoverage) {
        this.temporalCoverage = StringEscapeUtils.escapeXml(temporalCoverage);
    }

    /**
     * @param dataQuality the dataQuality to set
     */
    void setDataQuality(String dataQuality) {
        this.dataQuality = StringEscapeUtils.escapeXml(dataQuality);
    }

    /**
     * @param updateFrequency the updateFrequency to set
     */
    void setUpdateFrequency(String updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    /**
     * @param accessConstraint the accessConstraint to set
     */
    void setAccessConstraint(String accessConstraint) {
        this.accessConstraint = StringEscapeUtils.escapeXml(accessConstraint);
    }

    /**
     * @param useConstraint the useConstraint to set
     */
    void setUseConstraint(String useConstraint) {
        this.useConstraint = StringEscapeUtils.escapeXml(useConstraint);
    }

    /**
     * @param lastEdited the lastEdited to set
     */
    void setLastEdited(String lastEdited) {
        this.lastEdited = lastEdited;
    }

    /**
     * @param additionalInformation the additionalInformation to set
     */
    void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation =  StringEscapeUtils.escapeXml(additionalInformation);
    }

    /**
     * @param dateUploaded the dateUploaded to set
     */
    void setDateUploaded(String dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    /**
     * @param maxResolution the maxResolution to set
     */
    void setMaxResolution(Resolution maxResolution) {
        this.maxResolution = maxResolution;
    }

    void setAmountOfSpecies(int amountOfSpecies) {
        this.amountOfSpecies = amountOfSpecies;
    }

    void setAmountOfSites(int amountOfSites) {
        this.amountOfSites = amountOfSites;
    }

    void setAmountOfSamples(int amountOfSamples) {
        this.amountOfSamples = amountOfSamples;
    }

    void setAmountOfRecords(int amountOfRecords) {
        this.amountOfRecords = amountOfRecords;
    }

    void setCaptureStartYear(short captureStartYear) {
        this.captureStartYear = captureStartYear;
    }

    void setCaptureEndYear(short captureEndYear) {
        this.captureEndYear = captureEndYear;
    }

    TaxonDataset(String key, String title, Organisation provider) {
        super(key, title, provider);
        this._privs = new HashMap<User, Privileges>();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TaxonDataset) {
            if (((TaxonDataset)obj).getDatasetKey().equals(this.getDatasetKey()))
                return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.datasetKey != null ? this.datasetKey.hashCode() : 0);
        return hash;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

import java.util.Date;

/**
 *
 * @author Paul Gilbertson
 */
public class TaxonObservation {
    private int observationID;
    private boolean fullVersion;
    private String datasetKey;
    private String surveyKey;
    private String sampleKey;
    private String observationKey;
    private String siteKey;
    private String siteName;
    private int featureID;
    private String gridRef;
    private String polygonKey;
    private String projection;
    private String taxonVersionKey;
    private String pTaxonVersionKey;
    private String pTaxonName;
    private String pTaxonAuthority;
    private Date startDate;
    private Date endDate;
    private String dateType;
    private String recorder;
    private String determiner;
    private boolean sensitive;
    private boolean absence;

    /**
     * @return the observationID
     */
    public int getObservationID() {
        return observationID;
    }

    /**
     * @param observationID the observationID to set
     */
    public void setObservationID(int observationID) {
        this.observationID = observationID;
    }

    /**
     * @return the fullVersion
     */
    public boolean isFullVersion() {
        return fullVersion;
    }

    /**
     * @param fullVersion the fullVersion to set
     */
    public void setFullVersion(boolean fullVersion) {
        this.fullVersion = fullVersion;
    }

    /**
     * @return the datasetKey
     */
    public String getDatasetKey() {
        return datasetKey;
    }

    /**
     * @param datasetKey the datasetKey to set
     */
    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    /**
     * @return the surveyKey
     */
    public String getSurveyKey() {
        return surveyKey;
    }

    /**
     * @param surveyKey the surveyKey to set
     */
    public void setSurveyKey(String surveyKey) {
        this.surveyKey = surveyKey;
    }

    /**
     * @return the sampleKey
     */
    public String getSampleKey() {
        return sampleKey;
    }

    /**
     * @param sampleKey the sampleKey to set
     */
    public void setSampleKey(String sampleKey) {
        this.sampleKey = sampleKey;
    }

    /**
     * @return the observationKey
     */
    public String getObservationKey() {
        return observationKey;
    }

    /**
     * @param observationKey the observationKey to set
     */
    public void setObservationKey(String observationKey) {
        this.observationKey = observationKey;
    }

    /**
     * @return the siteKey
     */
    public String getSiteKey() {
        return siteKey;
    }

    /**
     * @param siteKey the siteKey to set
     */
    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    /**
     * @return the siteName
     */
    public String getSiteName() {
        return siteName;
    }

    /**
     * @param siteName the siteName to set
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    /**
     * @return the featureID
     */
    public int getFeatureID() {
        return featureID;
    }

    /**
     * @param featureID the featureID to set
     */
    public void setFeatureID(int featureID) {
        this.featureID = featureID;
    }

    /**
     * @return the gridRef
     */
    public String getGridRef() {
        return gridRef;
    }

    /**
     * @param gridRef the gridRef to set
     */
    public void setGridRef(String gridRef) {
        this.gridRef = gridRef;
    }

    /**
     * @return the polygonKey
     */
    public String getPolygonKey() {
        return polygonKey;
    }

    /**
     * @param polygonKey the polygonKey to set
     */
    public void setPolygonKey(String polygonKey) {
        this.polygonKey = polygonKey;
    }

    /**
     * @return the projection
     */
    public String getProjection() {
        return projection;
    }

    /**
     * @param projection the projection to set
     */
    public void setProjection(String projection) {
        this.projection = projection;
    }

    /**
     * @return the taxonVersionKey
     */
    public String getTaxonVersionKey() {
        return taxonVersionKey;
    }

    /**
     * @param taxonVersionKey the taxonVersionKey to set
     */
    public void setTaxonVersionKey(String taxonVersionKey) {
        this.taxonVersionKey = taxonVersionKey;
    }

    /**
     * @return the pTaxonVersionKey
     */
    public String getpTaxonVersionKey() {
        return pTaxonVersionKey;
    }

    /**
     * @param pTaxonVersionKey the pTaxonVersionKey to set
     */
    public void setpTaxonVersionKey(String pTaxonVersionKey) {
        this.pTaxonVersionKey = pTaxonVersionKey;
    }

    /**
     * @return the pTaxonName
     */
    public String getpTaxonName() {
        return pTaxonName;
    }

    /**
     * @param pTaxonName the pTaxonName to set
     */
    public void setpTaxonName(String pTaxonName) {
        this.pTaxonName = pTaxonName;
    }

    /**
     * @return the pTaxonAuthority
     */
    public String getpTaxonAuthority() {
        return pTaxonAuthority;
    }

    /**
     * @param pTaxonAuthority the pTaxonAuthority to set
     */
    public void setpTaxonAuthority(String pTaxonAuthority) {
        this.pTaxonAuthority = pTaxonAuthority;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the dateType
     */
    public String getDateType() {
        return dateType;
    }

    /**
     * @param dateType the dateType to set
     */
    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    /**
     * @return the recorder
     */
    public String getRecorder() {
        return recorder;
    }

    /**
     * @param recorder the recorder to set
     */
    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    /**
     * @return the determiner
     */
    public String getDeterminer() {
        return determiner;
    }

    /**
     * @param determiner the determiner to set
     */
    public void setDeterminer(String determiner) {
        this.determiner = determiner;
    }

    /**
     * @return the sensitive
     */
    public boolean isSensitive() {
        return sensitive;
    }

    /**
     * @param sensitive the sensitive to set
     */
    public void setSensitive(boolean sensitive) {
        this.sensitive = sensitive;
    }

    /**
     * @return the absence
     */
    public boolean isAbsence() {
        return absence;
    }

    /**
     * @param absence the absence to set
     */
    public void setAbsence(boolean absence) {
        this.absence = absence;
    }
}

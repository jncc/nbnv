package uk.org.nbn.nbnv.api.model;

import java.sql.Date;
//import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@XmlRootElement
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
    private String location;
    private String gridRef;
    private String resolution;
    private String polygonKey;
    private String projection;
    private String taxonVersionKey;
    private String pTaxonVersionKey;
    private String pTaxonName;
    private String pTaxonAuthority;
    private Date startDate;
    private Date endDate;
    private String dateTypeKey;
    private String dateType;
    private String recorder;
    private String determiner;
    private boolean sensitive;
    private boolean absence;

    public int getObservationID() {
        return observationID;
    }

    public void setObservationID(int observationID) {
        this.observationID = observationID;
    }

    public boolean isFullVersion() {
        return fullVersion;
    }

    public void setFullVersion(boolean fullVersion) {
        this.fullVersion = fullVersion;
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

    public String getSampleKey() {
        return sampleKey;
    }

    public void setSampleKey(String sampleKey) {
        this.sampleKey = sampleKey;
    }

    public String getObservationKey() {
        return observationKey;
    }

    public void setObservationKey(String observationKey) {
        this.observationKey = observationKey;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public int getFeatureID() {
        return featureID;
    }

    public void setFeatureID(int featureID) {
        this.featureID = featureID;
    }

    public String getGridRef() {
        return gridRef;
    }

    public void setGridRef(String gridRef) {
        this.gridRef = gridRef;
    }

    public String getPolygonKey() {
        return polygonKey;
    }

    public void setPolygonKey(String polygonKey) {
        this.polygonKey = polygonKey;
    }

    public String getProjection() {
        return projection;
    }

    public void setProjection(String projection) {
        this.projection = projection;
    }

    public String getTaxonVersionKey() {
        return taxonVersionKey;
    }

    public void setTaxonVersionKey(String taxonVersionKey) {
        this.taxonVersionKey = taxonVersionKey;
    }

    public String getpTaxonVersionKey() {
        return pTaxonVersionKey;
    }

    public void setpTaxonVersionKey(String pTaxonVersionKey) {
        this.pTaxonVersionKey = pTaxonVersionKey;
    }

    public String getpTaxonName() {
        return pTaxonName;
    }

    public void setpTaxonName(String pTaxonName) {
        this.pTaxonName = pTaxonName;
    }

    public String getpTaxonAuthority() {
        return pTaxonAuthority;
    }

    public void setpTaxonAuthority(String pTaxonAuthority) {
        this.pTaxonAuthority = pTaxonAuthority;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDateTypekey() {
        return this.dateTypeKey;
    }

    public void setDateTypeKey(String dateTypeKey) {
        this.dateTypeKey = dateTypeKey;
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    public String getDeterminer() {
        return determiner;
    }

    public void setDeterminer(String determiner) {
        this.determiner = determiner;
    }

    public boolean isSensitive() {
        return sensitive;
    }

    public void setSensitive(boolean sensitive) {
        this.sensitive = sensitive;
    }

    public boolean isAbsence() {
        return absence;
    }

    public void setAbsence(boolean absence) {
        this.absence = absence;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    /**
     * @return the resolution
     */
    public String getResolution() {
        return resolution;
    }

    /**
     * @param resolution the resolution to set
     */
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}

package uk.org.nbn.nbnv.api.model;

import java.util.Date;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Matt Debont
 */
@XmlRootElement
public class TaxonObservationDownload {
    
    private int observationID;
    private String observationKey;
    private String organisationName;
    private String datasetKey;
    private String surveyKey;
    private String sampleKey;
    private String gridReference;
    private String precision;
    private String siteKey;
    private String siteName;
    private String featureKey;
    private Date startDate;
    private Date endDate;
    private String dateType;
    private String recorder;
    private String determiner;
    private String pTaxonVersionKey;
    private String pTaxonName;
    private String authority;
    private String commonName;
    private String TaxonGroup;
    private boolean sensitive;
    private boolean zeroAbundance;
    private boolean fullVersion;
    private boolean publicAttribute;
    private String verification;
    private String attrStr;
    
    public TaxonObservationDownload() {
        this.fullVersion = false;
    }

    public int getObservationID() {
        return observationID;
    }

    public void setObservationID(int observationID) {
        this.observationID = observationID;
    }

    public String getObservationKey() {
        return observationKey;
    }

    public void setObservationKey(String observationKey) {
        this.observationKey = observationKey;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
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

    public String getGridReference() {
        return gridReference;
    }

    public void setGridReference(String gridReference) {
        this.gridReference = gridReference;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
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

    public String getFeatureKey() {
        return featureKey;
    }

    public void setFeatureKey(String featureKey) {
        this.featureKey = featureKey;
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

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
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

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getTaxonGroup() {
        return TaxonGroup;
    }

    public void setTaxonGroup(String TaxonGroup) {
        this.TaxonGroup = TaxonGroup;
    }

    public boolean isSensitive() {
        return sensitive;
    }

    public void setSensitive(boolean sensitive) {
        this.sensitive = sensitive;
    }

    public boolean isZeroAbundance() {
        return zeroAbundance;
    }

    public void setZeroAbundance(boolean zeroAbundance) {
        this.zeroAbundance = zeroAbundance;
    }

    public boolean isFullVersion() {
        return fullVersion;
    }

    public void setFullVersion(boolean fullVersion) {
        this.fullVersion = fullVersion;
    }

    public boolean isPublicAttribute() {
        return publicAttribute;
    }

    public void setPublicAttribute(boolean publicAttribute) {
        this.publicAttribute = publicAttribute;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public String getAttrStr() {
        return attrStr;
    }

    public void setAttrStr(String attrStr) {
        this.attrStr = attrStr;
    }
}

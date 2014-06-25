<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
<h1>TaxonObservation Source Code Java</h1>
<script type="syntaxhighlighter" class="brush: java; html-script: false"><![CDATA[
package nbnapi;

public class TaxonObservation implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private int observationID;
	private boolean fullVersion;
	private String datasetKey;
	private String surveyKey;
	private String sampleKey;
	private String observationKey;
	private int featureID;
	private String location;
	private String siteKey;
	private String siteName;
	private String resolution;
	private String taxonVersionKey;
	private String pTaxonVersionKey;
	private String pTaxonName;
	private String pTaxonAuthority;
	private String startDate;
	private String endDate;
	private boolean sensitive;
	private boolean absence;
	private String dateTypekey;
    
    public TaxonObservation() {
	}
    
    public int getObservationID() {
		return observationID;
	}
	public void setObservationID(int observationID) {
		this.observationID = observationID;
	}
	
	public boolean getFullVersion() {
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
	
	public int getFeatureID() {
		return featureID;
	}
	public void setFeatureID(int featureID) {
		this.featureID = featureID;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
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
	
	public String getResolution() {
		return resolution;
	}
	
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	
	public String getpTaxonVersionKey() {
		return pTaxonVersionKey;
	}
	
	public void setpTaxonVersionKey(String pTaxonVersionKey) {
		this.pTaxonVersionKey = pTaxonVersionKey;
	}
	
	public String getTaxonVersionKey() {
		return taxonVersionKey;
	}
	public void setTaxonVersionKey(String taxonVersionKey) {
		this.taxonVersionKey = taxonVersionKey;
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
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public boolean getSensitive() {
		return sensitive;
	}
	
	public void setSensitive(boolean sensitive) {
		this.sensitive = sensitive;
	}
	
	public boolean getAbsence() {
		return absence;
	}
	
	public void setAbsence(boolean absence) {
		this.absence = absence;
	}
	
	public String getDateTypekey() {
		return dateTypekey;
	}
	
	public void setDateTypekey(String dateTypekey) {
		this.dateTypekey = dateTypekey;
	}
}
]]></script>

<script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

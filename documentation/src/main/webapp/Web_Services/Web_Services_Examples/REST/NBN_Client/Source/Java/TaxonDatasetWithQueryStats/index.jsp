<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
<h1>TaxonDatasetWithQueryStats and TaxonDataset Source Code Java</h1>
<h2>TaxonDatasetWithQueryStats</h2>
<script type="syntaxhighlighter" class="brush: java; html-script: false"><![CDATA[
package nbnapi;

public class TaxonDatasetWithQueryStats implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

		private String datasetKey;
		private TaxonDataset taxonDataset;
		private AccessPosition[] accessPositions;
		private int querySpecificSensitiveObservationCount;
		private int querySpecificObservationCount;
	
		public TaxonDatasetWithQueryStats() {
		}
		
		public String getDatasetKey() {
			return datasetKey;
		}
		public void setDatasetKey(String datasetKey) {
			this.datasetKey = datasetKey;
		}
		
		public TaxonDataset getTaxonDataset() {
			return taxonDataset;
		}
		public void setTaxonDataset(TaxonDataset taxonDataset) {
			this.taxonDataset = taxonDataset;
		}
		
		public AccessPosition[] getAccessPositions() {
			return accessPositions;
		}
		public void setAccessPositions(AccessPosition[] accessPositions) {
			this.accessPositions = accessPositions;
		}
		
		public int getQuerySpecificSensitiveObservationCount() {
			return querySpecificSensitiveObservationCount;
		}
		public void setQuerySpecificSensitiveObservationCount(int querySpecificSensitiveObservationCount) {
			this.querySpecificSensitiveObservationCount = querySpecificSensitiveObservationCount;
		}
		
		public int getQuerySpecificObservationCount() {
			return querySpecificObservationCount;
		}
		public void setQuerySpecificObservationCount(int querySpecificObservationCount) {
			this.querySpecificObservationCount = querySpecificObservationCount;
		}
}
]]></script>

<h2>TaxonDataset</h2>
<script type="syntaxhighlighter" class="brush: java; html-script: false"><![CDATA[
package nbnapi;

public class TaxonDataset implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String href;
	private String organisationHref;
	private String key;
	private String title;
	private String description;
	private String typeName;
	private String organisationName;
	private String captureMethod;
	private String purpose;
	private String geographicalCoverage;
	private String quality;
	private String additionalInformation;
	private String accessConstraints;
	private String useConstraints;
	private String temporalCoverage;
	private String updateFrequency;
	private long dateUploaded;
	private long metadataLastEdited;
	private String formattedDateUploaded;
	private String formattedMetadataLastEdited;
	private int organisationID;
	private boolean conditionsAccepted;
	private String datasetKey;
	private String publicResolution;
	private boolean allowRecordValidation;
	private boolean publicAttribute;
	private boolean publicRecorder;
	private int recordCount;
	private int speciesCount;
	
	public TaxonDataset() {
	}
	
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	
	public String getOrganisationHref() {
		return organisationHref;
	}
	public void setOrganisationHref(String organisationHref) {
		this.organisationHref = organisationHref;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
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
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
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
	public void setCaptureMethod(String captureMethod) {
		this.captureMethod = captureMethod;
	}
	
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = captureMethod;
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
	
	public long getDateUploaded() {
		return dateUploaded;
	}
	public void setDateUploaded(long dateUploaded) {
		this.dateUploaded = dateUploaded;
	}
	
	public long getMetadataLastEdited() {
		return metadataLastEdited;
	}
	public void setMetadataLastEdited(long metadataLastEdited) {
		this.metadataLastEdited = metadataLastEdited;
	}
	
	public String getFormattedDateUploaded() {
		return formattedDateUploaded;
	}
	public void setFormattedDateUploaded(String formattedDateUploaded) {
		this.formattedDateUploaded = formattedDateUploaded;
	}
	
	public String getFormattedMetadataLastEdited() {
		return formattedMetadataLastEdited;
	}
	public void setFormattedMetadataLastEdited(String formattedMetadataLastEdited) {
		this.formattedMetadataLastEdited = formattedMetadataLastEdited;
	}
	
	public int getOrganisationID() {
		return organisationID;
	}
	public void setOrganisationID(int organisationID) {
		this.organisationID = organisationID;
	}
	
	public boolean getConditionsAccepted() {
		return conditionsAccepted;
	}
	public void setConditionsAccepted(boolean conditionsAccepted) {
		this.conditionsAccepted = conditionsAccepted;
	}
	
	public String getDatasetKey() {
		return datasetKey;
	}
	public void setDatasetKey(String datasetKey) {
		this.datasetKey = datasetKey;
	}
	
	public String getPublicResolution() {
		return publicResolution;
	}
	public void setPublicResoultion(String publicResolution) {
		this.publicResolution = publicResolution;
	}
	
	public boolean getAllowRecordValidation() {
		return allowRecordValidation;
	}
	public void setAllowRecordValidation(boolean allowRecordValidation) {
		this.allowRecordValidation = allowRecordValidation;
	}
	
	public boolean getPublicAttribute() {
		return publicAttribute;
	}
	public void setPublicAttributed(boolean publicAttribute) {
		this.publicAttribute = publicAttribute;
	}
	
	public boolean getPublicRecorder() {
		return publicRecorder;
	}
	public void setPublicRecorder(boolean publicRecorder) {
		this.publicRecorder = publicRecorder;
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
]]></script>

<script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
<h1>TaxonDatasetWithQueryStats and TaxonDataset Source Code C#</h1>
<h2>TaxonDatasetWithQueryStats</h2>
<script type="syntaxhighlighter" class="brush: csharp; html-script: false"><![CDATA[
namespace NBNAPI.REST
{
	public class TaxonDatasetWithQueryStats
	{
		public string datasetKey { get; set; }
        public TaxonDataset taxonDataset { get; set; }
        public AccessPosition[] accessPositions { get; set; }
		public int querySpecificSensitiveObservationCount { get; set; }
		public int querySpecificObservationCount { get; set; }
	}
}
]]></script>
<h2>TaxonDataset</h2>
<script type="syntaxhighlighter" class="brush: csharp; html-script: false"><![CDATA[
namespace NBNAPI.REST
{
	public class TaxonDataset
	{
		public string href { get; set; }
		public string organisationHref { get; set; }
		public string key { get; set; }
		public string title { get; set; }
		public string description { get; set; }
		public string typeName { get; set; }
		public string organisationName { get; set; }
		public string captureMethod { get; set; }
		public string purpose { get; set; }
		public string geographicalCoverage { get; set; }
		public string quality { get; set; }
		public string additionalInformation { get; set; }
		public string accessConstraints { get; set; }
		public string useConstraints { get; set; }
		public string temporalCoverage { get; set; }
		public string updateFrequency { get; set; }
		public long dateUploaded { get; set; }
		public long metadataLastEdited { get; set; }
		public string formattedDateUploaded { get; set; }
		public string formattedMetadataLastEdited { get; set; }
		public int organisationID { get; set; }
		public bool conditionsAccepted { get; set; }
		public string datasetKey { get; set; }
		public string publicResolution { get; set; }
		public bool allowRecordValidation { get; set; }
		public bool publicAttribute { get; set; }
		public bool publicRecorder { get; set; }
		public int recordCount { get; set; }
		public int speciesCount { get; set; }
	}
}
]]></script>

<script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

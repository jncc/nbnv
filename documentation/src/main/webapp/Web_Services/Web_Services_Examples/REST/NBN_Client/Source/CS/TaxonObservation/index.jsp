<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
<h1>TaxonObservation Source Code C#</h1>
<script type="syntaxhighlighter" class="brush: csharp; html-script: false"><![CDATA[
namespace NBNAPI.REST
{
    public class TaxonObservation
    {
        public int observationID { get; set; }
        public bool fullVersion { get; set; }
        public string datasetKey { get; set; }
        public string surveyKey { get; set; }
        public string sampleKey { get; set; }
        public string observationKey { get; set; }
        public int featureID { get; set; }
        public string location { get; set; }
        public string resolution { get; set; }
        public string siteKey { get; set; }
        public string siteName { get; set; }
        public string taxonVersionKey { get; set; }
        public string pTaxonVersionKey { get; set; }
        public string pTaxonName { get; set; }
        public string pTaxonAuthority { get; set; }
        public string startDate { get; set; }
        public string endDate { get; set; }
        public bool sensitive { get; set; }
        public bool absence { get; set; }
        public string dateTypekey { get; set; }
    }
}
]]></script>

<script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

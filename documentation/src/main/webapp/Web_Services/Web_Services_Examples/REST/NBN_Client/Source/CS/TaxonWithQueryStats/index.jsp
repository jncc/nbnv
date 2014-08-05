<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
<h1>TaxonWithQueryStats and Taxon Source Code C#</h1>
<h2>TaxonWithQueryStats</h2>
<script type="syntaxhighlighter" class="brush: csharp; html-script: false"><![CDATA[
namespace NBNAPI.REST
{
	public class TaxonWithQueryStats
	{
		public string taxonVersionKey { get; set; }
		public int querySpecificObservationCount { get; set; }
		public Taxon taxon { get; set; }
	}
}
]]></script>
<h2>Taxon</h2>
<script type="syntaxhighlighter" class="brush: csharp; html-script: false"><![CDATA[
namespace NBNAPI.REST
{
	public class Taxon
	{
		public string taxonVersionKey { get; set; }
        public string organismKey { get; set; }
		public string name { get; set; }
		public string authority { get; set; }
        public string rank { get; set; }
        public string taxonOutputGroupKey { get; set; }
        public string taxonOutputGroupName  { get; set; }
        public string ptaxonVersionKey { get; set; }
        public string commonNameTaxonVersionKey { get; set; }
        public string commonName { get; set; }
        public string href { get; set; }
		public string languageKey { get; set; }
        public string nameStatus { get; set; }
		public string versionForm { get; set; }
		public int gatewayRecordCount { get; set; }
	}
}
]]></script>

<script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

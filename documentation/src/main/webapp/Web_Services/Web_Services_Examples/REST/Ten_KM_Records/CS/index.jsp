<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Records for a Ten Km Square C#</h1>
        <a href="Source_Code" target="_blank"><h2>Page Source Code</h2></a>
        <p>Source code for the required classes:</p>
        <ul>
            <li><a href="../../NBN_Client/Source/CS/TaxonObservation" target="_blank">TaxonObservation</a></li>
        </ul>
        <p>This page simply logs the user in (note you need to replace the placeholders with a real username and password) and then calls the NBNClient method PostRecordsForDatasetAndGridRef. The data returned is then bound to a ListView control.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
        _client.Login("#username#", "#password#");
        //GA000466 in square TL28
        lvRecords.DataSource = _client.PostRecordsForDatasetAndGridRef("GA000466", "TL28");
        lvRecords.DataBind();
        </script>
        <p>For this example, the request is POSTed to the server (see thie discussion of this in the NBNClient description) in anticipation of the next examples that require this method to work properly with a long WKT string.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
        public TaxonObservation[] PostRecordsForDatasetAndGridRef(string datasetKey, string gridReference)
        {
            Uri uri = new Uri("https://data.nbn.org.uk/api/taxonObservations");
            Dictionary<string, string> filters = new Dictionary<string, string>();
            filters.Add("datasetKey", datasetKey);
            filters.Add("gridRef", gridReference);
            return PostData<TaxonObservation>(uri, filters);
        }
        </script>
        <p>Instead of attaching the filters directly as a querystring to the URI, they are passed in as a Dictionary to the PostData method of the NBNClient, which creates the form data for the request.</p>
        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

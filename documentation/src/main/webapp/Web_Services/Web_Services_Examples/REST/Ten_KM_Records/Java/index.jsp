<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Records for a Ten Km Square Java</h1>
        <a href="Source_Code" target="_blank"><h2>Page Source Code</h2></a>
        <ul>
            <li><a href="../../NBN_Client/Source/Java/TaxonObservation" target="_blank">TaxonObservation</a></li>
        </ul>

        <p>This page simply logs the user in (note you need to replace the placeholders with a real username and password) and then calls the NBNClient method PostRecordsForDatasetAndGridRef. The data returned is then output to a table.</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: true">
        <\%
                NBNClient nbnClient = new NBNClient();
                nbnClient.login("#USERNAME#", "#PASSWORD#");
                TaxonObservation[] observations = nbnClient.postRecordsForDatasetAndGridRef("GA000466", "TL28");
        %>
        </script>
        <p>For this example, the request is POSTed to the server (see thie discussion of this in the NBNClient description) in anticipation of the next examples that require this method to work properly with a long WKT string. This resource returns an array of TaxonObservation</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: false">
        public TaxonObservation[] postRecordsForDatasetAndGridRef(String datasetKey, String gridReference)
        {
                TaxonObservation[] data = null;
                try {
                        URI uri = new URI("https://data.nbn.org.uk/api/taxonObservations");
                        MultivaluedMap<String, String> filters = new MultivaluedMapImpl();
                    filters.add("datasetKey", datasetKey);
                    filters.add("gridRef", gridReference);
                    String json = postJSON(uri, filters);
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    data = mapper.readValue(json, TaxonObservation[].class);
                } catch (Exception ex) {
                        ex.printStackTrace();
                }
            return data;
        }
        </script>
        <p>Instead of attaching the filters directly as a querystring to the URI, they are passed in as a MultivaluedMap to the postJSON method of the NBNClient, which creates the form data for the request. Then the Jackon ObjectMapper is used to deserialize the JSON to the TaxonObservation array. The results are output to a table in the same way as previous examples.</p>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Ten km Species List Java</h1>
        <a href="Source_Code" target="_blank"><h2>Page Source Code</h2></a>
        <p>Source code for the required classes:</p>
        <ul>
            <li><a href="../../NBN_Client/Source/Java/TaxonWithQueryStats" target="_blank">TaxonWithQueryStats and Taxon</a></li>
            <li><a href="../../NBN_Client/Source/Java/TaxonDatasetWithQueryStats" target="_blank">TaxonDatasetWithQueryStats and TaxonDataset</a></li>
        </ul>
        <p>To get the data from the NBN API, we import the NBNClient package into our page and then call two of its methods GetTenKmSpecies and GetTenKmDatasets with the ten km grid reference set as a parameter.</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: true"><![CDATA[
        <\%@ page import="nbnapi.*" %>

        <\%
            NBNClient nbnClient = new NBNClient();
            TaxonWithQueryStats[] species = nbnClient.getTenKmSpecies("SO21");
            TaxonDatasetWithQueryStats[] datasets = nbnClient.getTenKmDatasets("SO21");
        %>
        ]]></script>
        <p>These methods (and the NBN API resources) return two different types: TaxonWithQueryStats and TaxonDatasetWithQueryStats. As their names suggest, TaxonWithQueryStats and TaxonDatasetWithQueryStats are composed of a Taxon or TaxonDataset and statistics on the number of observations for that taxon or dataset that were filtered by the search parameters supplied.</p>
        <p>As described earlier, the NBNClient methods are quite simple. The NBN API URI consists of a base resource location and the grid reference filter in the querystring. The method getJSON is called with the URI, and the JSON in the response is deserialized into an array of the appropriate type using the Jackson ObjectMapper class.</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: false"><![CDATA[
        public TaxonWithQueryStats[] getTenKmSpecies(String gridReference)
        {
            TaxonWithQueryStats[] data = null;
            try {
                URI uri = new URI(String.format("https://data.nbn.org.uk/api/taxonObservations/species?gridRef=%s", gridReference));
                String json = getJSON(uri);
                data = new ObjectMapper().readValue(json, TaxonWithQueryStats[].class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return data;
        }
        ]]></script>
        <p>The species list and dataset list will be rendered as two tables on the page. The template is created in HTML and a table row is created for each record returned. The contents of each cell in the row is set to the appropriate property of the record.</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: true"><![CDATA[
        <h3>Species List</h3>
        <table class="nbnData">
            <thead>
                <tr>
                    <th>Taxon Name</th>
                    <th>Taxon Authority</th>
                    <th>TaxonVersionKey</th>
                    <th>Taxon Group</th>
                    <th>Rank</th>
                    <th>Number of records</th>
                </tr>
            </thead>
            <tbody>
            <\%
                for (TaxonWithQueryStats item : species) {
                %>
                <tr>
                    <td>
                        <\%= item.getTaxon().getName() %>
                    </td>
                    <td>
                        <\%= item.getTaxon().getAuthority() %>
                    </td>
                    <td>
                        <\%= item.getTaxon().getTaxonVersionKey() %>
                    </td>
                    <td>
                        <\%= item.getTaxon().getTaxonOutputGroupName() %>
                    </td>
                    <td>
                        <\%= item.getTaxon().getRank() %>
                    </td>
                    <td>
                        <\%= item.getQuerySpecificObservationCount() %>
                    </td>
                </tr>
                <\%
                }
            %>
            </tbody>
        </table>
        ]]></script>
        <p>The number of taxa and datasets returned by this filter are very large. For a useful web page, you would probably want to introduce more filters (for example species group, dataset) to reduce this to a readable list. The next example demonstrates how to supply multiple search parameters in a single request.</p>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

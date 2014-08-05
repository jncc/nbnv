<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Ten km Species List PHP</h1>
        <a href="Source_Code" target="_blank"><h2>Page Source Code</h2></a>
        <p>To get the data from the NBN API, we include the NBNClient class in our page and then call two of its methods GetTenKmSpecies and GetTenKmDatasets with the ten km grid reference set as a parameter.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: true"><![CDATA[
        <?php
        require_once "NBNClient.php";

        $nbnClient = new NBNClient();
        $species = $nbnClient->GetTenKmSpecies("SO21");
        $datasets = $nbnClient->GetTenKmDatasets("SO21");
        ?>
        ]]></script>
        <p>The two NBN API resources return JSON with different structures: TaxonWithQueryStats and TaxonDatasetWithQueryStats. As their names suggest, TaxonWithQueryStats and TaxonDatasetWithQueryStats are composed of a Taxon or TaxonDataset and statistics on the number of observations for that taxon or dataset that were filtered by the search parameters supplied.</p>
        <p>As described earlier, these methods are quite simple. The base NBN API url is concatenated with the grid reference and the NBN Client CurlGetString method is called. Then the JSON in the response is decoded to an array and returned.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false"><![CDATA[
        function GetTenKmSpecies($gridReference)
        {
            $url = "https://data.nbn.org.uk/api/taxonObservations/species?gridRef=" . $gridReference;
            $json = $this->CurlGetString($url);
            return json_decode($json);
        }
        ]]></script>
        <p>The species list and dataset list will be rendered as two tables on the page. The template is created in HTML and a table row is created for each record returned. The contents of each cell in the row is set to the appropriate property of the record. If no records are returned (the array is NULL) then a message is shown.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: true"><![CDATA[
        <h3>Species List</h3>
        <table class="nbnData">
            <thead>
                <tr>
                    <th>Taxon Name</th>
                    <th>Taxon Authority</th>
                    <th>TaxonVersionKey</th>
                    <th>Rank</th>
                    <th>Number of records</th>
                </tr>
            </thead>
            <tbody>
            <?php if($species == NULL) { ?>
                <tr>
                    <td>
                        No records
                    </td>
                </tr>
            <?php } else { ?>
            <?php foreach($species as $record) { ?>
                <tr>
                    <td>
                        <?=$record->taxon->name?>
                    </td>
                    <td>
                        <?php if(property_exists($record->taxon, "authority")) { ?>
                        <?=$record->taxon->authority?>
                        <?php } ?>
                    </td>
                    <td>
                        <?=$record->taxon->taxonVersionKey?>
                    </td>
                    <td>
                        <?=$record->taxon->rank?>
                    </td>
                    <td>
                        <?=$record->querySpecificObservationCount?>
                    </td>
                </tr>
            <?php } ?>
            <?php } ?>
            </tbody>
        </table>
        ]]></script>
        <p>The number of taxa and datasets returned by this filter are very large. For a useful web page, you would probably want to introduce more filters (for example species group, dataset) to reduce this to a readable list. The next example demonstrates how to supply multiple search parameters in a single request.</p>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

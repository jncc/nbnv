<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Records for a User-Defined Polygon PHP</h1>
        <a href="Source_Code" target="_blank"><h2>Page Source Code</h2></a>

        <p>Since all of the complexity around geometry is handled by OpenLayers and the accompanying JavaScript, the code required for this page is relatively simple and very similar to previous examples.</p>
        <p>At the top of the page, the defaults are set for the controls and the dropdown list for designations is populated using the NBNClient GetDesignations method. The start year is set to 1990, the end year to this year, and the default designation to UK BAP.</p>
        <p>If the page is a postback, these values are overwritten with the user's selection, including the WKT for the geometry that has been created (for this simple example it is assumed the user created a polygon before clicking Refresh). Then the NBNClient logs the user in (change the placeholder values to a real username and password) and calls PostRecordsForWKTDesignationDatasetDates.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: true">
        <?php
        require_once "NBNClient.php"; 

        $nbnClient = new NBNClient();
        $designations = $nbnClient->GetDesignations();
        $startYear = "1990";
        $endYear = date("Y");
        $designation = "BAP-2007";
        $wkt = "";

        //if the form has been posted back, get the species and datasets
        if($_SERVER["REQUEST_METHOD"] == "POST")
        {
            $wkt = $_POST["hfWKT"];
            $designation = $_POST["designations"];
            $startYear = $_POST["startYear"];
            $endYear = $_POST["endYear"];
            $datasetKey = "GA000091";

            $nbnClient->login("#USERNAME#", "#PASSWORD#");
            $records = $nbnClient->PostRecordsForWKTDesignationDatasetDates($wkt, $designation, $datasetKey, $startYear, $endYear);
        }
        ?>
        </script>
        <p>PostRecordsForWKTDesignationDatasetDates supplies its parameters to CurlPostData as an array. Note that the WKT can be passed in in the same way as any other parameter.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false">
        function PostRecordsForWKTDesignationDatasetDates($wkt, $designation, $datasetkey, $startyear, $endyear)
        {
            $url = "https://data.nbn.org.uk/api/taxonObservations";
            $postData = array();
            $postData["polygon"] = $wkt;
            $postData["designation"] = $designation;
            $postData["datasetKey"] = $datasetkey;
            $postData["startYear"] = $startyear;
            $postData["endYear"] = $endyear;
            $json = $this->CurlPostData($url, $postData);
            return json_decode($json);
        }
        </script>
        <p>Finally, the JSON is converted to a PHP object and returned to the page, where it is displayed as a table in the usual way. A row for each record is created and the appropriate properties are displayed in table cells.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: true">
        <table class="nbnData">
            <thead>
                <tr>
                    <th>DatasetKey</th>
                    <th>Observation ID</th>
                    <th>Observation Key</th>
                    <th>Location</th>
                    <th>Resolution</th>
                    <th>TaxonVersionKey</th>
                    <th>Preferred TaxonVersionKey</th>
                    <th>Preferred Taxon Name</th>
                    <th>Preferred Taxon Authority</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Date Type</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach($records as $record) { ?>
                <tr>
                    <td>
                        <a href="https://data.nbn.org.uk/Datasets/<?=$record->datasetKey?>"><?=$record->datasetKey?></a>
                    </td>
                    <td>
                        <?=$record->observationID?>
                    </td>
                    <td>
                        <?=$record->observationKey?>
                    </td>
                    <td>
                        <?=$record->location?>
                    </td>
                    <td>
                        <?=$record->resolution?>
                    </td>
                    <td>
                        <?=$record->taxonVersionKey?>
                    </td>
                    <td>
                        <?=$record->pTaxonVersionKey?>
                    </td>
                    <td>
                        <?=$record->pTaxonName?>
                    </td>
                    <td>
                        <?=$record->pTaxonAuthority?>
                    </td>
                    <td>
                        <?=$record->startDate?>
                    </td>
                    <td>
                        <?=$record->endDate?>
                    </td>
                    <td>
                        <?=$record->dateTypekey?>
                    </td>
                </tr>
                <?php } ?>
            </tbody>
        </table>
        </script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

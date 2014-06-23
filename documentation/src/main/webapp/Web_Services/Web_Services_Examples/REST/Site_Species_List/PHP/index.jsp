<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Site Species List PHP</h1>
        <a href="Source_Code" target="_blank"><h2>Page Source Code</h2></a>
        <p>When this page is first requested, the user can select the vice county, taxon group and designation they wish to use as a filter on the request. The data for the dropdown lists are populated by calls to the NBNClient when the page loads.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false"><![CDATA[
        $nbnClient = new NBNClient();
        $viceCounties = $nbnClient->GetViceCounties();
        $taxonOutputGroups = $nbnClient->GetTaxonOutputGroups();
        $designations = $nbnClient->GetDesignations();
        </script>
        <p>Again, each of these methods are very simple within the NBNClient, following the same pattern as before.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false"><![CDATA[
        function GetViceCounties()
        {
            $url = "https://data.nbn.org.uk/api/siteBoundaryDatasets/GA000344/siteBoundaries";
            $json = $this->CurlGetString($url);
            return json_decode($json);
        }
        </script>
        <p>The data is then used to populate the dropdowns</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: true">
        <select name="viceCounties">
            <!-- Iterate the viceCounties collection adding an option for each -->
            <?php foreach ($viceCounties as $value) { ?>
                <option value="<?=$value->identifier?>"><?=$value->name?></option>
            <?php } ?>
        </select>
        </script>
        <p>When the user clicks refresh. The page is posted back to the server. Note that the action of the page is to post back to itself.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: true">
        <form method="post" action="SiteSpeciesList.php">
        </script>
        <p>If the page is being created as a result of a postback, we can detect this, using the $_SERVER array and checking the request method. If the page request is a result of a POST, then we know to get the filtered list of species and datasets from the NBN API via the NBNClient.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false"><![CDATA[
        //if the form has been posted back, get the species and datasets
        if($_SERVER["REQUEST_METHOD"] == "POST")
        {
            //Get the form variables from $_POST
            $viceCounty = $_POST["viceCounties"];
            $taxonOutputGroup = $_POST["taxonOutputGroups"];
            $designation = $_POST["designations"];

            //Get the species and datasets from the api wrapper
            $siteSpecies = $nbnClient->GetSiteSpecies($viceCounty, $taxonOutputGroup, $designation);
            $siteDatasets = $nbnClient->GetSiteDatasets($viceCounty, $taxonOutputGroup, $designation);
        }
        ]]></script>
        <p>The GetSiteSpecies and GetSiteDatasets methods each take three parameters that are the values of the DropDownLists representing the user's selection. The NBNClient adds these parameters to the URI of the request, first checking that a designation has been selected. (Note, you need to use identifier for the site boundary filter, not featureID in the JSON).</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false">
        function GetSiteSpecies($viceCounty, $taxonOutputGroup, $designation)
        {
            $url = "https://data.nbn.org.uk/api/taxonObservations/species?featureID=" . $viceCounty . "&taxonOutputGroup=" . $taxonOutputGroup;
            if(!(strlen($designation) == 0))
            {
                $url = $url . "&designation=" . $designation;
            }
            $json = $this->CurlGetString($url);
            return json_decode($json);
        }
        </script>
        <p>Then the species list and dataset tables can be created in the same way as the ten km species list example.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: true"><![CDATA[
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
            <?php foreach($siteSpecies as $species) { ?>
                <tr>
                    <td>
                        <?=$species->taxon->name?>
                    </td>
                    <td>
                        <?=$species->taxon->authority?>
                    </td>
                    <td>
                        <?=$species->taxon->taxonVersionKey?>
                    </td>
                    <td>
                        <?=$species->taxon->rank?>
                    </td>
                    <td>
                        <?=$species->querySpecificObservationCount?>
                    </td>
                </tr>
            <?php } ?>
            </tbody>
        </table>
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

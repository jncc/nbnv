<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Site Species List Source Code PHP</h1>
        <h2>PHP Markup</h2>
        <script type="syntaxhighlighter" class="brush: php; html-script: true"><![CDATA[
        <?php
        require_once "NBNClient.php"; 

        $nbnClient = new NBNClient();
        $viceCounties = $nbnClient->GetViceCounties();
        $taxonOutputGroups = $nbnClient->GetTaxonOutputGroups();
        $designations = $nbnClient->GetDesignations();

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
        ?>

        <!DOCTYPE html>

        <html xmlns="http://www.w3.org/1999/xhtml">
        <head runat="server">
            <title>Species List for a Vice County</title>
            <link rel="stylesheet" href="../css/style.css" />
        </head>
        <body>
            <h1>Species List for a Vice County</h1>
            <form method="post" action="SiteSpeciesList.php">

                <select name="viceCounties"> 
                <!-- Iterate the viceCounties collection adding an option for each -->
                    <?php foreach ($viceCounties as $value) { ?>
                        <option value="<?=$value->identifier?>"><?=$value->name?></option>
                    <?php } ?>
                </select>

                <select name="taxonOutputGroups">
                    <!-- Again for the taxonOutputGroups -->
                    <?php foreach($taxonOutputGroups as $value) { ?>
                        <option value="<?=$value->key?>"><?=$value->name?></option>
                    <?php } ?>
                </select>

                <select name="designations">
                    <!-- designations does not need to be set, add an empty value -->
                    <option value="" />
                    <!-- and once more for the designations -->
                    <?php foreach($designations as $value) { ?>
                        <option value=<?=$value->code?>><?=$value->name?></option>
                    <?php } ?>
                </select>

                <input type="submit" name="refresh" value="Refresh" />
            </form>

            <!-- Only display the following if the form has been posted back -->
            <?php if($_SERVER["REQUEST_METHOD"] == "POST") { ?>
            <h3>Species List</h3>
            <!-- if no results, merely report that fact, otherwise put it in a table -->
            <?php if($siteSpecies == NULL) { ?>
            No records
            <?php } else { ?>
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
            <?php } ?>

            <!-- Repeat for the datasets -->
            <h3>Dataset List</h3>
            <?php if($siteDatasets == NULL) { ?>
            No records
            <?php } else { ?>
            <table class="nbnData">
                <thead>
                    <tr>
                        <th>DatasetKey</th>
                        <th>Title</th>
                        <th>Data Provider</th>
                        <th>Number of records</th>
                    </tr>
                </thead>
                <tbody>
                <?php foreach($siteDatasets as $dataset) { ?>
                    <tr>
                        <td>
                            <a href="https://data.nbn.org.uk/Datasets/<?=$dataset->datasetKey?>"><?=$dataset->datasetKey?></a>
                        </td>
                        <td>
                            <?=$dataset->taxonDataset->title?>
                        </td>
                        <td>
                            <?=$dataset->taxonDataset->organisationName?>
                        </td>
                        <td>
                            <?=$dataset->querySpecificObservationCount?>
                        </td>
                    </tr>
                <?php } ?>
                </tbody>
            </table>
            <?php } ?>

            <?php } ?>
        </body>
        </html>
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

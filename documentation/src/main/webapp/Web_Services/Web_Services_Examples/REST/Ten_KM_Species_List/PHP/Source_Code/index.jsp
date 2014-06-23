<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Ten Km Species List Source Code PHP</h1>
        <h2>PHP Markup</h2>
        <script type="syntaxhighlighter" class="brush: php; html-script: true"><![CDATA[
        <?php
        require_once "NBNClient.php";

        $nbnClient = new NBNClient();
        $species = $nbnClient->GetTenKmSpecies("SO21");
        $datasets = $nbnClient->GetTenKmDatasets("SO21");
        ?>

        <!DOCTYPE html>

        <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <title>Species List for a Ten Km Square</title>
            <link rel="stylesheet" href="../css/style.css" />
        </head>
        <body>
            <h1>Species List for a Ten Km Square</h1>
            <h3>Ten Km Square: SO21</h3>
            <div>
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

                <h3>Dataset List</h3>
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
                    <?php if($species == NULL) { ?>
                        <tr>
                            <td>
                                No records
                            </td>
                        </tr>
                    <?php } else { ?>
                    <?php foreach($datasets as $dataset) { ?>
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
                    <?php } ?>
                    </tbody>
                </table>
            </div> 
        </body>
        </html>
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

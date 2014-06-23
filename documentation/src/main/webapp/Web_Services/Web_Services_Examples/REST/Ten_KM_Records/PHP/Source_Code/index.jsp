<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h2>PHP Markup</h2>
        <script type="syntaxhighlighter" class="brush: php; html-script: true"><![CDATA[
        <?php
        require_once "NBNClient.php"; 

        $nbnClient = new NBNClient();
        $nbnClient->login("#USERNAME#", "#PASSWORD#");
        $records = $nbnClient->PostRecordsForDatasetAndGridRef("GA000466", "TL28");
        ?>

        <!DOCTYPE html>

        <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <title>Records for a Ten Km Square</title>
            <link rel="stylesheet" href="../css/style.css" />
        </head>
        <body>
            <h1>Records for a Ten Km Square</h1>
            <h3>Ten Km Square: TL28</h3>
            <h3>Dataset: <a href="https://data.nbn.org.uk/Datasets/GA000466">GA000466</a>: Demonstration dataset for record access on the NBN Gateway</h3>
            <div>
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
                    <?php if($records == NULL) { ?>
                        <tr>
                            <td>
                                No records
                            </td>
                        </tr>
                    <?php } else { ?>
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

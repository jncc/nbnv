<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Taxon Search Source Code PHP</h1>
        <h2>TaxonSearch PHP Markup</h2>
        <script type="syntaxhighlighter" class="brush: php; html-script: true"><![CDATA[
        <?php
        require_once "NBNClient.php"; 

        $nbnClient = new NBNClient();

        $query = "";
        if($_SERVER["REQUEST_METHOD"] == "POST")
        {
            $query = $_POST["searchQuery"];
            $searchResults = $nbnClient->GetTaxonSearchResult($query);
        }
        ?>


        <!DOCTYPE html>

        <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <title>Taxon Search</title>
            <link rel="stylesheet" href="../css/style.css" />
        </head>
        <body>
            <h1>Taxon Search</h1>
            <form method="post" action="TaxonSearch.php">
                <label for="searchQuery">Search for a taxon:</label>
                <input type="text" id="searchQuery" name="searchQuery" value="<?=$query?>" />
                <input type="submit" value="Submit" />
            </form>

            <!-- Only display the following if the form has been posted back -->
            <?php if($_SERVER["REQUEST_METHOD"] == "POST") { ?>
            <h3>Species List</h3>
            <!-- if no results, merely report that fact, otherwise list them -->
            <?php if($searchResults == NULL) { ?>
            No records
            <?php } else { ?>
            <?php foreach($searchResults->results as $sr) { ?>
            <p>
                <a href="./Taxonomy.php?taxonVersionKey=<?=$sr->taxonVersionKey?>"><?=$sr->name?></a>
            </p>
            <?php } ?>
            <?php } ?>
            <?php } ?>
        </body>
        </html>
        ]]></script>

        <h2>Taxonomy PHP Markup</h2>
        <script type="syntaxhighlighter" class="brush: php; html-script: true"><![CDATA[
        <?php
        //create an instance of our api wrapper with username/password
        require_once "NBNClient.php"; 
        $nbnClient = new NBNClient();

        $taxonVersionKey = $_GET["taxonVersionKey"];

        $taxon = $nbnClient->GetTaxonomy($taxonVersionKey);
        $taxonName = $taxon->name;
        if($taxon->taxonVersionKey != $taxon->ptaxonVersionKey)
        {
            $preferredTaxon = $nbnClient->GetTaxonomy($taxon->ptaxonVersionKey);
            $taxonName .= " (".$preferredTaxon->name.")";
        }
        $parents = $nbnClient->GetTaxonParents($taxonVersionKey);
        $children = $nbnClient->GetTaxonChildren($taxonVersionKey);
        ?>

        <!DOCTYPE html>

        <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <title>Taxonomy</title>
            <link rel="stylesheet" href="../css/style.css" />
        </head>
        <body>
                <h2>Taxonomy for <?=$taxonName?></h2>
                <h2>Parents:</h2>
                <?php if($parents == NULL) { ?>
                No parents
                <?php } else { ?>
                <?php foreach($parents as $parent) { ?>
                <p><span><?=$parent->rank?></span> : <span><?=$parent->name?></span></p>
                <?php } ?>
                <?php } ?>

                <h2>Children:</h2>
                <?php if($children == NULL) { ?>
                No children
                <?php } else { ?>
                <?php foreach($children as $child) { ?>
                <p><span><?=$child->name?></span></p>
                <?php } ?>
                <?php } ?>
        </body>
        </html>
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

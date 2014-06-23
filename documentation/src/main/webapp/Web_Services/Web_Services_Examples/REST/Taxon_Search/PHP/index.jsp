<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Taxon Search PHP</h1>
        <a href="Source_Code" target="_blank"><h2>Page Source Code</h2></a>
        <p>TaxonSearch is a simple page with a TextBox for entering a search term and a button for starting the search. When clicked, the page calls the NBNClient GetTaxonSearchResult with the text of the search as a parameter.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: true"><![CDATA[
        <form method="post" action="TaxonSearch.php">
            <label for="searchQuery">Search for a taxon:</label>
            <input type="text" id="searchQuery" name="searchQuery" value="<?=$query?>" />
            <input type="submit" value="Submit" />
        </form>
        ]]></script>
        <p>As in the previous example, postback can be detected using the $_SERVER array and checking the request method.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: true"><![CDATA[
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
        ]]></script>
        <p>The returned results are then converted to a list of links to the Taxonomy page. The taxon version key for each search result is evaluated and added to the querystring of the Taxonomy page in the href attribute of the link. The taxon name is evaluated for the display text.</p>
        <p>The NBNClient GetTaxonSearchResult method follows the usual pattern of specifying the URL and calling CurlGetString. A <a href="http://en.wikipedia.org/wiki/Apache_Solr" target="_blank">Solr</a> search result is returned that contains the results.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false">
        function GetTaxonSearchResult($query)
        {
            $url = "https://data.nbn.org.uk/api/taxa?q=" . $query;
            $json = $this->CurlGetString($url);
            return json_decode($json);
        }
        </script>
        <h2>Taxonomy</h2>
        <p>The taxonomy page displays the taxonomy for the taxon specified by the taxon version key in the page's querystring. It consists of the taxon name (and preferred name if applicable) and two lists of the parents and children of the taxon respectively.</p>
        <p>The page calls the NBNClient GetTaxonomy method to get the taxon name. It also checks whether the taxon is a preferred taxon. If not, the preferred taxon name is looked up with the same method using the taxon's preferred taxon version key.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false">
        $nbnClient = new NBNClient();

        $taxonVersionKey = $_GET["taxonVersionKey"];

        $taxon = $nbnClient->GetTaxonomy($taxonVersionKey);
        $taxonName = $taxon->name;
        if($taxon->taxonVersionKey != $taxon->ptaxonVersionKey)
        {
            $preferredTaxon = $nbnClient->GetTaxonomy($taxon->ptaxonVersionKey);
            $taxonName .= " (".$preferredTaxon->name.")";
        }
        </script>

        <p>Two other NBNClient methods get the parents and children of the selected taxon respectively.</p>
        <p>The parent of a taxon can be queried from the NBN API using the parent resource for a given taxon version key, this is the method GetTaxonParent. To get all higher taxa for a taxon, we need to get the taxon's immediate parent and then its parent's parent and so on, until there are no more results returned. This is done in the GetTaxonParents method of the NBNClient:</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false">
        function GetTaxonParent($taxonVersionKey)
        {
            $url = "https://data.nbn.org.uk/api/taxa/" . $taxonVersionKey . "/parent";
            $json = $this->CurlGetString($url);
            return json_decode($json);
        }

        function GetTaxonParents($taxonVersionKey)
        {
            $parents = array();
            do
            {
                $parent = $this->GetTaxonParent($taxonVersionKey);
                if($parent != NULL)
                {
                    $taxonVersionKey = $parent->taxonVersionKey;
                    $parents[] = $parent;
                }
            }
            while($parent != NULL);
            $parents = array_reverse($parents);
            return $parents;
        }
        </script>
        <p>If each parent, going up the taxonmic hierarchy, isn't null then it's parent is queried until there are no more parents. To make displaying the results more logical, this array is reversed (so the top of the hierarchy is first) before being returned to the web page.</p>
        <p>The page only queries the immediate children of the taxon via GetTaxonChildren, using the children resource for the taxon version key.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false">
        function GetTaxonChildren($taxonVersionKey)
        {
            $url = "https://data.nbn.org.uk/api/taxa/" . $taxonVersionKey . "/children";
            $json = $this->CurlGetString($url);
            return json_decode($json);
        }
        </script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

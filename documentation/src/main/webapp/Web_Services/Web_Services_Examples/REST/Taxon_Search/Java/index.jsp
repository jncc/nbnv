<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Taxon Search Java</h1>
        <a href="Source_Code" target="_blank"><h2>Page Source Code</h2></a>
        <p>Source code for the required classes:</p>
        <ul>
            <li><a href="../../NBN_Client/Source/Java/SearchResult" target="_blank">SearchResult and Result</a></li>
            <li><a href="../../NBN_Client/Source/Java/TaxonWithQueryStats" target="_blank">TaxonWithQueryStats and Taxon</a></li>
        </ul>
        <p>TaxonSearch is a simple page with a TextBox for entering a search term and a button for starting the search. When clicked, the page calls the NBNClient GetTaxonSearchResult with the text of the search as a parameter.</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: true">
        <\%
            boolean isPostBack = ("POST".equalsIgnoreCase(request.getMethod()));
            String searchTerm = "";
            SearchResult searchResults = null;
            if (isPostBack) {
                searchTerm = request.getParameter("searchTerm");
                NBNClient nbnClient = new NBNClient();
                searchResults = nbnClient.getTaxonSearchResult(searchTerm);
            }
        %>
        </script>
        <p>As in the previous example, postback is detected before calling the NBNClient.</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: true"><![CDATA[
        <form action="TaxonSearch.jsp" method="post">
            <div>
                <h1>Taxon Search</h1>
                Search for a taxon:
                <input type="text" name="searchTerm" value=<\%= searchTerm %>>
                <button type="submit" name="btnSearch" value="Search">Search</button>
                <\% if(isPostBack) { %>
                <h3>Search Results</h3>
                <div>
                    <\% if (searchResults == null) { %>
                        No results found
                    <\% } else {
                        for (Result item : searchResults.getResults()) {
                    %>
                        <p>
                            <a href="./Taxonomy.jsp?taxonVersionKey=<\%= item.getTaxonVersionKey() %>"><\%= item.getName() %></a>
                        </p>
                    <\%
                        }
                    }
                    %>
                </div>
                <\% } %>
            </div>
        </form>
        ]]></script>
        <p>The returned results are converted to a list of links to the Taxonomy page. The taxon version key for each search result is evaluated and added to the querystring of the Taxonomy page in the href attribute of the link. The taxon name is evaluated for the display text.</p>
        <p>The NBNClient GetTaxonSearchResult method follows the usual pattern of specifying the URI and using the Jackson ObjectMapper to deserialize the JSON to the correct data type (in this case SearchResult). The NBN API uses <a href="http://en.wikipedia.org/wiki/Apache_Solr" target="_blank">Solr</a> for text searches.</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: false">
        public SearchResult getTaxonSearchResult(String query)
        {
            SearchResult data = null;
            try {
                URI uri = new URI(String.format("https://data.nbn.org.uk/api/taxa?q=%s", query));
                String json = getJSON(uri);
                data = new ObjectMapper().readValue(json, SearchResult.class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return data;
        }
        </script>
        <h2>Taxonomy</h2>
        <p>The taxonomy page displays the taxonomy for the taxon specified by the taxon version key in the page's querystring. It consists of the taxon name (and preferred name if applicable) and two lists of the parents and children of the taxon respectively.</p>
        <p>The page calls the NBNClient GetTaxonomy method to get the taxon name. It also checks whether the taxon is a preferred taxon. If not, the preferred taxon name is looked up with the same method using the taxon's preferred taxon version key.</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: true">
        <\%
            String taxonVersionKey = request.getParameter("taxonVersionKey");
            NBNClient nbnClient = new NBNClient();
            Taxon taxon = nbnClient.getTaxonomy(taxonVersionKey);
            String title = taxon.getName();
            if (taxon.getTaxonVersionKey() != taxon.getPtaxonVersionKey())
            {
                Taxon preferredTaxon = nbnClient.getTaxonomy(taxon.getPtaxonVersionKey());
                title += " (" + preferredTaxon.getName() + ")";
            }
            Taxon[] parents = nbnClient.getTaxonParents(taxonVersionKey);
            Taxon[] children = nbnClient.getTaxonChildren(taxonVersionKey);
        %>
        </script>

        <p>Two other NBNClient methods get the parents and children of the selected taxon respectively.</p>
        <p>The parent of a taxon can be queried from the NBN API using the parent resource for a given taxon version key, this is the method GetTaxonParent. To get all higher taxa for a taxon, we need to get the taxon's immediate parent and then its parent's parent and so on, until there are no more results returned. This is done in the GetTaxonParents method of the NBNClient:</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: false">
        public Taxon getTaxonParent(String taxonVersionKey)
        {
            Taxon data = null;
            try {
                URI uri = new URI(String.format("https://data.nbn.org.uk/api/taxa/%s/parent", taxonVersionKey));
                String json = getJSON(uri);
                data = new ObjectMapper().readValue(json, Taxon.class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return data;
        }

        public Taxon[] getTaxonParents(String taxonVersionKey)
        {
            String tvk = taxonVersionKey;
            List<Taxon> parents = new ArrayList<Taxon>();
            Taxon parent;
            do
            {
                parent = getTaxonParent(tvk);
                if (parent != null)
                {
                    tvk = parent.getTaxonVersionKey();
                    parents.add(parent);
                }
            } while (parent != null);
            Collections.reverse(parents);
            return parents.toArray(new Taxon[0]);
        }
        </script>
        <p>If each parent, going up the taxonmic hierarchy, isn't null then it's parent is queried until there are no more parents. To make displaying the results more logical, this array is reversed (so the top of the hierarchy is first) before being returned to the web page.</p>
        <p>The page only queries the immediate children of the taxon via GetTaxonChildren, using the children resource for the taxon version key.</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: false">
        public Taxon[] getTaxonChildren(String taxonVersionKey)
        {
            Taxon[] data = null;
            try {
                URI uri = new URI(String.format("https://data.nbn.org.uk/api/taxa/%s/children", taxonVersionKey));
                String json = getJSON(uri);
                data = new ObjectMapper().readValue(json, Taxon[].class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return data;
        }
        </script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

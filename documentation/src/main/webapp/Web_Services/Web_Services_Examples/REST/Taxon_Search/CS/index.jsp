<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Taxon Search C#</h1>
        <a href="Source_Code" target="_blank"><h2>Page Source Code</h2></a>
        <p>Source code for the required classes:</p>
        <ul>
            <li><a href="../../NBN_Client/Source/CS/SearchResult" target="_blank">SearchResult and Result</a></li>
            <li><a href="../../NBN_Client/Source/CS/TaxonWithQueryStats" target="_blank">TaxonWithQueryStats and Taxon</a></li>
        </ul>
        <h2>TaxonSearch</h2>
        <p>TaxonSearch is a simple page with a TextBox for entering a search term and a button for starting the search. When clicked, the page calls the NBNClient GetTaxonSearchResult with the text of the search as a parameter.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
        protected void btnSearch_Click(object sender, EventArgs e)
        {
            var searchResults = _client.GetTaxonSearchResult(HttpUtility.HtmlEncode(txtSearch.Text));
            lvResults.DataSource = searchResults.results;
            lvResults.DataBind();
        }
        </script>
        <p>The returned results are then bound to a very simple ASP.NET ListView that contains an ASP.NET LinkButton in the ItemTemplate. The taxon version key for each search result is evaluated and added to the querystring of the Taxonomy page in the PostBackURL of the LinkButton control. The name is evaluated for the display of each LinkButton.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: true">
            <asp:LinkButton ID="lnkTaxonomy" runat="server"
                PostBackUrl='<\%# "~/REST/Taxonomy.aspx?taxonVersionKey=" + Eval("taxonVersionKey") %>'><\%#Eval("name") %>
            </asp:LinkButton>
        </script>
        <p>The GetTaxonSearchResult method doesn't follow the usual pattern of specifying the URI and type to GetData. This is because NBN API searches don't return an array of results as in the other resources used in this tutorial. A single object, a <a href="http://en.wikipedia.org/wiki/Apache_Solr" target="_blank">Solr</a> search result is returned that contains the results. So the method makes the request and deserializes the response itself. But this is in the same way as the other examples.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
        public SearchResult GetTaxonSearchResult(string query)
        {
            Uri uri = new Uri(String.Format("https://data.nbn.org.uk/api/taxa?q={0}", query));
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(uri);
            HttpWebResponse response = (HttpWebResponse)request.GetResponse();
            StreamReader reader = new StreamReader(response.GetResponseStream());
            string json = reader.ReadToEnd();
            JavaScriptSerializer serializer = new JavaScriptSerializer();
            SearchResult data = serializer.Deserialize<SearchResult>(json);
            return data;
        }
        </script>
        <h2>Taxonomy</h2>
        <p>The taxonomy page displays the taxonomy for the taxon specified by the taxon version key in the page's querystring. It consists of a Label control for the taxon name and two ListViews for the taxonomic parents and children of the taxon respectively.</p>
        <p>The code behind uses the NBNClient GetTaxonomy method to get the taxon name. It also checks whether the taxon is a preferred taxon. If not, the preferred taxon name is looked up with the same method using the taxon's preferred taxon version key.</p>
        <p>Two other NBNClient methods get the parents and children of the selected taxon respectively and bind the results to the ListViews.</p>
        <p>The parent of a taxon can be queried from the NBN API using the parent resource for a given taxon version key, this is the method GetTaxonParent. To get all higher taxa for a taxon, we need to get the taxon's immediate parent and then its parent's parent and so on, until there are no more results returned. This is done in the GetTaxonParents method of the NBNClient:</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
        public Taxon GetTaxonParent(string taxonVersionKey)
        {
            Uri uri = new Uri(String.Format("https://data.nbn.org.uk/api/taxa/{0}/parent", taxonVersionKey));
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(uri);
            HttpWebResponse response = (HttpWebResponse)request.GetResponse();
            StreamReader reader = new StreamReader(response.GetResponseStream());
            string json = reader.ReadToEnd();
            JavaScriptSerializer serializer = new JavaScriptSerializer();
            Taxon data = serializer.Deserialize<Taxon>(json);
            return data;
        }

        public Taxon[] GetTaxonParents(string taxonVersionKey)
        {
            string tvk = taxonVersionKey;
            List<Taxon> parents = new List<Taxon>();
            Taxon parent;
            do
            {
                parent = GetTaxonParent(tvk);
                if (parent != null)
                {
                    tvk = parent.taxonVersionKey;
                    parents.Add(parent);
                }
            } while (parent != null);
            parents.Reverse();
            return parents.ToArray();
        }
        </script>
        <p>If each parent, going up the taxonmic hierarchy, isn't null then it's parent is queried until there are no more parents. To facilitate data binding later, this array is reversed (so the top of the hierarchy is first) before being returned to the web page.</p>
        <p>The page only queries the immediate children of the taxon via GetTaxonChildren. Since an array of Taxon is returned, the usual quick way of implementing this can be done in the NBNClient method.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
        public Taxon[] GetTaxonChildren(string taxonVersionKey)
        {
            Uri uri = new Uri(String.Format("https://data.nbn.org.uk/api/taxa/{0}/children", taxonVersionKey));
            return GetData<Taxon>(uri);
        }
        </script>
        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Ten km Species List C#</h1>
        <a href="Source_Code" target="_blank"><h2>Page Source Code</h2></a>
        <p>Source code for the required classes:</p>
        <ul>
            <li><a href="../../NBN_Client/Source/CS/TaxonWithQueryStats" target="_blank">TaxonWithQueryStats and Taxon</a></li>
            <li><a href="../../NBN_Client/Source/Java/TaxonDatasetWithQueryStats" target="_blank">TaxonDatasetWithQueryStats and TaxonDataset</a></li>
        </ul>
        <p>The species list and dataset list will be rendered as two tables on the page. Two ListViews, flexible templated controls, are created in the ASPX markup to display the data. The layout template is set to an HTML table with a placeholder for each data item set to a row of the table. In the ItemTemplate of the control, the content of each table cell in the row is evaluated to properties of the object in the ListView data source, using the Eval expression.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: true"><![CDATA[
        <asp:ListView ID="lvSpecies" runat="server">
            <LayoutTemplate>
                <h3>Species List</h3>
                <table class="nbnData">
                    <thead>
                        <tr>
                            <th>Taxon Name</th>
                            <th>Taxon Authority</th>
                            <th>TaxonVersionKey</th>
                            <th>Taxon Group</th>
                            <th>Rank</th>
                            <th>Number of records</th>
                        </tr>
                    </thead>
                    <tr runat="server" id="itemPlaceHolder"></tr>
                </table>
            </LayoutTemplate>
            <ItemTemplate>
                <tr>
                    <td>
                        <\%#Eval("taxon.name") %>
                    </td>
                    <td>
                        <\%#Eval("taxon.authority") %>
                    </td>
                    <td>
                        <\%#Eval("taxon.taxonVersionKey") %>
                    </td>
                    <td>
                        <\%#Eval("taxon.taxonOutputGroupName") %>
                    </td>
                    <td>
                        <\%#Eval("taxon.rank") %>
                    </td>
                    <td>
                        <\%#Eval("querySpecificObservationCount") %>
                    </td>
                </tr>
            </ItemTemplate>
        </asp:ListView>
        ]]></script>
        <p>In the code behind for the page, first an instance of the NBNClient class is created. Then two methods are called on the NBNClient that return data, which can be bound to the DataSource properties of the ListViews</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false"><![CDATA[
        NBNClient _client;

        protected void Page_Load(object sender, EventArgs e)
        {
            _client = new NBNClient();

            lvSpecies.DataSource = _client.GetTenKmSpecies("SO21");
            lvSpecies.DataBind();

            lvDatasets.DataSource = _client.GetTenKmDatasets("SO21");
            lvDatasets.DataBind();
        }
        ]]></script>
        <p>Thanks to some generalisation of the code, the two methods in the NBNClient are very simple. We need to supply the correct URI for the resource, including the grid reference filter parameter and the C# type that we expect it to return.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false"><![CDATA[
        public TaxonWithQueryStats[] GetTenKmSpecies(string gridReference)
        {
            Uri uri = new Uri(String.Format("https://data.nbn.org.uk/api/taxonObservations/species?gridRef={0}", gridReference));
            return GetData<TaxonWithQueryStats>(uri);
        }

        public TaxonDatasetWithQueryStats[] GetTenKmDatasets(string gridReference)
        {
            Uri uri = new Uri(String.Format("https://data.nbn.org.uk/api/taxonObservations/datasets?gridRef={0}", gridReference));
            return GetData<TaxonDatasetWithQueryStats>(uri);
        }
        ]]></script>
        <p>The resources return TaxonWithQueryStats and TaxonDatasetWithQueryStats. As their names suggest, TaxonWithQueryStats and TaxonDatasetWithQueryStats are composed of a Taxon or TaxonDataset and statistics on the number of observations for that taxon or dataset that were filtered by the search parameters supplied.</p>
        <p>The number of taxa and datasets returned by this filter are very large. For a useful web page, you would probably want to introduce more filters (for example species group, dataset) to reduce this to a readable list. The next example demonstrates how to supply multiple search parameters in a single request.</p>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

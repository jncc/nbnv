<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Site Species List C#</h1>
        <h2>Source Code</h2>
        <a href="Source_Code" target="_blank"><h2>Page Source Code</h2></a>
        <p>Source code for the required classes:</p>
        <ul>
            <li><a href="../../NBN_Client/Source/CS/SiteBoundary" target="_blank">SiteBoundary</a></li>
            <li><a href="../../NBN_Client/Source/CS/TaxonOutputGroup" target="_blank">TaxonOutputGroup</a></li>
            <li><a href="../../NBN_Client/Source/CS/Designation" target="_blank">Designation</a></li>
            <li><a href="../../NBN_Client/Source/CS/TaxonWithQueryStats" target="_blank">TaxonWithQueryStats</a></li>
            <li><a href="../../NBN_Client/Source/CS/TaxonDatasetWithQueryStats" target="_blank">TaxonDatasetWithQueryStats</a></li>
        </ul>
        <p>To create the html select controls for vice county, species group and designation, three ASP.NET DropDownList controls are used. Since designation is optional, an additional empty item is added to the designation list. Note that the value field of the DropDownList is set to the appropriate code or identifier used in the request filter.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: true">
        <asp:DropDownList ID="ddlDesignations" runat="server" DataTextField="name" DataValueField="code" AppendDataBoundItems="true">
            <asp:ListItem Text="" Value=""></asp:ListItem>
        </asp:DropDownList>
        </script>
        <p>When the page loads, these DropDownLists are populated with data returned from the NBNClient:</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
        var designations = _client.GetDesignations();
        ddlDesignations.DataSource = designations;
        ddlDesignations.DataBind();
        </script>
        <p>Again, the internal implementation of GetDesignations() is very simple. Just the URI and return type need to be specified for GetData:</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
        public Designation[] GetDesignations()
        {
            Uri uri = new Uri("https://data.nbn.org.uk/api/designations");
            return GetData<Designation>(uri);
        }
        </script>
        <p>When the user clicks the Refresh button, GetSiteSpecies and GetSiteDatasets are called on the NBN Client and the returned data is bound to the two listviews in the same way as for the ten km species list example.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
        protected void btnRefresh_Click(object sender, EventArgs e)
        {
            var species = _client.GetSiteSpecies(ddlViceCounties.SelectedValue, ddlTaxonOutputGroups.SelectedValue, ddlDesignations.SelectedValue);
            lvSpecies.DataSource = species;
            lvSpecies.DataBind();

            var datasets = _client.GetSiteDatasets(ddlViceCounties.SelectedValue, ddlTaxonOutputGroups.SelectedValue, ddlDesignations.SelectedValue);
            lvDatasets.DataSource = datasets;
            lvDatasets.DataBind();
        }
        </script>
        <p>Note that these two methods each take three parameters that are the values of the DropDownLists representing the user's selection. The NBNClient adds these parameters to the URI of the request, first checking that a designation has been selected. (Note, you need to use identifier for the site boundary filter, not featureID in the JSON and C# class).</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
        // identifier is the featureID for the query (not the featureID of the boundary)
        public TaxonWithQueryStats[] GetSiteSpecies(string featureID, string taxonOutputGroup, string designation)
        {
            string uriString = String.Format("https://data.nbn.org.uk/api/taxonObservations/species?featureID={0}&taxonOutputGroup={1}", featureID, taxonOutputGroup);
            if (!String.IsNullOrWhiteSpace(designation))
            {
                uriString = uriString + String.Format("&designation={0}", designation);
            }
            Uri uri = new Uri(uriString);
            return GetData<TaxonWithQueryStats>(uri);
        }
        </script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

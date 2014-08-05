<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Records for a User-Defined Polygon C#</h1>
        <a href="Source_Code" target="_blank"><h2>Page Source Code</h2></a>
        <p>Source code for the required classes:</p>
        <ul>
            <li><a href="../../NBN_Client/Source/CS/TaxonObservation" target="_blank">TaxonObservation</a></li>
            <li><a href="../../NBN_Client/Source/CS/Designation" target="_blank">Designation</a></li>
        </ul>
        <p>Since all of the complexity around geometry is handled by OpenLayers and the accompanying JavaScript, the code required for this page is relatively simple and very similar to previous examples.</p>
        <p>On the page, an ASP.NET HiddenField control is used for the hidden field that stores the WKT. Be sure to set its ClientIDMode to "Static" so that ASP.NET does not rename it, causing the JavaScript to fail.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: true">
        <asp:HiddenField ID="hfWKT" runat="server" ClientIDMode="Static" />
        </script>
        <p>In the code behind, the designations dropdown is populated in the same way as usual, with a call to the NBNClient GetDesignations method. By default, this is set to the UK BAP list. The start year is set to a default of 1800 and the end year to this year.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
        NBNClient _client = new NBNClient();

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!Page.IsPostBack)
            {
                txtStartYear.Text = "1800";
                txtEndYear.Text = DateTime.Now.Year.ToString();
                ddlDesignation.DataSource = _client.GetDesignations();
                ddlDesignation.DataBind();
                ddlDesignation.SelectedValue = "BAP-2007";
            }
        }
        </script>
        <p>Assuming the user has created a polygon before clicking refresh (and this is assumed for this demonstration page!), the WKT is read from the hidden field, the designation from the dropdown, and the start and end year date filters from two textboxes. Then the NBNClient logs the user in (replace the placeholders with a real username and password) and calls PostRecordsForWKTDesignationDatasetDates. The results are bound to a ListView control.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
        protected void btnRefresh_Click(object sender, EventArgs e)
        {
            string wkt = hfWKT.Value;
            string designation = ddlDesignation.SelectedValue;
            int startYear = Int32.Parse(txtStartYear.Text);
            int endYear = Int32.Parse(txtEndYear.Text);
            string datasetKey = "GA000091";
            _client.Login("#username#", "#password#");
            lvRecords.DataSource = _client.PostRecordsForWKTDesignationDatasetDates(wkt, designation, datasetKey, startYear, endYear);
            lvRecords.DataBind();
        }
        </script>
        <p>As in the previous example, the filters for the request are passed to PostData as a Dictionary. The WKT string is simply passed in in the same way as any other parameter. This resource returns an array of TaxonObservation.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
        public TaxonObservation[] PostRecordsForWKTDesignationDatasetDates(string wkt, string designation, string datasetKey, int startYear, int endYear)
        {
            Uri uri = new Uri("https://data.nbn.org.uk/api/taxonObservations");
            Dictionary<string, string> filters = new Dictionary<string, string>();
            filters.Add("polygon", wkt);
            filters.Add("designation", designation);
            filters.Add("datasetKey", datasetKey);
            filters.Add("startYear", startYear.ToString());
            filters.Add("endYear", endYear.ToString());
            return PostData<TaxonObservation>(uri, filters);
        }
        </script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Site Species List Java</h1>
        <h2>Source Code</h2>
        <a href="Source_Code" target="_blank"><h2>Page Source Code</h2></a>
        <p>Source code for the required classes:</p>
        <ul>
            <li><a href="../../NBN_Client/Source/Java/SiteBoundary" target="_blank">SiteBoundary</a></li>
            <li><a href="../../NBN_Client/Source/Java/TaxonOutputGroup" target="_blank">TaxonOutputGroup</a></li>
            <li><a href="../../NBN_Client/Source/Java/Designation" target="_blank">Designation</a></li>
            <li><a href="../../NBN_Client/Source/Java/TaxonWithQueryStats" target="_blank">TaxonWithQueryStats</a></li>
            <li><a href="../../NBN_Client/Source/Java/TaxonDatasetWithQueryStats" target="_blank">TaxonDatasetWithQueryStats</a></li>
        </ul>
        <p>When this page is first requested, the user can select the vice county, taxon group and designation they wish to use as a filter on the request. The data for the dropdown lists are populated by calls to the NBNClient when the page loads.</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: false"><![CDATA[
        NBNClient nbnClient = new NBNClient();
        SiteBoundary[] viceCounties = nbnClient.getViceCounties();
        TaxonOutputGroup[] taxonOutputGroups = nbnClient.getTaxonOutputGroups();
        Designation[] designations = nbnClient.getDesignations();
        </script>
        <p>Again, each of these methods are very simple within the NBNClient, following the same pattern as before. Each method returns arrays of data of the apppropriate type. The type returned from getViceCounties is an array of SiteBoundary.</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: false"><![CDATA[
        public SiteBoundary[] getViceCounties()
        {
            SiteBoundary[] data = null;
            try {
                URI uri = new URI("https://data.nbn.org.uk/api/siteBoundaryDatasets/GA000344/siteBoundaries");
                String json = getJSON(uri);
                data = new ObjectMapper().readValue(json, SiteBoundary[].class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return data;
        }
        </script>
        <p>The data is then used to populate the dropdowns. Note that if the page is a result of a postback (see below), we set the selected attribute of the option to the user's selection.</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: true">
        <select name="viceCounties">
        <\% for (SiteBoundary item : viceCounties) {
            if (item.getIdentifier().equals(viceCounty)) { %>
                <option value="<\%= item.getIdentifier() %>" selected="selected"><\%= item.getName() %></option>
            <\% } else { %>
                <option value="<\%= item.getIdentifier() %>"><\%= item.getName() %></option>
            <\% }
        } %>
        </select>
        </script>
        <p>When the user clicks refresh. The page is posted back to the server. Note that the action of the page is to post back to itself.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: true">
        <form method="post" action="SiteSpeciesList.php">
        </script>
        <p>If the page is being created as a result of a postback, we can detect this by checking the request method and set a boolean variable to be used elsewhere in the page. If the page request is a result of a POST, then we know to get the filtered list of species and datasets from the NBN API via the NBNClient.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false"><![CDATA[
        //determine whether this page is being generated after a submit
        boolean isPostBack = ("POST".equalsIgnoreCase(request.getMethod()));
        if (isPostBack) {
            //Get the form variables
            viceCounty = request.getParameter("viceCounties");
            taxonOutputGroup = request.getParameter("taxonOutputGroups");
            designation = request.getParameter("designations");

            //Get the species and datasets from the nbnClient
            species = nbnClient.getSiteSpecies(viceCounty, taxonOutputGroup, designation);
            datasets = nbnClient.getSiteDatasets(viceCounty, taxonOutputGroup, designation);
        }
        ]]></script>
        <p>The GetSiteSpecies and GetSiteDatasets methods each take three parameters that are the values of the DropDownLists representing the user's selection. The NBNClient adds these parameters to the URI of the request, first checking that a designation has been selected. (Note, you need to use identifier for the site boundary filter, not featureID in the JSON).</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: false">
        // featureID is the identifier (not the featureID of the boundary)
        public TaxonWithQueryStats[] getSiteSpecies(String featureID, String taxonOutputGroup, String designation)
        {
            TaxonWithQueryStats[] data = null;
            try {
                String uriString = String.format("https://data.nbn.org.uk/api/taxonObservations/species?featureID=%s&taxonOutputGroup=%s", featureID, taxonOutputGroup);
                if (designation != null && designation.length() > 0)
                {
                    uriString = uriString + String.format("&designation=%s", designation);
                }
                URI uri = new URI(uriString);
                String json = getJSON(uri);
                data = new ObjectMapper().readValue(json, TaxonWithQueryStats[].class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return data;
        }
        </script>
        <p>Then the species list and dataset tables can be created in the same way as the ten km species list example.</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: true"><![CDATA[
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
            <\% for (TaxonWithQueryStats item : species) { %>
                <tr>
                    <td>
                        <\%= item.getTaxon().getName() %>
                    </td>
                    <td>
                        <\%= item.getTaxon().getAuthority() %>
                    </td>
                    <td>
                        <\%= item.getTaxon().getTaxonVersionKey() %>
                    </td>
                    <td>
                        <\%= item.getTaxon().getRank() %>
                    </td>
                    <td>
                        <\%= item.getQuerySpecificObservationCount() %>
                    </td>
                </tr>
            <\% } %>
            </tbody>
        </table>
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

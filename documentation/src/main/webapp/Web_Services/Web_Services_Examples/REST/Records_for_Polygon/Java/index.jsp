<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Records for a User-Defined Polygon Java</h1>
        <a href="Source_Code" target="_blank"><h2>Page Source Code</h2></a>
        <p>Source code for the required classes:</p>
        <ul>
            <li><a href="../../NBN_Client/Source/Java/TaxonObservation" target="_blank">TaxonObservation</a></li>
            <li><a href="../../NBN_Client/Source/Java/Designation" target="_blank">Designation</a></li>
        </ul>

        <p>Since all of the complexity around geometry is handled by OpenLayers and the accompanying JavaScript, the code required for this page is relatively simple and very similar to previous examples.</p>
        <p>At the top of the page, the default values are set, with the designations list populated using the NBNClient getDesignations method. The default start year is 1990, end year is this year (you will need to import java.util.Calender), and the default designation is UK BAP.</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: true">
         ...
        <\%@ page import="java.util.Calendar" %>

        <\%
        int startYear = 1990;
        int endYear = Calendar.getInstance().get(Calendar.YEAR);
        String designation = "BAP-2007";
        String wkt = "";
        String datasetKey = "GA000091";
        TaxonObservation[] observations = null;

        NBNClient nbnClient = new NBNClient();
        Designation[] designations = nbnClient.getDesignations();
        ...
        %>
        </script>
        <p>If the page request is a POST, then the parameters are read from the request, the user is logged in (replace the placeholders with a real username and password), and the NBNClient method postRecordsForWKTDesignationDatasetDates is called. The data returned is then output to a table in the usual way. The code assumes that the user has correctly populated all the inputs and created a polygon before clicking Refresh. In a production system, some validation around this would be needed.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: true">
        <\%
        ...
        //determine whether this page is being generated after a submit
        boolean isPostBack = ("POST".equalsIgnoreCase(request.getMethod()));
        if (isPostBack) {
            wkt = request.getParameter("hfWKT");
            designation = request.getParameter("designation");
            startYear = Integer.parseInt(request.getParameter("startYear"));
            endYear = Integer.parseInt(request.getParameter("endYear"));

            nbnClient.login("#username#", "#password#");
            observations = nbnClient.postRecordsForWKTDesignationDatasetDates(wkt, designation, datasetKey, startYear, endYear);
        }
        %>
        </script>
        <p>In the NBNClient, the filters for the request are passed to PostData as a MultivaluedMap as in previous POST examples. The WKT string is simply passed in in the same way as any other parameter. This resource returns an array of TaxonObservation.</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: false">
        public TaxonObservation[] postRecordsForWKTDesignationDatasetDates(String wkt, String designation, String datasetKey, int startYear, int endYear)
        {
            TaxonObservation[] data = null;
            try {
            URI uri = new URI("https://data.nbn.org.uk/api/taxonObservations");
            MultivaluedMap<String, String> filters = new MultivaluedMapImpl();
            filters.add("polygon", wkt);
            filters.add("designation", designation);
            filters.add("startYear", Integer.toString(startYear));
            filters.add("endYear", Integer.toString(endYear));
            filters.add("datasetKey", datasetKey);
            String json = postJSON(uri, filters);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            data = mapper.readValue(json, TaxonObservation[].class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return data;
        }
        </script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

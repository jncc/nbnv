<%@ page import="net.searchnbn.webservices.service.GatewayWebService"%>
<%@ page import="net.searchnbn.webservices.query.*"%>
<%@ page import="net.searchnbn.webservices.data.*"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<!--
@author Richard Ostler, CEH Monks Wood
@date   09/02/2006
-->
<html>
<head><title>NBN Species List Web Service</title>
<link href="examples.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<body>
<div id="container">
<h1>Species List Web Service</h1>
<p>This is a java example of the Species List Web Service for a 10km square. If no request parameters are supplied,
    creates a BAP list for TL18.<p>
<%  String desig = "NONE";
    String square = request.getParameter("square");
    String taxCat = request.getParameter("taxCat");
    if (square != null && square.length() > 0) {
        desig = request.getParameter("desig");
    } else {
        square = "TL18";
        desig = "BIODIVERSITY_ACTION_PLAN_2007";
        taxCat = "";
    }

    GatewayWebService gws = new GatewayWebService();
    SpeciesListRequest slr = new SpeciesListRequest();

    GridSquare gs = new GridSquare();
    gs.setKey(square);
    GeographicalFilter gf = new GeographicalFilter();
    gf.setGridSquare(gs);

    // Set the map settings
    MapSettings mapSettings = new MapSettings();
    mapSettings.setFillColour("#FF0000");
    mapSettings.setHeight(new Integer(300));
    mapSettings.setWidth(new Integer(300));
    mapSettings.setOutlineWidth(new Integer(1));
    mapSettings.setOutlineColour("#000000");
    mapSettings.setFillTransparency(new Double(0.5));
    gf.setMapSettings(mapSettings);

    slr.setGeographicalFilter(gf);

    // Set the taxon group key to filter on
    if (taxCat.length() == 16) {
        slr.setTaxonReportingCategoryKey(taxCat);
    }

    // Set the designation
    slr.setDesignation(desig);

    // Process the results
    SpeciesListResponse list = gws.getGatewaySoapPort().getSpeciesList(slr);
    MapImage map = list.getMap();
    SpeciesList nbnSpeciesList = list.getSpeciesList();
    List<Species> speciesList=nbnSpeciesList.getSpecies();
    int size = speciesList.size();
 %>
    <div id="mapWrapper">
        <div id="mapImage">
            <h3>Map for <%=square%></h3>
            <img src="<%=map.getUrl()%>" alt="Map for <%=square%>" title="Map for <%=square%>"/>
        </div>
        <% if (size > 0) { %>
            <div id="rightOfMap">
            <h3><%=size%> species recorded</h3>
            <p>Click on a species name to view its data.</p>
            <table cellpadding="3" cellspacing="1" bgcolor="#C0C0C0">
            <% for (int i = 0; i < size; i++) { %>
                <tr>
                    <td bgcolor="#FFFFFF"><a href="ws-tenkmSpeciesData.jsp?tvk=<%=speciesList.get(i).getTaxonVersionKey()%>&square=<%=square%>"><%=speciesList.get(i).getScientificName()%></td>
                    <td bgcolor="#FFFFFF"><%=speciesList.get(i).getCommonName()%></td>
                </tr>
            <% } %>
            </table>
            </div>
        <% } %>
    </div>
    <div class="bottomLine">
    <h2>Datasets used</h2>
    <table class="tblBorder" cellspacing="0" cellpadding="2" width="100%">
        <tr>
            <th>Dataset</th>
            <th>Provider</th>
            <%  // Get an array of datasts
                DatasetSummaryList nbnDatasetSummaryList =  list.getDatasetSummaryList();
                List<DatasetSummary> datasetSummaryList= nbnDatasetSummaryList.getDatasetSummary();
                for (int i = 0; i < datasetSummaryList.size(); i++) { %>
                    <tr>
                        <td width="50%"><a href="ws-dataset.jsp?dsKey=<%=datasetSummaryList.get(i).getId()%>"><%=datasetSummaryList.get(i).getProviderMetadata().getDatasetTitle()%></a></td>
                        <td width="50%"><%=datasetSummaryList.get(i).getProviderMetadata().getDatasetProvider()%></td>
                    </tr>
                <% }
            %>
    </table>
    </div>
    <!-- Make sure to display the the link to the NBN Gateway terms and conditions and the powered by NBN logo. -->
    <div id="footer">
    <div id="tandc"><a href="<%=list.getTermsAndConditions()%>" class="popup">Gateway terms and conditions</a></div>
    <div id="NBNlogo"><a href="http://data.nbn.org.uk" target="_blank"><img alt="Powered by the NBN Gateway" title="Clicking on this logo will take you to the NBN Gateway website" border="0" src="<%=list.getNBNLogo()%>" /></a></div>
    </div>
</div>
</body>
</html>
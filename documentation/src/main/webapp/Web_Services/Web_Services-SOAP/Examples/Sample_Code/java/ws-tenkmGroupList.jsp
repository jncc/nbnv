<%@ page import="net.searchnbn.webservices.service.GatewayWebService"%>
<%@ page import="net.searchnbn.webservices.query.*"%>
<%@ page import="net.searchnbn.webservices.data.*"%>
<%@ page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!--
@author Richard Ostler, CEH Monks Wood
@date   24/02/2006
-->
<html>
<head><title>NBN Taxon Reporting Category List Web Service</title>
<link href="examples.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<body>
<div id="container">
<h1>Taxon Reporting Category List Web Service</h1>
<p>This is a java example of the Taxon Reporting Category List Web Service for a 10km, reqeusting a map image.</p>
<%
    String site = request.getParameter("site");
    String designationNoneSelectedValue = "NONE";
    String designationNoneSelectedText = "None";
    String desig = designationNoneSelectedValue;
    String square = request.getParameter("square");
    boolean hasSquare = false;
    if (square != null && square.length() > 0) {
        hasSquare = true;
        desig = request.getParameter("desig");
    } else {
        square = "";
    }
%>
<fieldset>
    <legend>Create a taxon group list</legend>
    <form method="post" action="ws-tenkmGroupList.jsp">
        <p><strong>Enter a 10km grid square (e.g. TL18): <input type="text" name="square" value="<%=square%>" /></strong></p>
        <p><strong>Enter start year <input type="text" name="start" size="4"/> and end year <input type="text" name="end" size="4"/></strong></p>
        <p><strong>Select a species designation: </strong>
        <%
        //Get the designations from the webservice for the user to choose from
        net.searchnbn.ws.resources.ResourcesService service = new net.searchnbn.ws.resources.ResourcesService();
        net.searchnbn.ws.resources.Resources port = service.getResourcesPort();
        java.util.List<net.searchnbn.ws.resources.SpeciesDesignation> result = port.getDesignations();

        StringBuffer sbDesigs = new StringBuffer();
        if(desig.equalsIgnoreCase(designationNoneSelectedValue)){
            sbDesigs.append("<option selected=\"selected\" value=\"").append(designationNoneSelectedValue).append("\">").append(designationNoneSelectedText).append("</option>");
        } else{
            sbDesigs.append("<option value=\"").append(designationNoneSelectedValue).append("\">").append(designationNoneSelectedText).append("</option>");
        }
        for(net.searchnbn.ws.resources.SpeciesDesignation sd : result){
          if(desig.equals(sd.getValue())){
            sbDesigs.append("<option selected=\"selected\" value=\"").append(sd.getValue()).append("\">").append(sd.getName()).append("</option>");
          }else{
            sbDesigs.append("<option value=\"").append(sd.getValue()).append("\">").append(sd.getName()).append("</option>");
          }
        }
        %>
        <select name="desig">
            <%=sbDesigs.toString()%>
        </select></p>
        <p><input type="submit" value="view list" /></p>
    </form>
</fieldset>
<%
if (hasSquare) {
    // The request
    TaxonReportingCategoryListRequest req = new TaxonReportingCategoryListRequest();
    GatewayWebService gws = new GatewayWebService();

    // Set the site ID
    GridSquare gs = new GridSquare();
    gs.setKey(request.getParameter("square"));
    // Add the site to a GeographicalFilter
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

    req.setGeographicalFilter(gf);

    // sort the years
    YearFilter yf = new YearFilter();
    if (request.getParameter("start") != null && request.getParameter("start").length() > 0) {
        yf.setStart(new Integer(request.getParameter("start")));
    }

    if (request.getParameter("end") != null && request.getParameter("end").length() > 0) {
        yf.setEnd(new Integer(request.getParameter("end")));
    }
    req.setDateRange(yf);

    req.setDesignation(desig);

    // Process the results
    TaxonReportingCategoryListResponse list = gws.getGatewaySoapPort().getTaxonReportingCategoryList(req);
    MapImage map = list.getMap();
    TaxonReportingCategoryList nbnTaxRepCatList = list.getTaxonReportingCategoryList();
    List<TaxonReportingCategory> taxRepCatList = nbnTaxRepCatList.getTaxonReportingCategory();
    int size = taxRepCatList.size();
%>
    <div id="mapWrapper">
        <div id="mapImage">
            <h3>Map for <%=square%></h3>
            <img src="<%=map.getUrl()%>" alt="Map for <%=square%>" title="Map for <%=square%>"/>
        </div>
        <% if (size > 0) { %>
            <div id="rightOfMap">
            <h3><%=size%> species groups recorded</h3>
            <p>Click on a taxon group name to view the species recorded.</p>
            <table cellpadding="3" cellspacing="1" bgcolor="#C0C0C0">
            <% for (int i = 0; i < size; i++) { %>
                <tr>
                    <td bgcolor="#FFFFFF"><a href="ws-tenkmSpeciesList.jsp?desig=<%=desig%>&taxCat=<%=taxRepCatList.get(i).getTaxonReportingCategoryKey()%>&square=<%=square%>"><%=taxRepCatList.get(i).getValue()%></td>
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
                List<DatasetSummary> datasetSumaryList = nbnDatasetSummaryList.getDatasetSummary();
                for (int i = 0; i < datasetSumaryList.size(); i++) { %>
                    <tr>
                        <td width="50%"><a href="ws-dataset.jsp?dsKey=<%=datasetSumaryList.get(i).getId()%>"><%=datasetSumaryList.get(i).getProviderMetadata().getDatasetTitle()%></a></td>
                        <td width="50%"><%=datasetSumaryList.get(i).getProviderMetadata().getDatasetProvider()%></td>
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
<% } %>
</div>
</body>
</html>
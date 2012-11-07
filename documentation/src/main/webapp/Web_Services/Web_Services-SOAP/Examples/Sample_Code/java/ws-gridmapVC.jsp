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
<head><title>NBN Gridmap Web Service</title></head>
<body>
<h2>NBN Gridmap Web Service, java example filtered to a vice county</h2>
<%
    String tvk = request.getParameter("tvk");
    GatewayWebService gws = new GatewayWebService();
	GridMapRequest r = new GridMapRequest();

    // Apply some map settings, for example image size, background, zoom to vice county.
    GridMapSettings mapSettings = new GridMapSettings();
    mapSettings.setWidth(new Integer(500));
	mapSettings.setHeight(new Integer(500));
    mapSettings.setBackground("OSMap");
    mapSettings.setGrid("None");
    mapSettings.setRegion("GBIreland");
    mapSettings.setViceCounty(new Integer(20)); // Set a vice county ID.
    mapSettings.setFillColour("#FF0000");
    
    r.setGridMapSettings(mapSettings);
    r.setTaxonVersionKey(tvk);  // set the taxon version key
    r.setResolution("_10km"); // set the square size
    GridMap gmap = gws.getGatewaySoapPort().getGridMap(r);

    Species sp = gmap.getSpecies();
    if (sp.getCommonName() != null && sp.getCommonName().length() > 0) { %>
        <p><b>Map for <%=sp.getCommonName()%> (<i><%=sp.getScientificName()%></i>)</b></p>
<%  } else { %>
        <p><b>Map for <i><%=sp.getScientificName()%></i></b></p>
<%  } %>
<div align="center"><img src="<%=gmap.getMap().getUrl()%>" alt="Grid map"/></div>
<h2>Datasets used to create map</h2>
<table border="0" cellpadding="2" cellspacing="1" align="center" width="90%" bgcolor="#000000">
	<tr bgcolor="#FFFFFF">
 		<th>Dataset</th>
		<th>Provider</th>
<%  // Get an array of datasts
    DatasetSummaryList nbnDatasetSummaryList =  gmap.getDatasetSummaryList();
    List<DatasetSummary> datasetSummaryList=nbnDatasetSummaryList.getDatasetSummary();
    for (int i = 0; i < datasetSummaryList.size(); i++) { %>
        <tr bgcolor="#FFFFFF">
            <td width="50%"><%=datasetSummaryList.get(i).getProviderMetadata().getDatasetTitle()%></td>
            <td width="50%"><%=datasetSummaryList.get(i).getProviderMetadata().getDatasetProvider()%></td>
        </tr>
    <% }
%>
</table>
<hr size="1" noshade="noshade" />
<!-- Make sure to display the the link to the NBN Gateway terms and conditions and the powered by NBN logo. -->
<table width="100%">
	<tr>
		<td align="left"><a href="<%=gmap.getTermsAndConditions()%>">Gateway terms and conditions</a></td>
		<td align="right"><a href="http://data.nbn.org.uk"><img  border="0" src="<%=gmap.getNBNLogo()%>" /></a></td>
	</tr>
</table>
</body>
</html>
<%@ page import="net.searchnbn.webservices.service.GatewayWebService"%>
<%@ page import="net.searchnbn.webservices.query.*"%>
<%@ page import="net.searchnbn.webservices.data.*"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Properties"%>
<!--
@author Richard Ostler, CEH Monks Wood
@date   09/02/2006
-->
<html>
<head><title>NBN Gridmap Web Service</title></head>
<body>
<h2>NBN Gridmap Web Service, Java example using date classifications</h2>
<%
          
            
    String tvk = request.getParameter("tvk");
    GatewayWebService gws = new GatewayWebService();
	GridMapRequest r = new GridMapRequest();

    // Apply some map settings, for example image size, background, zoom to vice county.
    GridMapSettings mapSettings = new GridMapSettings();
    mapSettings.setWidth(new Integer(500));
    mapSettings.setHeight(new Integer(500));
    mapSettings.setBackground("None");
    mapSettings.setGrid("None");
    mapSettings.setRegion("GBIreland");
    //mapSettings.setFillColour("#FF0000"); using date classifications so this setting will be ignored
    r.setGridMapSettings(mapSettings);
    r.setTaxonVersionKey(tvk);  // set the taxon version key
    r.setResolution("_10km"); // set the square size

    // Set the date classifications.
    
    Classification classification = new Classification();
     
     Band band1= new Band();
     band1.setFrom(2004);
     band1.setTo(2004);
     band1.setBorder("#980003");
     band1.setFill("#FF0000");
     classification.getBand().add(band1);
     
     Band band2= new Band();
     band2.setFrom(2005);
     band2.setTo(2005);
     band2.setFill("#FF9900");
     classification.getBand().add(band2);

     Band band3= new Band();
     band3.setFrom(2006);
     band3.setTo(2006);
     band3.setFill("#FFFF00");
     classification.getBand().add(band3);
             
    r.setClassification(classification);
    
   

    GridMap gmap = gws.getGatewaySoapPort().getGridMap(r);

    Species sp = gmap.getSpecies();
    if (sp.getCommonName() != null && sp.getCommonName().length() > 0) { %>
        <p><b>Map for <%=sp.getCommonName()%> (<i><%=sp.getScientificName()%></i>)</b></p>
<%  } else { %>
        <p><b>Map for <i><%=sp.getScientificName()%></i></b></p>
<%  } %>
<table border="0" cellpadding="2" cellspacing="0" align="center">
    <tr>
        <td><img src="<%=gmap.getMap().getUrl()%>" alt="grid map"/></td>
        <td>
            <table border="0" cellpadding="3" cellspacing="1" bgcolor="#000000">
                <% for (int i=0; i<3; i++) { %>
                    <tr>
                        <td bgcolor="<%=classification.getBand().get(i).getFill()%>"><b><%=classification.getBand().get(i).getFrom()%></b></td>
                    </tr>
                <% } %>
            </table>
        </td>
    </tr>
</table>
<h2>Datasets used to create map</h2>
<table border="0" cellpadding="2" cellspacing="1" align="center" width="90%" bgcolor="#000000">
	<tr bgcolor="#FFFFFF">
 		<th>Dataset</th>
		<th>Provider</th>
<%  // Get an array of datasts
    DatasetSummaryList nbnDatasetSummaryList =  gmap.getDatasetSummaryList();
    List<DatasetSummary> datasetSummaryList= nbnDatasetSummaryList.getDatasetSummary();
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

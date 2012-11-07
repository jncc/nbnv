<%@ page import="net.searchnbn.webservices.query.GridMapRequest"%>
<%@ page import="net.searchnbn.webservices.query.GridMapSettings"%>
<%@ page import="net.searchnbn.webservices.data.GridMap"%>
<%@ page import="net.searchnbn.webservices.data.DatasetSummary"%>
<%@ page import="net.searchnbn.webservices.data.DatasetSummaryList"%>
<%@ page import="net.searchnbn.webservices.data.ProviderMetadata"%>
<%@ page import="net.searchnbn.webservices.data.Species" %>
<%@ page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="net.searchnbn.webservices.service.GatewayWebService"%>
<%@ page import="net.searchnbn.webservices.service.GatewaySoapPort"%>
<%@ page import="java.util.Properties"%>
<!--
@author Richard Ostler, CEH Monks Wood
@date   09/02/2006
@comment adapted to new infrastructure by Nicole Jung, 14/07/2008
-->
<html>
    <head>
        <title>NBN Gridmap Web Service</title>
        <link href="examples.css" rel="stylesheet" type="text/css" media="screen" />
    </head>
    <body>
        <div id="container">
            <h1>NBN Gridmap Web Service, java example</h1>
            <p>This is a java example of the NBN Grid Map web service. In this example the taxon version key
                property is set from the <code>tvk</code> request parameter. The map image height and width are
                coded to 500 pixels, otherwise the default values are used.
            </p>
            <%
            

            String tvk = request.getParameter("tvk");
            GatewayWebService gws = new GatewayWebService();
            GridMapRequest r = new GridMapRequest();

            // Apply some map settings, for example image size, background, zoom to vice county.
            // Here only the image size is set, the default settings are used for remaining map properties
            GridMapSettings mapSettings = new GridMapSettings();
            mapSettings.setWidth(new Integer(500));
            mapSettings.setHeight(new Integer(500));
            r.setGridMapSettings(mapSettings);
            r.setTaxonVersionKey(tvk);
            r.getTaxonVersionKey();
            // send the request and put the results into a GridMap object
            GatewaySoapPort gwSoapPort = gws.getGatewaySoapPort();
            System.out.println(gwSoapPort.toString());
            GridMap gmap = gwSoapPort.getGridMap(r);
          

            Species sp = gmap.getSpecies();
            if (sp.getCommonName() != null && sp.getCommonName().length() > 0) {%>
            <p><b>Map for <%=sp.getCommonName()%> (<i><%=sp.getScientificName()%></i>)</b></p>
            <%  } else {%>
            <p><b>Map for <i><%=sp.getScientificName()%></i></b></p>
            <%  }%>
            <div>
                <img src="<%=gmap.getMap().getUrl()%>" alt="NBN Grid map for <%=sp.getScientificName()%>" title="NBN Grid map for <%=sp.getScientificName()%>" />
            </div>
            
            
            <h2>Datasets used</h2>
            <div class="bottomLine">
                <table class="tblBorder" cellspacing="0" cellpadding="2" width="100%">
                    <tr>
                    <th>Dataset</th>
                    <th>Provider</th>
                    <%  // Get an array of datasts
            DatasetSummaryList datasetSummarylist = gmap.getDatasetSummaryList();
            List<DatasetSummary> datasetsummarylist = datasetSummarylist.getDatasetSummary();
            DatasetSummary datasetSummary = new DatasetSummary();
                        
            ProviderMetadata providerMetadata = new ProviderMetadata();
            for (int i = 0; i < datasetsummarylist.size(); i++) {
               datasetSummary = (DatasetSummary)datasetsummarylist.get(i);
               
               providerMetadata = (ProviderMetadata)datasetSummary.getProviderMetadata(); 
               
                %>
                    <tr>
                       <td width="50%"><a href="ws-dataset.jsp?dsKey=<%=datasetSummary.getId()%>"><%=datasetSummary.getProviderMetadata().getDatasetTitle()%></a></td>
                        <td width="50%"><%=providerMetadata.getDatasetProvider()%></td>
                    </tr>
                    <% 
            
            }
                    %>
                </table>
            </div>
            <!-- Make sure to display the the link to the NBN Gateway terms and conditions and the powered by NBN logo. -->
            <div id="footer">
                <div id="tandc"><a href="<%=gmap.getTermsAndConditions()%>" class="popup">Gateway terms and conditions</a></div>
                <div id="NBNlogo"><a href="http://data.nbn.org.uk" target="_blank"><img alt="Powered by the NBN Gateway" title="Clicking on this logo will take you to the NBN Gateway website" border="0" src="<%=gmap.getNBNLogo()%>" /></a></div>
            </div>
        </div>
       
    </body>
</html>

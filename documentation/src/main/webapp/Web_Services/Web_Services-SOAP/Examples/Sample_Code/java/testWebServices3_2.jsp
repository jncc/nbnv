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

<%@ page import="java.net.URL"%>
<%@ page import="java.net.Authenticator" %>
<%@ page import="java.net.PasswordAuthentication"%>




<!--
@author Richard Ostler, CEH Monks Wood
@date   09/02/2006
@comment adapted to new infrastructure by Nicole Jung, 14/07/2008
-->
<html>
    <head>
        <title>NBN Gridmap Web Service Version 3.2</title>
        <link href="examples.css" rel="stylesheet" type="text/css" media="screen" />
    </head>
    <body>
        <div id="container">
            <h1>NBN Web Services 3.2 Test page</h1>
            <p>This page tests the web services and checks if they are responding.<br/>
            This page should be watched by host-tracker.

            You can run the test individually by using the following querystring:<br/>
            &server=NBNWEBS-1  tests only NBNWEBS-1<br/>
            &server=NBNWEBS-2  tests only NBNWEBS-2<br/>
            &server=NBNWS      tests only nbnws.net<br/>

            You can run this test against testnbn by adding the following querystring:<br/>
            &server=TESTNBN      tests only nbnws.net<br/>

            </p>
     <%


            String tvk = request.getParameter("tvk");
            String server = request.getParameter("server");

            URL baseUrl=null;
            URL serverURL = null;
            GatewayWebService gws=null;
            GridMapRequest r=null;
            GridMapSettings mapSettings=null;
            GatewaySoapPort gwSoapPort=null;
            GridMap gmap=null;
            Species sp=null;


            // test NBNWEBS-1

            if (server==null  || server.equals("NBNWEBS-1"))
		//if (server!=null && server.equals("NBNWEBS-1"))

                {

                baseUrl = net.searchnbn.webservices.service.GatewayWebService.class.getResource(".");
                serverURL = new URL(baseUrl, "http://192.171.173.135/ws/WSDL?WSDL");
                // call the web service with your new url
                gws = new GatewayWebService(serverURL);
                //GatewayWebService gws = new GatewayWebService();

                // do your requests as usual
                r = new GridMapRequest();
                // Apply some map settings, for example image size, background, zoom to vice county.
                // Here only the image size is set, the default settings are used for remaining map properties
                mapSettings = new GridMapSettings();
                mapSettings.setWidth(new Integer(500));
                mapSettings.setHeight(new Integer(500));
                r.setGridMapSettings(mapSettings);
                r.setTaxonVersionKey(tvk);
                r.getTaxonVersionKey();
                // send the request and put the results into a GridMap object
                gwSoapPort = gws.getGatewaySoapPort();
                gmap = gwSoapPort.getGridMap(r);


                sp = gmap.getSpecies();
                if (sp.getCommonName() != null && sp.getCommonName().length() > 0) {%>
                <p><h2>TEST NBNWEBS-1</h2></p>
                <p><b>Map for <%=sp.getCommonName()%> (<i><%=sp.getScientificName()%></i>)</b></p>
                <%  } else {%>
                <p><b>Map for <i><%=sp.getScientificName()%></i></b></p>
                <%  }%>
                <div>
                    <img src="<%=gmap.getMap().getUrl()%>" alt="NBN Grid map for <%=sp.getScientificName()%>" title="NBN Grid map for <%=sp.getScientificName()%>" />
                </div>

            <%

            baseUrl=null;
            serverURL = null;
            gws=null;
            r=null;
            mapSettings=null;
            gwSoapPort=null;
            gmap=null;
            sp=null;
            }

            // test server NBNWEBS-2
            if (server==null || server.equals("NBNWEBS-2"))
		//if (server!=null && server.equals("NBNWEBS-2"))

                {
            baseUrl = net.searchnbn.webservices.service.GatewayWebService.class.getResource(".");
            serverURL = new URL(baseUrl, "http://192.171.173.136/ws/WSDL?WSDL");
            // call the web service with your new url
            gws = new GatewayWebService(serverURL);

            // do your requests as usual
            r = new GridMapRequest();
            // Apply some map settings, for example image size, background, zoom to vice county.
            // Here only the image size is set, the default settings are used for remaining map properties
            mapSettings = new GridMapSettings();
            mapSettings.setWidth(new Integer(500));
            mapSettings.setHeight(new Integer(500));
            r.setGridMapSettings(mapSettings);
            r.setTaxonVersionKey(tvk);
            r.getTaxonVersionKey();
            // send the request and put the results into a GridMap object
            gwSoapPort = gws.getGatewaySoapPort();
            gmap = gwSoapPort.getGridMap(r);


            sp = gmap.getSpecies();
            if (sp.getCommonName() != null && sp.getCommonName().length() > 0) {%>
            <p><h2>TEST NBNWEBS-2</h2></p>
            <p><b>Map for <%=sp.getCommonName()%> (<i><%=sp.getScientificName()%></i>)</b></p>
            <%  } else {%>
            <p><b>Map for <i><%=sp.getScientificName()%></i></b></p>
            <%  }%>
            <div>
                <img src="<%=gmap.getMap().getUrl()%>" alt="NBN Grid map for <%=sp.getScientificName()%>" title="NBN Grid map for <%=sp.getScientificName()%>" />
            </div>

            <%
            baseUrl=null;
            serverURL = null;
            gws=null;
            r=null;
            mapSettings=null;
            gwSoapPort=null;
            gmap=null;
            sp=null;

            }
            // test nbnws.net
             if (server==null || server.equals("NBNWS"))
                {
            serverURL = new URL(baseUrl, "http://www.nbnws.net/ws/WSDL?WSDL");
            gws = new GatewayWebService(serverURL);

            // do your requests as usual
            r = new GridMapRequest();
            // Apply some map settings, for example image size, background, zoom to vice county.
            // Here only the image size is set, the default settings are used for remaining map properties
            mapSettings = new GridMapSettings();
            mapSettings.setWidth(new Integer(500));
            mapSettings.setHeight(new Integer(500));
            r.setGridMapSettings(mapSettings);
            r.setTaxonVersionKey(tvk);
            r.getTaxonVersionKey();
            // send the request and put the results into a GridMap object
            gwSoapPort = gws.getGatewaySoapPort();
            gmap = gwSoapPort.getGridMap(r);


            sp = gmap.getSpecies();
            if (sp.getCommonName() != null && sp.getCommonName().length() > 0) {%>
            <p><h2>Test Web Services on nbnws.net</h2></p>
            <p><b>Map for <%=sp.getCommonName()%> (<i><%=sp.getScientificName()%></i>)</b></p>
            <%  } else {%>
            <p><b>Map for <i><%=sp.getScientificName()%></i></b></p>
            <%  }%>
            <div>
                <img src="<%=gmap.getMap().getUrl()%>" alt="NBN Grid map for <%=sp.getScientificName()%>" title="NBN Grid map for <%=sp.getScientificName()%>" />
            </div>

<%
            baseUrl=null;
            serverURL = null;
            gws=null;
            r=null;
            mapSettings=null;
            gwSoapPort=null;
            gmap=null;
            sp=null;

            }
            // test nbnws.net
             if (server != null && server.equals("TESTNBN"))
                {
             baseUrl = net.searchnbn.webservices.service.GatewayWebService.class.getResource(".");
            serverURL = new URL(baseUrl, "http://www.testnbn.net/ws/WSDL?WSDL");
            // call the web service with your new url
            gws = new GatewayWebService(serverURL);



            // do your requests as usual
            r = new GridMapRequest();
            // Apply some map settings, for example image size, background, zoom to vice county.
            // Here only the image size is set, the default settings are used for remaining map properties
            mapSettings = new GridMapSettings();
            mapSettings.setWidth(new Integer(500));
            mapSettings.setHeight(new Integer(500));
            r.setGridMapSettings(mapSettings);
            r.setTaxonVersionKey(tvk);
            r.getTaxonVersionKey();
            // send the request and put the results into a GridMap object
            gwSoapPort = gws.getGatewaySoapPort();
            gmap = gwSoapPort.getGridMap(r);


            sp = gmap.getSpecies();
            if (sp.getCommonName() != null && sp.getCommonName().length() > 0) {%>
            <p><h2>Test Web Services on testnbn.net</h2></p>
            <p><b>Map for <%=sp.getCommonName()%> (<i><%=sp.getScientificName()%></i>)</b></p>
            <%  } else {%>
            <p><b>Map for <i><%=sp.getScientificName()%></i></b></p>
            <%  }%>
            <div>
                <img src="<%=gmap.getMap().getUrl()%>" alt="NBN Grid map for <%=sp.getScientificName()%>" title="NBN Grid map for <%=sp.getScientificName()%>" />
            </div>

            <%
             }
            if(gmap!=null)
                {
            %>
            <!-- Make sure to display the the link to the NBN Gateway terms and conditions and the powered by NBN logo. -->
            <div id="footer">
                <div id="tandc"><a href="<%=gmap.getTermsAndConditions()%>" class="popup">Gateway terms and conditions</a></div>
                <div id="NBNlogo"><a href="http://data.nbn.org.uk" target="_blank"><img alt="Powered by the NBN Gateway" title="Clicking on this logo will take you to the NBN Gateway website" border="0" src="<%=gmap.getNBNLogo()%>" /></a></div>
            </div>

            <%
             }
            %>

        </div>

    </body>
</html>

<%@ page import="net.searchnbn.webservices.service.GatewayWebService"%>
<%@ page import="net.searchnbn.webservices.data.*"%>
<%@ page import="net.searchnbn.webservices.query.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.Format"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="javax.xml.rpc.ServiceException"%>
<%@ page import="org.apache.jasper.JasperException"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<!--
@author Richard Ostler, CEH Monks Wood
@date   09/02/2006
-->
<html>
<head><title>NBN One Site Data Web Service</title>
<link href="examples.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<body>
<div id="container">
<h1>NBN One Site Data Request, java example</h1>
<p>This example demonstrates using the OneSiteDataRequest web service to fetch records and a
    distribution map for a taxon reporting category in a known site. It also uses the KnownSiteNameRequest
    web service to lookup the site name.</p>
<%
GatewayWebService gws = new GatewayWebService();
String trck = request.getParameter("trck");
String siteKey = request.getParameter("siteKey");
String dsKey = request.getParameter("dsKey");

try {
    // test if we have query parameter
    if (siteKey != null && siteKey.length() > 0) {
        // Create a KnownSite object, this is used to lookup the site name and data
        KnownSite site = new KnownSite();
        site.setProviderKey(dsKey);
        site.setSiteKey(siteKey);

        // Get the site details
        KnownSiteNameRequest siteReq = new KnownSiteNameRequest();
        siteReq.setKnownSite(site);
        KnownSiteNameResponse siteResp = gws.getGatewaySoapPort().getKnownSiteName(siteReq);
        site = siteResp.getKnownSite();

        // Get the species records
        OneSiteDataRequest req = new OneSiteDataRequest();

        // Set map settings
        MapSettings map = new MapSettings();
        map.setHeight(new Integer(300));
        map.setWidth(new Integer(300));
        map.setFillTransparency(new Double(0.1));

        // Create the geographical filter with a minimum biological record resolution and add the KnownSite object to it.
        GeographicalFilter gf = new GeographicalFilter();
        gf.setMapSettings(map);
        gf.setKnownSite(site);
        gf.setMinimumResolution("_1km");
        req.setGeographicalFilter(gf);
        req.setTaxonReportingCategoryKey(trck);
        GatewayData resp =  gws.getGatewaySoapPort().getSiteData(req);

        Data data = resp.getData();

        // Get the dataset array
        List<Dataset> dataset = data.getDataset();

        //Put the site list into a hashmap for later use
        SiteList nbnsiteList = data.getSiteList();
        List<Site> sites = nbnsiteList.getSite();
        Map siteMap = new HashMap(sites.size());
        for(int i = 0; i < sites.size(); i++) {
            siteMap.put(sites.get(i).getSiteKey(),sites.get(i));
        }

        //Put the species list into a Hashmap for later use
        SpeciesList nbnSpeciesList = data.getSpeciesList();
        List<Species> species = nbnSpeciesList.getSpecies();
        Map speciesMap = new HashMap(species.size());
        for(int i = 0; i < species.size(); i++) {
            speciesMap.put(species.get(i).getTaxonVersionKey(),species.get(i));
        } %>
        <div id="mapWrapper">
        <div id="mapImage">
            <h2>Site Map for <%=site.getKnownSiteName()%></h2>
            <img src="<%=data.getMap().getUrl()%>" alt="site map for <%=site.getKnownSiteName()%>" title="site map for <%=site.getKnownSiteName()%>"/>
        </div>
        </div>
        <h2>Species data for <%=site.getKnownSiteName()%></h2>
        <% Format formatter = new SimpleDateFormat("dd-MMM-yyyy"); // formats the XML dates to something nice.
        for (int i = 0; i < dataset.size(); i++) {
            ProviderMetadata md = dataset.get(i).getProviderMetadata(); %>
            <h3><i>Data from</i> <a href="ws-dataset.jsp?dsKey=<%=dataset.get(i).getId()%>"><%=md.getDatasetTitle()%></a> <i>provided by</i> <%=md.getDatasetProvider()%></h3>
            <table class=tblBorder cellspacing=0 cellpadding=2 align=center>
                <thead>
                    <tr>
                        <th>Species</th>
                        <th>Location</th>
                        <th>Gridref</th>
                        <th>Date from</th>
                        <th>Date to</th>
                        <th>Recorder</th>
                    </tr>
                </thead>
                <tbody>
                <%
                List<Location> location = dataset.get(i).getLocation();
                for (int j = 0; j < location.size(); j++) {
                    List<SpeciesRecord> record = location.get(j).getSpeciesRecord();
                    for (int k = 0; k < record.size(); k++) {
                        Species sp = (Species)speciesMap.get(((Species)(record.get(k)).getTaxonVersionKey()).getTaxonVersionKey());
                        Site recSite = (Site)siteMap.get(((Site)(location.get(j)).getSiteKey()).getSiteKey());
  %>
                        <tr>
                            <td><%=sp.getScientificName()%></td>
                            <td><%=location.get(j).getLocationName()%></td>
                            <td><%=recSite.getGridSquare().getKey()%></td>
                            <td><%=(record.get(k)).getRecordDate().getFrom().toString()%></td>
                            <td><%=(record.get(k)).getRecordDate().getTo().toString()%></td>
                            <td><%=record.get(k).getRecorder()%></td>
                        </tr>
                    <% }
                }  %>
                </tbody>
            </table>
        <% }
        %>
    <!-- make sure to display the the link to the NBN Gateway terms and conditions and the powered by NBN logo. -->
    <div id="footer">
    <div id="tandc"><a href="<%=resp.getTermsAndConditions()%>" class="popup">Gateway terms and conditions</a></div>
    <div id="NBNlogo"><a href="http://data.nbn.org.uk" target="_blank"><img alt="Powered by the NBN Gateway" title="Clicking on this logo will take you to the NBN Gateway website" border="0" src="<%=resp.getNBNLogo()%>" /></a></div>
    </div>
    </div>
<%  }
} catch (RuntimeException se) {
    out.print(se.getMessage());
} %>
</body>
</html>
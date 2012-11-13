<%@ page import="net.searchnbn.webservices.service.GatewayWebService"%>
<%@ page import="net.searchnbn.webservices.data.*"%>
<%@ page import="net.searchnbn.webservices.query.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.Format"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="nbn.dataset.DatasetList"%>
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
<h1>NBN One Species Data Request, java example</h1>
<p>Requests a distribution map and records for BAP species in a user defined point and buffer.</p>
<%
    GatewayWebService gws = new GatewayWebService();

    OneSiteDataRequest req = new OneSiteDataRequest();

    // Set the BAP filter
    req.setDesignation("BIODIVERSITY_ACTION_PLAN");

    // Set map settings
    MapSettings map = new MapSettings();
    map.setHeight(new Integer(300));
    map.setWidth(new Integer(300));
    map.setFillTransparency(new Double(0.1));

    // set the point/buffer
    Buffer buffer = new Buffer();
    buffer.setDistance(500f);
    Point p = new Point();
	p.setX(519700);
	p.setY(279800);
	p.setSrs(SpatialReferenceSystem.EPSG_27700);
    buffer.setPoint(p);

    // Add a dataset filter
    DatasetFilter df = new DatasetFilter();
    df.getDatasetKey().add("GA000074");
    req.setDatasetList(df);

    //Set the geographic filter, define a minimum record resolution and apply the buffer from above
    GeographicalFilter gf = new GeographicalFilter();
    gf.setMapSettings(map);
    gf.setBuffer(buffer);
    gf.setMinimumResolution("_1km");
    req.setGeographicalFilter(gf);
    GatewayData resp =  gws.getGatewaySoapPort().getSiteData(req);

    Data data = resp.getData();

    // Get the dataset array
    List<Dataset> dataset = data.getDataset();

    //Put the site list into a hashmap for later use
    SiteList siteList = data.getSiteList();
    List<Site> sites = siteList.getSite();
    Map siteMap = new HashMap(sites.size());
    for(int i = 0; i < sites.size(); i++) {
        siteMap.put(sites.get(i).getSiteKey(),sites.get(i));
    }

    //Put the species list into a Hashmap for later use
    SpeciesList speciesList = data.getSpeciesList();
    List<Species> species = speciesList.getSpecies(); 
    Map speciesMap = new HashMap(species.size());
    for(int i = 0; i < species.size(); i++) {
        speciesMap.put(species.get(i).getTaxonVersionKey(),species.get(i));
    } %>
    <div id="mapWrapper">
    <div id="mapImage">
        <h2>Area Map</h2>
        <img src="<%=data.getMap().getUrl()%>" alt="site map" title="site map"/>
    </div>
    </div>
    <%
    Format formatter = new SimpleDateFormat("dd-MMM-yyyy"); // formats the XML dates to something nice.
    for (int i = 0; i < dataset.size(); i++) {
        ProviderMetadata md = dataset.get(i).getProviderMetadata(); %>
        <h2><i>Data from</i> <%=md.getDatasetTitle()%> <i>provided by</i> <%=md.getDatasetProvider()%></h2>
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
                    Site site = (Site)siteMap.get(((Site)(location.get(j)).getSiteKey()).getSiteKey());
                %>
                    <tr>
                        <td><%=sp.getScientificName()%></td>
                        <td><%=location.get(j).getLocationName()%></td>
                        <td><%=site.getGridSquare().getKey()%></td>
                        <td><%=((SpeciesRecord)record.get(k)).getRecordDate().getFrom().toString()%></td>
                        <td><%=((SpeciesRecord)record.get(k)).getRecordDate().getTo().toString()%></td>
                        <td><%=((SpeciesRecord)record.get(k)).getRecorder()%></td>
                    </tr>
                <% }
            }  %>
            </tbody>
        </table>
    <% } %>
<!-- make sure to display the the link to the NBN Gateway terms and conditions and the powered by NBN logo. -->
<div id="footer">
<div id="tandc"><a href="<%=resp.getTermsAndConditions()%>" class="popup">Gateway terms and conditions</a></div>
<div id="NBNlogo"><a href="http://data.nbn.org.uk" target="_blank"><img alt="Powered by the NBN Gateway" title="Clicking on this logo will take you to the NBN Gateway website" border="0" src="<%=resp.getNBNLogo()%>" /></a></div>
</div>
</div>
</body>
</html>
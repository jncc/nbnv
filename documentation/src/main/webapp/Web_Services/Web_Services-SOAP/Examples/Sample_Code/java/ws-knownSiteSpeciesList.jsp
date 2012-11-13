<%@ page import="net.searchnbn.webservices.service.GatewayWebService"%>
<%@ page import="net.searchnbn.webservices.query.*"%>
<%@ page import="net.searchnbn.webservices.data.*"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Properties"%>
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
<h1>Site Report Web Services Example</h1>
<p>This is an example of the Species List Web Service for a known site, filtering on a taxonomic group
    and requsting a map image. The page also uses the Taxon Reporting Category Name web service to report the selected
category name and the Known Site Name web service to report the selected site details.</p>
<%

           
GatewayWebService gws = new GatewayWebService();



            
// Get the request details.
String desig = request.getParameter("desig");
String trck = request.getParameter("tgk");
String dsKey = request.getParameter("dsKey");
String siteKey = request.getParameter("siteKey");

// test if we have query parameter
if (siteKey != null && siteKey.length() > 0); {
    // Create a KnownSite
    KnownSite site = new KnownSite();
    site.setProviderKey(dsKey);
    site.setSiteKey(siteKey);

    // 1. get the site details
    KnownSiteNameRequest siteReq = new KnownSiteNameRequest();
    siteReq.setKnownSite(site);
    KnownSiteNameResponse siteResp = gws.getGatewaySoapPort().getKnownSiteName(siteReq);
    site = siteResp.getKnownSite();

    // 2. get the Taxon Reporting Category Name
    TaxonReportingCategoryNameRequest taxCatReq = new TaxonReportingCategoryNameRequest();
    taxCatReq.setTaxonReportingCategoryKey(trck);
    TaxonReportingCategoryNameResponse taxCatResp = gws.getGatewaySoapPort().getTaxonReportingCategoryName(taxCatReq);
    String taxCatName = taxCatResp.getTaxonReportingCategory().getValue();
%>
    <h3><%=taxCatName%> species recorded in <%=site.getKnownSiteName()%>, <%=site.getKnownSiteType()%></h3>
<%
    SpeciesListRequest slr = new SpeciesListRequest();
    // Set the site filter
    GeographicalFilter gf = new GeographicalFilter();
    gf.setKnownSite(site);
    // Set the map settings
    MapSettings mapSettings = new MapSettings();
    mapSettings.setFillColour("#FF0000");
    mapSettings.setHeight(new Integer(300));
    mapSettings.setWidth(new Integer(300));
    gf.setMapSettings(mapSettings);
    slr.setGeographicalFilter(gf);

    // Set the taxon group key to filter on
    slr.setTaxonReportingCategoryKey(trck);

    // Process the results
    SpeciesListResponse list = gws.getGatewaySoapPort().getSpeciesList(slr);
    MapImage map = list.getMap();
    SpeciesList nbnSpeciesList = list.getSpeciesList();
    List<Species> species = nbnSpeciesList.getSpecies();
    int size = species.size();
%>
    <div id="mapWrapper">
        <div id="mapImage">
            <h3>Site map for <%=site.getKnownSiteName()%></h3>
            <img src="<%=map.getUrl()%>" alt="site map for <%=site.getKnownSiteName()%>" title="site map for <%=site.getKnownSiteName()%>"/>
        </div>
        <% if (size > 0) { %>
            <div id="rightOfMap">
                <h3><%=size%> species recorded</h3>
                <p>Click on a taxon group name to view the species recorded.</p>
                <table cellpadding="3" cellspacing="1" bgcolor="#C0C0C0">
                <% for (int i = 0; i < size; i++) { %>
                    <tr>
                        <td bgcolor="#FFFFFF"><a href="ws-knownSiteSpeciesData.jsp?tvk=<%=species.get(i).getTaxonVersionKey()%>&dsKey=<%=dsKey%>&siteKey=<%=siteKey%>"><%=species.get(i).getScientificName()%></a></td>
                        <td bgcolor="#FFFFFF"><%=species.get(i).getCommonName()%></td>
                    </tr>
                <% } %>
                </table>
            </div>
        <% } %>
    </div>
    <div class="bottomLine" />
    <h2>Datasets used</h2>
    <div class="bottomLine">
    <table class="tblBorder" cellspacing="0" cellpadding="2" width="100%">
        <tr>
            <th>Dataset</th>
            <th>Provider</th>
            <%  // Get an array of datasts
                DatasetSummaryList nbnDatasetSummaryList =  list.getDatasetSummaryList();
                List<DatasetSummary> datasets = nbnDatasetSummaryList.getDatasetSummary();
                for (int i = 0; i < datasets.size(); i++) { %>
                    <tr>
                        <td width="50%"><a href="ws-dataset.jsp?dsKey=<%=datasets.get(i).getId()%>"><%=datasets.get(i).getProviderMetadata().getDatasetTitle()%></a></td>
                        <td width="50%"><%=datasets.get(i).getProviderMetadata().getDatasetProvider()%></td>
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
<% } %>
</body>
</html>
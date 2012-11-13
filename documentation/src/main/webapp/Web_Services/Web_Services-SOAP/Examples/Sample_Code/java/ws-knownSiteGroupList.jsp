<%@ page import="net.searchnbn.webservices.service.GatewayWebService"%>
<%@ page import="net.searchnbn.webservices.query.*"%>
<%@ page import="net.searchnbn.webservices.data.*"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
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
<p>This is an example of the Taxon Reporting Category List Web Service for a known site, requesting a map image.
The page also uses the Known Site List Web Service to build the drop down list of sites.</p>
<%
    // Get the parameters, if any!
    String site = request.getParameter("site");
    String designationNoneSelectedValue = "NONE";
    String designationNoneSelectedText = "None";
    String desig = designationNoneSelectedValue;
    boolean hasSite = false;
    KnownSite ks = null;
    if (site != null && site.length() > 0) {
        hasSite = true;
        desig = request.getParameter("desig");
    }

    GatewayWebService gws = new GatewayWebService();

    KnownSiteListRequest kslr = new KnownSiteListRequest();
    kslr.setKnownSiteType("RSPB_IMPORTANT_BIRD_AREAS");
    KnownSiteListResponse ksList =  gws.getGatewaySoapPort().getKnownSiteList(kslr);
    KnownSiteList nbnKnownSiteList= ksList.getKnownSiteList();
    List<KnownSite> knownSiteList = nbnKnownSiteList.getKnownSite();
    int selectedSiteIndex = 0;

%>
<fieldset>
    <legend>Create a species list for a known site</legend>
    <form method="post" action="ws-knownSiteGroupList.jsp">
        <p><strong>Select an Important Bird Area</strong>
        <select name="site">
            <option value="">-- Select a Site --</option>
            <% for (int i = 0; i < knownSiteList.size(); i++) {
                ks =knownSiteList.get(i);
                String value = ks.getSiteKey() + '!' + ks.getProviderKey();
                if (value.equals(site)) {
                    out.print("<option selected=\"selected\" value=\"" + value + "\">" + ks.getKnownSiteName() + "</option>");
                    selectedSiteIndex = i;
                } else {
                    out.print("<option value=\"" + value + "\">" + ks.getKnownSiteName() + "</option>");
                }
            } %>
        </select></p>
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
if (hasSite) {
    // The request
    TaxonReportingCategoryListRequest req = new TaxonReportingCategoryListRequest();

    // Set the site ID
    String[] param = request.getParameter("site").split("!");
    ks = new KnownSite();
    ks.setProviderKey(param[1]);
    ks.setSiteKey(param[0]);
    // Add the site to a GeographicalFilter
    GeographicalFilter gf = new GeographicalFilter();
    gf.setKnownSite(ks);
    // Set the map settings
    MapSettings mapSettings = new MapSettings();
    mapSettings.setFillColour("#FF0000");
    mapSettings.setHeight(new Integer(300));
    mapSettings.setWidth(new Integer(300));
    mapSettings.setFillTransparency(new Double(1.0));
    // Add the map settings to the GeographicalFilter
    gf.setMapSettings(mapSettings);
    // Add the GeographicalFilter to the request
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

    // Set the designation
    if(!desig.equalsIgnoreCase(designationNoneSelectedValue)){
        req.setDesignation(desig);
    }

    // Process the results
    TaxonReportingCategoryListResponse list = gws.getGatewaySoapPort().getTaxonReportingCategoryList(req);
    MapImage map = list.getMap();
    TaxonReportingCategoryList groupList = list.getTaxonReportingCategoryList();
    boolean recordsFound = list.isRecordsFound();
    List<TaxonReportingCategory> group = null;;
    int size = 0;
    if(recordsFound){
        group = groupList.getTaxonReportingCategory();
        size = group.size();
    }
%>
    <div id="mapWrapper">
        <div id="mapImage">
            <h3>Site map for <%=knownSiteList.get(selectedSiteIndex).getKnownSiteName()%></h3>
            <img src="<%=map.getUrl()%>" alt="site map for <%=knownSiteList.get(selectedSiteIndex).getKnownSiteName()%>" title="site map for <%=knownSiteList.get(selectedSiteIndex).getKnownSiteName()%>"/>
        </div>
        <% 
        if(recordsFound){
            if (size > 0) { %>
                <div id="rightOfMap">
                <h3><%=size%> species groups recorded</h3>
                <p>Click on a taxon group name to view the species recorded.</p>
                <table cellpadding="3" cellspacing="1" bgcolor="#C0C0C0">
                <% for (int i = 0; i < size; i++) { %>
                    <tr>
                        <td bgcolor="#FFFFFF"><a href="ws-knownSiteSpeciesList.jsp?desig=<%=desig%>&tgk=<%=group.get(i).getTaxonReportingCategoryKey()%>&dsKey=<%=ks.getProviderKey()%>&siteKey=<%=ks.getSiteKey()%>"><%=group.get(i).getValue()%></td>
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
                    DatasetSummaryList nbnDatasetList =  list.getDatasetSummaryList();
                    List<DatasetSummary> datasetSummaryList = nbnDatasetList.getDatasetSummary();
                    for (int i = 0; i < datasetSummaryList.size(); i++) { %>
                        <tr>
                            <td width="50%"><a href="ws-dataset.jsp?dsKey=<%=datasetSummaryList.get(i).getId()%>"><%=datasetSummaryList.get(i).getProviderMetadata().getDatasetTitle()%></a></td>
                            <td width="50%"><%=datasetSummaryList.get(i).getProviderMetadata().getDatasetProvider()%></td>
                        </tr>
                    <% }
                %>
        </table>
        </div>
    <%}else{%>
        <h2>No records found for your query</h2>
    <%}%>
    <!-- Make sure to display the the link to the NBN Gateway terms and conditions and the powered by NBN logo. -->
    <div id="footer">
    <div id="tandc"><a href="<%=list.getTermsAndConditions()%>" class="popup">Gateway terms and conditions</a></div>
    <div id="NBNlogo"><a href="http://data.nbn.org.uk" target="_blank"><img alt="Powered by the NBN Gateway" title="Clicking on this logo will take you to the NBN Gateway website" border="0" src="<%=list.getNBNLogo()%>" /></a></div>
    </div>
<% } %>
</div>
</body>
</html>
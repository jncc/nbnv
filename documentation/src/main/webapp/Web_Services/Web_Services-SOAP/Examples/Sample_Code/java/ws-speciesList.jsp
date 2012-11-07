<%@ page import="net.searchnbn.webservices.service.GatewayWebService"%>
<%@ page import="net.searchnbn.webservices.query.TaxonReportingCategoryListRequest"%>
<%@ page import="net.searchnbn.webservices.query.SpeciesListRequest"%>
<%@ page import="net.searchnbn.webservices.query.SpeciesSort"%>
<%@ page import="net.searchnbn.webservices.data.*"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%@ page import="java.net.URL"%>
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
<h1>NBN Species List Web Service, java example</h1>
<p>This example of the Species List web service uses no spatial filtering. The taxon group filter and designation filters are applied and names are sorted
by English name order. Click on a species name to view a grid map.</p>
<%
           
    String desig = "NONE";
    if (request.getParameter("desig") != null) {
        desig = request.getParameter("desig");
    }
    String tgKey = request.getParameter("tgKey");
    
   GatewayWebService gws = new GatewayWebService();


    TaxonReportingCategoryListRequest tglr = new TaxonReportingCategoryListRequest();
    TaxonReportingCategoryListResponse tgList =  gws.getGatewaySoapPort().getTaxonReportingCategoryList(tglr);

    StringBuffer sbGroups = new StringBuffer();
    TaxonReportingCategoryList nbngroups = tgList.getTaxonReportingCategoryList();
    List<TaxonReportingCategory> groups = nbngroups.getTaxonReportingCategory(); 
    String selGroup = "";
    for (int i = 0; i < groups.size(); i++) {
        TaxonReportingCategory tg = groups.get(i);
        if (tg.getTaxonReportingCategoryKey().equals(tgKey)) {
            sbGroups.append("<option selected=\"selected\" value=\"").append(tg.getTaxonReportingCategoryKey()).append("\">").append(tg.getValue()).append("</option>");
            selGroup = tg.getValue();
        } else {
            sbGroups.append("<option value=\"").append(tg.getTaxonReportingCategoryKey()).append("\">").append(tg.getValue()).append("</option>");
        }
    }

    StringBuffer sbDesigs = new StringBuffer();
    String[][] designations = {
            {"NONE","None"},
            {"BIODIVERSITY_ACTION_PLAN","Biodiversity Action Plan 2007"},
            {"SNH_SCOTTISH_LIST","SNH Scottish List"},
            {"ENVIRONMENT_AGENCY_PROTECTED_SPECIES", "Environment Agency Protected Species"},
            {"WELSH_SPECIES_OF_PRINCIPAL_IMPORTANCE", "Welsh Species of Principal Importance"}
            };


    String selDesig = "";
    for (int i = 0; i < designations.length; i++) {
        if (desig.equals(designations[i][0]) && !"NONE".equals(designations[i][0])) {
            sbDesigs.append("<option selected=\"selected\" value=\"").append(designations[i][0]).append("\">").append(designations[i][1]).append("</option>");
            selDesig = designations[i][1];
        } else {
            sbDesigs.append("<option value=\"").append(designations[i][0]).append("\">").append(designations[i][1]).append("</option>");
        }
    }
%>
<fieldset>
    <legend>Build Species List</legend>
    <form method="post" action="ws-speciesList.jsp">
        <p>Select taxon group: <select name="tgKey"><%=sbGroups.toString()%></select></p>
        <p>Select Designation: <select name="desig"><%=sbDesigs.toString()%></select> <input type="submit" value="go" /></p>
    </form>
</fieldset>
<% if (tgKey != null && tgKey.length() > 0) {
    SpeciesListRequest slr = new SpeciesListRequest();

    slr.setTaxonReportingCategoryKey(tgKey);
    
    slr.setSort(SpeciesSort.COMMON);
    if (!"NONE".equals(desig)) {
        slr.setDesignation(desig);
    }

    SpeciesListResponse list = gws.getGatewaySoapPort().getSpeciesList(slr);
    SpeciesList nbnSpeciesList = list.getSpeciesList();
    if (nbnSpeciesList != null) {
         List<Species> species = nbnSpeciesList.getSpecies();
         int size = species.size();
         if (size > 0) {
             out.print("<p><strong>" + size + ' ' + selDesig + ' ' + selGroup + " species recorded on the NBN Gateway</strong></p>");
             out.print("<table class=\"tblBorder\" cellspacing=\"0\" cellpadding=\"2\" align=\"center\">");
             out.print("<tr><th colspan=\"2\">Species</th></tr>");
             for (int i = 0; i < size; i++) {%>
            <tr>
                <td><a href="ws-gridmap.jsp?tvk=<%=species.get(i).getTaxonVersionKey()%>"><%=species.get(i).getScientificName()%></a></td>
                <td><%=species.get(i).getCommonName()%></td>
            </tr>
            <% }
             out.print("</table>");
         }
     }%>
     <h2>Datasets used</h2>
     <div class="bottomLine">
         <table class="tblBorder" cellspacing="0" cellpadding="2" width="100%">
             <tr>
                 <th>Dataset</th>
                 <th>Provider</th>
                 <%  // Get an array of datasts
          DatasetSummaryList nbnsummaryList = list.getDatasetSummaryList();
          if (nbnsummaryList != null) {
              List<DatasetSummary> datasets = nbnsummaryList.getDatasetSummary();
              for (int i = 0; i < datasets.size(); i++) {%>
             <tr>
                 <td width="50%"><a href="ws-dataset.jsp?dsKey=<%=datasets.get(i).getId()%>"><%=datasets.get(i).getProviderMetadata().getDatasetTitle()%></a></td>
                 <td width="50%"><%=datasets.get(i).getProviderMetadata().getDatasetProvider()%></td>
             </tr>
             <% }%>
         </table>
     </div>
     <%   }
}%>
<!-- make sure to display the the link to the NBN Gateway terms and conditions and the powered by NBN logo. -->
<div id="footer">
<div id="tandc"><a href="<%=tgList.getTermsAndConditions()%>" class="popup">Gateway terms and conditions</a></div>
<div id="NBNlogo"><a href="http://data.nbn.org.uk" target="_blank"><img alt="Powered by the NBN Gateway" title="Clicking on this logo will take you to the NBN Gateway website" border="0" src="<%=tgList.getNBNLogo()%>" /></a></div>
</div>
</div>
</body>
</html>
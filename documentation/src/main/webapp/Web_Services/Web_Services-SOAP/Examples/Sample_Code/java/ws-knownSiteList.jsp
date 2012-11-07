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
<head><title>NBN Known Site List Web Service</title>
<link href="examples.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<body>
<div id="container">
<h1>Known Site List Web Services Example</h1>
<p>This is an example of the Known Site List web service creating a list of Vice County names.</p>
<%
GatewayWebService gws = new GatewayWebService();

KnownSiteListRequest siteReq = new KnownSiteListRequest();
siteReq.setKnownSiteType("WATSONIAN_VICE_COUNTY");
KnownSiteListResponse siteResp = gws.getGatewaySoapPort().getKnownSiteList(siteReq);
KnownSiteList siteList = siteResp.getKnownSiteList();
List<KnownSite> sites = siteList.getKnownSite();
%>
<h3>Vice Counties on the NBN Gateway</h3>
<table cellpadding="3" cellspacing="1" bgcolor="#C0C0C0">
    <tr>
        <th>site key</th>
        <th>site name</th>
    </tr>
<% for (int i = 0; i < sites.size(); i++) {
    KnownSite site = sites.get(i); %>
    <tr>
        <td><%=site.getSiteKey()%></td>
        <td><%=site.getKnownSiteName()%></td>
    </tr>
<% } %>
</table>
<!-- Make sure to display the the link to the NBN Gateway terms and conditions and the powered by NBN logo. -->
<div id="footer">
<div id="tandc"><a href="<%=siteResp.getTermsAndConditions()%>" class="popup">Gateway terms and conditions</a></div>
<div id="NBNlogo"><a href="http://data.nbn.org.uk" target="_blank"><img alt="Powered by the NBN Gateway" title="Clicking on this logo will take you to the NBN Gateway website" border="0" src="<%=siteResp.getNBNLogo()%>" /></a></div>
</div>
</div>
</body>
</html>
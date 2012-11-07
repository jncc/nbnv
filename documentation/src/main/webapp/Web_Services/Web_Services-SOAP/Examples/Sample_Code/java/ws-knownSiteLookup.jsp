<%@ page import="net.searchnbn.webservices.service.GatewayWebService"%>
<%@ page import="net.searchnbn.webservices.query.*"%>
<%@ page import="net.searchnbn.webservices.data.*"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--
@author Richard Ostler, CEH Monks Wood
@date   09/02/2006
-->
<html>
<head><title>NBN Known Site Name Web Service</title>
<link href="examples.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<body>
<div id="container">
<h1>Known Site Name Web Services Example</h1>
<p>This is an example of the Known Site Name web service, used to lookup a site name from a site key and provider key.</p>
<%
GatewayWebService gws = new GatewayWebService();

// Get the request details.
String siteKey = "292";
String providerKey = "GA000374";

// Create a KnownSite
KnownSite site = new KnownSite();
site.setProviderKey(providerKey);
site.setSiteKey(siteKey);

// 1. get the site details
KnownSiteNameRequest siteReq = new KnownSiteNameRequest();
siteReq.setKnownSite(site);
KnownSiteNameResponse siteResp = gws.getGatewaySoapPort().getKnownSiteName(siteReq);
site = siteResp.getKnownSite();
%>
    <h3>Site Details:</h3>
    <p>site key: <b><%=site.getSiteKey()%></b></p>
    <p>provider key: <b><%=site.getProviderKey()%></b></p>
    <p>name: <b><%=site.getKnownSiteName()%></b></p>
    <p>type: <b><%=site.getKnownSiteType()%></b></p>
</body>
</html>
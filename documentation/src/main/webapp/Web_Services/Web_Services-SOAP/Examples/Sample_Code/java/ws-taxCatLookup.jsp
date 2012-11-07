<%@ page import="net.searchnbn.webservices.service.GatewayWebService"%>
<%@ page import="net.searchnbn.webservices.query.*"%>
<%@ page import="net.searchnbn.webservices.data.*"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--
@author Richard Ostler, CEH Monks Wood
@date   09/02/2006
-->
<html>
<head><title>NBN Taxon Reporting Category Name Web Service</title>
<link href="examples.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<body>
<div id="container">
<h1>Taxon Reporting Category Name Web Services Example</h1>
<p>This is an example of the Taxon Reporting Category Name web service, used to lookup a Taxon Reporting Category name from a key.</p>
<%
GatewayWebService gws = new GatewayWebService();
TaxonReportingCategoryNameRequest req = new TaxonReportingCategoryNameRequest();
req.setTaxonReportingCategoryKey("NHMSYS0000080071");
TaxonReportingCategoryNameResponse resp = gws.getGatewaySoapPort().getTaxonReportingCategoryName(req);
%>
    <h3>Taxon Reporting Category Details:</h3>
    <p>key: <b><%=resp.getTaxonReportingCategory().getTaxonReportingCategoryKey()%></b></p>
    <p>name: <b><%=resp.getTaxonReportingCategory().getValue()%></b></p>
</body>
</html>
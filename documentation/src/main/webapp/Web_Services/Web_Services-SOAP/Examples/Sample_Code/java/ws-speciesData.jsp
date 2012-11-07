<%@ page import="net.searchnbn.webservices.service.GatewayWebService"%>
<%@ page import="net.searchnbn.webservices.data.*"%>
<%@ page import="net.searchnbn.webservices.query.*"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<!--
@author Richard Ostler/Jon Cooper, CEH Monks Wood
@date   09/02/2006
-->
<html>
<head><title>NBN Species List Web Service</title>
<link href="examples.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<body>
<div id="container">
<h1>NBN One Species Data Request, java example</h1>
<%
    String tvk = request.getParameter("tvk");
%>


<p>Datasets with locations for the species: <%=tvk%>, <a href="toSource.jsp?fname=ws-speciesData.jsp">here is the source code</a> (note, I've not finished tyding this page, but you can see how it works ;)</p>

<%
    GatewayWebService gws = new GatewayWebService();

    OneSpeciesDataRequest req = new OneSpeciesDataRequest();
    req.setTaxonVersionKey(tvk);
    GatewayData resp =  gws.getGatewaySoapPort().getSpeciesData(req);

    Data data = resp.getData();
    List<Dataset> datasets = data.getDataset();
    for (int i = 0; i < datasets.size(); i++) {
        Dataset d = datasets.get(i);
        out.println("Dataset: " + ((ProviderMetadata)d.getProviderMetadata()).getDatasetTitle());
        out.println("<table><tr><td>Site Id</td><td>Site name</td></tr>");
        for (int j = 0; j < d.getLocation().size(); j++){
            out.println("<table><tr><td>" + d.getLocation().get(j).getId() + "</td><td>" + d.getLocation().get(j).getLocationName() + "</td></tr>");
        }
        out.println("</table>");
    }

    %>
<!-- make sure to display the the link to the NBN Gateway terms and conditions and the powered by NBN logo. -->
<div id="footer">
<div id="tandc"><a href="<%=resp.getTermsAndConditions()%>" class="popup">Gateway terms and conditions</a></div>
<div id="NBNlogo"><a href="http://data.nbn.org.uk" target="_blank"><img alt="Powered by the NBN Gateway" title="Clicking on this logo will take you to the NBN Gateway website" border="0" src="<%=resp.getNBNLogo()%>" /></a></div>
</div>
</div>
</body>
</html>3:39 PM 12/14/2007
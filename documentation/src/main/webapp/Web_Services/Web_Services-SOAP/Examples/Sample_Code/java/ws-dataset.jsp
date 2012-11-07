<%@ page import="net.searchnbn.webservices.service.GatewayWebService"%>
<%@ page import="net.searchnbn.webservices.query.DatasetListRequest"%>
<%@ page import="net.searchnbn.webservices.query.DatasetFilter"%>
<%@ page import="net.searchnbn.webservices.data.*"%>
<%@ page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--
@author Richard Ostler, CEH Monks Wood
@date   30/06/2006
-->
<html>
<head><title>NBN Species List Web Service</title>
<link href="examples.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<body>
<div id="container">
<h1>Dataset Metadata Web Service, java example</h1>
<p>This is an example of the dataset metadata web service. This service returns metadata for one or more datasets.
 This example fetches metadata for the dataset identified by a dataset key request parameter</p>
<%
    GatewayWebService gws = new GatewayWebService();
	DatasetListRequest dlr = new DatasetListRequest();

    DatasetFilter df = new DatasetFilter();
    df.getDatasetKey().add(request.getParameter("dsKey"));

    dlr.setDatasetList(df);
    DatasetSummaryList datasetSummaryList = gws.getGatewaySoapPort().getDatasetList(dlr);
    List<DatasetSummary> list = datasetSummaryList.getDatasetSummary();
            if (list.size() > 0) {
        ProviderMetadata md = list.get(0).getProviderMetadata();
        Abstract  abs = md.getAbstract();
        %>
        <div class="bottomLine">
        <table cellspacing="1" bgcolor="#CCCCCC" cellpadding="2" width="100%">
            <tr>
                <th>Field</th>
                <th>Value</th>
            </tr><tr>
                <td><b>Title</b></td>
                <td><%=md.getDatasetTitle()%></td>
            </tr><tr>
                <td><b>Provider</b></td>
                <td><%=md.getDatasetProvider()%></td>
            </tr><tr>
                <td><b>Description</b></td>
                <td><%=abs.getDescription()%></td>
            </tr><tr>
                <td><b>Data Capture Method</b></td>
                <td><%=abs.getDataCaptureMethod()%></td>
            </tr><tr>
                <td><b>Dataset Purpose</b></td>
                <td><%=abs.getDatasetPurpose()%></td>
            </tr><tr>
                <td><b>Geographical Coverage</b></td>
                <td><%=abs.getGeographicalCoverage()%></td>
            </tr><tr>
                <td><b>Temporal Coverage</b></td>
                <td><%=abs.getTemporalCoverage()%></td>
            </tr><tr>
                <td><b>Data Quality</b></td>
                <td><%=abs.getDataQuality()%></td>
            </tr><tr>
                <td><b>Additional Information</b></td>
                <td><%=abs.getAdditionalInformation()%></td>
            </tr><tr>
                <td><b>Access Constraints</b></td>
                <td><%=md.getAccessConstraints()%></td>
            </tr><tr>
                <td><b>Use Constraints</b></td>
                <td><%=md.getUseConstraints()%></td>
            </tr>
        </table>
        </div>
    <% } %>
    <!-- Make sure to display the the link to the NBN Gateway terms and conditions and the powered by NBN logo. -->
<div id="footer">
<div id="tandc"><a href="http://data.nbn.org.uk/help/popups/generalTerms.jsp" class="popup">Gateway terms and conditions</a></div>
<div id="NBNlogo"><a href="http://data.nbn.org.uk" target="_blank"><img alt="Powered by the NBN Gateway" title="Clicking on this logo will take you to the NBN Gateway website" border="0" src="http://data.nbn.org.uk/images/NBNPower.gif" /></a></div>
</div>
</div>
</body>
</html>
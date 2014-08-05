<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h2>JSP</h2>
        <script type="syntaxhighlighter" class="brush: java; html-script: true"><![CDATA[
        <\%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
        <\%@ page import="nbnapi.*" %>

        <\%
                NBNClient nbnClient = new NBNClient();
                nbnClient.login("#username#", "#password#");
                TaxonObservation[] observations = nbnClient.postRecordsForDatasetAndGridRef("GA000466", "TL28");
        %>

        <!DOCTYPE html>
        <html>
        <head>
                <title>Records for a Ten Km Square</title>
                <link rel="stylesheet" href="../css/style.css" />
        </head>
        <body>
                <h1>Records for a Ten Km Square</h1>
            <h3>Ten Km Square: TL28</h3>
            <h3>Dataset: <a href="https://data.nbn.org.uk/Datasets/GA000466">GA000466</a>: Demonstration dataset for record access on the NBN Gateway</h3>
            <div>
            <\% if (observations == null || observations.length == 0) { %>
                        No records
            <\% } else { %>
                <table class="nbnData">
                    <thead>
                        <tr>
                                <th>DatasetKey</th>
                                <th>Observation ID</th>
                                <th>Observation Key</th>
                                <th>Location</th>
                                <th>Resolution</th>
                                <th>TaxonVersionKey</th>
                                <th>Preferred TaxonVersionKey</th>
                                <th>Preferred Taxon Name</th>
                                <th>Preferred Taxon Authority</th>
                                <th>Start Date</th>
                                <th>End Date</th>
                                <th>Date Type</th>
                        </tr>
                    </thead>
                    <tbody>
                    <\%
                        for (TaxonObservation item : observations) {
                                String href= String.format("https://data.nbn.org.uk/Datasets/%s", item.getDatasetKey());
                    %>
                    <tr>
                        <td>
                            <a href="<\%= href %>"><\%= item.getDatasetKey() %></a>
                        </td>
                        <td>
                            <\%= item.getObservationID() %>
                        </td>
                        <td>
                            <\%= item.getObservationKey() %>
                        </td>
                        <td>
                            <\%= item.getLocation() %>
                        </td>
                        <td>
                            <\%= item.getResolution() %>
                        </td>
                        <td>
                            <\%= item.getTaxonVersionKey() %>
                        </td>
                        <td>
                            <\%= item.getpTaxonVersionKey() %>
                        </td>
                        <td>
                            <\%= item.getpTaxonName() %>
                        </td>
                        <td>
                            <\%= item.getpTaxonAuthority() %>
                        </td>
                        <td>
                            <\%= item.getStartDate() %>
                        </td>
                        <td>
                            <\%= item.getEndDate() %>
                        </td>
                        <td>
                            <\%= item.getDateTypekey() %>
                        </td>
                    </tr>
                    <\% } %>
                    </tbody>
                </table>
                <\% } %>
            </div>
        </body>
        </html>
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

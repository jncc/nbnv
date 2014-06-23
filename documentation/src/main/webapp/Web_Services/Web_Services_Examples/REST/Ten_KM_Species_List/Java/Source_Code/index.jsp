<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Ten Km Species List Source Code Java</h1>
        <h2>TenKmSpeciesList.jsp</h2>
        <script type="syntaxhighlighter" class="brush: java ; html-script: true"><![CDATA[
        <\%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
        <\%@ page import="nbnapi.*" %>

        <\% 
                NBNClient nbnClient = new NBNClient();
                TaxonWithQueryStats[] species = nbnClient.getTenKmSpecies("SO21");
                TaxonDatasetWithQueryStats[] datasets = nbnClient.getTenKmDatasets("SO21");
        %>

        <!DOCTYPE html>
        <html>
        <head>
                <title>Species List for a Ten Km Square</title>
            <link rel="stylesheet" href="../css/style.css" />
        </head>
        <body>
                <h1>Species List for a Ten Km Square</h1>
            <h3>Ten Km Square: SO21</h3>
                <h3>Species List</h3>
                <table class="nbnData">
                <thead>
                        <tr>
                           <th>Taxon Name</th>
                           <th>Taxon Authority</th>
                           <th>TaxonVersionKey</th>
                           <th>Taxon Group</th>
                           <th>Rank</th>
                           <th>Number of records</th>
                        </tr>
                        </thead>
                        <tbody>
                        <\%
                                for (TaxonWithQueryStats item : species) {
                                %>
                                <tr>
                                        <td>
                                <\%= item.getTaxon().getName() %>
                            </td>
                            <td>
                                                <\%= item.getTaxon().getAuthority() %>
                                        </td>
                                        <td>
                                                <\%= item.getTaxon().getTaxonVersionKey() %>
                                        </td>
                                        <td>
                                                <\%= item.getTaxon().getTaxonOutputGroupName() %>
                                        </td>
                                        <td>
                                                <\%= item.getTaxon().getRank() %>
                                        </td>
                                        <td>
                                                <\%= item.getQuerySpecificObservationCount() %>
                                        </td>
                                </tr>
                                <\%
                                }
                        %>
                        </tbody>
                </table>

                <h3>Dataset List</h3>
                <table class="nbnData">
                <thead>
                        <tr>
                       <th>DatasetKey</th>
                       <th>Title</th>
                       <th>Data Provider</th>
                       <th>Number of records</th>
                   </tr>
                        </thead>
                        <tbody>
                        <\%
                                for (TaxonDatasetWithQueryStats item : datasets) {
                                %>
                                <tr>
                                        <td>
                                <a href='<\%= item.getTaxonDataset().getHref() %>'><\%= item.getDatasetKey() %></a>
                            </td>
                            <td>
                                                <\%= item.getTaxonDataset().getTitle() %>
                                        </td>
                                        <td>
                                                <\%= item.getTaxonDataset().getOrganisationName() %>
                                        </td>
                                        <td>
                                                <\%= item.getQuerySpecificObservationCount() %>
                                        </td>
                                </tr>
                                <\%
                                }
                        %>
                        </tbody>
                </table>
        </body>
        </html>
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

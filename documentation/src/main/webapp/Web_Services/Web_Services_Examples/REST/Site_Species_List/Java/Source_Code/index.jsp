<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Site Species List Source Code Java</h1>
        <h2>JSP Markup</h2>
        <script type="syntaxhighlighter" class="brush: js; html-script: true"><![CDATA[
        <\%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
        <\%@ page import="nbnapi.*" %>

        <\% 
                NBNClient nbnClient = new NBNClient();
                SiteBoundary[] viceCounties = nbnClient.getViceCounties();
                TaxonOutputGroup[] taxonOutputGroups = nbnClient.getTaxonOutputGroups();
                Designation[] designations = nbnClient.getDesignations();

                String viceCounty = "";
                String taxonOutputGroup = "";
                String designation = "";

                TaxonWithQueryStats[] species = null;
                TaxonDatasetWithQueryStats[] datasets = null;

                //determine whether this page is being generated after a submit
                boolean isPostBack = ("POST".equalsIgnoreCase(request.getMethod()));
                if (isPostBack) {
                        //Get the form variables
                    viceCounty = request.getParameter("viceCounties");
                    taxonOutputGroup = request.getParameter("taxonOutputGroups");
                    designation = request.getParameter("designations");

                    //Get the species and datasets from the nbnClient
                    species = nbnClient.getSiteSpecies(viceCounty, taxonOutputGroup, designation);
                    datasets = nbnClient.getSiteDatasets(viceCounty, taxonOutputGroup, designation);
                }
        %>

        <!DOCTYPE html>
        <html>
        <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Species List for a Vice County</title>
        <link rel="stylesheet" href="../css/style.css" />
        </head>
        <body>
                <h1>Species List for a Vice County</h1>
                <form method="post" action="SiteSpeciesList.jsp">

                        <select name="viceCounties"> 
                        <\% for (SiteBoundary item : viceCounties) { 
                                if (item.getIdentifier().equals(viceCounty)) { %>
                                        <option value="<\%= item.getIdentifier() %>" selected="selected"><\%= item.getName() %></option>
                                <\% } else { %>
                                        <option value="<\%= item.getIdentifier() %>"><\%= item.getName() %></option>
                                <\% }
                        } %>
                        </select>

                        <select name="taxonOutputGroups">
                        <\% for (TaxonOutputGroup item : taxonOutputGroups) { 
                                if (item.getKey().equals(taxonOutputGroup)) { %>
                                        <option value="<\%= item.getKey() %>" selected="selected"><\%= item.getName() %></option>
                                <\% } else { %>
                                        <option value="<\%= item.getKey() %>"><\%= item.getName() %></option>
                                <\% }
                        } %>
                        </select>

                        <select name="designations">
                                <option value="" />
                        <\% for (Designation item : designations) { 
                                if (item.getCode().equals(designation)) { %>
                                        <option value="<\%= item.getCode() %>" selected="selected"><\%= item.getName() %></option>
                                <\% } else { %>
                                        <option value="<\%= item.getCode() %>"><\%= item.getName() %></option>
                                <\% }
                                } %>
                </select>

                <input type="submit" name="refresh" value="Refresh" />
            </form>

            <\% if (isPostBack) { %>
                <div>
                        <h3>Species List</h3>
                        <\% if (species == null || species.length == 0) { %>
                                No records
                        <\% } else { %>
                    <table class="nbnData">
                        <thead>
                                <tr>
                                    <th>Taxon Name</th>
                                    <th>Taxon Authority</th>
                                    <th>TaxonVersionKey</th>
                                    <th>Rank</th>
                                    <th>Number of records</th>
                            </tr>
                        </thead>
                        <tbody>
                        <\% for (TaxonWithQueryStats item : species) { %>
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
                                        <\%= item.getTaxon().getRank() %>
                                    </td>
                                    <td>
                                        <\%= item.getQuerySpecificObservationCount() %>
                                    </td>
                                </tr>
                        <\% } %>
                        </tbody>
                    </table>
                        <\% } %>

            <h3>Dataset List</h3>
            <\% if (datasets == null || datasets.length == 0) { %>
                No records
            <\% } else { %>
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
                        <\% for(TaxonDatasetWithQueryStats item : datasets) { %>
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
                        <\% } %>
                </tbody>
            </table>
            <\% } %>
            </div>
                <\% } %>
        </body>
        </html>
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

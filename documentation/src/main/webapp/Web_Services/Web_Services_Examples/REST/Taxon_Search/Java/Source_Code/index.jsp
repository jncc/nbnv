<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>TaxonSearch and Taxonomy Source Code Java</h1>
        <h2>TaxonSearch JSP</h2>
        <script type="syntaxhighlighter" class="brush: java; html-script: true"><![CDATA[
        <\%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
        <\%@ page  import="nbnapi.*" %>

        <\%
                boolean isPostBack = ("POST".equalsIgnoreCase(request.getMethod()));
                String searchTerm = "";
                SearchResult searchResults = null;
                if (isPostBack) {
                        searchTerm = request.getParameter("searchTerm");
                        NBNClient nbnClient = new NBNClient();
                        searchResults = nbnClient.getTaxonSearchResult(searchTerm);
                }
        %>

        <!DOCTYPE html>
        <html>
        <head>
                <title>Taxon Search</title>
                <link rel="stylesheet" href="../css/style.css" />
        </head>
        <body>
        <form action="TaxonSearch.jsp" method="post">
            <div>
                <h1>Taxon Search</h1>
                Search for a taxon: 
                <input type="text" name="searchTerm" value=<\%= searchTerm %>>
                <button type="submit" name="btnSearch" value="Search">Search</button>
                <\% if(isPostBack) { %>
                <h3>Search Results</h3>
                <div>
                        <\% if (searchResults == null) { %>
                                No results found
                        <\% } else { 
                                for (Result item : searchResults.getResults()) { 
                        %>
                                <p>
                                        <a href="./Taxonomy.jsp?taxonVersionKey=<\%= item.getTaxonVersionKey() %>"><\%= item.getName() %></a>
                                </p>
                        <\%
                                }
                        }
                        %>
                </div>
                <\% } %>
            </div>
        </form>
        </body>
        </html>
        ]]></script>

        <h2>Taxonomy JSP </h2>
        <script type="syntaxhighlighter" class="brush: java; html-script: true"><![CDATA[
        <\%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
        <\%@ page  import="nbnapi.*" %>

        <\%
                String taxonVersionKey = request.getParameter("taxonVersionKey");
                NBNClient nbnClient = new NBNClient();
                Taxon taxon = nbnClient.getTaxonomy(taxonVersionKey);
                String title = taxon.getName();
                if (taxon.getTaxonVersionKey() != taxon.getPtaxonVersionKey())
            {
                Taxon preferredTaxon = nbnClient.getTaxonomy(taxon.getPtaxonVersionKey());
                title += " (" + preferredTaxon.getName() + ")";
            }
                Taxon[] parents = nbnClient.getTaxonParents(taxonVersionKey);
                Taxon[] children = nbnClient.getTaxonChildren(taxonVersionKey);
        %>

        <!DOCTYPE html>
        <html>
        <head>
                <title>Taxonomy</title>
                <link rel="stylesheet" href="../css/style.css" />
        </head>
        <body>
                <h2>Taxonomy for <\%= title %></h2>
                <h2>Parents:</h2>
                        <\% if (parents == null) { %>
                        No parents
                <\% } else {
                        for (Taxon parent : parents) {
                 %>
                        <p><span><\%= parent.getRank() %></span> : <span><\%= parent.getName() %></span></p>
                <\%  } 
                           } %>

                <h2>Children:</h2>
                <\% if (children == null) { %>
                        No children
                <\% } else {
                        for (Taxon child : children) {
                        %>
                                <p><span><\%= child.getName() %></span></p>
                <\% }
                        } %>
        </body>
        </html>
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

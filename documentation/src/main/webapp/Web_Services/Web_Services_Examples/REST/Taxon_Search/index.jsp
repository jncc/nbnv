<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Taxon Search</h1>
        <h2>Overview</h2>
        <p>This example demonstrates use of the TaxonResource to search for taxa that match a particular search term. After a search, the page displays a list of results as links to basic taxonomy pages. The taxonomy page uses the supplied taxon version key to query the TaxonResource and display the taxonomic hierarchy (parents, children and preferred name) of the selected taxon.</p>
        <h2>Resources used</h2>
        <ul>
            <li><a href="https://data.nbn.org.uk/Documentation/Web_Services/Web_Services-REST/resources/restapi/resource_TaxonResource.html#path__taxa.html" target="_blank">/taxa</a></li>
            <li><a href="https://data.nbn.org.uk/Documentation/Web_Services/Web_Services-REST/resources/restapi/resource_TaxonResource.html#path__taxa_-taxonVersionKey-_parent.html" target="_blank">/taxa/{taxonVersionKey}/parent</a></li>
            <li><a href="https://data.nbn.org.uk/Documentation/Web_Services/Web_Services-REST/resources/restapi/resource_TaxonResource.html#path__taxa_-taxonVersionKey-_children.html" target="_blank">/taxa/{taxonVersionKey}/children</a></li>
        </ul>
        <img src="../img/Taxonomy.jpg" alt="Taxonomy" title="Taxonomy" />
        <a href="CS"><h2>C# Example</h2></a>
        <a href="Java"><h2>Java Example</h2></a>
        <a href="PHP"><h2>PHP Example</h2></a>
    </jsp:attribute>
</t:webserviceDocumentationPage>

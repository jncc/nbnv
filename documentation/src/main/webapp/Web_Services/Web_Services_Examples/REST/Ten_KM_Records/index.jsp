<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Records for a Ten Km Square</h1>
        <h2>Overview</h2>
        <p>This page returns the species observations for a ten km square from one dataset. In order to access individual records on the NBN Gateway, you need to log in. Please refer to the <a href="../NBN_Client">NBN Client description</a> to see how this works.</p>
        <p>The output simply consists of a data table, displaying all of the returned records.</p>
        <h2>Resources used</h2>
        <ul>
            <li><a href="https://data.nbn.org.uk/Documentation/Web_Services/Web_Services-REST/resources/restapi/resource_TaxonResource.html#path__taxa.html" target="_blank">/taxa</a></li>
            <li><a href="https://data.nbn.org.uk/Documentation/Web_Services/Web_Services-REST/resources/restapi/resource_TaxonResource.html#path__taxa_-taxonVersionKey-_parent.html" target="_blank">/taxa/{taxonVersionKey}/parent</a></li>
            <li><a href="https://data.nbn.org.uk/Documentation/Web_Services/Web_Services-REST/resources/restapi/resource_TaxonResource.html#path__taxa_-taxonVersionKey-_children.html" target="_blank">/taxa/{taxonVersionKey}/children</a></li>
        </ul>
        <img src="../img/TenKmRecords.jpg" alt="Records for a Ten Km Square" title="Records for a Ten Km Square" />
        <a href="CS"><h2>C# Example</h2></a>
        <a href="Java"><h2>Java Example</h2></a>
        <a href="PHP"><h2>PHP Example</h2></a>
    </jsp:attribute>
</t:webserviceDocumentationPage>

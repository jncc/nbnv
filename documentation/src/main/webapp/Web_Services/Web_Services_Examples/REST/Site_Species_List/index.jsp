<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Site Species List</h1>
        <h2>Overview</h2>
        <p>This example builds upon the simple list of species and datasets for a ten km grid square. It uses the same resources as the previous examples but this time the user is able to select a vice county, species group and, optionally, a conservation designation as search parameters. For example, "Which UK BAP bird species are present in Breconshire" (qualified, of course, by the data available on the NBN Gateway and public access to them).</p>
        <p>The page consists of three dropdown lists to select the filter. The selection in the form is then 'posted back' and the results rendered in two tables as in the ten km species list example.</p>
        <h2>Resources used</h2>
        <ul>
            <li><a href="https://data.nbn.org.uk/Documentation/Web_Services/Web_Services-REST/resources/restapi/resource_TaxonObservationResource.html#path__taxonObservations_species.html" target="_blank">/taxonObservations/species</a></li>
            <li><a href="https://data.nbn.org.uk/Documentation/Web_Services/Web_Services-REST/resources/restapi/resource_TaxonObservationResource.html#path__taxonObservations_datasets.html" target="_blank">/taxonObservations/datasets</a></li>
        </ul>
        <img src="../img/SiteSpeciesList.jpg" alt="Site Species List" title="Site Species List" />
        <a href="CS"><h2>C# Example</h2></a>
        <a href="Java"><h2>Java Example</h2></a>
        <a href="PHP"><h2>PHP Example</h2></a>
    </jsp:attribute>
</t:webserviceDocumentationPage>

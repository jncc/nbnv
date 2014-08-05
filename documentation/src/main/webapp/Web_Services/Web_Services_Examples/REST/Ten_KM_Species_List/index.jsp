<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Ten Km Species List</h1>
        <h2>Overview</h2>
        <p>This example retrieves species and dataset lists for a single 10km square, SO21, and renders them to two tables. It uses the TaxonObservationResource, which allows you to query species records on the NBN Gateway, filtered by a broad range of search parameters, including year ranges, datasets, conservation designations, grid references and geometry.</p>
        <p>As well as the observations, the TaxonObservationResource can return summaries of the records returned from the filter. In our example, we'll use the gridRef parameter for a 10km square and request a species list and dataset list summary from the resource. It will demonstrate our first use of the NBNClient class.</p>
        <h2>Resources used</h2>
        <ul>
            <li><a href="https://data.nbn.org.uk/Documentation/Web_Services/Web_Services-REST/resources/restapi/resource_TaxonObservationResource.html#path__taxonObservations_species.html" target="_blank">/taxonObservations/species</a></li>
            <li><a href="https://data.nbn.org.uk/Documentation/Web_Services/Web_Services-REST/resources/restapi/resource_TaxonObservationResource.html#path__taxonObservations_datasets.html" target="_blank">/taxonObservations/datasets</a></li>
        </ul>
        <img src="../img/TenKmSpeciesList.jpg" alt="Ten km Species List" title="Ten km Species List" />
        <a href="CS"><h2>C# Example</h2></a>
        <a href="Java"><h2>Java Example</h2></a>
        <a href="PHP"><h2>PHP Example</h2></a>
    </jsp:attribute>
</t:webserviceDocumentationPage>

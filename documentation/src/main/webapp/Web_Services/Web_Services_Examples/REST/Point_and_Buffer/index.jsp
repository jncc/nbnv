<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Records for a Point and Buffer</h1>
        <h2>Overview</h2>
        <p>This example adds functonality to the user-defined polygon by allowing the user to click on the map to create a (roughly) circular polygon, centred on where they have clicked. This geometry is then used to filter records from the NBN API. This type of query is called a point and buffer query in GIS and is equivalent to saying "Select all records within x metres of this location" where the location is the point where the user clicked and x is the radius of the buffer around it.</p>
        <p>A toolbar is added to the OpenLayers map to allow the user to choose between a polygon or point and buffer. Also, a dropdown is added to the page to allow the user to select the buffer size from three options: 1km, 5km, 10km.</p>
        <p>Apart from the additional dropdown and the JavaScript, the source code for the pages is exactly the same as the user-defined polygon example. This is because a point and buffer request is essentially a request with a circular polygon as the spatial filter. OpenLayers and the accompanying JavaScript manage the rest (and is described below). However, the other links point to the source code. Please refer to the commentary for the <a href="../Records_for_Polygon">user-defined polygon example</a> for the language you are using.</p>
        <h2>Resources used</h2>
        <ul>
            <li><a href="https://data.nbn.org.uk/Documentation/Web_Services/Web_Services-REST/resources/restapi/resource_TaxonObservationResource.html#path__taxonObservations.html" target="_blank">/taxonObservations</a></li>
            <li><a href="https://data.nbn.org.uk/Documentation/Web_Services/Web_Services-REST/resources/restapi/resource_DesignationResource.html#path__designations.html" target="_blank">/designations</a></li>
        </ul>
        <img src="../img/PointAndBuffer.jpg" alt="Point and Buffer" title="Point and Buffer" />
        <a href="Javascript"><h2>The JavaScript</h2></a>
        <h2>Source Code</h2>
        <a href="CS"><h2>C# Example</h2></a>
        <a href="Java"><h2>Java Example</h2></a>
        <a href="PHP"><h2>PHP Example</h2></a>
    </jsp:attribute>
</t:webserviceDocumentationPage>

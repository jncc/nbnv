<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Records for a User-Defined Polygon</h1>
        <h2>Overview</h2>
        <p>This page displays records from the Vascular Plant Database, provided by the <a href="http://www.bsbi.org.uk/" target="_blank">Botanical Society of the British Isles</a>, that intersect a polygon drawn on an OpenLayers map.</p>
        <p>As with many of the other examples in this tutorial, this page passes the user's selection of various parameters to the NBNClient, which then creates the request and processes the response from the NBN. However, part of the user's selection in this example (and the next) is some geometry that can be created on a map. The NBN API can use this geometry to create a spatial query on the species records on the Gateway, returning records that intersect or are contained within it.</p>
        <p>Geometry can be represented in many formats but one of the simplest is <a href="http://en.wikipedia.org/wiki/Well-known_text" target="_blank">Well Known Text</a> (WKT). As the name suggests this is a text representation of geometry, where the geometry type (point, line, polygon etc.) is specified followed by an ordered list of points that make up that geometry. For example a unit square polygon would be represented as:</p>
        <pre>POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))</pre>
        <p>The square is a polygon followed by the ordered x,y coordinates of the corners. Note that, even though a square has four vertices (corners), five points are supplied in the WKT. This is because all polygons must be closed - meaing that the start point and end point must be the same for the WKT to be valid. For complex geometries, such as the boundary of a wildlife site or an administrative area, the WKT can be very long and couldn't practically be created by hand. Neither can it be supplied in the querystring of a URL, which is why the NBNClient methods for making these requests use the POST method.</p>
        <p>Fortunately, OpenLayers supports conversion of geometry drawn on the map to WKT. This example illustrates how to do this and to supply it as a spatial filter to the NBN API. The strategy in the example is to convert a polygon to WKT after the user has drawn it on the map and to write that WKT to a hidden field on the web page. When the user clicks Refresh, the contents of this hidden field are posted to the server and become part of the request to the NBN API. The filtered observations in the response are then returned and rendered to a table.</p>
        <p>The NBN API accepts geometry filters in geographic (latitude and longitude) coordinates according to the <a href="http://en.wikipedia.org/wiki/World_Geodetic_System" target="_blank">WGS84 system</a>. This is the standard coordinate system used across the globe. However, many online map data sources, including the one in the examples, use a spherical mercator projected coordinate system (often called the 'Google projection') with coordinates in metres. So before the polygon is submitted to the NBN API, it needs to be transformed to WGS84. Fortunately, this is straightforward to do in OpenLayers.</p>
        <p>The user is free to zoom, pan and draw any polygon they wish. However, there are limits to the size of the polygon allowed by the API, which would need to be managed in a production environment. For this example, restrict yourself to drawing over a relatively small area (<100km<sup>2</sup>).</p>
        <p>For all three languages, the code for pages is relatively simple and the JavaScript is exactly the same. Most of the discussion will be about the JavaScript necessary for the map (see the link below).</p>
        <h2>Resources used</h2>
        <ul>
            <li><a href="https://data.nbn.org.uk/Documentation/Web_Services/Web_Services-REST/resources/restapi/resource_TaxonObservationResource.html#path__taxonObservations.html" target="_blank">/taxonObservations</a></li>
            <li><a href="https://data.nbn.org.uk/Documentation/Web_Services/Web_Services-REST/resources/restapi/resource_DesignationResource.html#path__designations.html" target="_blank">/designations</a></li>
        </ul>
        <img src="../img/RecordsForPolygon.jpg" alt="Records for a polygon" title="Records for a polygon" />
        <a href="Javascript"><h2>The JavaScript</h2></a>
        <a href="CS"><h2>C# Example</h2></a>
        <a href="Java"><h2>Java Example</h2></a>
        <a href="PHP"><h2>PHP Example</h2></a>
    </jsp:attribute>
</t:webserviceDocumentationPage>

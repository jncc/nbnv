<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Records for a User-Defined Polygon Source Code Java</h1>
        <h2>RecordsForPolygonMap JavaScript</h2>
        <script type="syntaxhighlighter" class="brush: jscript; html-script: false"><![CDATA[
        var vectorLayer;
        var proj4326 = new OpenLayers.Projection("EPSG:4326");
        var proj900913 = new OpenLayers.Projection("EPSG:900913");

        function loadmap() {
            map = new OpenLayers.Map({ div: 'map' });

            var osmLayer = new OpenLayers.Layer.OSM("OpenStreetMap");
            osmLayer.isBaseLayer = true;
            map.addLayer(osmLayer);

            vectorLayer = new OpenLayers.Layer.Vector();
            map.addLayer(vectorLayer);
            var polygonControl = new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Polygon);
            map.addControl(polygonControl);
            polygonControl.activate();

            var hfWKT = document.getElementById("hfWKT");
            if (hfWKT.value != "") {
                var feature = new OpenLayers.Feature.Vector();
                feature.geometry = OpenLayers.Geometry.fromWKT(hfWKT.value);
                feature.geometry.transform(proj4326, proj900913);
                vectorLayer.addFeatures([feature]);
                map.zoomToExtent(vectorLayer.getDataExtent());
            } else {
                //an example extent of reasonable dimensions
                var bounds = new OpenLayers.Bounds(-3.65, 51.6, -2.60, 52.0);
                bounds.transform(proj4326, proj900913);
                map.zoomToExtent(bounds);
            }

            vectorLayer.events.register("featureadded", vectorLayer, onFeatureAdded);
            vectorLayer.events.register("sketchcomplete", vectorLayer, onSketchComplete);
        }

        function onFeatureAdded(e) {
            var format = new OpenLayers.Format.WKT();
            //transform in place so clone geometry
            var transformedGeom = e.feature.geometry.clone();
            transformedGeom.transform(proj900913, proj4326);
            var transformedFeature = new OpenLayers.Feature.Vector(transformedGeom);
            document.getElementById("hfWKT").value = format.write(transformedFeature);
        }

        function onSketchComplete(e) {
            vectorLayer.removeAllFeatures();
        }
        ]]></script>
        <h1>RecordsForPolygon</h1>
        <h2>JSP</h2>
        <script type="syntaxhighlighter" class="brush: java; html-script: true"><![CDATA[
        <\%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
        <\%@ page import="nbnapi.*" %>
        <\%@ page import="java.util.Calendar" %>

        <\%
        int startYear = 1990;
        int endYear = Calendar.getInstance().get(Calendar.YEAR);
        String designation = "BAP-2007";
        String wkt = "";
        String datasetKey = "GA000091";
        TaxonObservation[] observations = null;

        NBNClient nbnClient = new NBNClient();
        Designation[] designations = nbnClient.getDesignations();

        //determine whether this page is being generated after a submit
        boolean isPostBack = ("POST".equalsIgnoreCase(request.getMethod()));
        if (isPostBack) {
                wkt = request.getParameter("hfWKT");
                designation = request.getParameter("designation");
                startYear = Integer.parseInt(request.getParameter("startYear"));
                endYear = Integer.parseInt(request.getParameter("endYear"));

                nbnClient.login("#username#", "#password#");
                observations = nbnClient.postRecordsForWKTDesignationDatasetDates(wkt, designation, datasetKey, startYear, endYear);
        }
        %>

        <!DOCTYPE html>
        <html>
        <head>
                <title>Records for a Custom Polygon</title>
                <link rel="stylesheet" href="../css/style.css" />
                <script type="text/javascript" src="../js/OpenLayers-2.13.1/OpenLayers.js">&lt;/script>
                <script type="text/javascript" src="../js/RecordsForPolygonMap.js">&lt;/script>
        </head>
        <body onload="loadmap()">
        <form action="RecordsForPolygon.jsp" method="post">
                <input type="hidden" name="hfWKT" id="hfWKT" value="<\%= wkt %>" />
                <div>
                        <h1>Records from BSBI Vascular Plants Database for a Custom Polygon</h1>

                        <div id="map"></div>

                        <div>
                                <p>
                                        Select a designation:
                                        <select name="designation">
                                        <\% for (Designation item : designations) {
                                                if (item.getCode().equals(designation)) { %>
                                                        <option value="<\%= item.getCode() %>" selected="selected"><\%= item.getName() %></option>
                                                <\% } else { %>
                                                        <option value="<\%= item.getCode() %>"><\%= item.getName() %></option>
                                                <\% }
                                                } %>
                                        </select>
                                </p>
                                <p>
                                        StartYear: <input type="text" name="startYear" value="<\%= startYear %>"> 
                                        EndYear: <input type="text" name="endYear" value="<\%= endYear %>">
                                        <input type="submit" value="Refresh">
                                </p>
                        </div>

                        <\% if (isPostBack) { %>
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
                <\% } %>
                </div>
        </form>
        </body>
        </html>
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

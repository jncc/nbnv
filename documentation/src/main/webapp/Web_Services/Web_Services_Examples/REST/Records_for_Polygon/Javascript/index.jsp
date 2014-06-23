<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Records for a User-Defined Polygon JavaScript</h1>
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

        <p>The OpenLayers map is set up in the same way as the <a href="../../../WMS/Web_Map_Service">WMS examples</a>. When the page is loaded the JavaScript loadmap() function is called, which puts an OpenLayers map into an HTML &lt;div&gt; element with the id "map" (see the page source for the language you are using):</p>
        <script type="syntaxhighlighter" class="brush: jscript; html-script: true">
            <body onload="loadmap()">
            ...
            <div id="map"></div>
        </script>
        <p>This map has an <a href="http://www.openstreetmap.org/" target="_blank">OpenStreetMap</a> background layer. OpenStreetMap is an open source mapping project that is maintained by volunteer mappers all over the world.</p>
        <p>To support geometry created by the user, first we need to add an empty OpenLayers vector layer to the map and add a polygon editing control to it.</p>
        <script type="syntaxhighlighter" class="brush: jscript; html-script: false">
        vectorLayer = new OpenLayers.Layer.Vector();
        map.addLayer(vectorLayer);
        var polygonControl = new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Polygon);
        map.addControl(polygonControl);
        polygonControl.activate();
        </script>
        <p>The polygon editing control doesn't automatically have a user interface, such as a button to switch it on and off. In this example, it will be active all the time. So the user can just start clicking on the map to create their polygon. In the next example, where the user can choose between two types of geometry creation (polygon, point and buffer), we will add a simple toolbar.</p>
        <p>The next section of code checks to see if there is geometry stored in the the hidden field (there will be if the user has already submitted a request).</p>
        <script type="syntaxhighlighter" class="brush: jscript; html-script: false">
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
        </script>
        <p>If there is WKT in this field, an OpenLayers feature is created from it and transformed from WGS84 to spherical mercator (see the overview) so that it can be mapped correctly. This is done using the geometry's transform method, which takes two parameters: the projection to transform from and the projection to transform to. These are declared as OpenLayers projections at the top of the file:</p>
        <script type="syntaxhighlighter" class="brush: jscript; html-script: false">
        var proj4326 = new OpenLayers.Projection("EPSG:4326");
        var proj900913 = new OpenLayers.Projection("EPSG:900913");
        </script>
        <p>Once the feature has been added to the vector layer, the map is zoomed to its extent.</p>
        <p>If there is no WKT in the hidden field, the map is zoomed to the extent of an arbitrary bounding box (in this case SE Wales), specified by the left, bottom, right and top coordinates (minimum longitude, minimum latitude, maximum longitude, maximum latitude). The coordinates of this bounding box can be changed to any area of interest.</p>
        <p>Many components in OpenLayers raise events when certain actions have occurred. For example, when the map has just been zoomed out or when the user has finished drawing some geometry. You can register JavaScript functions to handle these events when they occur. There are two in this example.</p>
        <p>When the user has completed drawing their geometry, the "sketchcomplete" event is raised. The code below registers the onSketchComplete function to be run when this happens. All this does is remove any existing features from the map, preventing more than one polygon appearing on the map at a time:</p>
        <script type="syntaxhighlighter" class="brush: jscript; html-script: false">
        vectorLayer.events.register("sketchcomplete", vectorLayer, onSketchComplete);
        ...
        function onSketchComplete(e) {
            vectorLayer.removeAllFeatures();
        }
        </script>
        <p>When the newly drawn geometry is added to the vector layer, the "featureadded" event is raised. The onFeatureAdded function is called when this occurs. Note that OpenLayers supplies this function with a parameter (e, which stands for event here) when it is called and the newly created feature can be read from this. The function takes a copy of the geometry (using clone) and transforms it from the map's coordinate system to WGS84. Then it converts it to WKT and writes it to the hidden field. So when the user clicks Refresh, the WKT is posted back to the server alongside the other form data.</p>
        <script type="syntaxhighlighter" class="brush: jscript; html-script: false">
        vectorLayer.events.register("featureadded", vectorLayer, onFeatureAdded);
        ...
        function onFeatureAdded(e) {
            var format = new OpenLayers.Format.WKT();
            //transform in place so clone geometry
            var transformedGeom = e.feature.geometry.clone();
            transformedGeom.transform(proj900913, proj4326);
            var transformedFeature = new OpenLayers.Feature.Vector(transformedGeom);
            document.getElementById("hfWKT").value = format.write(transformedFeature);
        }
        </script>
        <p>The rest of the functionality for the page is handled by server side code. Please refer to the commentary for the language you are using.</p>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

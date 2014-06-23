<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Records for a Point and Buffer JavaScript</h1>
        <h2>PointAndBufferMap JavaScript</h2>
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

            var polygonControl = new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Polygon, { title: 'polygon', displayClass: 'polygon' });
            var pointControl = new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Point, { title: 'point and buffer', displayClass: 'pointAndBuffer' });
            var panel = new OpenLayers.Control.Panel();
            panel.addControls([polygonControl, pointControl]);
            map.addControl(panel);

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
            vectorLayer.events.register("beforefeatureadded", vectorLayer, onBeforeFeatureAdded);
            vectorLayer.events.register("sketchcomplete", vectorLayer, onSketchComplete);

        }

        function onBeforeFeatureAdded(e) {
            if (e.feature.geometry instanceof OpenLayers.Geometry.Point) {
                var selRadius = document.getElementById("selRadius");
                var radius = parseInt(selRadius.value);
                e.feature.geometry = OpenLayers.Geometry.Polygon.createRegularPolygon(e.feature.geometry, radius, 20, 0);
            }
        }

        function onFeatureAdded(e) {
            var format = new OpenLayers.Format.WKT();
            var proj4326 = new OpenLayers.Projection("EPSG:4326");
            var proj900913 = new OpenLayers.Projection("EPSG:900913");

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

        <p>The JavaScript for the map adds new functionality to the previous example. This commentary will just describe the differences. Please review the <a href="../../Records_for_Polygon/Javascript">Records For Polygon JavaScript</a> if you haven't done so already</p>
        <p>This map has a toolbar with two controls added to it: the polygon drawing control and the point and buffer control. To do this, create the OpenLayers Controls, create an OpenLayers Panel, add the Controls to the Panel and add the Panel to the map.</p>
        <script type="syntaxhighlighter" class="brush: jscript; html-script: false">
        var polygonControl = new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Polygon, { title: 'polygon', displayClass: 'polygon' });
        var pointControl = new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Point, { title: 'point and buffer', displayClass: 'pointAndBuffer' });
        var panel = new OpenLayers.Control.Panel();
        panel.addControls([polygonControl, pointControl]);
        map.addControl(panel);
        </script>
        <p>Note that we don't have to write any code to activate the controls when a button on the panel is clicked, OpenLayers handles this.</p>
        <p>The displayClass property of the control is related to its CSS class. A control panel button can be in two states: active and inactive. When OpenLayers renders a button it looks for CSS classes that have the displayClass name followed by ItemActive and ItemInactive e.g. polygonItemActive and polygonItemInactive for our polygon control. This takes a little getting used to but it means you can display control buttons any way you want, including using images. This is what we've done here with two very simple images representing a polygon <img src="../../img/polygon.png" title="polygon" alt="polygon" /> and the point and buffer <img src="../../img/pointandbuffer.png" title="pointAndBuffer" alt="pointAndBuffer" /></p>
        <p>Both of these controls draw on the same vector layer. As in the previous example, when the user has finished drawing, any other geometry is removed from the map and when a feature is added, it is transformed to WKT and written to the hidden field.</p>
        <p>For the point and buffer, we're using the point control and processing the point after it has been created. When the geometry has been created, but before it's added to a vector layer, the layer's "beforefeatureadded" event is raised. We will use this to draw a buffer around the point.</p>
        <script type="syntaxhighlighter" class="brush: jscript; html-script: false">
        vectorLayer.events.register("beforefeatureadded", vectorLayer, onBeforeFeatureAdded);
        ...
        function onBeforeFeatureAdded(e) {
            if (e.feature.geometry instanceof OpenLayers.Geometry.Point) {
                var selRadius = document.getElementById("selRadius");
                var radius = parseInt(selRadius.value);
                e.feature.geometry = OpenLayers.Geometry.Polygon.createRegularPolygon(e.feature.geometry, radius, 20, 0);
            }
        }
        </script>
        <p>Like onFeatureAdded, onBeforeFeatureAdded is supplied with a parameter when it is called and the newly created feature can be read from it. In this function, we check to see if the feature's geometry is a point, which indicates it has come from the point and buffer control. If it is, the radius is read from the dropdown and a new geometry is created using a convenience method on OpenLayers Polygon called createRegularPolygon. This does exactly what we need. It takes a point origin (our original geometry), a radius (the value in the dropdown), the number of sides the polygon should have (20 seems reasonable), and a rotation in degrees (which we don't need). It returns a geometry that we can assign to our original feature. It's this updated feature that is then added to the vector layer.</p>
        <p>The rest of the functionality for the page is handled by server side code. Please refer to the commentary for the language you are using.</p>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

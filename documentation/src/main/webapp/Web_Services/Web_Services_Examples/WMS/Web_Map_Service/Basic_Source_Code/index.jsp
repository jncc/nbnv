<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
    <h1>Source Code for the Basic Web Map Service Example</h1>
    <script type="syntaxhighlighter" class="brush: js; html-script: true"><![CDATA[
        <!DOCTYPE html>
        <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <title>NBN Web Services</title>
            <link rel="stylesheet" href="../css/style.css" />
                <script type="text/javascript" src="../js/OpenLayers-2.13.1/OpenLayers.js">&lt;/script>
                <script type="text/javascript">
                    function loadmap() {
                        var map = new OpenLayers.Map('map');
                        var layer = new OpenLayers.Layer.WMS("OpenLayers WMS",
                            "http://vmap0.tiles.osgeo.org/wms/vmap0",
                            { layers: 'basic' },
                            { isBaseLayer: true });
                        map.addLayer(layer);

                        var nbnLayer = new OpenLayers.Layer.WMS('NBNWMS',
                                        'https://gis.nbn.org.uk/SingleSpecies/NBNSYS0000005629',
                                        { layers: 'Grid-10km', transparent: true, format: 'image/gif' },
                                        { isBaseLayer: false, singleTile: true });
                        map.addLayer(nbnLayer);

                        var bounds = new OpenLayers.Bounds(-10, 50, 3, 60);
                        map.zoomToExtent(bounds);
                    }
                &lt;/script>
            </head>
            <body onload="loadmap()">
                    <h1>NBN Web Map Services Basic Example</h1>
                <h3>NBN Gateway records for <i>Aeshna isosceles</i> (Norfolk Hawker)</h3>
                    <div id="map"></div>
            </body>
            </html>
            ]]>
        </script>
        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

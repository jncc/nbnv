<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Source Code for the Select Species Web Map Service Example</h1>
        <script type="syntaxhighlighter" class="brush: js; html-script: true"><![CDATA[
            <!DOCTYPE html>
            <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <title>NBN Web Services</title>
                <link rel="stylesheet" href="../css/style.css" />
                    <script type="text/javascript" src="../js/OpenLayers-2.13.1/OpenLayers.js">&lt;/script>
                    <script type="text/javascript">
                        var map, selSpecies, mapTitle;

                        window.onload = function () {
                            var btnRefresh = document.getElementById("btnRefresh");
                            btnRefresh.onclick = refreshWMS;
                            selSpecies = document.getElementById("selSpecies");
                            mapTitle = document.getElementById("mapTitle");
                            loadmap();
                            refreshWMS();
                        }

                        function loadmap() {
                            map = new OpenLayers.Map('map');
                            var baseLayer = new OpenLayers.Layer.WMS("OpenLayers WMS",
                                "http://vmap0.tiles.osgeo.org/wms/vmap0",
                                { layers: 'basic' },
                                { isBaseLayer: true });
                            map.addLayer(baseLayer);
                            var bounds = new OpenLayers.Bounds(-10, 50, 3, 60);
                            map.zoomToExtent(bounds);
                        }

                        function refreshWMS() {
                            var nbnLayers = map.getLayersByName('NBNWMS');
                            for (var index = 0; index < nbnLayers.length; ++index) {
                                map.removeLayer(nbnLayers[index]);
                            }
                            var nbnLayer = new OpenLayers.Layer.WMS('NBNWMS',
                                            'https://gis.nbn.org.uk/SingleSpecies/' + selSpecies.value,
                                            { 'layers': 'Grid-10km', transparent: true },
                                            { isBaseLayer: false, singleTile: true });
                            map.addLayer(nbnLayer);
                            mapTitle.innerHTML = "NBN Gateway records for " + selSpecies[selSpecies.selectedIndex].innerHTML;
                        }
                    &lt;/script>
            </head>
            <body>
                    <h1>NBN Web Map Services Select Species</h1>
                Select Species:
                    <select id="selSpecies">
                            <option value="NHMSYS0000530726">Cirl Bunting</option>
                            <option value="NHMSYS0001688304">Corn Bunting</option>
                            <option value="NHMSYS0000530233">Lapland Bunting</option>
                            <option value="NHMSYS0000530734">Reed Bunting</option>
                            <option value="NHMSYS0000530776">Snow Bunting</option>
                            <option value="NHMSYS0000530727">Yellowhammer</option>
                    </select>
                <button id="btnRefresh" type="button">Refresh</button>
                <h3 id="mapTitle"></h3>
                <div id="map"></div>
            </body>
            </html>
        ]]>
        </script>
        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

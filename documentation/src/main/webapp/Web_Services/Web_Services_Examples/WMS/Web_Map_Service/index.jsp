<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>NBN Gateway Web Mapping Service Examples</h1>
        <h2>Basic Web Mapping Service Example</h2>
        <a href="Basic_Source_Code" target="_blank"><h2>Page Source Code</h2></a>
        <p>The Gateway supports OGC compliant web mapping services (WMS). WMS return appropriate map images based on the request from a client application. These images can be viewed in your browser if the correct URL is supplied but WMS can be accessed and explored most easily from a GIS. If you would like to try this, there is a free GIS available called Quantum GIS, which can be downloaded <a href="http://www.qgis.org/en/site/" target="_blank">here</a>. It's relatively easy to add a WMS layer to Quantum GIS. Just click the 'Add WMS Layer' button and set up the service with the appropriate URL, which will become clearer below and is fully described in the <a href="https://data.nbn.org.uk/Documentation/Web_Services/Web_Map_Services/" target="_blank">NBN Gateway documentation</a>.</p>
        <p>WMS layers can also be added to a web map in a browser. A very popular and powerful JavaScript web mapping library is OpenLayers that supports a wide range of data sources and has comprehensive editing capabilities. You can read about and download OpenLayers <a href="http://openlayers.org/" target="_blank">here</a>.</p>
        <p>This page describes how to create a very simple OpenLayers map and include a Gateway WMS layer on it. The example is then extended so that the layer can be changed based on a species selected from a drop-down list.</p>
        <p>First, you will need to download the OpenLayers JavaScript library and include it in your website. When you have done this, you reference the library from your web page so it can call the OpenLayers functions.</p>
        <p>An OpenLayers map is displayed in a &lt;div&gt; element. You assign an id to this &lt;div&gt; and pass it to OpenLayers when the page has loaded, which creates the map. Additional JavaScript can then manipulate the map by calling further OpenLayers functions.</p>
        <p>A map including a Gateway WMS layer can be created with very few lines of code. This example displays an OpenLayers map with a simple background and the distribution of records for the Norfolk Hawker. We'll keep this page minimal to demonstrate the basic concepts.</p>
        <img src="img/BasicWMS.jpg" alt="Basic WMS" title="Basic WMS" />
        <p>The body of the page consists of some headings and an empty &lt;div&gt; with the id "map" where the map will be loaded. This div will need to be assigned a width and height for the map to be visible so be careful to do this in a stylesheet or by adding width and height attributes directly. Importantly, when the page has loaded it needs to call your JavaScript to create the map in OpenLayers. One way to do this is to add the onload attribute to the page body tag and include your JavaScript call there. Another way is illustrated in the next example. If you are using a JavaScript library, such as <a href="http://jquery.com/" target="_blank">JQuery</a>, there are alternative ways to do this.</p>
        <script type="syntaxhighlighter" class="brush: js; html-script: true"><![CDATA[
        <body onload="loadmap()">
            <h1>NBN Web Map Services Basic Example</h1>
            <h3>NBN Gateway records for <i>Aeshna isosceles</i> (Norfolk Hawker)</h3>
            <div id="map"></div>
        </body>
        ]]></script>
        <p>In this example, a single JavaScript function called loadmap handles the OpenLayers calls.</p>
        <script type="syntaxhighlighter" class="brush: js; html-script: false"><![CDATA[
        function loadmap() {
                    var map = new OpenLayers.Map('map');
                    layer = new OpenLayers.Layer.WMS("OpenLayers WMS",
                    "http://vmap0.tiles.osgeo.org/wms/vmap0",
                    { layers: 'basic' },
                    { isBaseLayer: true });
                    map.addLayer(layer);

                    var nbnLayer = new OpenLayers.Layer.WMS('NBNWMS',
                            'https://gis.nbn.org.uk/SingleSpecies/NBNSYS0000005629',
                            { 'layers': 'Grid-10km', transparent: true, format: 'image/gif' },
                            { isBaseLayer: false, singleTile: true });
                    map.addLayer(nbnLayer);

                    var bounds = new OpenLayers.Bounds(-10, 50, 3, 60);
                    map.zoomToExtent(bounds);
            }
        ]]></script>
        <p>loadmap creates a new OpenLayers map by passing in the id of the div where it should be displayed</p>
        <p>Next it adds two layers. This is done by creating the layer and then adding it to the map.</p>
        <p>In this example both layers are WMS layers. The background layer is a very simple layer with global extent and is used in many OpenLayers examples. When you create the layer you specify its name (OpenLayers WMS), the URL of the WMS (http://vmap0.tiles.osgeo.org/wms/vmap0), the options you wish to pass into the WMS (here it's just specifying the 'basic' layer), and a number of options for OpenLayers. In this example, the option is that this layer is the base layer for the map, upon which other layers will be drawn.</p>
        <p>The next layer added is the Gateway WMS for Norfolk Hawker. The name is set as 'NBNWMS'. Next, the location of the service is set. Note that this includes the taxon version key for Norfolk Hawker. Then the WMS options are specified. This example requests a 10km grid map and that the background of this layer should be transparent. If you don't set the background transparent then the background map will be obscured by the NBN layer that is drawn on top of it. Finally, two options are passed in to OpenLayers. This layers is an overlay, not a background layer, so this option is set to false. The other options rquests the layer as a single image (or tile) rather than the default (multiple tiles), which reduces the number of requests made to a WMS. This can be an appropriate option to set when your map is displaying the full extent of a simple layer. Finally, a bounding box is set up with the approximate coordinates (in latitude and longitude) of the British Isles and the map is called to zoom to that extent.</p>
        <p>This is a very simple implementation of OpenLayers with very limited functionality. However, you are still able to zoom in and out of this map using the zoom buttons or your mouse wheel.</p>

        <h2>Web Mapping Service with Species Selection</h2>
        <a href="Species_Select_Source_Code" target="_blank"><h2>Page Source Code</h2></a>
        <p>The next example will partially recreate the <a href="../Atlas/SpeciesMap.html" target="_blank">Gateway atlas example</a>. The user will be able to select a species of bunting and refresh the OpenLayers map to see the distribution of the records on the Gateway for this species.</p>
        <img src="img/SelectSpeciesWMS.jpg" alt="Select Species WMS" title="Select Species WMS" />
        <p>As in the atlas example, we set up a drop-down list containing the species, with values corresponding to their taxon version keys. A refresh button is added, a title and a div that will contain the OpenLayers map.</p>
        <script type="syntaxhighlighter" class="brush: js; html-script: true"><![CDATA[
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
        ]]></script>
        <p>The JavaScript consists of variable declarations for the OpenLayers map, species drop-down and title element. These are used by the other functions to refresh the map. When the page has loaded (this time handled via the window.onload event), the refresh button click event is set to call a function to refresh the WMS, the map is loaded and the WMS layer is refreshed based on the currently selected species.</p>
        <script type="syntaxhighlighter" class="brush: js; html-script: false"><![CDATA[
        var map, selSpecies, mapTitle;

        window.onload = function () {
            var btnRefresh = document.getElementById("btnRefresh");
            btnRefresh.onclick = refreshWMS;
            selSpecies = document.getElementById("selSpecies");
            mapTitle = document.getElementById("mapTitle");
            loadmap();
            refreshWMS();
        }
        ]]></script>
        loadmap is reduced to just loading the background layer with the same options as before and zooming to the British Isles.
        <script type="syntaxhighlighter" class="brush: js; html-script: false"><![CDATA[
        function loadmap() {
            map = new OpenLayers.Map('map');
            baseLayer = new OpenLayers.Layer.WMS("OpenLayers WMS",
                    "http://vmap0.tiles.osgeo.org/wms/vmap0",
                    { layers: 'basic' });
            map.addLayer(baseLayer);
            var bounds = new OpenLayers.Bounds(-10, 50, 3, 60);
            map.zoomToExtent(bounds);
        }
        ]]></script>
        A new funciton, refreshWMS, is called that refreshes the WMS layer on the map.
        <script type="syntaxhighlighter" class="brush: js; html-script: false"><![CDATA[
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
        ]]></script>
        <p>This function searches for any existing NBN layer on the map by retrieving all layers with the NBNWMS name. It then loops through these layers and removes them. Unfortunately, there isn't a function to remove a layer by name so this slightly more indirect approach has to be taken. Next it adds a new layer to the map with the WMS location set by the selected value in the species drop-down with the same layer options as before. Finally, it updates the text of the heading to reflect the new species selection.</p>
        <p>These are just short, introductory examples for the Gateway WMS and OpenLayers. Some elementary geometry editing is demonstrated in the RESTful web service custom polygon and point and buffer examples.</p>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

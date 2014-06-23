<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Creating a species map using the atlas grade mapping service</h1>
        <a href="Source_Code" target="_blank"><h2>Page Source Code</h2></a>
        <p>Atlases are a common way of displaying summaries of species distributions. They usually comprise of a map for each species in the group of interest. The Gateway provides a similar service that maps a 10km resolution summary of the records on the Gateway for any species. To request an atlas image for a species, the URL should take the form</p>
        <pre>
            https://gis.nbn.org.uk/SingleSpecies/<i>taxon version key</i>/atlas/circle/map
        </pre>
        Replace <i>taxon version key</i> with the taxon version key of the species of interest. For example for marsh harrier (taxon version key: NHMSYS0000530293) the request would be:
        <p><a href="https://gis.nbn.org.uk/SingleSpecies/NHMSYS0000530293/atlas/circle/map" target="_blank">https://gis.nbn.org.uk/SingleSpecies/NHMSYS0000530293/atlas/circle/map</a></p>
        <p>This gives you a basic, black and white map of the distribution of records on the NBN Gateway for Marsh Harrier</p>
        <p>The atlas service provides a variety of options for customising the map size and colours, as well as displaying a legend and a list of datasets providers for the map. Full documentation for the service is on the <a href="https://data.nbn.org.uk/Documentation/Web_Services/Atlas_Grade_Maps/" target="_blank">NBN Gateway web service pages</a></p>
        <p>In this example, we will create a simple web page that allows a visitor to map one of the six resident species of UK buntings with data from the Gateway. You can also change the marker colours and the background for the map. The page will display the map, legend and the list of datasets from which the map is derived. It will involve some coding in html and JavaScript to handle when the user refreshes the map.</p>

        <img src="img/SpeciesMap.jpg" alt="Species map" title="Species map" />

        <p>First, in the body of the page, create drop-down lists with select tags for the species, map background type and colour of the markers. The species drop-down (with id "selSpecies") displays the species name and the value assigned to each option is the species' taxon version key. You can add any species you like. Find its taxon version key by searching for the species on the Gateway and viewing its summary page e.g. <a href="https://data.nbn.org.uk/Taxa/NHMSYS0000530726" target="_blank">Cirl Bunting (Emberiza cirlus)</a>.</p>
        <script type="syntaxhighlighter" class="brush: js; html-script: true"><![CDATA[
        Species:
            <select id="selSpecies">
                    <option value="NHMSYS0000530726">Cirl Bunting</option>
                    <option value="NHMSYS0001688304">Corn Bunting</option>
                    <option value="NHMSYS0000530233">Lapland Bunting</option>
                    <option value="NHMSYS0000530734">Reed Bunting</option>
                    <option value="NHMSYS0000530776">Snow Bunting</option>
                    <option value="NHMSYS0000530727">Yellowhammer</option>
            </select>
        ]]></script>
        <p>Two more drop-downs are added for the background map and marker colour. The options for the background map are given on the Atlas service documentation page. The colour values are hex RGB values. You can generate these using the colour selector on the same documentation page.</p>
        <script type="syntaxhighlighter" class="brush: js; html-script: true"><![CDATA[
            Background:
        <select id="selBackground">
            <option value="gb">Great Brtain</option>
            <option value="gbi">Great Britain and Ireland</option>
            <option value="gb100kgrid">Great Britain (100km grid)</option>
            <option value="gbi100kgrid">Great Britain and Ireland (100km grid)</option>
            <option value ="vicecounty">Vice Counties</option>
        </select>

        Colour:
        <select id="selColour">
            <option value="000000">Black</option>
            <option value="ff0000">Red</option>
            <option value="00ff00">Green</option>
            <option value="0000ff">Blue</option>
        </select>
        ]]></script>
        <p>Next add a button to call the JavaScript to refresh the map.</p>
        <script type="syntaxhighlighter" class="brush: js; html-script: true"><![CDATA[
        <button type="button" id="btnRefresh">Refresh</button>
        ]]></script>
        <p>And some empty placeholders that the JavaScript will populate for the title, map image, legend image and an iframe for the list of datasets.</p>
        <script type="syntaxhighlighter" class="brush: js; html-script: true"><![CDATA[
         <h1 id="mapTitle"></h1>
         <div>
            <img id="imgMap" src=""/>
            <img id="imgLegend" src="" />
        </div>
        <iframe id="frmAcknowledgements" width="800" height="300"></iframe>
        ]]></script>
        <p>The JavaScript consists of two functions. The first function is run when the page has loaded and sets up a set of variables that can be used to read or assign values from the html controls. It also assigns a function to be called when the refresh button is clicked.</p>
        <script type="syntaxhighlighter" class="brush: js; html-script: false"><![CDATA[
        window.onload = function () {
            var btnRefresh = document.getElementById("btnRefresh");
            btnRefresh.onclick = showAtlas;
            var imgMap = document.getElementById("imgMap");
            var imgLegend = document.getElementById("imgLegend");
            var frmAcknowledgements = document.getElementById("frmAcknowledgements");

            var selSpecies = document.getElementById("selSpecies");
            var selColour = document.getElementById("selColour");
            var selBackground = document.getElementById("selBackground");
            var mapTitle = document.getElementById("mapTitle");
            showAtlas();
        }
        ]]></script>
        <p>The second function loads the selected atlas images when Refresh is clicked.</p>
        <script type="syntaxhighlighter" class="brush: js; html-script: false"><![CDATA[
        function showAtlas() {
            mapTitle.innerHTML = "NBN Gateway records for " + selSpecies[selSpecies.selectedIndex].innerHTML;
            var baseUrl = "https://gis.nbn.org.uk/SingleSpecies/" + selSpecies.value + "/atlas/circle/";
            imgMap.src = baseUrl + "map?imagesize=6&background=" + selBackground.value + "&fillcolour=" + selColour.value;
            imgLegend.src = baseUrl + "legend?imagesize=6&background=" + selBackground.value + "&fillcolour=" + selColour.value;
            frmAcknowledgements.src = baseUrl + "acknowledgement";
         }
        ]]></script>
        <p>It sets the title to the selected species name from the drop-down list ("selSpecies"). Then it creates a URL from the value of this drop-down list, which is used to populate the source for the map image, legend and acknowledgements. These URLs are calls to the Gateway atlas service. For the map, the 'map' option is added to the URL, which has a querystring that includes a fixed image size (see the documentation), the selected background map ("selBackground") and the fill colour ("selColour"), from the options the user selected.</p>
        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

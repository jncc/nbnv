<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Source Code for the Species Map Example</h1>
        <script type="syntaxhighlighter" class="brush: js; html-script: true"><![CDATA[
            <!DOCTYPE html>
            <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <title>NBN Atlas Example</title>
                <link rel="stylesheet" href="../css/style.css" />
                    <script type="text/javascript">
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

                            function showAtlas() {
                                    mapTitle.innerHTML = "NBN Gateway records for " + selSpecies[selSpecies.selectedIndex].innerHTML;
                                    var baseUrl = "https://gis.nbn.org.uk/SingleSpecies/" + selSpecies.value + "/atlas/circle/";
                                    imgMap.src = baseUrl + "map?imagesize=6&background=" + selBackground.value + "&fillcolour=" + selColour.value;
                                    imgLegend.src = baseUrl + "legend?imagesize=6&background=" + selBackground.value + "&fillcolour=" + selColour.value;
                                    frmAcknowledgements.src = baseUrl + "acknowledgement";

                            }
                    &lt;/script>
                    <style type="text/css">
                            #acknowledgement {width: 800px; height: 600px;}
                    </style>
            </head>
            <body>
                    Species:
                    <select id="selSpecies">
                            <option value="NHMSYS0000530726">Cirl Bunting</option>
                            <option value="NHMSYS0001688304">Corn Bunting</option>
                            <option value="NHMSYS0000530233">Lapland Bunting</option>
                            <option value="NHMSYS0000530734">Reed Bunting</option>
                            <option value="NHMSYS0000530776">Snow Bunting</option>
                            <option value="NHMSYS0000530727">Yellowhammer</option>
                    </select>

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

                    <button type="button" id="btnRefresh">Refresh</button>

                    <h1 id="mapTitle"></h1>

                    <div>
                            <img id="imgMap" src=""/>
                            <img id="imgLegend" src="" />
                    </div>
                    <iframe id="frmAcknowledgements" width="800" height="300"></iframe>

            </body>
            </html>
        ]]></script>
        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

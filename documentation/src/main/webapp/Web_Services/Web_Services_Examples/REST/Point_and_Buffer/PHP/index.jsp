<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Records for a Point and Buffer Source Code PHP</h1>
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
        <h1>PointAndBuffer</h1>
        <h2>PHP</h2>
        <script type="syntaxhighlighter" class="brush: php; html-script: true"><![CDATA[
        <?php
        require_once "NBNClient.php";

        $nbnClient = new NBNClient();
        $designations = $nbnClient->GetDesignations();
        $startYear = "1990";
        $endYear = date("Y");
        $designation = "BAP-2007";
        $wkt = "";

        if($_SERVER["REQUEST_METHOD"] == "POST")
        {
            $wkt = $_POST["hfWKT"];
            $designation = $_POST["designations"];
            $startYear = $_POST["startYear"];
            $endYear = $_POST["endYear"];
            $datasetKey = "GA000091";

            $nbnClient->login("#USERNAME#", "#PASSWORD#");
            $records = $nbnClient->PostRecordsForWKTDesignationDatasetDates($wkt, $designation, $datasetKey, $startYear, $endYear);
        }
        ?>

        <!DOCTYPE html>

        <html xmlns="http://www.w3.org/1999/xhtml">
        <head id="Head1">
            <title>Records For A Polygon Or Point And Buffer</title>
            <link rel="stylesheet" href="../css/style.css" />
            <link rel="stylesheet" href="../css/style-openlayers.css" />
            <script type="text/javascript" src="../js/OpenLayers-2.13.1/OpenLayers.js">&lt;/script>
            <script type="text/javascript" src="../js/PointAndBufferMap.js">&lt;/script>
        </head>
        <body onload="loadmap()">
            <div>
                <h1>Records for a custom polygon or point and buffer</h1>
                <div id="Div1">
                </div>
                <form method="post" action="PointAndBuffer.php">
                    <input type="hidden" name="hfWKT" id="Hidden1" value="<?=$wkt?>" />
                    <div>
                        <p>
                            Select a designation:
                            <select name="designations">
                                <?php foreach($designations as $d) { ?>
                                    <?php if($d->code == $designation) { ?>
                                    <option value="<?=$d->code?>" selected="selected"><?=$d->name?></option>
                                    <?php } else { ?>
                                    <option value="<?=$d->code?>"><?=$d->name?></option>
                                    <?php } ?>
                                <?php } ?>
                            </select>
                        </p>
                        <p>
                            <label for="startYear">Start year:</label>
                            <input type="text" id="Text1" name="startYear" value="<?=$startYear?>" />
                            <label for="endYear">End year:</label>
                            <input type="text" id="Text2" name="endYear" value="<?=$endYear?>" />
                            Radius for point and buffer:
                            <select id="selRadius">
                                <option value="1000">1 km</option>
                                <option value="10000">10 km</option>
                                <option value="50000">50 km</option>
                            </select>
                            <input type="submit" value="Submit" />
                        </p>
                    </div>
                </form>

                <?php if($_SERVER["REQUEST_METHOD"] == "POST") { ?>
                <div>
                    <?php if($records == NULL) { ?>
                    No records
                    <?php } else { ?>
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
                            <?php foreach($records as $record) { ?>
                            <tr>
                                <td>
                                    <a href="https://data.nbn.org.uk/Datasets/<?=$record->datasetKey?>"><?=$record->datasetKey?></a>
                                </td>
                                <td>
                                    <?=$record->observationID?>
                                </td>
                                <td>
                                    <?=$record->observationKey?>
                                </td>
                                <td>
                                    <?=$record->location?>
                                </td>
                                <td>
                                    <?=$record->resolution?>
                                </td>
                                <td>
                                    <?=$record->taxonVersionKey?>
                                </td>
                                <td>
                                    <?=$record->pTaxonVersionKey?>
                                </td>
                                <td>
                                    <?=$record->pTaxonName?>
                                </td>
                                <td>
                                    <?=$record->pTaxonAuthority?>
                                </td>
                                <td>
                                    <?=$record->startDate?>
                                </td>
                                <td>
                                    <?=$record->endDate?>
                                </td>
                                <td>
                                    <?=$record->dateTypekey?>
                                </td>
                            </tr>
                            <?php } ?>
                        </tbody>
                    </table>
                    <?php } ?>
                </div>
                <?php } ?>
            </div>
        </body>
        </html>
        ]]></script>
        <h1>RecordsForPolygon</h1>
        <h2>PHP</h2>
        <script type="syntaxhighlighter" class="brush: php; html-script: true"><![CDATA[
        <?php
        require_once "NBNClient.php"; 

        $nbnClient = new NBNClient();
        $designations = $nbnClient->GetDesignations();
        $startYear = "1990";
        $endYear = date("Y");
        $designation = "BAP-2007";
        $wkt = "";

        //if the form has been posted back, get the species and datasets
        if($_SERVER["REQUEST_METHOD"] == "POST")
        {
            $wkt = $_POST["hfWKT"];
            $designation = $_POST["designations"];
            $startYear = $_POST["startYear"];
            $endYear = $_POST["endYear"];
            $datasetKey = "GA000091";

            $nbnClient->login("#USERNAME#", "#PASSWORD#");
            $records = $nbnClient->PostRecordsForWKTDesignationDatasetDates($wkt, $designation, $datasetKey, $startYear, $endYear);
        }
        ?>

        <html xmlns="http://www.w3.org/1999/xhtml">
        <head >
            <title>Records For A Custom Polygon</title>
            <link rel="stylesheet" href="../css/style.css" />
            <script type="text/javascript" src="../js/OpenLayers-2.13.1/OpenLayers.js"><\/script>
            <script type="text/javascript" src="../js/RecordsForPolygonMap.js"><\/script>
        </head>
        <body onload="loadmap()">
            <h1>Records from the BSBI Vascular Plants Database for a Custom Polygon</h1>
            <div id="map">
            </div>

            <form id="form1" method="post" action="RecordsForPolygon.php">
                <input type="hidden" ID="hfWKT" name="hfWKT" value="<?=$wkt?> />
                <div>
                    <p>
                        Select a designation:
                        <select name="designations">
                            <?php foreach($designations as $d) { ?>
                                <?php if($d->code == $designation) { ?>
                                <option value="<?=$d->code?>" selected="selected"><?=$d->name?></option>
                                <?php } else { ?>
                                <option value="<?=$d->code?>"><?=$d->name?></option>
                                <?php } ?>
                            <?php } ?>
                        </select>
                    </p>
                    <p>
                        <label for="startYear">Start year:</label>
                        <input type="text" id="startYear" name="startYear" value="<?=$startYear?>" />
                        <label for="endYear">End year:</label>
                        <input type="text" id="endYear" name="endYear" value="<?=$endYear?>" />
                        <input type="submit" value="Submit" />
                    </p>
                </div>
            </form>
                <?php if($_SERVER["REQUEST_METHOD"] == "POST") { ?>
                <div>
                    <?php if($records == NULL) { ?>
                    No records
                    <?php } else { ?>
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
                            <?php foreach($records as $record) { ?>
                            <tr>
                                <td>
                                    <a href="https://data.nbn.org.uk/Datasets/<?=$record->datasetKey?>"><?=$record->datasetKey?></a>
                                </td>
                                <td>
                                    <?=$record->observationID?>
                                </td>
                                <td>
                                    <?=$record->observationKey?>
                                </td>
                                <td>
                                    <?=$record->location?>
                                </td>
                                <td>
                                    <?=$record->resolution?>
                                </td>
                                <td>
                                    <?=$record->taxonVersionKey?>
                                </td>
                                <td>
                                    <?=$record->pTaxonVersionKey?>
                                </td>
                                <td>
                                    <?=$record->pTaxonName?>
                                </td>
                                <td>
                                    <?=$record->pTaxonAuthority?>
                                </td>
                                <td>
                                    <?=$record->startDate?>
                                </td>
                                <td>
                                    <?=$record->endDate?>
                                </td>
                                <td>
                                    <?=$record->dateTypekey?>
                                </td>
                            </tr>
                            <?php } ?>
                        </tbody>
                    </table>
                    <?php } ?>
                </div>
                <?php } ?>
        </body>
        </html>
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>

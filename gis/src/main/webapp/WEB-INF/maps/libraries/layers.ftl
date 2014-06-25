[#ftl]

[#-- 
    The following macro defines the data location for a given layer. Some 
    layers are stored in a cache next to the GIS application in Shapefiles
    these can be read quicker than from the database. 
--]
[#macro data sqlGenerator shapefiles name srs]
    [#if shapefiles.isShapefilePresent(name)]
        [#assign projections = shapefiles.getProjections(name)/]
        [#if projections?keys?seq_contains(srs)]
            [@shapefile shapefiles.getProjections(name) srs/]
        [#else]
            [@shapefile shapefiles.getProjections(name) projections?keys?first/]
        [/#if]
    [#else]
        CONNECTIONTYPE          OGR
        CONNECTION              "${properties.spatialConnection}"
        PROCESSING              "CLOSE_CONNECTION=DEFER"
        EXTENT                  -180 -90 180 90
        DATA                    "${sqlGenerator.getData(name)}"
        PROJECTION "init=epsg:4326" END
    [/#if]

[/#macro]

[#macro shapefile projections projection]
    DATA       "${projections[projection].getAbsolutePath()}"
    PROJECTION "init=${projection?lower_case}" END
[/#macro]


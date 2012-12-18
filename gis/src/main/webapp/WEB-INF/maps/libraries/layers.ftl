[#ftl]

[#-- 
    The following macro defines the data location for a given layer. Some 
    layers are stored in a cache next to the GIS application in Shapefiles
    these can be read quicker than from the database. 
--]
[#macro data sqlGenerator shapefiles name]
    [#if shapefiles.isShapefilePresent(name)]
        DATA                    "${shapefiles.getShapefile(name).getAbsolutePath()}"
    [#else]
        CONNECTIONTYPE          PLUGIN
        PLUGIN                  "msplugin_mssql2008.dll"
        CONNECTION              "${properties.spatialConnection}"
        PROCESSING              "CLOSE_CONNECTION=DEFER"
        EXTENT                  -180 -90 180 90
        DATA                    "${sqlGenerator.getData(name)}"
    [/#if]
[/#macro]


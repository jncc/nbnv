[#ftl]

[#macro join list][#list list as element][${element}][#if element_has_next] OR [/#if][/#list][/#macro]

[#macro os location requires]
    #
    # Start of layer definitions
    #

    LAYER
        NAME "MiniScale-NoGrid"
        GROUP "OS-Scale-Dependent"
        [#if requires?size != 0]REQUIRES "[@join requires/]"[/#if]
        METADATA
            "wms_title"    "Ordnance Survey MiniScale - No Grid (Modern)"   ##required
        END
        TYPE RASTER
        STATUS ON
        DATA "${location}/OS/Modern/MiniScale/raster/MiniScale_(no_grid)_R13.tif"
        PROJECTION
            "init=epsg:27700"   ##recommended
        END

        MINSCALEDENOM 150000   ##Scale Dependency
    END # Layer

    LAYER
        NAME "OS250k"
        GROUP "OS-Scale-Dependent"
        [#if requires?size != 0]REQUIRES "[@join requires/]"[/#if]
        METADATA
            "wms_title"    "Ordnance Survey 1:250000 Scale Colour Raster (Modern)"   ##required
        END
        TYPE RASTER
        STATUS ON
        TILEINDEX "${location}/OS/Modern/250k/index.shp"
        TILEITEM "Location"
        PROJECTION
            "init=epsg:27700"   ##recommended
        END

        MINSCALEDENOM 35000   ##Scale Dependency
        MAXSCALEDENOM 150000
    END # Layer

    LAYER
        NAME "OS50k"
        GROUP "OS-Scale-Dependent"
        [#if requires?size != 0]REQUIRES "[@join requires/]"[/#if]
        METADATA
            "wms_title"    "Ordnance Survey 1:50000 Scale Colour Raster (Modern)"   ##required
        END
        TYPE RASTER
        STATUS ON
        TILEINDEX "${location}/OS/Modern/50k/index.shp"
        TILEITEM "Location"
        PROJECTION
            "init=epsg:27700"   ##recommended
        END

        MINSCALEDENOM 15000   ##Scale Dependency
        MAXSCALEDENOM 35000
    END # Layer

    LAYER
        NAME "OS25k"
        GROUP "OS-Scale-Dependent"
        [#if requires?size != 0]REQUIRES "[@join requires/]"[/#if]
        METADATA
            "wms_title"    "Ordnance Survey 1:25000 Scale Colour Raster (Modern)"   ##required
        END
        TYPE RASTER
        STATUS ON
        TILEINDEX "${location}/OS/Modern/25k/index.shp"
        TILEITEM "Location"
        PROJECTION
            "init=epsg:27700"   ##recommended
        END

        MAXSCALEDENOM 15000
    END # Layer
[/#macro]

[#macro selectedFeature data spatialConnection fill=false]
    LAYER
        NAME                                                "Selected-Feature"
        TYPE                                                POLYGON
        STATUS                                              OFF
        CONNECTIONTYPE                                      PLUGIN
        PLUGIN                                              "msplugin_mssql2008.dll"
        CONNECTION                                          "${spatialConnection}"
        PROCESSING                                          "CLOSE_CONNECTION=DEFER"
        OPACITY                                             60
        DATA                                                "${data}"

        METADATA
            "wms_title"                                       "Selected-Feature"
            "wms_include_items"                               "all"
        END

        PROJECTION
            "init=epsg:4326"
        END

        CLASS
            NAME                                              "default"

            STYLE
                [#if fill] 
                    COLOR                                       210 240 248 
                [/#if]
                OUTLINECOLOR                                    0 0 0
                WIDTH                                           3
            END
        END
    END
[/#macro]

[#macro vectors location]
    [#assign vectorLayers = [
        { "name" : "Vice-counties",                     "data" : "vice_counties",                   "projection" : 27700},
        { "name" : "GB-Coast",                          "data" : "gb_coast",                        "projection" : 27700 },
        { "name" : "GB-Coast-with-Hundred-km-Grid",     "data" : "gb_coast_with_100k_grid",         "projection" : 27700 },
        { "name" : "GB-Hundred-km-Grid",                "data" : "gb_100k_grid",                    "projection" : 27700 },
        { "name" : "GB-Hundred-km-Grid-Ireland-cutout", "data" : "gb_100k_grid_ireland_cutout",     "projection" : 27700 },
        { "name" : "GB-Ten-km-Grid",                    "data" : "gb_10k_grid",                     "projection" : 27700,   "colour": "150 150 150" },
        { "name" : "GB-Ten-km-Grid-Ireland-cutout",     "data" : "gb_10k_grid_ireland_cutout",      "projection" : 27700,   "colour": "150 150 150" },

        { "name" : "Ireland-Coast",                     "data" : "ireland_coast",                   "projection" : 29903 },
        { "name" : "Ireland-Coast-with-Hundred-km-Grid","data" : "ireland_coast_with_100k_grid",    "projection" : 29903 },
        { "name" : "Ireland-Hundred-km-Grid",           "data" : "ireland_100k_grid",               "projection" : 29903 },
        { "name" : "Ireland-Hundred-km-Grid-GB-cutout", "data" : "ireland_100k_grid_gb_cutout",     "projection" : 29903 },
        { "name" : "Ireland-Ten-km-Grid",               "data" : "ireland_10k_grid",                "projection" : 29903,   "colour": "150 150 150" },
        { "name" : "Ireland-Ten-km-Grid-GB-cutout",     "data" : "ireland_10k_grid_gb_cutout",      "projection" : 29903,   "colour": "150 150 150" }
    ]]

    [#list vectorLayers as layer]
        LAYER
            NAME                    "${layer.name}"
            TYPE                    LINE
            DATA                    "${location}/Vector/${layer.data}/${layer.projection?c}"
            STATUS                  ON
            PROJECTION              "init=epsg:${layer.projection?c}"          END
            METADATA
                "wms_title"                                       "${layer.name}"
                "wms_include_items"                               "all"
            END
            CLASS
                NAME                                              "${layer.name}"
                OUTLINECOLOR                                      ${layer.colour!"0 0 0"}
            END
        END
    [/#list]
[/#macro]

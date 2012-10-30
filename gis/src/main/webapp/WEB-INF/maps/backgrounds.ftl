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
    DATA "${location}\MiniScale\raster\MiniScale_(no_grid)_R13.tif"
    PROJECTION
      "init=epsg:27700"   ##recommended
    END

    MINSCALEDENOM 150000   ##Scale Dependancy
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
    TILEINDEX "${location}\250k\index.shp"
    TILEITEM "Location"
    PROJECTION
      "init=epsg:27700"   ##recommended
    END

    MINSCALEDENOM 35000   ##Scale Dependancy
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
    TILEINDEX "${location}\50k\index.shp"
    TILEITEM "Location"
    PROJECTION
      "init=epsg:27700"   ##recommended
    END

    MINSCALEDENOM 15000   ##Scale Dependancy
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
    TILEINDEX "${location}\25k\index.shp"
    TILEITEM "Location"
    PROJECTION
      "init=epsg:27700"   ##recommended
    END

    MAXSCALEDENOM 15000
  END # Layer
[/#macro]

[#macro selectedFeature featureID spatialConnection]
    LAYER
        NAME                                                "Selected-Feature"
        TYPE                                                POLYGON
        STATUS                                              OFF
        CONNECTIONTYPE                                      PLUGIN
        PLUGIN                                              "msplugin_mssql2008.dll"
        CONNECTION                                          "${spatialConnection}"
        PROCESSING                                          "CLOSE_CONNECTION=DEFER"
        OPACITY                                             60
        DATA                        "geom from (
                                        SELECT id, geom
                                        FROM FeatureData
                                        WHERE identifier = '${featureID}'
                                    ) AS foo USING UNIQUE id USING SRID=4326"

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
                COLOR                                           210 240 248
                OUTLINECOLOR                                    0 0 0
                WIDTH                                           3
            END
        END
    END
[/#macro]

[#macro contextLayers spatialConnection]
    [#assign layers=[
        "GB-Coast", "Ireland-Coast", "GB-Hundred-km-Grid", "Ireland-Hundred-km-Grid",
        "GB-Coast-with-Hundred-km-Grid", "GB-and-Ireland-Coasts-with-Hundred-km-Grid",
        "Vice-counties-(low-res)", "GB-and-Ireland-Hundred-km-Grid"
    ]]
    [#list layers as contextLayer]
        LAYER
            NAME                                                "${contextLayer}"
            TYPE                                                LINE
            STATUS                                              OFF
            CONNECTIONTYPE                                      PLUGIN
            PLUGIN                                              "msplugin_mssql2008.dll"
            CONNECTION                                          "${spatialConnection}"
            PROCESSING                                          "CLOSE_CONNECTION=DEFER"

            DATA                        "geom from (
                                            SELECT id, geom
                                            FROM ContextLayerFeatureData
                                            WHERE contextLayerID = ${contextLayer_index+1}
                                        ) AS foo USING UNIQUE id USING SRID=27700"

            METADATA
                "wms_title"                                       "${contextLayer}"
                "wms_include_items"                               "all"
            END

            PROJECTION
                "init=epsg:27700"
            END

            CLASS
                NAME                                              "default"

                STYLE
                    COLOR                                           0 0 0
                    WIDTH                                           1
                END
            END
        END
    [/#list]
[/#macro]

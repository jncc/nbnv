[#ftl]

[#macro join list][#list list as element][${element}][#if element_has_next] OR [/#if][/#list][/#macro]

[#macro os location requires]
  #
  # Start of layer definitions
  #

  LAYER
    NAME "MiniScale-NoGrid"
    GROUP "OS-Scale-Dependant"
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
    GROUP "OS-Scale-Dependant"
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
    GROUP "OS-Scale-Dependant"
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
[/#macro]

[#macro selectedFeature featureID]
    LAYER
        NAME                                                "Selected-Feature"
        TYPE                                                POLYGON
        STATUS                                              OFF
        CONNECTIONTYPE                                      PLUGIN
        PLUGIN                                              "msplugin_mssql2008.dll"
        CONNECTION                                          "${properties.spatialConnection}"
        PROCESSING                                          "CLOSE_CONNECTION=DEFER"

        DATA                        "geom from (
                                        SELECT featureID, geom
                                        FROM FeatureData
                                        WHERE featureID = '${featureID}'
                                    ) AS foo USING UNIQUE featureID USING SRID=4326"

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
                COLOR                                           255 255 255
                OUTLINECOLOR                                    0 0 0
                WIDTH                                           1
            END
        END
    END
[/#macro]
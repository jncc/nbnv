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
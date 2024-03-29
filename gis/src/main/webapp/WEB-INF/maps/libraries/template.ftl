[#ftl]
[#import "backgrounds.ftl" as backgrounds]

[#-- 
    The following macro will create a general map file which only needs to have
    layers added to it. The Default projection of the map will be 4326 and the 
    extent will be the world
--]
[#macro master title]
    MAP
        IMAGETYPE                                             PNG
        SIZE                                                  2800 2800
        IMAGECOLOR                                            255 255 255
        FONTSET                                               "fonts/fontset"
        EXTENT                                                -180 -90 180 90

		[#-- 
			Explictially defining gif as an output format. According to 
			http://mapserver.org/mapfile/outputformat.html this should be 
			implicitally defined. This was spotted when migrating to version 
			6.4 of mapserver (NBNV-822). To check if gif output is working,
			you can use grid mapping @ SingleSpecies/NBNSYS0000003278/map?format=gif
		--]
		OUTPUTFORMAT
		  NAME "gif"
		  DRIVER GD/GIF
		  MIMETYPE "image/gif"
		  IMAGEMODE PC256
		  EXTENSION "gif"
		END

        WEB
            IMAGEPATH                                           "/ms4w/tmp/ms_tmp/"
            IMAGEURL                                            "/ms_tmp/"
            METADATA
                "wms_title"                                       "${title}"
                "wms_onlineresource"                              "${mapServiceURL}"
                "wms_srs"                                         "EPSG:29903 EPSG:27700 EPSG:4326 EPSG:3857"
                "wms_enable_request"                              "*"
            END
        END

        PROJECTION                                  "init=epsg:4326"         END
        
        [#nested]
    END
[/#macro]

[#--
    The following macro will create a Map general map file as defined from master
    macro. It will also have :
        OS Layers           - Only visible to globally defined 'layers' list
        Vector layers       - Those configured in the backgrounds.ftl
        Selected-Feature    - If feature data is defined, a Selected Feature layer will be added
--]
[#macro context title requires]
    [@master title=title]
        [@backgrounds.vectors location=properties.contextLayersLocation/]    
        [@backgrounds.os location=properties.contextLayersLocation requires=requires/]

        [#if featureData??]
            [@backgrounds.selectedFeature data=featureData spatialConnection=properties.spatialConnection/]
        [/#if]

        [#nested]
    [/@master]
[/#macro]

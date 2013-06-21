define [
  "openlayers"
  "cs!helpers/Globals"
], (OpenLayers, Globals) ->
  #Resolutions optimized for Ordnance Survey map [2500,1e3,500,200,100,50,25,4,2.5,2,1],
  __EPSG_27700_RESOLUTIONS__ : [  45074.742999999995, 
                                  22537.389,
                                  11268.6965, 
                                  5634.3485, 
                                  2817.174,
                                  1408.587, 
                                  704.2935, 
                                  352.147, 
                                  176.0735, 
                                  88.0365, 
                                  44.0185, 
                                  22.009, 
                                  11.005, 
                                  5.502, 
                                  2.751, 
                                  1.376, 
                                  0.688, 
                                  0.344],

  getBaseLayer: (name) ->
    #TODO this is openlayers key, we need our own
    apiKey = "AqTGBsziZHIJYYxgivLBf0hVdrAk9mWO5cQcb8Yux8sW5M8c8opEC2lZqKR1ZZXf"
    switch name
      when "Shaded" then return new OpenLayers.Layer.Bing( type: "Road", key: apiKey, projection: new OpenLayers.Projection("EPSG:3857") )
      when "Hybrid" then return new OpenLayers.Layer.Bing( type: "AerialWithLabels", key: apiKey, projection: new OpenLayers.Projection("EPSG:3857") )
      when "Aerial" then return new OpenLayers.Layer.Bing( type: "Aerial", key: apiKey, projection: new OpenLayers.Projection("EPSG:3857"))
      when "OS" then return new OpenLayers.Layer.WMS name, Globals.gis("OS-Modern"), 
              layers: "MiniScale-NoGrid,OS250k,OS50k,OS25k"
              format:"image/png"
            ,
              isBaseLayer: true
              projection: new OpenLayers.Projection("EPSG:27700")
              resolutions: @__EPSG_27700_RESOLUTIONS__
              attribution: "&copy; Crown copyright and database rights 2011 Ordnance Survey [100017955]"

      when "Outline" then return new OpenLayers.Layer.WMS name, Globals.gis("Context"), 
              layers: "Vice-counties,Ireland-Coast"
              format:"image/png"
            ,
              isBaseLayer: true
              projection: new OpenLayers.Projection("EPSG:27700")
              resolutions: @__EPSG_27700_RESOLUTIONS__

  ###
  Create an openlayers layer given some model/Layer which updates when different parts
  of the layer change
  ###
  createLayer: (layer) -> 
    wmsLayer = new OpenLayers.Layer.WMS layer.getName(), layer.getWMS(), 
        layers: [layer.getLayer()]
        format:"image/png"
        transparent: true
        "SLD_BODY": layer.getSLD()
      ,
        isBaseLayer:false
        opacity: layer.getOpacity()

    layer.on 'change:layer', -> wmsLayer.mergeNewParams layers : [layer.getLayer()]
    layer.on 'change:sld', -> wmsLayer.mergeNewParams "SLD_BODY" : layer.getSLD() 
    layer.on 'change:opacity', -> wmsLayer.setOpacity layer.getOpacity()

    return wmsLayer
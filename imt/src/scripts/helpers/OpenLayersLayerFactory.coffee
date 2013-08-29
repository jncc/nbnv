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
    apiKey = "AnpABRCh8GIf4UnwABXiVK1eqKup7XvvMx2LLM4ijcLv5Ym6OyKm_5KMsFmYvtty"
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
        visibility: layer.isVisible()

    layer.on 'change:layer', -> wmsLayer.mergeNewParams layers : [layer.getLayer()]
    layer.on 'change:sld', -> wmsLayer.mergeNewParams "SLD_BODY" : layer.getSLD() 
    layer.on 'change:opacity', -> wmsLayer.setOpacity layer.getOpacity()
    layer.on 'change:visibility', -> wmsLayer.setVisibility layer.isVisible()
    layer.on 'change:wms', -> 
      wmsLayer.setUrl layer.getWMS()
      do wmsLayer.redraw #trigger a redraw of the layer. Openlayers doesn't do this when we update the url

    return wmsLayer

  ###
  Create a drawing layer which is listens to the Picker and updates the picker
  if features are drawn.
  ###
  getDrawingLayer: (picker) ->
    wktFactory = new OpenLayers.Format.WKT #Create the wktFactory to convert openlayers features to wkt
    drawingLayer = new OpenLayers.Layer.Vector "Drawing Layer"
    epsg4326 = new OpenLayers.Projection("EPSG:4326")

    #Define a simple function which sets the features on the drawing layer
    #to that of the wkt in the picker (if there is any defined)
    updateDrawingLayer = ->
      wkt = picker.get "wkt"
      do drawingLayer.removeAllFeatures
      
      #If a wkt value is set then we need to read it into a vector,
      #convert it from epsg4326 to the projection system of the map
      #then add it to the picking layer
      if wkt
        vector = wktFactory.read wkt
        vector.geometry.transform epsg4326, drawingLayer.map.getProjectionObject()
        drawingLayer.addFeatures vector, silent:true

    picker.on 'change:wkt', updateDrawingLayer #When the wkt is changed, update the layer

    #If a feature is added to the layer then update the wkt
    drawingLayer.events.register "featureadded", drawingLayer, (evt)->
      feature = evt.feature.clone() #clone the feature so we can perform a transformation
      feature.geometry.transform drawingLayer.map.getProjectionObject(), epsg4326 #convert to 4326
      picker.set "wkt", wktFactory.write(feature)
    
    #Update the drawing layer when added to a map
    drawingLayer.events.register "added", drawingLayer, updateDrawingLayer

    drawingLayer.update = updateDrawingLayer #Attaching the update method to be called from elsewhere
    return drawingLayer
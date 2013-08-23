define [
  "jquery",
  "underscore",
  "backbone",
  "openlayers",
  "cs!helpers/OpenLayersLayerFactory"
], ($, _, Backbone, OpenLayers, OpenLayersLayerFactory) -> 
  #this is actually SR-ORG:7094, rather than EPSG:3857, it matches the bing map better than EPSG:3857. See NBNIV-534
  Proj4js.defs['EPSG:3857'] = "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext  +no_defs";
  Proj4js.defs["EPSG:27700"] = "+proj=tmerc +lat_0=49 +lon_0=-2 +k=0.9996012717 +x_0=400000 +y_0=-100000 +ellps=airy +datum=OSGB36 +units=m +no_defs";
  
  Backbone.View.extend
    # Register to imt events which the view should respond to
    initialize: ->
      @drawingLayer = OpenLayersLayerFactory.getDrawingLayer @model.getPicker()
      @drawingControl = new OpenLayers.Control.DrawFeature @drawingLayer, OpenLayers.Handler.RegularPolygon,
        handlerOptions: sides: 4

      @map = new OpenLayers.Map
        div: @el,
        maxExtent: new OpenLayers.Bounds(-20037508.34,-20037508.34,20037508.34,20037508.34),
        displayProjection: new OpenLayers.Projection("EPSG:3857"),
        theme: null,
        layers: [OpenLayersLayerFactory.getBaseLayer( @model.get "baseLayer" ), @drawingLayer],
        eventListeners: 
          moveend: => @model.set "viewport", do @getOpenlayersViewport
          zoomend: => @model.getLayers().setZoom do @map.getZoom

      @map.addControl(@drawingControl)
      #If base layer changes, drawing layer might need to be reprojected
      @map.events.register "changebaselayer", @map, @drawingLayer.update 
      
      @zoomToViewport null, @model.get("viewport"), showAll: true
      @listenTo @model, "change:viewport", @zoomToViewport
      @listenTo @model, "change:baseLayer", @updateBaseLayer
      @listenTo @model.getLayers(), "add", @addLayer
      @listenTo @model.getLayers(), "position", @positionLayer
      @listenTo @model.getLayers(), "reset", @resetLayers
      @listenTo @model.getLayers(), "remove", @removeLayer
      @listenTo @model.getPicker(), "change:isPicking", @toggleDrawing

    ###
    Create an openlayers version of the desired baselayer
    ###
    updateBaseLayer: (evt, baselayer) -> 
      oldViewport = @model.get 'viewport'
      oldProjection = @map.getProjectionObject()

      @map.removeLayer @map.baseLayer #remove the current baseLayer
      @map.addLayer OpenLayersLayerFactory.getBaseLayer baselayer #Find the openlayers version of baseLayer and add

      #Fix the Openlayers bugs 
      @map.setLayerIndex @map.baseLayer, 0 #move the baselayer to the 0th position

      #Zoom to the same viewport as before reprojection (Ticket #1249)
      if oldProjection.getCode() isnt @map.getProjectionObject().getCode()
        @zoomToViewport null, viewport, showAll: false

    ###
    If the picker is in picking mode then enable the drawing control
    ###
    toggleDrawing: (evt, isPicking)->
      if isPicking then @drawingControl.activate() else @drawingControl.deactivate()

    ###
    Add a new wms layer for the given layer. Associates the new Openlayers.Layer.WMS to
    the Backbone layer model for easy removing at a later date
    ###
    addLayer: (layer)-> 
      @map.addLayer layer._openlayersWMS = OpenLayersLayerFactory.createLayer(layer)
      #move the new layer to below the drawing layer(s). this can be done by setting
      #the index of the new layer to the amount of layers - 2.
      @map.setLayerIndex layer._openlayersWMS, @map.getNumLayers() - 2

    ###
    Listens to when layers have been repositioned. Notify the OpenLayers Map and set the 
    new index for that layer
    ###
    positionLayer: (layer, collection, newPosition)->
      @map.setLayerIndex layer._openlayersWMS, newPosition + 1

    ###
    Remove the wms layer associated with the given layer
    ###
    removeLayer: (layer)-> @map.removeLayer layer._openlayersWMS

    ###
    Remove all the old wms layers and replace with the reset collection
    ###
    resetLayers: (layers, options) ->
      _.each options.previousModels, (layer) => @removeLayer layer
      layers.forEach (layer) => @addLayer layer

    ###
    Event listener for viewport changes on the model. Update the Openlayers Map.
    ###
    zoomToViewport: (evt, viewport, options) ->
      extent = @map.getExtent()
      if not extent? or not _.isEqual( viewport, @getOpenlayersViewport() )
        #Check if view port already set is not the same as the openlayers viewport
        @map.zoomToExtent @getOpenlayersBounds(viewport), not options.showAll

    ###
    Translate the given viewport which is in 4326 into an openlayers bounds in the
    projection system that the OpenLayers.Map is currently working in
    ###
    getOpenlayersBounds: (viewport)-> 
      openlayersBounds = new OpenLayers.Bounds  viewport.minX, 
                                                viewport.minY, 
                                                viewport.maxX,
                                                viewport.maxY
      openlayersBounds.transform new OpenLayers.Projection("EPSG:4326"), @map.getProjectionObject()

    ###
    Obtain the current viewport of the openlayers map and return it in EPSG:4326
    ###
    getOpenlayersViewport: ->
      extent = @map.getExtent().transform @map.getProjectionObject(), 
                                          new OpenLayers.Projection("EPSG:4326")

      minX: extent.left, 
      minY: extent.bottom,
      maxX: extent.right,
      maxY: extent.top
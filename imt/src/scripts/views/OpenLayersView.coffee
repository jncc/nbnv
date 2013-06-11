define [
  "jquery",
  "underscore",
  "backbone",
  "openlayers",
  "cs!helpers/OpenLayersLayerFactory"
], ($, _, Backbone, OpenLayers, OpenLayersLayerFactory) -> 
  Proj4js.defs["EPSG:27700"] = "+proj=tmerc +lat_0=49 +lon_0=-2 +k=0.9996012717 +x_0=400000 +y_0=-100000 +ellps=airy +datum=OSGB36 +units=m +no_defs";
  Backbone.View.extend
    # Register to imt events which the view should respond to
    initialize: ->
      @map = new OpenLayers.Map
        div: @el,
        maxExtent: new OpenLayers.Bounds(-20037508.34,-20037508.34,20037508.34,20037508.34),
        displayProjection: new OpenLayers.Projection("EPSG:4326"),
        theme: null,
        layers: [OpenLayersLayerFactory.getBaseLayer( @model.get "baseLayer" )],
        eventListeners: 
          moveend : => @model.set("viewport", do @getOpenlayersViewport)
        ,
        controls : [ new OpenLayers.Control.Navigation,
                     new OpenLayers.Control.ArgParser,
                     new OpenLayers.Control.Attribution]

      @zoomToViewport(null, @model.get "viewport")
      @listenTo @model, "change:viewport", @zoomToViewport
      @listenTo @model, "change:baseLayer", @updateBaseLayer

    ###
    Create an openlayers version of the desired baselayer
    ###
    updateBaseLayer: (evt, baselayer) -> 
      centre = @map.getCenter()
      zoom = @map.getZoom()
      oldProjection = @map.getProjectionObject()

      @map.removeLayer(@map.baseLayer); #remove the current baseLayer
      @map.addLayer OpenLayersLayerFactory.getBaseLayer baselayer #Find the openlayers version of baseLayer and add

      #this line is required to fix a bug in OpenLayers 2.10 (Ticket #1249)
      @map.setCenter(centre.transform(oldProjection, @map.getProjectionObject()),zoom); 


    ###
    Event listener for viewport changes on the model. Update the Openlayers Map
    ###
    zoomToViewport: (evt, viewport) ->
      extent = @map.getExtent();
      #Check if view port is already and is not the same as the openlayers viewport
      @map.zoomToExtent @getOpenlayersBounds(viewport) if not extent? or not _.isEqual( viewport, @getOpenlayersViewport() )

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
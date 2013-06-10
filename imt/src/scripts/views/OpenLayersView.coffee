define [
  "jquery",
  "underscore",
  "backbone",
  "openlayers"
], ($, _, Backbone, OpenLayers) -> 
  Proj4js.defs["EPSG:27700"] = "+proj=tmerc +lat_0=49 +lon_0=-2 +k=0.9996012717 +x_0=400000 +y_0=-100000 +ellps=airy +datum=OSGB36 +units=m +no_defs";
  Backbone.View.extend
    # Register to imt events which the view should respond to
    initialize: ->
      @map = new OpenLayers.Map
        div: @el,
        maxExtent: new OpenLayers.Bounds(-20037508.34,-20037508.34,20037508.34,20037508.34),
        displayProjection: new OpenLayers.Projection("EPSG:4326"),
        theme: null,
        layers: [new OpenLayers.Layer.OSM sphericalMercator:true],
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
      @map.removeLayer(@map.baseLayer); #remove the current baselayer
      #Find the openlayers version of baselayer and add
      #TODO, add the requested layer
      @map.addLayer new OpenLayers.Layer.OSM sphericalMercator:true 


    ###
    * Event listener for viewport changes on the model. Update the Openlayers Map
    ###
    zoomToViewport: (evt, viewport) ->
      extent = @map.getExtent();
      #Check if view port is already and is not the same as the openlayers viewport
      @map.zoomToExtent @getOpenlayersBounds(viewport) if not extent? or not _.isEqual( viewport, @getOpenlayersViewport() )

    getOpenlayersBounds: (viewport)-> 
      openlayersBounds = new OpenLayers.Bounds  viewport.minX, 
                                                viewport.minY, 
                                                viewport.maxX,
                                                viewport.maxY
      openlayersBounds.transform new OpenLayers.Projection("EPSG:4326"), @map.getProjectionObject()

    getOpenlayersViewport: ->
      extent = @map.getExtent().transform @map.getProjectionObject(), 
                                          new OpenLayers.Projection("EPSG:4326")

      minX: extent.left, 
      minY: extent.bottom,
      maxX: extent.right,
      maxY: extent.top
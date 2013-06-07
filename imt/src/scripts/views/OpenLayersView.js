define([
		"jquery",
		"underscore",
		"backbone",
		"openlayers"
], function($, _, Backbone, OpenLayers) {

Proj4js.defs["EPSG:27700"] = "+proj=tmerc +lat_0=49 +lon_0=-2 +k=0.9996012717 +x_0=400000 +y_0=-100000 +ellps=airy +datum=OSGB36 +units=m +no_defs";
	return Backbone.View.extend({	
		// Register to imt events which the view should respond to
		initialize: function() {
			var _me = this;
			this.map = new OpenLayers.Map({
                div: this.el,
				maxExtent: new OpenLayers.Bounds(-20037508.34,-20037508.34,20037508.34,20037508.34),
				displayProjection: new OpenLayers.Projection("EPSG:4326"),
				theme: null,
                layers: [
                    new OpenLayers.Layer.OSM({sphericalMercator:true})
				],
				eventListeners: {
                    moveend : function() {
						_me.model.set("viewport", _me.getOpenlayersViewport());
                    }            
                },
				controls : [	new OpenLayers.Control.Navigation(),
								new OpenLayers.Control.ArgParser(),
								new OpenLayers.Control.Attribution()]
			});
			this.zoomToViewport(null, this.model.get("viewport"));
			this.listenTo(this.model, "change:viewport", this.zoomToViewport);
			this.listenTo(this.model, "change:baseLayer", this.updateBaseLayer);
		},
		
		/**
		* Create an openlayers version of the desired baselayer
		*/
		updateBaseLayer: function(evt, baselayer) {
			this.map.removeLayer(this.map.baseLayer); //remove the current baselayer
			//Find the openlayers version of baselayer and add
			//TODO, add the requested layer
			this.map.addLayer(new OpenLayers.Layer.OSM({sphericalMercator:true})); 
		},
		
		/**
		* Event listener for viewport changes on the model. Update the Openlayers Map
		**/
		zoomToViewport: function(evt, viewport) {
			var extent = this.map.getExtent();
			//Check if view port is already and is not the same as the openlayers viewport
			if(!(extent && _.isEqual(viewport, this.getOpenlayersViewport()))) {
				this.map.zoomToExtent(this.getOpenlayersBounds(viewport));
			}
		},
		
		getOpenlayersBounds: function(viewport) {
			var openlayersBounds = new OpenLayers.Bounds(	viewport.minX, 
															viewport.minY, 
															viewport.maxX,
															viewport.maxY);
			return openlayersBounds.transform(
							new OpenLayers.Projection("EPSG:4326"), 
							this.map.getProjectionObject())
		},
		
		getOpenlayersViewport: function() {
			var extent = this.map.getExtent().transform(
					this.map.getProjectionObject(), new OpenLayers.Projection("EPSG:4326")
			);
			return {
				minX: extent.left, 
				minY: extent.bottom,
				maxX: extent.right,
				maxY: extent.top
			};
		}		
	});
});
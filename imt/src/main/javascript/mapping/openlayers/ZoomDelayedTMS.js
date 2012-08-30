/**
*
* @author		:- Christopher Johnson
* @date			:- 25rd-May-2011
* @description	:- This JScript wraps up the OpenLayers.Layer.TMS logic and delates the delay logic to the ZoomDelayImage
* @dependencies	:-
*	OpenLayers.Layer.TMS
*	OpenLayers.Tile.ZoomDelayImage
*/

OpenLayers.Layer.ZoomDelayedTMS = OpenLayers.Class(OpenLayers.Layer.TMS, {
	_toRedraw:[],
	
	initialize: function(name, url, options) {
		options.tileZoomDelay = options.tileZoomDelay || 1000; //set the default tileZoom Delay
		OpenLayers.Layer.TMS.prototype.initialize.apply(this,arguments);
	},
	
	addTile : function(bounds, position) { //add a tile in the form of a ZoomDelayImage
		return new OpenLayers.Tile.ZoomDelayImage(this, position, bounds, null, this.tileSize, this.tileOptions);
	},
	
	setMap : function(map) {
		OpenLayers.Layer.TMS.prototype.setMap.apply(this,arguments);
		this._lastZoom = map.zoom; //store the last zoom
	},
	
	CLASS_NAME: "OpenLayers.Layer.ZoomDelayedTMS"
});
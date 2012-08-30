/**
*
* @author		:- Christopher Johnson
* @date			:- 25rd-May-2011
* @description	:- This JScript wraps up the logic of OpenLayers.Tile.Image and ensures that draw operations only 
*	occur a specific time after a zoom. Doing so ensures that a rapid zoom activity does not result in many map requests
*	which can add unnecessary load to the Mapping servers and get blocked by a clients browser.
* @dependencies	:-
*	OpenLayers.Tile.Image
*	nbn.util.ArrayTools
*/

OpenLayers.Tile.ZoomDelayImage = OpenLayers.Class(OpenLayers.Tile.Image, {
	draw: function() {
		var layer = this.layer;
		this.clear(); //clear the original image so that there are no tile artifacts
		nbn.util.ArrayTools.removeFrom(this, layer._toRedraw);
		
		if(layer._lastZoom !== layer.map.zoom) { //timer is not running but will be
			layer._toRedraw.push(this);
			clearTimeout(layer._timer);
			layer._timer = setTimeout(function() { //delay the loading of the tiles
				while(layer._toRedraw.length)
					OpenLayers.Tile.Image.prototype.draw.apply(layer._toRedraw.pop());
				layer._timer = undefined;
			},layer.options.tileZoomDelay);
			layer._lastZoom = layer.map.zoom;
		}
		else if(layer._timer) //timer is running
			layer._toRedraw.push(this); //add to living timer
		else //no change in zoom, this is a pan operation. Load like normal
			OpenLayers.Tile.Image.prototype.draw.apply(this);
		return true;
	},
	
	CLASS_NAME: "OpenLayers.Tile.ZoomDelayImage"
});
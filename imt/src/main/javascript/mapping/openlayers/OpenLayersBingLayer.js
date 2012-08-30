/**
*
* @author       :- Christopher Johnson
* @date         :- 1-Aug-2011
* @description  :- This JScript represents a OpenLayers Bing Map in a form which is compatible with the nbn mapping framework
*/
window.nbn = window.nbn || {};
nbn.mapping = nbn.mapping || {};
nbn.mapping.openlayers = nbn.mapping.openlayers || {};

nbn.mapping.openlayers.OpenLayersBingLayer = function(options) {
	options.mappingType = "BING"; //set the mapping type
	var _bingLayerInNBNForm = new nbn.mapping.openlayers.OpenLayersLayer(new OpenLayers.Layer.VirtualEarth(options.type, {
			type: VEMapStyle[options.type],
			isBaseLayer: true,
			sphericalMercator: true,
			animationEnabled: false,
			projection: new OpenLayers.Projection("EPSG:3857"),
			maxExtent: new OpenLayers.Bounds(-20037508.34,-20037508.34,20037508.34,20037508.34),
			numZoomLevels:18
		}), options);
	
	$.extend(this, new nbn.layer.BaseLayer(_bingLayerInNBNForm, options)); //make this layer be an nbn base layer form of a bing map
};
/**
*
* @author       :- Christopher Johnson
* @date         :- 2-Dec-2011
* @description  :- This JScript wraps an OpenLayers Layer and presents it in a form which is compatible with the nbn mapping framework
*/
window.nbn = window.nbn || {};
nbn.mapping = nbn.mapping || {};
nbn.mapping.openlayers = nbn.mapping.openlayers || {};

nbn.mapping.openlayers.OpenLayersLayer = function(layer, options) {
	$.extend(this,
		new nbn.util.ObservableAttribute('Name',options.name || options.type), //create attributes, with defaults
		new nbn.util.ObservableAttribute('Copyright',options.copyright || '')
	);
	
	this.mappingType = options.mappingType;
	this.layer = layer;
};

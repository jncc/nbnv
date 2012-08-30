/**
*
* @author		:- Christopher Johnson
* @date			:- 23-May-2011
* @description	:- This JScript addes the getMapURL method to the interactive mapper
* @dependencies	:-
*	nbn.mapping.InteractiveMappingClient.js (MUST BE IMPORTED FIRST)
*	nbn.util.ServerGeneratedLoadTimeConstants
*	nbn.util.ArrayTools
*/

nbn.mapping.InteractiveMappingClient.prototype.getMapURL = function() {
	function _createMapParams(map) {
		var extent = map.Map.getViewportBBox();
		return {
			baselayer: map.Map.getBaseLayer().getID(),
			bbox: [extent.xmin,extent.ymin,extent.xmax,extent.ymax].join(',')
		}
	}
	
	function _parameteriseLayers(layers) {
		var typesSeen = {}, toReturn = {};
		
		function _hasAlreadyBeenSeen(type) { //util function, will flag the elements which have been seen
			var toReturn = typesSeen[type];
			typesSeen[type] = true; //flag the element as having been seen
			return toReturn;
		}
		
		for(var i in layers) {
			var currLayerConstructionObject = layers[i].layer.getReconstructionObject();
			if(!_hasAlreadyBeenSeen(currLayerConstructionObject.type)) { //has this element be seen before
				delete currLayerConstructionObject.type; //remove the type
				$.extend(toReturn,currLayerConstructionObject);
			}
			else
				return false; //can not create a paramaterised layer object
		}
		return toReturn;
	}

	function _createMapURL(map) {
		var paramLayers = _parameteriseLayers(map.getUnderlyingNBNMapLayerArray());
		if(paramLayers) {
			var toParameterize = $.extend(_createMapParams(map), paramLayers);
			return nbn.util.ServerGeneratedLoadTimeConstants.context.appPath + nbn.util.ArrayTools.joinAndPrepend(nbn.util.ArrayTools.fromObject(toParameterize),'&','/?');
		}
		return false;
	};

	return _createMapURL(this);
};

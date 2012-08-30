/**
*
* @author		:- Christopher Johnson
* @date			:- 23rd-May-2011
* @description	:- This JScript defines the Concrete constructors for the Context Layers
* @dependencies	:-
*	nbn.layer.ResolvingVisibleContextLayer
*/
window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};

(function() {
	var obtainElementDatasetKey = function(elem) {
		return elem.datasetKey;
	}
	
	var reconstructionObjectFromResolvedFilter = function(resolvedObject) {
		return nbn.util.ArrayTools.getArrayByFunction(resolvedObject, obtainElementDatasetKey).join(',');
	}
	
	nbn.layer.BoundaryLayer = function(hosts, googleMap, options) {	
		$.extend(this, new nbn.layer.ResolvingVisibleContextLayer('Boundary', 'boundary', obtainElementDatasetKey, hosts, 'arcgis/rest/services/siteBoundary/siteBoundaries/MapServer', googleMap, options)); //extend the layer
	};
	
	nbn.layer.HabitatLayer = function(hosts, googleMap, options) {	
		$.extend(this, new nbn.layer.ResolvingVisibleContextLayer('Habitat', 'habitats', reconstructionObjectFromResolvedFilter, hosts, 'arcgis/rest/services/habitat/habitats/MapServer', googleMap, options)); //extend the layer
	};
})();
/**
*
* @author		:- Christopher Johnson
* @date			:- 23rd-May-2011
* @description	:- This JScript creates a ArcGisMap layer which is based on a single ResolvingVisibleLayersArcGisMapFilter.
*	This object is cabaple of being reconstructed by the Construction factory of the IMT
* @dependencies	:-
*	nbn.layer.ArcGISMap
*	nbn.layer.ResolvingVisibleLayersArcGisMapFilter
*/

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};

nbn.layer.ResolvingVisibleContextLayer = function(type, resolvingParam, reconstructionObjectFromResolvedFilter, hosts, url, googleMap, options) {
	var _resolvingVisibleFilter = new nbn.layer.ResolvingVisibleLayersArcGisMapFilter(resolvingParam, options[resolvingParam])
	$.extend(this, new nbn.layer.ArcGISMap(hosts, url, googleMap, options));
	this.addLayerFilter(_resolvingVisibleFilter);
	
	this.getResolvingVisibleFilter = function() {
		return _resolvingVisibleFilter;
	};
	
	this.getReconstructionObject = function() {
		var toReturn = {type: type};
		if(_resolvingVisibleFilter.isFiltering())
			toReturn[resolvingParam] = reconstructionObjectFromResolvedFilter(_resolvingVisibleFilter.getResolvedFilter()[resolvingParam])
		return toReturn;
	};
}
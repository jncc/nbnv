/**
*
* @author		:- Christopher Johnson
* @date			:- 7th-April-2011
* @dependencies	:- This JScript file defines arc gis filter which will filter a layer based on a resolveable entity.
*	jquery
*	nbn.layer.ArcGisMapFilter
*/

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};

nbn.layer.ResolvingVisibleLayersArcGisMapFilter = function(type, initial) {
    var _underlyingFilter, _resolvedFilter;
    $.extend(this, _underlyingFilter = new nbn.layer.ArcGisMapFilter());
	
    this.setFilter = function(filterToSet) {
        if(filterToSet) {
            nbn.util.EntityResolver.resolve(new function() {
                this[type] = filterToSet;
            },function(resolvedFilter) {
                _resolvedFilter = resolvedFilter; //store the resolved object
                _underlyingFilter.setFilter({
                    visibleLayers: (!$.isArray(resolvedFilter[type])) ? [resolvedFilter[type].datasetKey] : nbn.util.ArrayTools.getArrayByFunction(resolvedFilter[type], function(element) {
                        return element.datasetKey;
                    })
                });
            });
        }
    };
	
    this.getResolvedFilter = function() {
        return _resolvedFilter;
    };
	
    this.setFilter(initial); //set the map for an initial call
};
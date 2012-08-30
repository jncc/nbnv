/**
*
* @author		:- Christopher Johnson
* @date			:- 1st-Feb-2011
* @description	:- This JScript represents the model for a Context ArcGisMapFilter. The context being a ArcGISMap object.
*	This particular filter requires some context to exist in so that calls to .getFilter() can return a contextual response.
*
*	Optionally a ArcGisMapFilter to which this one is based on can be provided. This heirarchy enables a general filter to 
*	be used in multiple contexts. That is a single filter for multiple ArcGisMaps
* @dependencies	:-
*	nbn.util.Observable
*	nbn.layer.ArcGisMapFilter
*/

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.ContextArcGisMapFilter = function(context, filter) {
	$.extend(this, filter = filter || new nbn.layer.ArcGisMapFilter()); //either extend a passed in filters functionality, or create a new one to extend
	
	var filterInContext = new nbn.util.ObservableAttribute('Filter', filter.getFilter(context.getZoom()));
	var _contextListenerHandle, amountOfListeners = 0; //vars which handle the context listenering
	
	var _updateFunction = function() { //create the function to call when an update to either the zoom or the filter changes
		filterInContext.setFilter(filter.getFilter(context.getZoom()));
	};
	
	var _filterObject = { //create an update object to add to the filter this object represents
		update: _updateFunction
	};

	this.getFilter = filterInContext.getFilter; //return the contextual filter rather than the underlyinging, possibly dynamic filter
	
	this.addFilterUpdateListener = function(toAdd) {
		if(++amountOfListeners == 1) { //if added first listener, register to the context listener
			context.addZoomUpdateListener(_filterObject);
			filter.addFilterUpdateListener(_filterObject);
		}
		filterInContext.addFilterUpdateListener(toAdd);
	};
	
	this.removeFilterUpdateListener = function(toRemove) {
		if(--amountOfListeners == 0) { //if removed last lisener, remove the context listener
			context.removeZoomUpdateListener(_filterObject);
			filter.removeFilterUpdateListener(_filterObject);
		}
			
		return filterInContext.removeFilterUpdateListener(toRemove);
	};
};
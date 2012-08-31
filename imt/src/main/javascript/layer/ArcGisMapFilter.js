/**
*
* @author		:- Christopher Johnson
* @date			:- 1st-Feb-2011
* @description	:- This JScript represents the model for a Context-less ArcGisMapFilter. It adapts the observable 
*	JScript to provide listenable notifications of update events.
*
* 	The context-less nature means that a call to .getFilter() will require a zoom level to be presented. The context less logic means that a 
*	ArcGisMapFilter can be used on different ArcGisMaps simultaneously even if they are rendered on different Google Maps
* @dependencies	:-
*	nbn.util.ObservableAttribute
*/

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.ArcGisMapFilter = function() { 
	var _observableAttribute = new nbn.util.ObservableAttribute('Filter');//adapts the observable attribute object
	$.extend(this,_observableAttribute); //copy all the observableAttributes methods

	/**This method will return a filter object for a particular zoom level*/
	this.getFilter = function(zoom) { //override the getFilterMethod
		var _filter = _observableAttribute.getFilter();
		if($.isFunction(_filter)) 
			return _filter(zoom); //execute dynamic filter
		else 
			return _filter; //return static filter
	};
		
	this.clearFilter = function() {
		_observableAttribute.setFilter(undefined);
	};
	
	/**This method will determine weather or not this ArcGisMapFilter has a filter set*/
	this.isFiltering = function() {
		return _observableAttribute.getFilter() != undefined;
	};
};
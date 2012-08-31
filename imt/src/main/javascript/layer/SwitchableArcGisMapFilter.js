/**
*
* @author		:- Christopher Johnson
* @date			:- 1st-Feb-2011
* @description	:- This JScript extends the ArcGisFilter by adding the ability to switch the logic on and off
* @dependencies	:-
*	nbn.util.ObservableAttribute
*	nbn.layer.ArcGisMapFilter
*/

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.SwitchableArcGisMapFilter = function(defaultState) {
	var _me = this, underlyingFilterLogic, underlyingFilter;
	$.extend(this,
		new nbn.util.ObservableAttribute('Enabled',defaultState),
		underlyingFilter = new nbn.layer.ArcGisMapFilter()
	);
	
	this.setFilter = function(filterLogic) {
		underlyingFilterLogic = filterLogic;
		if(_me.getEnabled())
			underlyingFilter.setFilter(filterLogic); //pass the logic through if this filter is enabled
	};
	
	this.addEnabledUpdateListener({
		Enabled: function(newValue) {
			if(newValue)
				_me.setFilter(underlyingFilterLogic);
			else
				_me.clearFilter(); //in effect disable filter
		}
	});
};
/**
*
* @author       :- Christopher Johnson
* @date         :- 4-Aug-2011
* @description  :- This JScript represents a BaseLayer which can represent a base layer of the interactive map.
*	It copies the pre constructed nbn layer and adds the additional properties required to be an interactive map base layer
*/
window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};

nbn.layer.BaseLayer = function(layer, options) {
	options = options || {}; //ensure options is constructed 
	
	$.extend(this, layer, 
		new nbn.util.ObservableAttribute('ID',options.id || layer.getName())//simply extend the input layer and add an ID param
	); 
};
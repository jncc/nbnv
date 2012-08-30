/**
*
* @author		:- Christopher Johnson
* @date			:- 7-April-2011
* @description	:- This JScript will resolve the properties of an object to there complete nbn concept.
*	If the property is already complete then it will be returned in a callback without bothering the JSON service
* @dependencies	:-
*	jquery
*/
window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.EntityResolver = new function() {
	this.resolve = function(data, callback) {
		var resolveNeeded = false;
		var dataToResolve = {};
		for(var i in data) {//go through each of the properties and check if they are strings, if they are resolve them using the JSON Name servlet
			if(typeof data[i] === "string") {
				dataToResolve[i] = data[i];
				resolveNeeded = true;
			}
		}
		
		if(resolveNeeded) {
			_previousNameResolvingRequest = $.getJSON('EntityResolver', dataToResolve, function(resolvedData) {
				callback($.extend(data,resolvedData));
			});
		}
		else
			callback(data);
	};
}
/**
*
* @author		:- Christopher Johnson
* @date			:- 20-Jan-2011
* @description	:- This JScript defines the common behaviour for an observable object.
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.Observable = function() {
	var _listeners = [];
	
	this.notifyListeners = function(event) {
		var args = Array.prototype.slice.call(arguments, 1); //remove the first element from the arguments as that is the event name
		for(var i in _listeners) {
			if(_listeners[i][event] != undefined)
				_listeners[i][event].apply(this,args);
		}
	};
	
	this.ObservableMethods = {
		addListener: function(listener) {
			_listeners.push(listener);
		},
		removeListener: function(toRemove) {
			return nbn.util.ArrayTools.removeFrom(toRemove,_listeners);
		}
	};
};
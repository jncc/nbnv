/**
*
* @author		:- Christopher Johnson
* @date			:- 4-Feb-2011
* @description	:- This JScript defines the behaviour for an observable collection whose elements can be observed collectively.
*	Notifications can be added when the underlying array is manipulated and when the underlying elements have been changed 
*	given a list of events to listen to
*
* @usage		:- 
*	$.extend(objectToAddObservableCollectionTo, new nbn.util.PropagatingObservableCollection('Name'));
* @dependencies	:-
*	nbn.util.Observable
*	nbn.util.ObservableCollection
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.PropagatingObservableCollection = function(_type, _eventsToListenTo) {
	var observableCollection = new nbn.util.ObservableCollection(_type);
	$.extend(this, observableCollection); //extend the ObservableCollection
	
	var _observable = new nbn.util.Observable();
	var _eventListener = new function() { //register all the events to a single object
		var _propertiesToAdd = _eventsToListenTo.slice(); //clone the events to add
		_propertiesToAdd.unshift('update'); //add an update property to deal with the common update notification
		for(var i in _propertiesToAdd) {
			this[_propertiesToAdd[i]] = new function() { //create a new function which returns the correct function
				var _currProperty = _propertiesToAdd[i]; //store the current property so that the function I call below has the correct args
				return function() {
					var args = Array.prototype.slice.call(arguments); //convert the arguments object to an array
					args.unshift(_currProperty); //put the argument name at the front of the new arguments array
					_observable.notifyListeners.apply(this,args); //call the notifyListeners with the correct params
				};
			};
		}
	};
	
	var _callEventListenerMethod = function(toCallOn, type) {
		for(var i in _eventsToListenTo)
			toCallOn[type + _eventsToListenTo[i] + 'UpdateListener'](_eventListener);
	};
	
	this['add' + _type] = function(toAdd) {
		observableCollection['add' + _type](toAdd); //store this layer filter
		_callEventListenerMethod(toAdd,'add');
	};
	
	this['remove' + _type] = function(toRemove) {
		var toReturn = observableCollection['remove' + _type](toRemove);
		if(toReturn) 
			_callEventListenerMethod(toRemove,'remove'); //remove update listener
		return toReturn;
	};
	
	this['add' + _type + 'PropagatingObservableCollectionUpdateListener'] = _observable.ObservableMethods.addListener;
	this['remove' + _type + 'PropagatingObservableCollectionUpdateListener'] = _observable.ObservableMethods.removeListener;
};
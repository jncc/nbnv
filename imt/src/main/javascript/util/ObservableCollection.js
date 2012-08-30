/**
*
* @author		:- Christopher Johnson
* @date			:- 4-Feb-2011
* @description	:- This JScript defines the behaviour for an observable collection.
*	Notifications can be added when the underlying array is manipulated
*
* @usage		:- 
*	$.extend(objectToAddObservableCollectionTo, new nbn.util.ObservableCollection('Name'));
* @dependencies	:-
*	nbn.util.Observable
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.ObservableCollection = function(_type) {
	_type = _type || ''; //if no name is specified then this will just be a normal collection
	var _observable = new nbn.util.Observable();
	var _underlyingCollection = [];
	
	var _notify = function(event) { //the maximum number of arguments I will cope with is 3
		var args = Array.prototype.slice.call(arguments, 1); //remove the first element from the arguments as that is the event name
		args.unshift(event, _underlyingCollection);
		_observable.notifyListeners.apply(this,args);//notify of change of underlying collection
		args.unshift('update');
		_observable.notifyListeners.apply(this,args);//call general update 
	};
	
	this['add' + _type] = function(toAdd) {
		_underlyingCollection.push(toAdd); //store this layer filter
		_notify('add', toAdd);
	};
	
	this['remove' + _type] = function(toRemove) {
		var toReturn = nbn.util.ArrayTools.removeFrom(toRemove,_underlyingCollection);
		if(toReturn)
			_notify('remove', toRemove);
		return toReturn;
	};
	
	this['position' + _type] = function(element, position) { //this function will take the element and insert it to a specific position. if it already exists then the original will be removed
		nbn.util.ArrayTools.removeFrom(element, _underlyingCollection); //remove the element if it exists
		_underlyingCollection.splice(position, 0, element);
		_notify('reposition', element, position);
	};
	
	this['forEach' + _type] = function(func) {
		for(var i in _underlyingCollection)
			func(_underlyingCollection[i], i, _underlyingCollection); //call the passed in function for each element
	};
	
	/**	This function will return the underlying array, it should be noted that changes to this array will not cause notifications*/
	this['getUnderlying' + _type + 'Array'] = function() {
		return _underlyingCollection;
	};
	
	this['add' + _type + 'CollectionUpdateListener'] = _observable.ObservableMethods.addListener;
	this['remove' + _type + 'CollectionUpdateListener'] = _observable.ObservableMethods.removeListener;
}
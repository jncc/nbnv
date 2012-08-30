/**
*
* @author		:- Christopher Johnson
* @date			:- 29-July-2011
* @description	:- Elements added to this type of ObservableCollection must be objects with an ObservableAttribute of type "_hashedParam".
* These objects can then be obtained with there current name by using the get<Type> method
*
* @usage		:- 
*	$.extend(objectToAddObservableCollectionTo, new nbn.util.HashedObservableCollection('Name', <_hashedParam>));
* @dependencies	:-
*	nbn.util.Observable
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.HashedObservableCollection = function(_type, _hashedParam) {
	var _resolvableObject = {};
	var _underlyingObservableCollection;
	$.extend(this, _underlyingObservableCollection = new nbn.util.ObservableCollection(_type)); //extend a named ObservableCollection
	
	function _createHashedParamChangeListener(object) {
		var currentName = object['get' + _hashedParam]();
		return function(newName) {
			_resolvableObject[newName] = _resolvableObject[currentName]; //transfer the object
			delete _resolvableObject[currentName]; //remove the old reference
			currentName = newName; //save the current name as old name
		}
	};
	
	this['get' + _type] = function(hashParamValue) {
		return _resolvableObject[hashParamValue].item;
	};
	
	this['has' + _type] = function(hashParamValue) {
		return _resolvableObject[hashParamValue] != undefined;
	};
	
	this['add' + _type] = function(toAdd) {
		var itemToStore = _resolvableObject[toAdd['get' + _hashedParam]()] = {
			item: toAdd,
			listener: new (function() {
				this[_hashedParam] = _createHashedParamChangeListener(toAdd)
			})
		};
		toAdd['add' + _hashedParam + 'UpdateListener'](itemToStore.listener); //create a listener to see when the name changes
		_underlyingObservableCollection['add' + _type].apply(this, arguments);
	};
	
	this['remove' + _type] = function(toRemove) {
		var internalToRemove = _resolvableObject[toRemove['get' + _hashedParam]()]; //get the element to remove from the internal store
		toRemove['remove' + _hashedParam + 'UpdateListener'](toRemove.listener); //remove the listener
		delete internalToRemove; //remove the reference
		_underlyingObservableCollection['remove' + _type].apply(this, arguments);
	};
}
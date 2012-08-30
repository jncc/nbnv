/**
*
* @author		:- Christopher Johnson
* @date			:- 2-Feb-2011
* @description	:- This JScript defines the behaviour for an observable attribute.
*	It is intended that instances of this object are extendees for objects which want a specific attribute.
*
* @usage		:- 
*	$.extend(objectToAddObservableAttributeTo, new nbn.util.ObservableAttribute('Name', 'initial Name'));
*	This will provide objectToAddObservableAttributeTo with methods:
*		getName();
*		setName();
*		addNameUpdatedListener();
*		removeNameUpdatedListener();
* @dependencies	:-
*	nbn.util.Observable
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.ObservableAttribute = function(_type, _content, _equalityFunction) {
	_type = _type || ''; //if no type is provided, this will just be a normal attribute
	_equalityFunction = _equalityFunction || function() { //if not equality function is provided, provide one which always returns false
		return false;
	};
	var _observable = new nbn.util.Observable();
	
	this['get' + _type] = function() {
		return _content;
	}
	
	this['set' + _type] = function(contentToSet) {
		if(!(_content == contentToSet || _equalityFunction(_content, contentToSet))) {
			_content = contentToSet;
			_observable.notifyListeners(_type,contentToSet); //do a segmented call
			_observable.notifyListeners('update',_type,contentToSet); //do a default update call, so that can be consolidated into a single function
		}
	}

	this['add' + _type + 'UpdateListener'] = _observable.ObservableMethods.addListener;
	this['remove' + _type + 'UpdateListener'] = _observable.ObservableMethods.removeListener;
}
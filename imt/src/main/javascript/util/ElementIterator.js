/**
*
* @author		:- Christopher Johnson
* @date			:- 27-Apr-2011
* @description	:- This JScript extends the ObservableAttribute and adds the method getTypeNextElement.
*	The content of this object may or may not be an array. If it is not an array, the getTypeNextElement 
*	method will always return the content of the ObservableAttribute.
* @dependencies	:-
*	nbn.util.ObservableAttribute
*	nbn.util.ArrayTools
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.ElementIterator = function(_type, _content) {
	var _currElement = 0;
	$.extend(this, new nbn.util.ObservableAttribute(_type, _content, nbn.util.ArrayTools.equals));
	
	this['get' + _type + 'NextElement'] = function() {//override the get method of the nbn.util.ObservableAttribute
		var content = this['get' + _type](); //call super
		if($.isArray(content)) { //if the content is an array then use it as is
			_currElement = (_currElement < content.length) ? _currElement : 0; //loop if nessersary
			return content[_currElement++]; //iterate return the curr element
		}
		else
			return content; //a single element has been detected, use this
	};
}
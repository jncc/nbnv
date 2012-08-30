/**
*
* @author		:- Christopher Johnson
* @date			:- 25-Feb-2011
* @description	:- This JScript will extend the capabilites of Jquery and create a function 
* that enables an element to be measured before it is added to the DOM
*
* @usage		:- Just import
* @dependencies	:-
*	jQuery
*/
(function($, undefined) {
	var _createToReturn = function(element) {
		return { //store all the values that I want to return
			width: element.width(),
			height: element.height(),
			outerWidth : element.outerWidth(),
			outerHeight: element.outerHeight(),
			position: element.position()
		};
	};
	
	$.fn.measure = function(constraints) {
		var element = $(this[0]).clone(false); //clone the element I shall perform this method on
		constraints = constraints || {};
		element.height(constraints.height || 'auto');
		element.width(constraints.width || 'auto');
		
		var body = $('body'), hiddenElement = $('<div>')
			.css({
				position: 'absolute',
				top: 0,
				left: -body.width() //draw the element off screen
			})
			.append(element); //add the cloned element

		if(this.is(':attached')) //is this element attached to the dom
			$(this).before(hiddenElement); //attach before to retain stylings
		else
			body.append(hiddenElement); //just render offscreen. Results may be approximate
		
		var toReturn = _createToReturn(element);
		hiddenElement.remove(); //remove the temp element and therefore the cloned element
		return toReturn;
	}
})(jQuery);
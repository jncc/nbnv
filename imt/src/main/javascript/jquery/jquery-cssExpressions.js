/**
*
* @author		:- Christopher Johnson
* @date			:- 5th-May-2011
* @description	:- This JScript enables CSS properties to be set with functions, these are then continually called and set.
*	It should be noted that this jQuery plugin was specifically written to support css properties which were missing from old browsers.
*	Therefore this plugin should only be used if it is not possible or deemed incorrect to hook into the necessary events which 
*	are required to support, your dynamic css property.
*/
(function($, undefined) {
	$.fn.cssExpression = function(property, expression) {
		$(this).each(function() {
			var _me = $(this);
			var runningExpressions = _me.data('runningExpressions') || {};
			if(runningExpressions[property])//check if an expression is already running for this property
				clearInterval(runningExpressions[property]); //stop it
			_me.css(property, expression.call(_me[0])); //run once
			runningExpressions[property] = setInterval(function() { //set up a function to loop around
				_me.css(property, expression.call(_me[0]));
			},1000);
			_me.data('runningExpressions', runningExpressions); //store the expressions object
		});
	};
})(jQuery);
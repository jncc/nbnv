/**
*
* @author		:- Christopher Johnson
* @date			:- 5th-May-2011
* @description	:- This JScript will add support for the css max height value when the browser this 
*	plugin is running is does not support them.
* @dependencies	:-
*	jquery-cssExpressions
*/
(function($, undefined) {
	$(document).ready(function() {
		var toCheck = $('<div>').css({position: 'absolute',width:10,height:10, maxHeight:5}).appendTo($('body'));
		$.support.maxHeight = toCheck.height() === 5; //perform the support check
		toCheck.remove(); //remove the unwanted container
		
		if(!$.support.maxHeight) {
			$.cssHooks.maxHeight = {
				set: function(elem, value){
					value = parseInt(value);
					$(elem).cssExpression('height', function() {
						return this.scrollHeight > value ? value : 'auto';
					});
				}
			};
			
			$.fx.step.maxHeight = function(fx) { //fix for animate too
				$.cssHooks.maxHeight.set(fx.elem, fx.now + fx.unit);
			};
		}
	});
})(jQuery);
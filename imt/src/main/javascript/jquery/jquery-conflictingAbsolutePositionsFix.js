/**
*
* @author		:- Christopher Johnson
* @date			:- 5th-May-2011
* @description	:- This JScript will add support for Conflicting Absolute positions in browsers which do not support them.
*	An example of this is when an element has been styled without a height but does have top and bottom positions.
*	Specifically written to enable support for IE6, this fix may work for other browsers which do not support conflicting absolute positions
*	however this is untested at the time of writting.
* @dependencies	:-
*	jquery-cssExpressions
*/
(function($, undefined) {
	$(document).ready(function() {
		var container = $('<div>').css({position: 'absolute',width:10,height:10}).appendTo($('body'));
		var toCheck = $('<div>').css({position: 'absolute',top:0,bottom:0,width:10}).appendTo(container);
		$.support.conflictingAbsolutePositions = toCheck.height() === 10; //perform the support check
		container.remove(); //remove the unwanted container
		
		if(!$.support.conflictingAbsolutePositions) {
			function _hasValueSet(value) {
				return value && value.length > 0 && value !== 'auto';
			};
			
			function _createCSSConflictHookFix(fixObj) {
				$.each(fixObj.conflictProperties, function(index, toFixValue){
					$.cssHooks[toFixValue] = {
						set: function(elem, value){
							elem.style[toFixValue] = value; //set the value initially
							var _me = $(elem);
							for(var i in fixObj.conflictProperties) {
								if(!_hasValueSet(_me.css(fixObj.conflictProperties[i]))) //if no value is set, there is no need to fix
									return;
							}
							fixObj.fix(_me);//failed to break out of loop, I am in a conflicted state which needs to be fixed
						}
					};
					$.fx.step[toFixValue] = function(fx) { //fix for animate too
						$.cssHooks[toFixValue].set(fx.elem, fx.now + fx.unit);
					};
				});
			};

			_createCSSConflictHookFix({
				conflictProperties: ['top', 'bottom'],
				fix: function(_me) {
					_me.cssExpression('height', function() {
						return this.parentElement.offsetHeight - parseInt(_me.css('top')) - parseInt(_me.css('bottom'));
					});
				}
			});
			
			_createCSSConflictHookFix({
				conflictProperties: ['left', 'right'],
				fix: function(_me) {
					_me.cssExpression('width', function() {
						return this.parentElement.offsetWidth - parseInt(_me.css('left')) - parseInt(_me.css('right'));
					});
				}
			});
		}
	});
})(jQuery);
/**
*
* @author		:- Christopher Johnson
* @date			:- 21-Feb-2011
* @description	:- This JScript will work out weather or not an element is attached to the dom
* @usage		:- Just import
* @dependencies	:-
*	jquery
*/
(function( $, undefined ) {
	var _isAttached = function(_me) {
		return $.contains(document.documentElement, _me);
	};
	
	$.extend( $.expr[':'], {
		attached: _isAttached,
		detached: function(_me){ 
			return !_isAttached( _me ); 
		}
	});
})(jQuery);
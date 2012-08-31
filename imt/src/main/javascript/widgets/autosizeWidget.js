/**
*
* @author		:- Christopher Johnson
* @date			:- 21-Feb-2011
* @description	:- This JScript defines an auto resizable widget. This implementation will only resize in the vertical component
* @usage		:- 
* @dependencies	:-
*	jquery
*	jquery.ui
*	jquery-measure
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_autosize", {
		options: {
			animationDuration: 'fast'
		},
		
		_create: function() {
			this.resize();
		},
		
		resize: function() {
			/*var _measuredElement = this.element.measure({width: this.element.width()});
			this.element.stop(true, false).animate({
				height: _measuredElement.height
			}, (this.element.is(':attached')) ? this.options.animationDuration : 0, function() {
				$(this).css({ //once complete remove the explicit height value
					height: 'auto'
				});
			});*/
		},
		
		destroy: function() {
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
    });

    $.extend( $.ui.nbn_autosize, {
		version: "@VERSION"
    });

})( jQuery );
/**
*
* @author	    :- Christopher Johnson
* @date		    :- 15-Feb-2011
* @description	:- This widget represents a bar of buttons, named by the properties of the passed in buttons option.
*	The functions which are performed on button presses are the values of the given property.
* @dependencies	:-
*	Widget.css
*	jquery 1.4.4
*	jquery ui-1.8.8
*/

(function( $, undefined ) {
	$.widget( "ui.nbn_buttonbar", {
		options: {
			buttons: {}
		},

		_create: function() {
			this.element
				.append(this._buttonSet = $('<div>')
					.addClass('nbn-buttonBar-buttonSet') //set the initial state as the current
				)
			.addClass('nbn-buttonBar ui-widget-content ui-helper-clearfix');
			for(var i in this.options.buttons) {
				this._buttonSet.append($('<button>')
					.button({
						label: i //set the button name
					})
					.click(this.options.buttons[i])
				);
			}
		},

		destroy: function() {
			this._buttonSet.remove();
			this.element.removeClass('nbn-buttonBar ui-widget-content ui-helper-clearfix');
			$.Widget.prototype.destroy.apply( this, arguments );
		}
	});

	$.extend( $.ui.nbn_buttonbar, {
		version: "@VERSION"
	});

})( jQuery );
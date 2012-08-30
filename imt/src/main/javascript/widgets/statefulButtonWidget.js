/**
*
* @author	    :- Christopher Johnson
* @date		    :- 15-Feb-2011
* @description	:- This widget represents a button which is based on a jquery ui icon,
* 
* Tooltips, click operations and icons can be defined per state
* @dependencies	:-
*	Widget.css
*	jquery 1.4.4
*	jquery ui-1.8.8
*/

(function( $, undefined ) {
	$.widget( "ui.nbn_statefulbutton", {
		options: {
			initialState: 'default',
			states: { //defines the states along with there respective icon
				"default" : {
					icon: "ui-icon-close"
				}
			}
		},

		_create: function() {
			this.element
				.append(this._icon = $('<span>')
					.addClass('ui-icon') //set the initial state as the current
				)
				.addClass('ui-corner-all nbn-statefulButton');
			this._setNewState(this.options.initialState);

			this.element.hover(function() {
				$(this).toggleClass('ui-state-hover');
			});
			var _me = this;
			this.element.click(function() {
			if(_me._currentState.click != undefined)
				_me._currentState.click.apply(_me.element);
			});
		},

		setState: function(newState) {
			this._icon.removeClass(this._currentState.icon);
			this._setNewState(newState);
			this._trigger('statechange',newState);
		},

		_setNewState : function(newState) {
			this._currentState = this.options.states[newState]; //set the new current state
			this._icon.addClass(this._currentState.icon); //set the correct icon
			if(this._currentState.tooltip)
				this._icon.attr('title',this._currentState.tooltip); //set the title
			else
				this._icon.removeAttr('title'); //no title to set
		},

		destroy: function() {
			this._icon.remove();
			$.Widget.prototype.destroy.apply( this, arguments );
		}
	});

	$.extend( $.ui.nbn_statefulbutton, {
		version: "@VERSION"
	});

})( jQuery );
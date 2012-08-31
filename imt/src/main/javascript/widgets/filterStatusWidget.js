/**
*
* @author		:- Christopher Johnson
* @date			:- 2-Feb-2011
* @description	:- This JQuery Ui Widget will render the textual description of a dynamic or static filter. 
* @dependencies	:-
*	jquery
*	jquery.ui
* @usage 		:-
*	$('<div>').nbn_filterStatus({filter: FilterToRender});
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_filterStatus", {
		options: {
			statusFunction : function() {
				return '';
			},
			activeFilteringLabel: "",
			inactiveFilteringLabel: ""
		},
		
		_updateLabels: function() {
			var filter = this.options.layerFilter; //get the filter
			if(filter.isFiltering()) {
				this._label.html(this.options.activeFilteringLabel);
				this._status.html(this.options.statusFunction.call(filter.getFilter())); //if no description, remove
			}
			else {
				this._label.html(this.options.inactiveFilteringLabel);
				this._status.html('');
			}
		},
		
		_create: function() {
			var _me = this;
			this.element
				.append(this._label = $('<span>'))
				.append(this._status = $('<span>'));
			this._updateLabels(); //update the filter the first time
			this._updateListener = {
				update: function() {
					_me._updateLabels();
				}
			}
			this.options.layerFilter.addFilterUpdateListener(this._updateListener);
		},

		destroy: function() {
			this._label.remove();
			this._status.remove();
			this.options.layerFilter.removeFilterUpdateListener(this._updateListener);
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
    });

    $.extend( $.ui.nbn_filterStatus, {
		version: "@VERSION"
    });

})( jQuery );

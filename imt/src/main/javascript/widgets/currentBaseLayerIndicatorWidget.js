/**
*
* @author		:- Christopher Johnson
* @date			:- 22-August-2011
* @description	:- This JScript creates a graphical base layer icon, which represents a maps current view
* @usage		:- $('<div>').nbn_currentBaseLayerIndicator({map:map});
* @dependencies	:-
*	nbn_baseLayerIcon widget
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_currentBaseLayerIndicator", {
		_create: function() {
			var _me = this;
			this.options.map.addBaseLayerUpdateListener(this._baseLayerUpdateObj = {
				BaseLayer: function(newIcon) {_me.element.nbn_baseLayerIcon('setBaseLayer', newIcon);}
			});
			this.element.nbn_baseLayerIcon({baseLayer: this.options.map.getBaseLayer()}); //set the baseLayer in it's initial state
		},
		
		destroy: function() {
			this.options.map.removeBaseLayerUpdateListener(this._baseLayerUpdateObj);
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
	});
	
	$.extend( $.ui.nbn_currentBaseLayerIndicator, {
		version: "@VERSION"
    });

})( jQuery );

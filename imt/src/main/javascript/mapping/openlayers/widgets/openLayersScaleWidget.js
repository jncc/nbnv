/**
*
* @author		:- Christopher Johnson
* @date			:- 28-July-2011
* @description	:- This JScript creates a JQuery widget styled Openlayers scale bar
* @usage		:- $('<div>').nbn_openLayersScaleWidget({openLayersMap:openLayersMap});
* @dependencies	:-
*	jquery
*	jquery.ui
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_openLayersScaleWidget", {
		_create: function() {
			this._scale = new OpenLayers.Control.ScaleLine({geodesic:true}); //create a scale
			this.options.openLayersMap.addControl(this._scale); //add the scale to the map
			this.element
				.append($(this._scale.div)) //switch this widgets element to the scale div
				.addClass('ui-widget ui-widget-content ui-corner-all nbn-openLayersScaleWidget');
		},
		
		destroy: function() {
			this.element.removeClass('ui-widget ui-widget-content ui-corner-all nbn-openLayersScaleWidget');
			this.options.openLayersMap.removeControl(this._scale);
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
    });

    $.extend( $.ui.nbn_openLayersScaleWidget, {
		version: "@VERSION"
    });

})( jQuery );
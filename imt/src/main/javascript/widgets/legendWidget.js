/**
*
* @author		:- Christopher Johnson
* @date			:- 17-Feb-2011
* @description	:- This JScript defines the legend widget
*
* @usage		:- 
*	$('<div>').nbn_legend({layer: new nbn.layer.ArcGISMap()});
* @dependencies	:-
*	jquery
*	jquery.ui
*	nbn.layer.ArcGISMap
*	nbn.util.DataURIShemeSupport
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_legend", {
		options: {
			titleLayer:true,
			animationDuration: 'fast'
		},
		
		_renderLegend : function() {
                        var _me = this;
                        _me._legendDiv.empty(); //remove any old legend details
                        if(_me.options.layer.getMapService()) {
                            $.each(_me.options.layer.getLegend(), function(i, item) {
                                _me._legendDiv.append($('<img>').attr('src', item));
                            });
                        }
                        _me._trigger('contentchange');
		},
		
		_create: function() {
			var _me = this;
			this._updateObject = {
				update: function() {
					_me._renderLegend();
				}
			};
			
			this.element
				.addClass('nbn-legend')
				.append(this._legendDiv = $('<div>'));
			this.options.layer.addCurrentVisibleLayersUpdateListener(this._updateObject);
			this.options.layer.addMapServiceUpdateListener(this._updateObject);
			this._renderLegend(); //do an initial render
		},

		destroy: function() {
			this.options.layer.removeMapServiceUpdateListener(this._updateObject);
			this.options.layer.removeCurrentVisibleLayersUpdateListener(this._updateObject);
			this._legendDiv.remove();
			this.element.removeClass('nbn-legend');
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
    });

    $.extend( $.ui.nbn_legend, {
		version: "@VERSION"
    });

})( jQuery );
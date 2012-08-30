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
		
		_createLayerLegendList : function(layer) {
			var _me = this; //store a reference to me
			var _legendList = $('<ul>').addClass('nbn-legend-list');
			$.each(layer.legend, function(i, entry) {
				var toAdd = $('<li>')
					.append($('<img>')
						.attr('src', ($.support.imageUri) //is image uri supported
							? 'data:' + entry.contentType + ';base64,' + entry.imageData //use uri
							: _me.options.layer.getHostsNextElement() + _me.options.layer.getMapService() + '/' + layer.layerId + '/images/' + entry.url //use image url
						)
						.addClass('legend-layer-item-image')
					)
					.append($('<a>')
						.addClass('legend-layer-item-name')
						.html((layer.legend.length!=1) ? entry.label : layer.layerName)
					)
					.addClass('legend-layer-item')
				toAdd.attr("description", (layer.legend.length!=1) ? entry.description : layer.description);
				_legendList.append(toAdd);
			});
			return _legendList;
		},
		
		_createLayerLegend: function(layer) {
			var _layerDiv = $('<div>').addClass('nbn-legend-layer');
			if(this.options.titleLayer && layer.legend.length!=1) {
				_layerDiv.append($('<p>')
					.addClass('nbn-legend-layer-title')
					.html(layer.layerName)
				);
			}
			var legendList = this._createLayerLegendList(layer); //create the legend
			var subLegendCount = Math.floor(150/legendList.measure().width); //how many sub legends should there be
			return _layerDiv.append(legendList.split(subLegendCount).width(Math.ceil(200/subLegendCount))); //split it
		},
		
		_renderLegend : function() {
			var _me = this; //store a reference to me
			_me._legendDiv.stop(true, false).fadeTo(_me.options.animationDuration, 0, function() {
				if(_me._lastLegendCall)
					_me._lastLegendCall.abort(); //kill old requests
				if(_me.options.layer.getMapService()) {
					_me._lastLegendCall = _me.options.layer.getLegend(function(json) { //perform an async call
						_me._legendDiv.empty(); //remove any old legend details
						$.each(json.layers,	function(i, item) {
							_me._legendDiv.append(_me._createLayerLegend(item));
						});
						$('.legend-layer-item[description]',_me._legendDiv).tipTip({attribute: "description"});
						_me._legendDiv.stop(true, false).fadeTo(_me.options.animationDuration, 1); //fade the legend back in
						_me._trigger('contentchange');
					});
				}
				else { //no map, remove legend
					_me._legendDiv.empty(); //remove any old legend details
					_me._trigger('contentchange');
				}
			});
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
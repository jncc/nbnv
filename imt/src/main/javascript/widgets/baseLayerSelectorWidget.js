/**
*
* @author		:- Christopher Johnson
* @date			:- 22-August-2011
* @description	:- This JScript creates an icon which represents a given base layer in the NBN Mapping framework
* @usage		:- $('<div>').nbn_baseLayerControllerWidget({map:map});
* @dependencies	:-
*	nbn_baseLayerIcon
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_baseLayerSelector", {
		options : {
			selectionPaneWidth: 410
		},
		
		_create: function() {
			var _me = this;
			this.element
				.addClass('nbn-baseLayerSelector ui-corner-all')
				.append(this._baseLayerButton = $('<div>').nbn_currentBaseLayerIndicator({map: this.options.map}).addClass('ui-state-default')) //create a base layer icon for this selectors map. Give it a default state
				.append(this._selectionPane = $('<div>').hide().addClass('nbn-baseLayerSelectorSelectionPane ui-widget ui-widget-content ui-corner-all') //create the baseLayer selection pane, add classes and hide
					.append(this._title = $('<div>').addClass('nbn-baseLayerSelectorTitle').html('Available Layers'))
					.append(this._baseLayerTypes = $('<div>').addClass('nbn-baseLayerSelectorTypes ui-helper-clearfix'))
				);
			
			/*Decide when the selection pane should become visible*/
			this.element.hover(
				function() { _me._selectionPane.show().stop(true, false).animate({width: _me.options.selectionPaneWidth});},
				function() { _me._selectionPane.stop(true, false).animate({width: 0}, function(){_me._selectionPane.hide()});}
			);
			
			this._redrawListener = { update: function() {_me._render();	}};
			this.options.map.addBaseLayerTypeCollectionUpdateListener(this._redrawListener);
			this.options.map.addBaseLayerUpdateListener(this._redrawListener);
			this._render(); //do an initial draw		
		},
		
		_render: function() {
			var _me = this;
			_me._baseLayerTypes.empty(); //clear the pane
			$.each(_me.options.map.getUnderlyingBaseLayerTypeArray(), function(index, currLayer) { //iterate around the layer types
				var isBaseLayer = currLayer === _me.options.map.getBaseLayer();
				var toAdd = $('<div>')
					.nbn_baseLayerIcon({baseLayer: currLayer, active: isBaseLayer, partOfButtonSet: true})
					.click(function() {_me.options.map.setBaseLayer(currLayer); }); //set the new base layer
				_me._baseLayerTypes.append(toAdd);
			});
		},
		
		destroy: function() {
			this.element.removeClass('nbn-baseLayerSelector ui-corner-all'); //remove class
			this._baseLayerButton.remove(); //remove elements
			this._selectionPane.remove();
			this.options.map.removeBaseLayerTypeCollectionUpdateListener(this._redrawListener);//remove listeners
			this.options.map.removeBaseLayerUpdateListener(this._redrawListener);
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
	});
	
	$.extend( $.ui.nbn_baseLayerSelector, {
		version: "@VERSION"
    });

})( jQuery );
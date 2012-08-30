/**
*
* @author		:- Christopher Johnson
* @date			:- 22-August-2011
* @description	:- This JScript creates an icon which represents a given base layer in the NBN Mapping framework
* @usage		:- $('<div>').nbn_baseLayerControllerWidget({map:map});
* @dependencies	:-
*	nbn_icon widget
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_baseLayerIcon", {
		_create: function() {
			var _icon = this.element;		
			this._nameAndIDListener = {
				ID: function(newID) 	{ _icon.nbn_icon('setIcon',newID);},
				Name: function(newName) {_icon.nbn_icon('setLabel',newName);}
			};
			this.element.nbn_icon(this._addListenersAndCreateContent());
		},
		
		/*The following function allows a base layer to be changed*/
		setBaseLayer: function(baseLayer) {
			this._removeListeners(); //remove existing listeners;
			this.options.baseLayer = baseLayer; //update the baselayer
			this.element.nbn_icon('setContent',this._addListenersAndCreateContent()); //addListeners and set content
		},
		
		_addListenersAndCreateContent: function() {
			this.options.baseLayer.addNameUpdateListener(this._nameAndIDListener);
			this.options.baseLayer.addIDUpdateListener(this._nameAndIDListener);
			return $.extend({
				icon: this.options.baseLayer.getID(),
				label: this.options.baseLayer.getName()
			},this.options);
		},
		
		_removeListeners: function() {
			this.options.baseLayer.removeNameUpdateListener(this._nameAndIDListener);
			this.options.baseLayer.removeIDUpdateListener(this._nameAndIDListener);
		},
		
		destroy: function() {
			this._removeListeners();
			this.element.nbn_icon('destroy');
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
	});
	
	$.extend( $.ui.nbn_baseLayerIcon, {
		version: "@VERSION"
    });

})( jQuery );
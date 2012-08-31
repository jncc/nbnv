/**
*
* @author	    :- Christopher Johnson
* @date		    :- 19-Nov-2010
* @description	:- This JScript defines the widget which contains the copyright statements of the NBN interactive mapper.
* @usage		:- $('<div>').nbn_copyrightController({map:interactiveMapper.Map});
* @dependencies	:-
*   jquery.ui.core.js
*   jquery.ui.widget.js
*/
(function( $, undefined ) {
$.widget( "ui.nbn_copyrightController", {
		options: {
			globalCopyright: '&copy; NBN ' + nbn.util.ServerGeneratedLoadTimeConstants.date.year + ' - <a href="http://data.nbn.org.uk/help/popups/generalTerms.jsp" target="_blank">Terms & Conditions</a>'
		},
		
		_create: function() {
			var _me = this;
			this.element.addClass( "nbn-copyrightControl ui-widget ui-widget-content" ); //set the class of the container to an nbn-copyright controller

			function _updateCopyright(type){
				var copyrightText = _me.options.globalCopyright;
				var baseLayerCopyrightText = type.getCopyright();
				if(baseLayerCopyrightText) //is there an extra copyright statement on the base layer?
					copyrightText += ' ' + baseLayerCopyrightText; //append it
				_me.element.html(copyrightText);
			};

			_updateCopyright(this.options.map.Map.getBaseLayer()); //set the copyright text the first time
			this._updateCopyrightListener = {BaseLayer:_updateCopyright};
			this.options.map.Map.addBaseLayerUpdateListener(this._updateCopyrightListener);
		},

		destroy: function() {
			this.element.removeClass( "nbn-copyrightControl ui-widget ui-widget-content"); //remove the appended class
			this.options.map.Map.removeBaseLayerTypeListener(this._updateCopyrightListener);
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
	});

	$.extend( $.ui.nbn_copyrightController, {
		version: "@VERSION"
	});

})( jQuery );
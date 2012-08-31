/**
*
* @author	    :- Christopher Johnson
* @date		    :- 
* @description	:- This is a helper widget which creates the dom structure nessersary for creating a tabs widget
* @dependencies	:- jQuery
*	nbn.util.IDTools
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_dynamictabs", {			
			_create: function() {
				this.element
					.append(this._tabs = $('<ul>'))
					.append(this._contents = $('<div>'))
			},
			
			_createTab: function(label, id) {
				var tabTitle = $('<a>').attr('href','#' + id);
				if(typeof label == "string")
					tabTitle.html(label);
				else
					tabTitle.nbn_label({label: label});
				this._tabs.prepend($('<li>').append(tabTitle));
			},
			
			add: function(label, elementToAdd) {
				var id = 'dynamic-tabs-' + nbn.util.IDTools.generateUniqueID();
				elementToAdd
					.attr('id', id)
					.appendTo(this._contents);
				this._createTab(label, id);
			},
		
			destroy: function() {
				this.element.tabs('destroy');
				$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
			}
		});

    $.extend( $.ui.nbn_dynamictabs, {
		version: "@VERSION"
    });

})( jQuery );
/**
*
* @author		:- Christopher Johnson
* @date			:- 22-August-2011
* @description	:- This JScript creates an NBN Icon widget
* @usage		:- $('<div>').nbn_icon({icon: 'icontype', label: 'label'});
* @dependencies	:-
*	jquery
*	jquery.ui
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_icon", {
		_makeElementAButton: function() {
			if(this.options.active)
				this.element.addClass('ui-state-default');
			else
				this.element.hover(function() {$(this).toggleClass('ui-state-hover');});
				
			if(!this.options.partOfButtonSet) {
				function removeActiveClass() {$(this).removeClass('ui-state-active');}
				this.element.mouseup(removeActiveClass);
				this.element.mouseleave(removeActiveClass);
				this.element.mousedown(function(){$(this).addClass('ui-state-active');});
			}
			this.element.disableSelection();
		},
		
		_create: function() {	
			this.element.addClass('nbn-icon ui-widget ui-widget-content ui-corner-all');
			this.element.append(this._backgroundElement = $('<div>').addClass('nbn-iconImage ui-corner-top'));
			this.element.append(this._iconLabel = $('<div>').addClass('nbn-iconLabel ui-corner-bottom'));
			this.setContent(this.options);
			this._makeElementAButton();
		},
		
		setContent: function(content){
			this.setIcon(content.icon);
			this.setLabel(content.label);
		},
		
		setIcon: function(iconCSS) {
			if(this._currentIconCSS)
				this._backgroundElement.removeClass(this._currentIconCSS);
			this._backgroundElement.addClass(this._currentIconCSS = iconCSS);
		},
		
		setLabel: function(label) {
			this._iconLabel.html(label);
		},
		
		destroy: function() {
			this._backgroundElement.remove(); //remove elements
			this._iconLabel.remove();
			this.element.removeClass('nbn-icon ui-widget ui-widget-content ui-corner-all'); //remove classes
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
    });

    $.extend( $.ui.nbn_icon, {
		version: "@VERSION"
    });

})( jQuery );
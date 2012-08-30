/**
*
* @author	    :- Christopher Johnson
* @date		    :- 21-Feb-2011
* @description	:- This widget will listen to changes of a label of a specific type and render them on change
* @dependencies	:- 
*	nbn_contentListener
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_label", {
			options: {
				type: 'Name' //the default label to render is name
			},
			
			_create: function() {
				this.element.nbn_contentListener({
					type: this.options.type,
					content: this.options.label,
					updateFunction: function(newName) {$(this).html(newName);}//define function which updates label
				});
			},
		
			destroy: function() {
				this.element.nbn_contentListener('destroy');
				$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
			}
		});

    $.extend( $.ui.nbn_label, {
		version: "@VERSION"
    });

})( jQuery );
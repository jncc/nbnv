/**
*
* @author	    :- Christopher Johnson
* @date		    :- 22-August-2011
* @description	:- This widget will listen to changes of a particular observable attribute and execute the specified 
*	update function in the context of the dom element which is going to be updated.
* @dependencies	:- 
*	nbn.util.ObservableAttribute
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_contentListener", {
			_create: function() {
				var _me = this;
				var _updateFunction = function(newName) {
					_me.options.updateFunction.call(_me.element,newName);
					_me._trigger('contentchange');
				};
				this.options.content['add' + this.options.type + 'UpdateListener'](this._updateObject = new function(){
					this[_me.options.type] = _updateFunction;
				});
				_updateFunction(this.options.content['get' + this.options.type]());
			},
		
			destroy: function() {
				this.options.content['remove' + this.options.type + 'UpdateListener'](this._updateObject);
				$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
			}
		});

    $.extend( $.ui.nbn_contentListener, {
		version: "@VERSION"
    });

})( jQuery );
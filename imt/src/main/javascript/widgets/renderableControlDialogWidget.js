/**
*
* @author		:- Christopher Johnson
* @date			:- 22-March-2011
* @description	:- This JQuery Ui Widget will render and allow interaction with an renderableControl object
* @dependencies	:-
*	jquery
*	jquery.ui 1.8.10
* @usage 		:-
*	$('<div>').nbn_renderableControlDialog({renderableControl: FilterToRender});
*/

(function( $, undefined ) {

    $.widget( "ui.nbn_renderableControlDialog", {
		options: {
			resetButtonText: 'Reset',
			applyButtonText: 'Ok',
			title: 'Renderable Control Dialog',
			resizable: false,
			width: 500,
			autoOpen:false,
			modal: true
		},
		
		_createDialogOptions: function() {
			var _me = this;
			var dialogOptionsToReturn = {};
			$.extend(dialogOptionsToReturn, this.options, {
				buttons: new function() {
					this[_me.options.resetButtonText] = function() {
						_me.options.renderableControl.reset();
					};
					
					this[_me.options.applyButtonText] = function() {
						_me.options.renderableControl.apply();
						_me._backTrackableState = undefined;
						_me.close();
					};
				},
				open: function() {
					_me._backTrackableState = _me.options.renderableControl.getState(); //save the state of the representation when the dialog opens
				},
				close: function() {
					if(_me._backTrackableState)
						_me.options.renderableControl.setState(_me._backTrackableState);
				}
			});
			return dialogOptionsToReturn;
		},
		
		/*
			WARNING- THIS FUNCTION USES KNOWLEDGE OF THE JQUERY UI DIALOG IN ORDER TO GET A HANDLE ON THE BUTTONS SO THAT THEY CAN BE MANIPULATED
			THIS MAKES THIS WIDGET VERY MUCH TIED TO VERSION 1.8.10 OF JQUERY UI
		*/
		_buildReferencesToButtons: function () {
			var _me = this;
			var buttons = $('.ui-dialog-buttonset button',this.element.parent());
			this._buttons = {};
			$.each(buttons, function(index,elem) {
				_me._buttons[$('.ui-button-text', elem).html()] = $(elem);
			});
		},
		
		_create: function() {
			var _me = this;
			this.element
				.append(this._content = this.options.renderableControl.getRepresentation())
				.dialog(this._createDialogOptions());
			this._buildReferencesToButtons();

			var _renderableUpdateFunc = function(value) {
				_me._buttons[_me.options.applyButtonText].button((value) ? 'enable' : 'disable');
			};	
			
			_renderableUpdateFunc(this.options.renderableControl.getRenderable());		
			this.options.renderableControl.addRenderableUpdateListener(this._renderableUpdateListener = {
				Renderable: _renderableUpdateFunc
			});
		},
		
		open: function() {
			this.element.dialog('open');
		},
		
		close: function() {
			this.element.dialog('close');
		},

		destroy: function() {
			this._content.remove();
			this.options.renderableControl.removeRenderableUpdateListener(this._renderableUpdateListener);
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
    });

    $.extend( $.ui.nbn_renderableControlDialog, {
		version: "@VERSION"
    });

})( jQuery );
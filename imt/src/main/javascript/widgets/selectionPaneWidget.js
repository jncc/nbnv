/**
*
* @author		:- Christopher Johnson
* @date			:- 23-March-2011
* @description	:- This JQuery Ui Widget will render a box selection pane window
* @dependencies	:-
*	jquery
*	jquery.ui
* @usage 		:-
*/
(function( $, undefined ) {
	$.widget( "ui.nbn_selectionPane", {
		options: {
			label: 'Choose an option',
			selectables:{}
		},
		
		_create: function() {
			var _me = this;//store reference to me
			
			this.element
				.append(this._selectionBox = $('<span>')
					.append(this.options.label)
					.append(this._selectionDropDown = $('<select>'))
				)
				.append(this._selectionContentArea = $('<div>'))
				.addClass("nbn-selectionPane");
			
			for(var i in this.options.selectables) { //add all the selectable items
				this._selectionDropDown.append($('<option>')
					.attr('value',i)
					.html(i)
				);
			}
			
			this._selectionDropDown.change(function() {
				if(_me._currentlySelected && _me._currentlySelected.content) //if currently selected and has content, then remove it from the selection pane
					_me._currentlySelected.content.detach();
				
				var currVal = $(this).val();
				var currSelected = _me._currentlySelected = _me.options.selectables[currVal]; //set the newly selected element

				var selectedFunc = currSelected.selected;
				if($.isFunction(selectedFunc))
					selectedFunc.call(_me.element);
				
				if(currSelected.content) //if this one has content, then append it to the selection pane
					_me._selectionContentArea.append(currSelected.content);
				_me._trigger('selectionchange',currVal);
			});
			
			this.setSelected(this.options.initial); //call for an inital time
		},
		
		getSelected: function() {
			return this._selectionDropDown.val();
		},
		
		setSelected: function(name) {
			this._selectionDropDown.prop('selectedIndex',$('option[value="' + name + '"]',this._selectionDropDown).index());
			this._selectionDropDown.change();
		},
		
		destroy: function() {
			this._selectionBox.remove();
			this._selectionContentArea.remove();
			this.element.removeClass( "nbn-selectionPane" ); //remove the appended class
			$.Widget.prototype.destroy.apply( this, arguments );
		}
	});
	
    $.extend( $.ui.nbn_selectionPane, {
		version: "@VERSION"
    });
})( jQuery );
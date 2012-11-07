(function($, undefined){
	$.widget("ui.nbn_colourPicker", {
		options:{ startingColour: 'ff0000', change: function(){} },
		
		_create:function(){
			var _me = this, hexLabel = $('<div>').addClass('hexLabel').html(_me.options.startingColour);
			this.hexOutput = $('<div>').addClass('hexOutput').append(hexLabel).css({backgroundColor: '#'+_me.options.startingColour});
			this.picker = $('<div>').addClass('colourPickerContainer').ColorPicker({onChange: function (hsb, hex, rgb) {
				_me.hexOutput.css({backgroundColor: '#'+hex});
				hexLabel.html(hex);
				_me._trigger('change', 0, hex);//notify callback
			}, flat: true, color: _me.options.startingColour});
			
			this.element.append(this.picker).append(this.hexOutput);
		}
	});
})(jQuery);
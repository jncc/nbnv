(function( $, undefined ) {
    $.widget( "ui.nbn_list", {
		options: {
			data: []
		},
		
		_create: function() {
			this.element.addClass('ui-widget');
			this._updateList();
		},
		
		_updateList : function() {
			this.element.empty();
			if(this.options.sortFunction) //if has a sort function, then sort
				this.options.data.sort(this.options.sortFunction); //sort it with the sort function
			for(var i in this.options.data) 
				this.element.append($('<li>').append(this.options.elementRenderFunction(this.options.data[i])));
		},
		
		_animateUpdateList : function() {
			var _me = this;
			this.element.stop(true,false).fadeTo('fast', 0, function() {//fade out
				_me._updateList();
				_me.element.fadeTo('fast', 1); //fade back in
			})
		},
		
		setData: function(data) {
			this.options.data = (data) ? data.slice(0) : [] //perform a shallow copy of the array
			this._animateUpdateList(); //update the list
		},
		
		setSortFunction: function(sortFunction) {
			this.options.sortFunction = sortFunction;
			this._animateUpdateList(); //update the list
		},
		
		destroy: function() {
			this.element.removeClass('ui-widget');
		}
	});
		
    $.extend( $.ui.nbn_list, {
		version: "@VERSION"
    });

})( jQuery );
define([
		"jquery-ui",
		"underscore",
		"backbone",
], function($, _, Backbone) {
	return Backbone.View.extend({
		initialize: function () {
			this.$el.html('<input type="text" size="30"></input>');
			//Start a new search 
			this.searchCollection = this.model.getSearch();
			
			// Create a jquery autocomplete widget. Use 
			$('input', this.$el).autocomplete({ 
				source: _.bind(this.search, this),
				select: _.bind(this.select, this)
			});
		},
		
		select: function(event, ui) {
			this.model.addSearchResult(ui.item);
		},
		
		search: function (request, response) {
			var _me = this;
			this.searchCollection
				.fetch( { data : {q:request.term, rows:3}} )
				.success(function() {
					response(_me.searchCollection.map(function(model) { 
						return model.attributes;
					})); 
				});
		}
	});
});
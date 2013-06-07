define([
		'backbone', 
		'collections/Search'
], function(Backbone, Search) {
	return Backbone.Model.extend({
		defaults: {
			viewport: {
                minX: -24.90413904330953,
				minY: 42.925823307748196,
				maxX: 18.29410313645321,
				maxY: 64.17861427158351
			},
			baseLayer: "Ordnance"
		},
		
		/**
		* Return an instance of the NBN Gateways search api
		*/
		getSearch: function() {
			return new Search();
		},
		
		/**
		* Process a search result and update the application
		* accordingly
		*/
		addSearchResult: function(searchResult) {
			if(searchResult.worldBoundingBox) {
				this.set("viewport",searchResult.worldBoundingBox);
			}
		}
	});
});
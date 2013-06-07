define([
		"backbone",
		"models/SearchResult",
		"helpers/Globals"
], function(Backbone, SearchResult, Globals) {
	return Backbone.Collection.extend({
		model: SearchResult,
		url: function() {
			return Globals.api("search");
		},
		parse: function(resp,xhr){
			return Backbone.Model.prototype.parse.call(this, resp.results, xhr);
		}
	});
});
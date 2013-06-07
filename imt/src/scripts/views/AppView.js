define([
		"jquery",
		"backbone",
		"hbs!templates/AppScaffolding",
		"views/OpenLayersView",
		"views/SearchView"
], function($, Backbone, imtScaffolding, OpenLayersView, SearchView) {
	return Backbone.View.extend({
		el: '#imt',
		
		// Register to imt events which the view should respond to
		initialize: function() {
			this.$el.addClass("interactiveMapTool");
			this.render();
		},
		
		render: function() {
			this.$el.html(imtScaffolding());
			this.openlayersView = new OpenLayersView({
				el: $('.openlayers', this.$el),
				model: this.model
			});
			
			this.searchView = new SearchView({
				model: this.model,
				el: $('.search', this.$el)
			});
		}
	});
});
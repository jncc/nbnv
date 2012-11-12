/**
*
* @author		:- Christopher Johnson
* @date			:- 20th-April-2011
* @description	:- This JScript file will resolve and render a datasets metadata
* @dependencies	:-
*	nbn.util.EntityResolver
*/
(function( $, undefined ) {

$.widget( "ui.nbn_datasetmetadata", {
	options: {
		animationDuration: 'fast',
		renderFunction: function(datasetToRender) {
			return {
				title: $('<a>')
					.html(datasetToRender.title)
					.attr('title', 'Click for more information')
					.attr('target', '_blank')
					.attr('href', 'http://data.nbn.org.uk/datasetInfo/taxonDataset.jsp?dsKey=' + datasetToRender.datasetKey),
				content: datasetToRender.description
			}
		}
	},
	
	_create: function() {
        this.element
			.addClass("nbn-datasetmetadata ui-widget ui-widget-content ui-corner-all")
			.append(this._titleContainer = $('<div>').addClass('nbn-datasetmetadata-title ui-widget-header'))
			.append(this._contentContainer = $('<div>').addClass('nbn-datasetmetadata-content'));
			
		this.setDataset(this.options.dataset);
    },

	_render: function(datasetToRender) {
		var renderedResults = this.options.renderFunction(datasetToRender);
		this._titleContainer.empty().append(renderedResults.title);
		this._contentContainer.empty().append(renderedResults.content);
	},
	
	setDataset: function(dataset) {
		var _me = this;
		_me.element.stop(true, false).fadeTo(_me.options.animationDuration, 0, function() {
			if(dataset) {
				nbn.util.EntityResolver.resolve({
					datasetWithMetadata: dataset
				}, function(result) {
					_me._render(result.datasetWithMetadata);
					_me.element.stop(true, false).fadeTo(_me.options.animationDuration, 1);
				});
			}
		});
	},
	
    destroy: function() {
        this.element.removeClass( "nbn-datasetmetadata ui-widget ui-widget-content ui-corner-all" ); //remove the appended class
		$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
    }
})

$.extend( $.ui.nbn_datasetmetadata, {
	version: "@VERSION"
});

})( jQuery );
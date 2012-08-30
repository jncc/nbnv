nbn.mapping.construction.WidgetOptionsCreator.prototype.createLoadingWidget = function() {
	var loadingWidget = $('<div>').nbn_loading();
	this._map.addNBNMapLayerCollectionUpdateListener({
		add: function(collection, added) {
			loadingWidget.nbn_loading('listenToTheLoadingOf',added.layer);
		},
		remove: function(collection, removed) {
			loadingWidget.nbn_loading('stopListeningToTheLoadingOf', removed.layer);
		}
	});
	return loadingWidget;
};
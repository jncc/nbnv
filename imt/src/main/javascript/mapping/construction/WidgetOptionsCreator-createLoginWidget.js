nbn.mapping.construction.WidgetOptionsCreator.prototype.createLoginWidget = function() {
	return $('<div>').nbn_login({
		loginUser: this._map
	});
};
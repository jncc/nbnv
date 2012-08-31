nbn.mapping.construction.WidgetOptionsCreator.prototype.createLoginFilter = function() {
	var loginFilter = new nbn.layer.ArcGisMapFilter(); //make the login filter availble as a property
	this._map.addUserUpdateListener({
		User: function(newUser) {
			if(newUser) {
				loginFilter.setFilter({
					filters: {
						username: newUser.username,
						userkey: newUser.passhash
					}
				});
			}
			else
				loginFilter.clearFilter();
		}
	});
	return loginFilter;
};
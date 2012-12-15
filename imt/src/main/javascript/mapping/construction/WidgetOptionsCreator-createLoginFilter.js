nbn.mapping.construction.WidgetOptionsCreator.prototype.createLoginFilter = function() {
	var loginFilter = new nbn.layer.ArcGisMapFilter(); //make the login filter availble as a property
	this._map.addUserUpdateListener({
		User: function(newUser) {
			if(newUser) {
				loginFilter.setFilter({
					filters: {
//Jon Cooper says - the loginFilter information should no longer be passed to the url of map image calls (this was pre-NBNV),
//however this filter seems to be used to ensure there is at least one filter on the InteractiveMapClient, so I've not removed the actual filter'
//						username: newUser.username,
//						userkey: newUser.passhash
					}
				});
			}
			else
				loginFilter.clearFilter();
		}
	});
	return loginFilter;
};
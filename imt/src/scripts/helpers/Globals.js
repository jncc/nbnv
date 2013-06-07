define([], function() {
	return {
		api: function(path) {
			return "http://staging.testnbn.net/api/" + path + "?callback=?";
		}
	};
});
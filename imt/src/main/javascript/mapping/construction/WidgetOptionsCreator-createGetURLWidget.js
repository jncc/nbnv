nbn.mapping.construction.WidgetOptionsCreator.prototype.createGetURLWidget = function() {
	var interactiveMapper = this._map, urlBox, message;
	
	function generateMapURL() {
		var mapURL = interactiveMapper.getMapURL();
		urlBox.toggle(mapURL != false); //hide or show the get map url box
		message.html((mapURL) ? 'Below is the url for your map' : 'Unfortunately it is currently not possible to create a url for a map with multiple layers of the same time');
		if(mapURL)
			urlBox.attr('value',mapURL);
	}
		
	var getURLDialog = $('<div>')
		.append(message = $('<h3>'))
		.append(urlBox = $('<input type="text">').width(400))
		.dialog({ title: 'Interactive Map URL', autoOpen: false, modal: true, resizable: false, width: 500, open: generateMapURL });
		
	return $('<div>')
		.html('Get Map URL')
		.button()
		.click(function() {
			getURLDialog.dialog('open');
		});
};
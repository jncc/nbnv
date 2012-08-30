nbn.mapping.construction.WidgetOptionsCreator.prototype.createLayersCreatorBox = function() {
	var _me = this;
	
	function _createLayerIcon(type, dialog) {
		return $('<div>').nbn_icon({label: type, icon: type})
			.click(function() {
				_me._map.addNBNMapLayer(_me['createNBN' + type + 'Layer']({configureOnLoad: true}));
				dialog.dialog('destroy');
			});
	}
	
	var _dialog = $('<div>').addClass('nbn-layersCreator')
		.append($('<h3>').html('What type of Layer would you like to create?').addClass('nbn-layersCreatorTitle'))
		.dialog({
			modal: true,
			resizable: false,
			title: 'Create a Layer',
			width: 450
		});
		
	_dialog.append($('<div>').addClass('nbn-layersCreatorIconBar')
		.append(_createLayerIcon('Species',_dialog))
		.append(_createLayerIcon('Habitat',_dialog))
		.append(_createLayerIcon('Boundary',_dialog))
	)
	return _dialog;
}
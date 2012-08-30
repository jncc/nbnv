nbn.mapping.construction.WidgetOptionsCreator.prototype.createLayersContainer = function() {
	var _me = this;
	var interactiveMapper = this._map;

	var layersBoxContainer = $('<div class="nbn-layersContainer">');
	interactiveMapper.addNBNMapLayerCollectionUpdateListener({
		add: function(collection, added) {layersBoxContainer.prepend(added.view);},
		remove: function(collection, removed) {removed.view.remove(); },//remove the layer
		reposition: function(collection) {
			for(var i in collection)
			  layersBoxContainer.prepend(collection[i].view);
		}
	});
	
	return {
		representation: $('<div>')
			.append(layersBoxContainer.sortable({
				handle:'.nbn-statefulBox-handleBar',
				placeholder: "ui-state-highlight",
				forcePlaceholderSize: true,
				delay: 300,
				update: function(event, ui) {
					var underlyingArray = interactiveMapper.getUnderlyingNBNMapLayerArray();
					var initPosition = nbn.util.ArrayTools.getIndexOfByFunction(underlyingArray,ui.item.context, function(element){
						return element.view[0];
					});
					var newPosition = ($(this).children().size()-1) - $(ui.item.context).index(); //calculate the correct position for the layer
					interactiveMapper.positionNBNMapLayer(underlyingArray[initPosition],newPosition);
				}
			}).disableSelection()).nbn_statefulbox({
				title: "Layers",
				padding: false,
				outerClass: 'nbn-layersContainer-outerBox',
				additionalButtons: [{ //define the create new layer button
                    initialState: 'default',
                    states: {
                        "default" : {
                            icon : "ui-icon ui-icon-newwin",
                            click: function() {_me.createLayersCreatorBox()},
                            tooltip: 'Create a new Layer'
                        }
                    }
                }]
			}
		),
		validate: function(newVal) {layersBoxContainer.css({ maxHeight: newVal-170 });}
	};
}
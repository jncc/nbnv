nbn.mapping.construction.WidgetOptionsCreator.prototype.createNBNHabitatLayer = function(options) {
	var _layerHelper = this._layerHelper; //take the layer helper from the contstructor factory
	function _createHabitatLayerBox(layer, filter) {
        var _createHabitatAppliedFilters = function(tempFilter) {
            return $('<div>')
                .append($('<div>') //filter species status
                    .append(_layerHelper.createLabeledContent('',$('<div style="height: 20px; overflow-y:auto; overflow-x:hidden;">').nbn_filterStatus({layerFilter: tempFilter,inactiveFilteringLabel:'No habitats selected'})))
                );
        };
        var _habitatDialog = $('<div>').nbn_renderableControlDialog({
            renderableControl: new function() {
                var content = $('<div>').nbn_treewidget({
                    urlOfDescriptionFile : nbn.util.ServerGeneratedLoadTimeConstants.data_api + "/habitatDatasets",
                    allowMultipleSelection: 'checkbox'
                });

                this.apply = function() {
                    var layers = content.nbn_treewidget('getAllChildrenChecked');
                    if(layers.length) {
                        filter.setFilter(nbn.util.ArrayTools.getArrayByFunction(layers, function(element) {
                            return {
                                gisLayerID: element,
                                datasetKey: content.nbn_treewidget('getChildUserData', element, 'datasetKey')
                            };
                        }));
                    }
                    else
                        filter.clearFilter();
                };
				
                this.getRepresentation = function() {return content;};
                this.reset = function() {content.nbn_treewidget('unCheckAll');};
                this.getState = function() {return content.nbn_treewidget('getState');};
                this.setState = function(state) {content.nbn_treewidget('setState', state);};
				
                $.extend(this, new nbn.util.ObservableAttribute('Renderable',true));
            },
            title: 'Habitats',
            resetButtonText: 'Clear Layer',
			autoOpen: options.configureOnLoad
        });
        return _layerHelper.createNBNLayerBox($('<div>').append(_createHabitatAppliedFilters(filter)), layer, {
            "Choose Habitats" : function() {
                _habitatDialog.nbn_renderableControlDialog("open");
            }
        });
    };

	var habitatMapLayer = _layerHelper.createMapLayer(nbn.layer.HabitatLayer, options, 'Habitat');
	var toReturn =  {
		layer: habitatMapLayer,
		picker : new nbn.layer.picker.HabitatPicker(habitatMapLayer)
	};
	
	toReturn.view = _createHabitatLayerBox(toReturn,habitatMapLayer.getResolvingVisibleFilter());
	return toReturn;
};
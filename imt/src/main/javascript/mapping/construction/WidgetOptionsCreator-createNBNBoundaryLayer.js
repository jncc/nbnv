nbn.mapping.construction.WidgetOptionsCreator.prototype.createNBNBoundaryLayer = function(options) {
	var _layerHelper = this._layerHelper; //take the layer helper from the contstructor factory
	function _createBoundaryLayerBox(layer, filter) {    
        var _createBoundaryAppliedFilters = function(tempFilter) {
            return $('<div>')
                .append($('<div>') //filter species status
                    .append(_layerHelper.createLabeledContent('',$('<div style="height: 20px; overflow-y:auto; overflow-x:hidden;">').nbn_filterStatus({layerFilter: tempFilter,inactiveFilteringLabel:'No boundary selected'})))
                );
        };

        var _boundaryDialog = $('<div>').nbn_renderableControlDialog({
            renderableControl: new function() {
                var content = $('<div>').nbn_treewidget({
                    urlOfDescriptionFile : 'TreeWidgetGenerator?type=s',
                    allowMultipleSelection: 'radio'
                });

                this.apply = function() {
                    var layers = content.nbn_treewidget('getAllChildrenChecked');
                    if(layers.length) {
                        var element = layers[0];
						filter.setFilter({
							gisLayerID: element,
							datasetKey: content.nbn_treewidget('getChildUserData', element, 'datasetKey')
						});
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
            title: 'Boundary',
            resetButtonText: 'Clear Layer',
			autoOpen: options.configureOnLoad
        });
        return _layerHelper.createNBNLayerBox($('<div>').append(_createBoundaryAppliedFilters(filter)), layer, {
            "Choose Boundary" : function() {
                _boundaryDialog.nbn_renderableControlDialog("open");
            }
        });
    };

	var siteboundaryMapLayer = _layerHelper.createMapLayer(nbn.layer.BoundaryLayer, options, 'Boundary')
	var toReturn = {
		layer: siteboundaryMapLayer,
		picker : new nbn.layer.picker.SiteBoundaryPicker(siteboundaryMapLayer)
	};
	toReturn.view = _createBoundaryLayerBox(toReturn,siteboundaryMapLayer.getResolvingVisibleFilter());
	return toReturn;
}
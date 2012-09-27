/**
*
* @author	    :- Christopher Johnson
* @date		    :- 3rd-December-2010
* @description	:- This JScript file defines the the options which are used to create the interactive mapper widgets
* @dependencies	:-
*	mapLayer.js
*/
window.nbn = window.nbn || {};
nbn.mapping = nbn.mapping || {};
nbn.mapping.construction = nbn.mapping.construction || {};

nbn.mapping.construction.WidgetOptionsCreator = function(interactiveMapper){
	this._map = interactiveMapper;
	var _layerHelper = this._layerHelper = new nbn.mapping.construction.LayerHelper(interactiveMapper);

	function _createNamedFilter(name, layers, initialState) {
		return { name: name, filter: (function(){
			var toReturn = new nbn.layer.SwitchableArcGisMapFilter(initialState & true === true);
			toReturn.setFilter({visibleLayers: ($.isArray(layers)) ? layers : [layers]});
			return toReturn;
		})() };
	};
	
    var _createZoomFilters = function() {
		return [_createNamedFilter('10km', 'Grid-10km'),_createNamedFilter('2km', 'Grid-2km'),_createNamedFilter('1km', 'Grid-1km'),_createNamedFilter('100m', 'Grid-100m'),{
                name: 'Default',
                filter: function() {
                    var zoomFilter = new nbn.layer.SwitchableArcGisMapFilter(true);
                    zoomFilter.setFilter(function(zoom) {
                    var hundredMAppear = 13;
                    var oneKAppear = 10;
                    var twoKAppear = 8;
                    if((zoom >= twoKAppear ) && (zoom < oneKAppear))
                        return {visibleLayers: ['Grid-2km']}
                    else if((zoom >= oneKAppear) && (zoom < hundredMAppear))
                        return {visibleLayers: ['Grid-1km']}
                    else if(zoom >= hundredMAppear)
                        return {visibleLayers: ['Grid-100m']}
                    else
                        return {visibleLayers: ['Grid-10km']}
                    });
                    return zoomFilter;
                }()
            }];
    };

	function _createPolygonFilters() {
		return [_createNamedFilter('Polygons', '5', true), _createNamedFilter('Markers', '4', true)];
	};
	
    this.createNBNSpeciesLayer = function(options) {
        var speciesMapLayer = _createFilteringMapLayer(options);
        var zoomFilters = _createZoomFilters();
		var polygonFilters = _createPolygonFilters();
        for(var i in zoomFilters) speciesMapLayer.addLayerFilter(zoomFilters[i].filter);
        for(var i in polygonFilters) speciesMapLayer.addLayerFilter(polygonFilters[i].filter);
        speciesMapLayer.addLayerFilter(interactiveMapper.LoginFilter);
        var layerToReturn = {layer: speciesMapLayer};
        
		layerToReturn.view = _createSpeciesLayerBox(layerToReturn, zoomFilters, polygonFilters, options);
		layerToReturn.picker = new nbn.layer.picker.SpeciesLayerPicker(speciesMapLayer);

        return layerToReturn;
    };

    var _createFilteringMapLayer = function(options) {
        options = $.extend({
            opacity: 1,
            name: 'Species'
        }, options);
        
        options.logger = interactiveMapper.Logger; //force the logger to be the one which created this map layer
	return new nbn.layer.SpeciesLayer(nbn.util.ServerGeneratedLoadTimeConstants.gisServers, interactiveMapper.Map, options);
    };

    var _createYearFilter = function(temporalFilter) {
        var startDate = 1799, endDate = nbn.util.ServerGeneratedLoadTimeConstants.date.year;
		var currFilter = temporalFilter.getFilter();
		var currStartDate = (temporalFilter.isFiltering() && currFilter.filters.startyear) ? currFilter.filters.startyear : startDate;
		var currEndDate = (temporalFilter.isFiltering() && currFilter.filters.endyear) ? currFilter.filters.endyear : endDate;
		
		var slider = $('<div>').slider({ //create the date range slider
			range: true,
			min: startDate,
			max: endDate,
			values: [ currStartDate, currEndDate ],
			slide: function(event, ui) {
				if(ui.values[0] != startDate || ui.values[1] != endDate) {
					temporalFilter.setFilter({
						filters: {
							startyear: ui.values[0],
							endyear: ui.values[1]
						}
					});
				}
				else
					temporalFilter.clearFilter();
			}
		});
		
		temporalFilter.addFilterUpdateListener({ //add a listener to the filter and update the slider when nessersary
			Filter: function(val) {
				slider.slider('values',0,(val && val.filters.startyear) ? val.filters.startyear : startDate);
				slider.slider('values',1,(val && val.filters.endyear) ? val.filters.endyear : endDate);
			}
		});
		
        return $('<div style="padding-bottom: 15px;">')
            .append($('<div>').nbn_filterStatus({
				statusFunction : function() {
					return ((this.filters.startyear == startDate) ? 'pre ' : '') + (this.filters.startyear || startDate) +  " - " + (this.filters.endyear || endDate);
				},
				layerFilter:temporalFilter,
				inactiveFilteringLabel:'All Years'
			}))
            .append(slider);
    };

    var _createVisibleLayersControl = function(zoomFilters) {
        var toReturn = $('<table>');
        var row1 = $('<tr>');
        for(var i in zoomFilters) {
            $('<td>')
                .text(zoomFilters[i].name)
                .appendTo(row1);
        }
        row1.appendTo(toReturn);
        var row2 = $('<tr>');
        for(var i in zoomFilters) {
            $('<td>')
                .append(_layerHelper.createCheckboxControl(zoomFilters[i].filter))
                .appendTo(row2);
        }
        row2.appendTo(toReturn);
        return toReturn;
    };

	function _createAbundanceLabeledContent(filter) {
		var content = $('<table><tr><td>All</td><td>Presence</td><td>Absence</td></tr></table>'); //create the table with headers
		var uniqueRadioButtonID = nbn.util.IDTools.generateUniqueID();
		
		function createRadioButtonTableEntry(abundanceValueToSetOnSelect, selected) {
			return $('<td>').append($('<input type="radio"' + ((selected) ? 'checked="checked"' :'') + '>').attr('value',abundanceValueToSetOnSelect).attr('name',uniqueRadioButtonID).click(function() {
				filter.setFilter({filters:{abundance: abundanceValueToSetOnSelect}}); //on click set the new filter value
			}));
		}

		content.append($('<tr>')
			.append(createRadioButtonTableEntry("all", true))
			.append(createRadioButtonTableEntry("presence"))
			.append(createRadioButtonTableEntry("absence"))
		);
		
		var toReturn = _layerHelper.createLabeledContent('Abundance: ',content); //create the labled content
		
		function setVisibilityOfAbundanceFilter(visible) { toReturn[(visible) ? 'show' : 'hide'].call(toReturn); }//set the visiblity of the abundance filter
		setVisibilityOfAbundanceFilter(filter.getEnabled());
		filter.addEnabledUpdateListener({Enabled:setVisibilityOfAbundanceFilter});
		return $('<div>').append(toReturn); //wrap up so that show/ hide functionality works
	};
	
    var _createSpeciesLayerBox = function(nbnLayer, zoomFilters, polygonFilters, options) {	
        var _createSpeciesAppliedFilters = function(layer, zoomFilters) {
		
			var _gridVisibleLayersBox = _layerHelper.createLabeledContent('Visible Layers: ',_createVisibleLayersControl(zoomFilters)).attr('advancedControl','true');
            			
			return $('<div>')
                .append($('<div>') //filter species status
                    .append(_layerHelper.createLabeledContent('Summary: ',$('<div>')
                        .nbn_autosize()
                        .nbn_label({
                            label: layer,
                            type:'Description',
                            contentchange: function() {$(this).nbn_autosize('resize');}
                        }))
                    )
                )
                .append(_layerHelper.createLabeledContent('Years: ',_createYearFilter(layer.YearFilter)).attr('advancedControl','true'))
                .append(_layerHelper.createLabeledContent('Polygon Records: ', _createVisibleLayersControl(polygonFilters)).attr('advancedControl','true'))
                .append(_createAbundanceLabeledContent(layer.AbundanceFilter).attr('advancedControl','true'))
                .append(_gridVisibleLayersBox);
        };
        var _speciesDialog = _createSpeciesDialog(nbnLayer.layer, options);
        var _datasetsUsedDialog = _createDatasetInfoDialog(nbnLayer.layer); //the map to create the datasets used for dialog
        return _layerHelper.createNBNLayerBox($('<div>').append(_createSpeciesAppliedFilters(nbnLayer.layer, zoomFilters)), nbnLayer, {
                "Dataset Info" : function() {_datasetsUsedDialog.dialog('open');},
                "Species Selector" : function(){_speciesDialog.dialog("open");}
            });
    };

    var _createSpeciesDialog = function(layer, options) {
        var singleSpecies = function(renderableToControl) {
            var _me = this, _selectedSpecies, _selectedDatasets = [], _datasetSelectionBox, _singleSpeciesDatasetSelectionTree;
            var _setSelectedSpecies = function(selection) {
                    if(_selectedSpecies = selection) {//save the species selection
                            var currUser = interactiveMapper.getUser();//get the current user
                            _singleSpeciesDatasetSelectionTree.nbn_treewidget('setUrlOfDescriptionFile',nbn.util.ServerGeneratedLoadTimeConstants.data_api + "/taxa/" + _selectedSpecies.taxonVersionKey.taxonVersionKey + "/datasets");
                            _datasetSelectionBox.show(); //show the selection box
                    }
                    _selectedDatasets=[];
                    renderableToControl.setRenderable(_me.isRenderable()); //is this map now renderable?
            };

            var _setSelectedSpeciesDatasets = function(selected) {
                    _selectedDatasets = [];
                    if(!_singleSpeciesDatasetSelectionTree.nbn_treewidget('isFullyChecked')) {
                        for(var currentDatasetKey in selected){
                                _selectedDatasets.push({
                                        datasetKey:selected[currentDatasetKey],
                                        name: _singleSpeciesDatasetSelectionTree.nbn_treewidget('getChildUserData', selected[currentDatasetKey],'name')
                                });
                        }
                    }
                    renderableToControl.setRenderable(_me.isRenderable()); //is this map now renderable?
            };

            var _speciesAutoComplete = $('<input>')
                    .addClass("nbn-autocomplete")
                    .autocomplete({
                            source : 'TaxonSearch',
                            select: function(event, ui) {
                                    _setSelectedSpecies({
                                            taxonVersionKey: ui.item.key,
                                            name: ui.item.name
                                    });
                            }
                    });
            _speciesAutoComplete.data( "autocomplete" )._renderItem = function(ul, item) {
                    return $( "<li></li>" )
                            .data( "item.autocomplete", item )
                            .append( "<a><strong>" + item.value + "</strong><br>" + item.authority + ", " + item.taxonRank + ", " + item.taxonGroup + "</a>" )
                            .appendTo(ul);
            };

            
            var _speciesTree = $('<div>').nbn_treewidget({
                    urlOfDescriptionFile : nbn.util.ServerGeneratedLoadTimeConstants.data_api + "/taxonNavigationGroups",
                    dataFilter: function(taxonNavGroup) {
                        function transformTaxonNavGroup(taxonNavGroup) {
                            var base = { 
                                title : taxonNavGroup.name,
                                unselectable: true, 
                                isLazy: true,hideCheckbox: false
                            };
                            if(taxonNavGroup.children) {
                                return $.extend(base, taxonNavGroup, {
                                    children : $.map(taxonNavGroup.children, transformTaxonNavGroup)
                                });
                            }
                            else {
                                return $.extend(base, taxonNavGroup);
                            }
                        }
                        return transformTaxonNavGroup(taxonNavGroup);
                    },
                    allowMultipleSelection: 'none',
                    selected: function(event, selected) {
                            _setSelectedSpecies({
                                    taxonVersionKey: selected.taxonVersionKey,
                                    name: $(this).nbn_treewidget('getChildText', selected)
                            });
                    }
            });

            _singleSpeciesDatasetSelectionTree = $('<div>').nbn_treewidget({
                    urlOfDescriptionFile : nbn.util.ServerGeneratedLoadTimeConstants.data_api + "/taxa/NBNSYS0000005629/datasets",
                    allowMultipleSelection: 'checkbox',
                    selectDeselect: true,
                    dataFilter: function(dataset) {
                        return $.extend({ title : dataset.name }, dataset);
                    },
                    childrenSelectionListener: function(event, selected) {
                        _setSelectedSpeciesDatasets(selected);
                    },
                    loaded: function() {
                        _setSelectedSpeciesDatasets($(this).nbn_treewidget("getAllChildrenChecked"));
                    }
            });

            var _content = $('<div>')
                    .append($('<span>')
                            .append('Search for species:')
                            .append(_speciesAutoComplete)
                    )
                    .append(_speciesTree)
                    .append(_datasetSelectionBox = $('<div>')
                            .hide() //tree box hidden by default
                            .append($('<div>').html('Datasets:'))
                            .append(_singleSpeciesDatasetSelectionTree)
                    );


            this.isRenderable = function() {
                    return _selectedSpecies != undefined && (_selectedDatasets.length > 0 || _singleSpeciesDatasetSelectionTree.nbn_treewidget('isFullyChecked'));
            };

            this.getRepresentation = function() {
                    return _content;
            };

            this.reset = function() {
                    _setSelectedSpecies(undefined); //set no selected species
            };

            this.apply = function() {
                    layer.setMode(layer.Modes.SPECIES,_selectedSpecies,_selectedDatasets);
            };

            this.getState = function() {
            };

            this.setState = function(state) {
            };
        };

        var dataset = function(renderableToControl) {
            var _selectedDataset;
            var _datasetMetadata = $('<div>').nbn_datasetmetadata();
			
            var _setSelectedDataset = function(selection) {
                    _selectedDataset = selection//save the species selection
                    _datasetMetadata.nbn_datasetmetadata('setDataset', _selectedDataset.datasetKey + '');
                    renderableToControl.setRenderable(_selectedDataset != undefined); //is this map now renderable?
            };

            var _singleDatasetSelectionAutocomplete = $('<input>')
                    .addClass("nbn-autocomplete")
                    .autocomplete({
                            source : 'DatasetSearch',
                            select: function(event, ui) {
                                    _setSelectedDataset({
                                            datasetKey: ui.item.key,
                                            name: ui.item.name
                                    });
                            }
                    });

            var _singleDatasetSelectionTree = $('<div>').nbn_treewidget({
                    urlOfDescriptionFile : nbn.util.ServerGeneratedLoadTimeConstants.data_api + "/datasets",
                    dataFilter: function(dataset) {
                        return $.extend({ title : dataset.name }, dataset);
                    },
                    allowMultipleSelection: 'none',
                    selected: function(event, selected) { _setSelectedDataset(selected); }
                    });
			
            var _content = $('<div>')
                    .append($('<span>')
                            .html('Select a dataset:')
                            .append(_singleDatasetSelectionAutocomplete)
                    )
                    .append(_singleDatasetSelectionTree)
                    .append(_datasetMetadata);

            this.isRenderable = function() {
                    return _selectedDataset != undefined;
            };

            this.getRepresentation = function() {
                    return _content;
            };

            this.reset = function() {
                    _setSelectedDataset(undefined); //set no selected species
            };

            this.apply = function() {
                    layer.setMode(layer.Modes.SINGLE_DATASET,_selectedDataset);
            };

            this.getState = function() {
            };

            this.setState = function(state) {
            };
        };

        var designation = function(renderableToControl) {
            var _me = this, _selectedDesignation, _selectedDatasets=[], _datasetSelectionBox, _designationDatasetsTree;

            var _setSelectedDesignation = function(selection) {
                    if(_selectedDesignation = selection) {//save the species selection
                            var currUser = interactiveMapper.getUser();//get the current user
                            _designationDatasetsTree.nbn_treewidget('setUrlOfDescriptionFile',nbn.util.ServerGeneratedLoadTimeConstants.data_api + "/designations/" + _selectedDesignation.designationKey.code + "/datasets");
                            _datasetSelectionBox.show();
                    }
                    _selectedDatasets=[];
                    renderableToControl.setRenderable(_me.isRenderable()); //is this map now renderable?
            };

            var _setSelectedDesignationDatasets = function(selected) {
                _selectedDatasets = [];
                if(!_designationDatasetsTree.nbn_treewidget('isFullyChecked')) {
                    for(var currentDatasetKey in selected){
                            _selectedDatasets.push({
                                    datasetKey:selected[currentDatasetKey],
                                    name: _designationDatasetsTree.nbn_treewidget('getChildUserData', selected[currentDatasetKey], 'name')
                            });
                    }
                }
                renderableToControl.setRenderable(_me.isRenderable()); //is this map now renderable?
            };

            _designationDatasetsTree = $('<div>').nbn_treewidget({
                    urlOfDescriptionFile : nbn.util.ServerGeneratedLoadTimeConstants.data_api + "/designations/BIRDSDIR-A1/datasets",
                    dataFilter: function(designation) {
                        return $.extend({ title : designation.name }, designation);
                    },
                    allowMultipleSelection: 'checkbox',
                    selectDeselect: true,
                    childrenSelectionListener: function(event, selected) {
                        _setSelectedDesignationDatasets(selected);
                    },
                    loaded: function() {
                        _setSelectedDesignationDatasets($(this).nbn_treewidget("getAllChildrenChecked"));
                    }
            });

            var _designationTree = $('<div>').nbn_treewidget({
                    urlOfDescriptionFile : nbn.util.ServerGeneratedLoadTimeConstants.data_api + "/designations",
                    dataFilter: function(designation) {
                        return $.extend({ title : designation.name }, designation);
                    },
                    allowMultipleSelection: 'none',
                    selected: function(event, selected) {
                            _setSelectedDesignation({
                                    designationKey: selected,
                                    name: $(this).nbn_treewidget('getChildText', selected)
                            });
                    }
            });

            var _content = $('<div>')
                    .append('Select a designation:')
                    .append(_designationTree)
                    .append(_datasetSelectionBox = $('<div>')
                            .hide() //tree box hidden by default
                            .append($('<div>').html('Datasets:'))
                            .append(_designationDatasetsTree)
                    );

            this.isRenderable = function() {
                    return _selectedDesignation != undefined && (_selectedDatasets.length > 0 || _designationDatasetsTree.nbn_treewidget('isFullyChecked'));
            };

            this.getRepresentation = function() {
                    return _content;
            };

            this.reset = function() {
                    _setSelectedDesignation(undefined); //set no selected species
            };

            this.apply = function() {
                    layer.setMode(layer.Modes.DESIGNATION,_selectedDesignation.designationKey, _selectedDatasets);
            };

            this.getState = function() {
            };

            this.setState = function(state) {
            };
        };
	return $('<div>').nbn_renderableControlDialog({
            width: 600,
            height: 600,
            renderableControl: new function() {
                var _me = this;
                $.extend(this, new nbn.util.ObservableAttribute('Renderable',false));

                var _none = {
                    isRenderable: function() {
                        return true;
                    },
                    apply: function() {
                        layer.setMode(layer.Modes.NONE);
                    }
                };
                var _dataset = new dataset(_me);
                var _species = new singleSpecies(_me);
                var _designation = new designation(_me);

                var _selectedWindow = _none;
                var _setSelected = function(newSelection) {
                    _selectedWindow = newSelection;
                    _me.setRenderable(newSelection.isRenderable());
                };

                var content = $('<div>').nbn_selectionPane({
                    label: 'Choose a type of map:',
                    initial: "None",
                    selectables: {
                        "None" : {
                            selected: function() {
                                _setSelected(_none);
                            }
                        },
                        "Single Species" : {
                            content: _species.getRepresentation(),
                            selected: function() {
                                _setSelected(_species);
                            }
                        },
                        "Dataset" : {
                            content: _dataset.getRepresentation(),
                            selected: function() {
                                _setSelected(_dataset);
                            }
                        },
                        "Designation" : {
                            content: _designation.getRepresentation(),
                            selected: function() {
                                _setSelected(_designation);
                            }
                        }
                    }
                });

                this.getRepresentation = function() {return content;};
                this.reset = function() {content.nbn_selectionPane('setSelected','None');};
                this.apply = function() {_selectedWindow.apply();};

                this.getState = function() {
                    return {
                        currMode: content.nbn_selectionPane('getSelected'),
                        datasetWindow: _dataset.getState(),
                        speciesWindow: _species.getState(),
                        designationWindow: _designation.getState()
                    };
                };

                this.setState = function(state) {
                    _dataset.setState(state.datasetWindow);
                    _species.setState(state.speciesWindow);
                    _designation.setState(state.designationWindow);
                    content.nbn_selectionPane('setSelected', state.currMode);
                };
            },
            title: 'Species Selector',
            resetButtonText: 'Clear Layer',
			autoOpen: options.configureOnLoad
        });
    };

    var _createDatasetInfoDialog = function(nbnMapLayer) {
        var _organisationNameSortFunction = function(a,b) {
            var x = a.name, y = b.name;
            return ((x < y) ? -1 : ((x > y) ? 1 : 0));
        };

        var _getDatasetProviderRecordCount = function(datasetProvider) {
            var toReturn = 0;
            for(var i in datasetProvider.datasets)
                toReturn += datasetProvider.datasets[i].contextRecordCount || datasetProvider.datasets[i].recordCount;
            return toReturn;
        };

        var _recordCountSortFunction = function(a,b) {
            return _getDatasetProviderRecordCount(b) - _getDatasetProviderRecordCount(a);
        };

        var _renderDatasetBoolean = function(value) {
            var toReturn = $('<span>');
            if(value)
                toReturn.append($('<span>').addClass('ui-icon ui-icon-check'));
            return toReturn;
        };

        var datasetsSelectedTable = function() {
            return $('<ul>').addClass('dataset-provider-list').nbn_list({
            sortFunction: _recordCountSortFunction,
            elementRenderFunction: function(datasetProviderData) {
                var organisationProviderTitle = $('<div>').addClass('nbn-dataset-provider');
                if(datasetProviderData.imageUrl) {
                    organisationProviderTitle.append($('<span>')
                        .css('backgroundImage', 'url(' + datasetProviderData.imageUrl + ')')
                        .addClass('nbn-dataset-provider-logo')
                    );
                }
                organisationProviderTitle.append($('<a>')
                    .html(datasetProviderData.name)
                    .attr('href','http://data.nbn.org.uk/organisation/organisation.jsp?orgKey=' + datasetProviderData.organisationKey)
                    .addClass('nbn-dataset-provider-title')
                )

                return $('<div>')
                    .append(organisationProviderTitle)
                    .append($('<ul>').addClass('dataset-list').nbn_list({
                        elementRenderFunction: function(datasetData) {
							return $('<a>')
                                .addClass('dataset-name')
                                .html(datasetData.name)
                                .attr('href', 'http://data.nbn.org.uk/datasetInfo/taxonDataset.jsp?dsKey=' + datasetData.datasetKey)
                                .attr('target','_blank')
                                .add($('<span>').addClass("dataset-resolution").html(datasetData.datasetResolution))
                                .add($('<span>').addClass("dataset-userResolution").html((datasetData.hasFullAccess) ? "Full" : datasetData.userResolution))
                                .add(_renderDatasetBoolean(datasetData.sensitiveAccess).addClass('dataset-sensitiveAccess'))
                                .add(_renderDatasetBoolean(datasetData.downloadRawData).addClass('dataset-downloadRawData'))
                                .add(_renderDatasetBoolean(datasetData.viewAttributes).addClass('dataset-viewAttributes'))
                                .add(_renderDatasetBoolean(datasetData.viewRecorder).addClass('dataset-viewRecorder'));
                        },
                        data: datasetProviderData.datasets
                    }));
            }
        })};

        var datasetsNotSelectedTable = function() {
            return $('<ul>').addClass('dataset-provider-list').nbn_list({
            sortFunction: _recordCountSortFunction,
            elementRenderFunction: function(datasetProviderData) {
                if(datasetProviderData){
                    var organisationProviderTitle = $('<div>').addClass('nbn-dataset-provider').addClass('datasetlist').addClass('ui-widget');
                    if(datasetProviderData.imageUrl) {
                        organisationProviderTitle.append($('<span>')
                            .css('backgroundImage', 'url(' + datasetProviderData.imageUrl + ')')
                            .addClass('nbn-dataset-provider-logo')
                        );
                    }
                    organisationProviderTitle.append($('<a>')
                        .html(datasetProviderData.name)
                        .attr('href','http://data.nbn.org.uk/organisation/organisation.jsp?orgKey=' + datasetProviderData.organisationKey)
                        .addClass('nbn-dataset-provider-title')
                    )
                    return $('<div>')
                        .append(organisationProviderTitle)
                        .append($('<ul>').addClass('dataset-list').nbn_list({
                            elementRenderFunction: function(datasetData) {
                                return $('<a>')
                                    .addClass('dataset-name')
                                    .html(datasetData.name)
                                    .attr('href', 'http://data.nbn.org.uk/datasetInfo/taxonDataset.jsp?dsKey=' + datasetData.datasetKey)
                                    .attr('target','_blank')
                                    .add($('<span>').addClass("dataset-resolution").html(datasetData.datasetResolution))
                                    .add($('<span>').addClass("dataset-userResolution").html((datasetData.hasFullAccess) ? "Full" : datasetData.userResolution))
                                    .add(_renderDatasetBoolean(datasetData.sensitiveAccess).addClass('dataset-sensitiveAccess'))
                                    .add(_renderDatasetBoolean(datasetData.downloadRawData).addClass('dataset-downloadRawData'))
                                    .add(_renderDatasetBoolean(datasetData.viewAttributes).addClass('dataset-viewAttributes'))
                                    .add(_renderDatasetBoolean(datasetData.viewRecorder).addClass('dataset-viewRecorder'));
                            },
                            data: datasetProviderData.datasets
                        }));
                }
            }
        })};
        
        var datasetsNotViewableTable = function() {
            return $('<ul>').addClass('dataset-provider-list').nbn_list({
            sortFunction: _recordCountSortFunction,
            elementRenderFunction: function(datasetProviderData) {
                var organisationProviderTitle = $('<div>').addClass('nbn-dataset-provider');
                if(datasetProviderData.imageUrl) {
                    organisationProviderTitle.append($('<span>')
                        .css('backgroundImage', 'url(' + datasetProviderData.imageUrl + ')')
                        .addClass('nbn-dataset-provider-logo')
                    );
                }
                organisationProviderTitle.append($('<a>')
                    .html(datasetProviderData.name)
                    .attr('href','http://data.nbn.org.uk/organisation/organisation.jsp?orgKey=' + datasetProviderData.organisationKey)
                    .addClass('nbn-dataset-provider-title')
                )
                return $('<div>')
                    .append(organisationProviderTitle)
                    .append($('<ul>').addClass('dataset-list').nbn_list({
                        elementRenderFunction: function(datasetData) {
                            return $('<a>')
                                .addClass('dataset-name')
                                .html(datasetData.name)
                                .attr('href', 'http://data.nbn.org.uk/datasetInfo/taxonDataset.jsp?dsKey=' + datasetData.datasetKey)
                                .attr('target','_blank');
                        },
                        data: datasetProviderData.datasets
                    }));
            }
        })};

        var _createSortByButtonSet = function(sortableTables) {
            return $('<div class="dataset-list-sortByControl" id="radio">')
                .append($('<span>')
                    .html('Sort By :')
                    .addClass('dataset-list-sortByControl-label')
                )
                .append('<input type="radio" id="nbn-datasetNoOfRecordsSortBtn" name="nbn-datasetSortButton" checked="checked"/><label for="nbn-datasetNoOfRecordsSortBtn">Number of records</label>')
                .append('<input type="radio" id="nbn-datasetAlphaSortBtn" name="nbn-datasetSortButton"/><label for="nbn-datasetAlphaSortBtn">Organisation name</label>')
                .buttonset()
                .change(function(){
                    var selected = $(':checked',this).attr('id');
                    if(selected == 'nbn-datasetNoOfRecordsSortBtn'){
                        $.each(sortableTables,function(){
                            this.nbn_list('setSortFunction',_recordCountSortFunction);
                        });
                    }else if(selected == 'nbn-datasetAlphaSortBtn'){
                              $.each(sortableTables,function(){
                                   this.nbn_list('setSortFunction',_organisationNameSortFunction);
                        });
                    }
                });
        };

        var headerDatasetsSelected = $('<div class="ui-widget-header">')
            .append($('<span>').addClass('dataset-name-heading').html('Datasets you have selected'))
            .append($('<span>').addClass('dataset-resolution-heading').html('Dataset Resolution'))
            .append($('<span>').addClass('dataset-userResolution-heading').html('Your Resolution'))
            .append($('<span>').addClass('dataset-sensitiveAccess-heading').html('Sensitive Access'))
            .append($('<span>').addClass('dataset-downloadRawData-heading').html('Download Raw Data'))
            .append($('<span>').addClass('dataset-viewAttributes-heading').html('View Attributes'))
            .append($('<span>').addClass('dataset-viewRecorder-heading').html('View Recorder'));

        var headerDatasetsNotSelected = $('<div class="ui-widget-header">')
            .append($('<span>').addClass('dataset-name-heading').html('Datasets you have not selected'))
            .append($('<span>').addClass('dataset-resolution-heading').html('Dataset Resolution'))
            .append($('<span>').addClass('dataset-userResolution-heading').html('Your Resolution'))
            .append($('<span>').addClass('dataset-sensitiveAccess-heading').html('Sensitive Access'))
            .append($('<span>').addClass('dataset-downloadRawData-heading').html('Download Raw Data'))
            .append($('<span>').addClass('dataset-viewAttributes-heading').html('View Attributes'))
            .append($('<span>').addClass('dataset-viewRecorder-heading').html('View Recorder'));

        var headerDatasetsNotViewable = function(){
                return $('<div class="ui-widget-header">')
                    .empty()
                    .append($('<span>').addClass('dataset-heading datasetName')).html('Datasets you don\'t have access to. ')
                    .html(
                    (interactiveMapper.getUser()) ?
                        'Datasets you don\'t have access to. To request access, click on them and look for \'Apply for access\'.'
                    :
                        'Datasets you don\'t have access to. You will need to login to apply for access to them.'
                    )
        };

        var tabContainerDiv;
        
        var addDynamicTab = function(tabName, contentHeader, contentData){
            tabContainerDiv.nbn_dynamictabs('add',tabName,
                $('<div>')
                    .addClass('dataset-list-container')
                    .append(contentHeader)
                    .append(contentData)
            );
        }

        return $('<div>')
            .addClass('dataset-dialog')
            .dialog({
                title: 'Datasets',
                autoOpen: false,
                modal: true,
                resizable: false,
                width: 830,
                height: 590,
                open: function() { //on open refresh
                    var _me = $(this);
                    _me.empty();
                    var loadingDiv = $('<div>').addClass('loading');
                    _me.append(loadingDiv);
                    $.getJSON('OrganisationAcknowledgementServlet' + nbn.util.ArrayTools.joinAndPrepend(nbn.util.ArrayTools.fromObject(nbnMapLayer.getNBNSpeciesLayerFilters()),'&', '?'), function(response) {
                        var dataDiv = $('<div>')
                        dataDiv.append(tabContainerDiv = $('<div>').nbn_dynamictabs());
                        _me.append($('<div>')
                            .addClass('nbn-datasetmetadata-caveat')
                            .append($('<div>')
                              .addClass('nbn-datasetmetadata-caveat-header')
                              .html("IMPORTANT NOTICE TO THOSE INTENDING TO USE THESE DATA FOR RESEARCH, PLANNING OR LAND MANAGEMENT PURPOSES")
                            ).append($('<div>')
                              .addClass('nbn-datasetmetadata-caveat-text')
                              .html("You may not have full access to all the datasets relating to this query. Your current level of access can be identified in the list provided below.  You are STRONGLY ADVISED to seek improved access by selecting the relevant datasets below and then clicking to request improved access.  Please ensure that your use of these data complies with the NBN Gateway <a href=\"/help/popups/generalTerms.jsp\" target=\"_blank\">Terms and Conditions</a>")
                            )
                        );
                        _me.append(dataDiv);
                    var sortableTables = new Array();
                        if(response){
                            if(response.datasetsUnviewable){
                                sortableTables.push(datasetsNotViewableTable().nbn_list('setData',response.datasetsUnviewable));
                                addDynamicTab(
                                    'Datasets Not Available',
                                    headerDatasetsNotViewable(),
                                    sortableTables[sortableTables.length - 1]
                                );
                            }
                            if(response.datasetsNotUsed){
                                sortableTables.push(datasetsNotSelectedTable().nbn_list('setData',response.datasetsNotUsed));
                                addDynamicTab(
                                    'Datasets Not Selected',
                                    headerDatasetsNotSelected,
                                    sortableTables[sortableTables.length - 1]
                                );
                            }
                            if(response.datasetsUsed){
                                sortableTables.push(datasetsSelectedTable().nbn_list('setData',response.datasetsUsed));
                                addDynamicTab(
                                    'Datasets Selected',
                                    headerDatasetsSelected,
                                    sortableTables[sortableTables.length - 1]
                                );
                            }
                            loadingDiv.fadeOut('slow');
                            tabContainerDiv.tabs();
                            _me.append(_createSortByButtonSet(sortableTables));
                        }else{
                            loadingDiv.fadeOut('slow');
                            _me.append($('<div>').html('<h2>You don\'t currently have any species data on the map.</h2>'));
                        }
                    });
                }
            });
    };

};
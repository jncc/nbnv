/**
*
* @author		:- Christopher Johnson
* @date			:- 23rd-Feb-2011
* @description	:- This JScript file defines the logic of switching between the different map services based on what filters are set.
* @dependencies	:-
*	jquery
*	nbn.layer.ArcGISMap
*/

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};

nbn.layer.SpeciesLayer = function(hosts, googleMap, options) {
	var _me = this;
	var _modeAttribute = new nbn.util.ObservableAttribute('Mode');
	var _datasetFilter = new nbn.layer.ArcGisMapFilter();
	var _temporalFilter = new nbn.layer.ArcGisMapFilter();
	var _abundanceFilter = new nbn.layer.SwitchableArcGisMapFilter({filters:{abundance:"all"}});
	var _modeFunctions = [], _modeNameLookup = [];
	
	var additionalFilterParams = {}, _constructionFilterParams = {};
	var _arcGisMap, _descriptionAttribute, _layerParameters;

    $.extend(this,
		_arcGisMap = new nbn.layer.ArcGISMap(hosts, undefined, googleMap, options),
		_modeAttribute,
		_descriptionAttribute = new nbn.util.ObservableAttribute('Description', 'No species selected'),
		_layerParameters = new nbn.util.ObservableAttribute('LayerParameters')
	);
	
	this.Modes = new function() {
		var _previousNameResolvingRequest;
		
		var _creationFunctionManager = new function(_self) {	
			this.createNBNSpeciesLayerMode = function(name, func) {
				_self[name] = _modeFunctions.push(function(){
					if(_previousNameResolvingRequest)
						_previousNameResolvingRequest.abort();
					func.apply(this,arguments);
				})-1;
				_modeNameLookup[_self[name]] = name; //store the id to do a lookup later
			};
		}(this);
		
		var _setDatasets = function(datasets) {
			if(datasets && datasets.length > 0) {
				var datasetKeys = [];
				for(var i in datasets)
					datasetKeys.push(datasets[i].datasetKey);
				_datasetFilter.setFilter({ //set the filter to the correct state
					filters: {
						datasets: datasetKeys.join(',')
					}
				});
			}
			else
				_datasetFilter.clearFilter();
		};
		
		var _createDatasetFilteringSummary = function(datasets) {
			if(datasets && datasets.length > 0) {
				if(datasets.length == 1)
					return ' filtered by ' + datasets[0].name;
				else
					return ' filtered by ' + datasets.length + ' datasets.';
			}
			else
				return '';
		};
			
		_creationFunctionManager.createNBNSpeciesLayerMode('SPECIES', function(species, datasets) {
			nbn.util.EntityResolver.resolve({
					species: species,
					datasets: datasets
				}, function(data) {
					_setDatasets(data.datasets);
					_abundanceFilter.setEnabled(true);
					_constructionFilterParams = additionalFilterParams = {species: data.species.taxonVersionKey};
					_arcGisMap.setMapService('SingleSpecies/' + data.species.taxonVersionKey);
					_descriptionAttribute.setDescription('Single Species Map for ' + data.species.name + _createDatasetFilteringSummary(data.datasets));
					_layerParameters.setLayerParameters(data);
			});
		});
		
		_creationFunctionManager.createNBNSpeciesLayerMode('DESIGNATION', function(designation, datasets) {
			nbn.util.EntityResolver.resolve({
					designation: designation,
					datasets: datasets
				}, function(data) {
					_setDatasets(data.datasets);
					_abundanceFilter.setEnabled(false);
					_constructionFilterParams = {designation: data.designation.code};
					additionalFilterParams = {desig: data.designation.code};
					_arcGisMap.setMapService('DesignationSpeciesDensity/' + data.designation.code);
					_descriptionAttribute.setDescription('Designation Map for ' + data.designation.name + _createDatasetFilteringSummary(data.datasets));
					_layerParameters.setLayerParameters(data);
			});
		});
		
		_creationFunctionManager.createNBNSpeciesLayerMode('SINGLE_DATASET', function(dataset) {
			nbn.util.EntityResolver.resolve({
					dataset: dataset
				}, function(data) {
					_datasetFilter.clearFilter();
					_abundanceFilter.setEnabled(false);
					_constructionFilterParams = {dataset: data.dataset.datasetKey};
					additionalFilterParams = {datasets: data.dataset.datasetKey};
					_arcGisMap.setMapService('DatasetSpeciesDensity/' + data.dataset.datasetKey);
					_descriptionAttribute.setDescription('Single Dataset map for ' + data.dataset.name);
					_layerParameters.setLayerParameters(data);
			});	
		});
		
		_creationFunctionManager.createNBNSpeciesLayerMode('NONE', function() {
			_datasetFilter.clearFilter();
			_constructionFilterParams = additionalFilterParams = {};
			_arcGisMap.setMapService();
			_descriptionAttribute.setDescription('No species selected');
			_layerParameters.setLayerParameters();
		});
	};
	
	delete this.setMapService;
	delete this.setDescription;
	delete this.setLayerParameters;
	
	this.addLayerFilter(_datasetFilter);
	this.addLayerFilter(this.YearFilter = _temporalFilter); //publicise the year filter
	this.addLayerFilter(this.AbundanceFilter = _abundanceFilter); //publicise the year filter
	
	this.getNBNSpeciesLayerFilters = function() {
		return $.extend({},additionalFilterParams,this.getCurrentFilters());
	};
	
	this.getReconstructionObject = function() {
		var toReturn = {type: 'Species'};
		if(_modeAttribute.getMode() !== _me.Modes.NONE) { //if this mode is filtering
			$.extend(toReturn, {
				mode: _modeNameLookup[_modeAttribute.getMode()]
			}, _constructionFilterParams);
			
			if(_datasetFilter.isFiltering()) $.extend(toReturn, _datasetFilter.getFilter().filters); //add datasets if they exist
			if(_temporalFilter.isFiltering()) $.extend(toReturn, _temporalFilter.getFilter().filters); //add temp filter if it exists
		}
		return toReturn;
	};
	
	this.setMode = function(mode) {
		try {
			var args = Array.prototype.slice.call(arguments, 1); //remove the first element from the arguments as that is the mode
			var modeDetails = _modeFunctions[mode].apply(this,args);
			_modeAttribute.setMode(mode); //call the overridden setMode method
		}
		catch(err) {
			throw "Unable to set mode " + options.mode + " " + err;
		}
	};
	
	this.getSpeciesLayerServiceName = function() { //define the function which returns the current Service name for the current set mode
		switch(_me.getMode()) {
			case _me.Modes.DESIGNATION 		: return "Designation Species Density";
			case _me.Modes.SINGLE_DATASET 	: return "Dataset Species Density";
			case _me.Modes.SPECIES	 		: return "Single Species Layer";
			case _me.Modes.NONE				: return "No Species Layer Selected";
			default	: return "Unknown Mode";
		}		
	};

	this.addCurrentVisibleLayersUpdateListener = function(filter) { //override the addCurrentVisibleLayersUpdateListener to notify of changes about abundance
		_abundanceFilter.addFilterUpdateListener(filter);
		_arcGisMap.addCurrentVisibleLayersUpdateListener(filter);
	};
	
	this.removeCurrentVisibleLayersUpdateListener = function(filter) { //override the removeCurrentVisibleLayersUpdateListener to notify of changes about abundance
		_abundanceFilter.removeFilterUpdateListener(filter);
		_arcGisMap.removeCurrentVisibleLayersUpdateListener(filter);
	};

	(function() { //define constuction function
		var initialMode = options.mode || 'NONE'; //set the default mode
		switch(initialMode) {
			case 'SPECIES':
				_me.setMode(_me.Modes[initialMode], options.species, options.datasets);
			break;
			case 'DESIGNATION':
				_me.setMode(_me.Modes[initialMode], options.designation, options.datasets);
			break;
			case 'SINGLE_DATASET':
				_me.setMode(_me.Modes[initialMode], options.dataset);
			break;
			case 'NONE' :
				_me.setMode(_me.Modes[initialMode]);
			break;
			default:
				throw "Unknown Species Layer Mode : " + options.mode;
		}
		
		if(options.startyear || options.endyear) {//initalise the temportal filter
			_temporalFilter.setFilter({
				filters: {
					startyear: options.startyear,
					endyear: options.endyear
				}
			});
		}
	})();
}
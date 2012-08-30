/**
*
* @author       :- Christopher Johnson
* @date         :- 13th-December-2010
* @description  :- This JScript was created to replace the ArcGIS Library so that tiling can be done performantly.
* @dependencies :-
*   jquery
*   google maps
*	nbn.layer.ExtendedProjectedImageMap
*   nbn.uti.ArrayTools
*   nbn.util.ObservableAttribute
*   nbn.util.ObservableCollection
*   nbn.util.PropagatingObservableCollection
*   nbn.util.Logger
*/
window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};

nbn.layer.ArcGISMap = function(mapHosts, mapService, map, options) {
	options = options || {}; //ensure options is set
	var _me = this;
	var _updateTiles;
	
	var currVisLayers = new nbn.util.ObservableAttribute('CurrentVisibleLayers', [], nbn.util.ArrayTools.equals), currFilters = new nbn.util.ObservableAttribute('CurrentFilters', {});
	var currZoom = new nbn.util.ObservableAttribute('CurrentZoom'), currProjection = new nbn.util.ObservableAttribute('CurrentProjection');
		
	var _updateCurrentLayerFilterContainer = function() { //this function will combine all the filters and return the resultant filter
		var objOfFilters = {}, arrOfVisibleLayers = [];
		_me.forEachLayerFilter(function(currElement) {
			if(currElement.isFiltering()) { //is this arcgismap filter filtering?
				var currFilterContainer = currElement.getFilter(currZoom.getCurrentZoom()); //execute the filter function
				if(currFilterContainer.filters)
					$.extend(objOfFilters, currFilterContainer.filters);
				if(currFilterContainer.visibleLayers)
					arrOfVisibleLayers.push(currFilterContainer.visibleLayers);
			}
		});
		currFilters.setCurrentFilters(objOfFilters);
		currVisLayers.setCurrentVisibleLayers(nbn.util.ArrayTools.uniqueFlatten(arrOfVisibleLayers));
	};
	
	var _createIdentifyURL = function(latLng) {
		var boundedBox = map.getViewportBBox();
		var mapDiv = map.getViewportDimensions();
		var bboxSR = _me.getCurrentProjection().latLngEPSG;
		return encodeURI(
			_me.getHostsNextElement() + _me.getMapService() + '/identify?geometry={' +
			'x:' + latLng.lon +
			',y:' + latLng.lat + ',spatialReference:{wkid:' + bboxSR + '}}&geometryType=esriGeometryPoint&mapExtent={' +
			'xmin:' + boundedBox.xmin +
			',ymin:' + boundedBox.ymin +
			',xmax:' + boundedBox.xmax +
			',ymax:' + boundedBox.ymax + ',spatialReference:{wkid:' + bboxSR + '}}' +
			'&tolerance=' + _me.getIdentify() + '&sr=' + bboxSR + '&imageDisplay=' + mapDiv.width + ',' + mapDiv.height + ',96' +
			nbn.util.ArrayTools.joinAndPrepend(nbn.util.ArrayTools.fromObject(_me.getCurrentFilters()),'&') +
			'&layers=all:' + _me.getCurrentVisibleLayers().join(',') +'&returnGeometry=false&f=json&callback=?'
		);
	};
	
	var _checkIfInPositionToMakeACall = function(typeOfCall) {
		if(!_me.getMapService() || !_me.getHosts())
			throw "I can not construct a valid url as either a map service or host has not been defined. Therefore an " + typeOfCall + " call can not be performed";
	};
	
	function createLegendURL() {
		var urlParams = $.extend({}, _me.getCurrentFilters(), { callback:'?', f:'pjson', layers: _me.getCurrentVisibleLayers().join(',')});
		return encodeURI(
			_me.getHostsNextElement() + _me.getMapService() + '/legend' + 
			nbn.util.ArrayTools.joinAndPrepend(nbn.util.ArrayTools.fromObject(urlParams),'&', '?') 
		);
	}
	
	this.identifyFeature = function(latLng, callback) { //call the identification server
		_checkIfInPositionToMakeACall('identify');
		return $.getJSON(_createIdentifyURL(latLng), callback);
	};
	
	this.getLegend = function(callback) {
		_checkIfInPositionToMakeACall('legend');
		return $.getJSON(createLegendURL(), callback);
	};

	$.extend(this,
		new nbn.util.ObservableAttribute('ToRenderLogic',options.toRenderLogic || function(combinedFilter) {
			return combinedFilter.getCurrentVisibleLayers().length != 0;
		}),
		new nbn.util.ObservableAttribute('Identify', options.identifyTolerance || 3),
		new nbn.util.ObservableAttribute('UpdateDelay', options.updateDelay || 300),
		new nbn.util.ElementIterator('Hosts', mapHosts),
		new nbn.util.ObservableAttribute('MapService', mapService),
		new nbn.util.PropagatingObservableCollection('LayerFilter', ['Filter']),
		currVisLayers, currFilters, currZoom, currProjection
	);
	
	$.extend(this, new map.ImageMapConstructor(function(updateTiles) {
		var _updateTimeoutHandle;
		_updateTiles = function() {
			clearTimeout(_updateTimeoutHandle); //clear the original timeout
			_updateTimeoutHandle = setTimeout(function() {
				updateTiles();
			},_me.getUpdateDelay());
		};
		
		this.isToRender = function() {
			return (_me.getHosts() && _me.getMapService() && _me.getToRenderLogic()(_me)) == true;
		};
		
		this.getTileUrl = function(position, zoom, projection) {
			currZoom.setCurrentZoom(zoom);
			currProjection.setCurrentProjection(projection);
			if(this.isToRender()) {
				return encodeURI(
					_me.getHostsNextElement() + _me.getMapService() + 
                                        '?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap' +
                                        '&BBOX=' + position.xmin + ',' + position.ymin + ',' + position.xmax + ',' + position.ymax + 
					'&WIDTH=' + _me.tileSize.width + 
                                        '&HEIGHT=' + _me.tileSize.height +
					//'&imageSR=' + projection.imageEPSG +
					'&SRS=EPSG:' + projection.latLngEPSG +
					nbn.util.ArrayTools.joinAndPrepend(nbn.util.ArrayTools.fromObject(_me.getCurrentFilters()),'&') +
					'&LAYERS='+ _me.getCurrentVisibleLayers().join(',') + 
					'&TRANSPARENT=true' +
					'&FORMAT=image/png'
				);
			}
		};
	}, map.getUnderlyingMap(), options));
	
	delete this.setCurrentVisibleLayers;
	delete this.setCurrentFilters;
	delete this.setCurrentZoom;
	delete this.setCurrentProjection;
	
	//Add action listeners to changes in attributes
	this.addToRenderLogicUpdateListener({ToRenderLogic: _updateTiles});
	this.addMapServiceUpdateListener({MapService: _updateTiles}); 
	this.addCurrentZoomUpdateListener({update: _updateCurrentLayerFilterContainer});
	
	var currentFilterUpdate = {
		update: function() {
			_updateCurrentLayerFilterContainer();
			_updateTiles();
		}
	};
	this.addLayerFilterCollectionUpdateListener(currentFilterUpdate);
	this.addLayerFilterPropagatingObservableCollectionUpdateListener(currentFilterUpdate);
};
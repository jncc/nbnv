/**
*
* @author		:- Christopher Johnson
* @date			:- 27-April-2011
* @description	:- This JScript is the the Open Layers underlying mapping technology implementation for the NBN Interactive Mapping Tool
* @dependencies	:-
*	OpenLayersSpatialReferenceSystemDefs.js
*	OpenLayers.js
*	virtualearth.js
*	proj4js-combined.js
*	nbn.mapping.openlayers.OpenLayersImageMap.js
*/
window.nbn = window.nbn || {};
nbn.mapping = nbn.mapping || {};
nbn.mapping.openlayers = nbn.mapping.openlayers || {};

nbn.mapping.openlayers.OpenLayersMap = function(options, interactiveMap) {
	var EPSG_27700_RESOLUTIONS = [45074.742999999995, 22537.389, 11268.6965, 5634.3485, 2817.174, 1408.587, 704.2935, 352.147, 176.0735, 88.0365, 44.0185, 22.009, 11.005, 5.502, 2.751, 1.376, 0.688, 0.344];
	var _me = this;
	var _infoWindow, _zoomAttribute, _baseLayerAttribute;
	
	var _map = new OpenLayers.Map({
		maxExtent: new OpenLayers.Bounds(-20037508.34,-20037508.34,20037508.34,20037508.34),
		maxResolution:156543.0339,
		units:'m',
		projection: new OpenLayers.Projection("EPSG:3857"),
		displayProjection: new OpenLayers.Projection("EPSG:4326")
	}); //create open layers map
	
	var _mappingManager = new nbn.mapping.openlayers.OpenLayersMappingLayoutManager(_map);

	$.extend(this, //extend the attribute which I wish to make public
		_zoomAttribute = new nbn.util.ObservableAttribute('Zoom',_map.getZoom()),
		_baseLayerAttribute = new nbn.util.ObservableAttribute('BaseLayer'),
		new nbn.util.HashedObservableCollection('BaseLayerType', 'ID')
	);
		
	delete this.setZoom; //delete redundant methods
	
	/*Start Defining external private methods*/	
	this._registerNBNLayersManipulation = function(nbnLayers) {
		var _setLayerIndexes = function(collection) {
			for(var i in collection) 
				_map.setLayerIndex(collection[i].layer.layer, i);
		};
		
		nbnLayers.addNBNMapLayerCollectionUpdateListener({
			add: function(collection, added) {
				_map.addLayer(added.layer.layer);
				_setLayerIndexes(collection);
			},
			remove: function(collection, removed) {
				_map.removeLayer(removed.layer.layer);
				_setLayerIndexes(collection);
			},
			reposition : function(collection, moved, position) {
				_map.setLayerIndex(moved.layer.layer, position);
			}
		});
	}

	this._registerPickingListener = function(nbnLayers) {
		var pickerController = new nbn.layer.picker.PickerInfoWindow(this, nbnLayers); //create the picker window
		var clickHandler = new OpenLayers.Handler.Click({ 'map': _map },{
			'click': function(event) {
				var lonlat = _map.getLonLatFromPixel(event.xy);
				pickerController.createOrUpdateInfoWindowIfApplicable(lonlat);
			}}
		);
		clickHandler.activate();
	};
	
	var _updateBaseLayer = function(baseLayer) {
		_map.addLayer(baseLayer.layer);
		_baseLayerAttribute.setBaseLayer(baseLayer); // update the state of the base layer attribute
	};
	
	/*Start Defining external public methods*/	
	this.setBaseLayer = function(baseLayer) {
		var centre = _map.getCenter();
		var zoom = _map.getZoom();
		var oldProjection = _map.getProjectionObject();
		_map.removeLayer(_map.baseLayer); //remove the current base layer
		_updateBaseLayer(baseLayer);
		_map.setCenter(centre.transform(oldProjection, _map.getProjectionObject()),zoom); //this line is required to fix a bug in OpenLayers 2.10 (Ticket #1249)#
	};
	
	this.closePickingDialogMapDialog = function() {//close the picking dialog if it is open
		if(_infoWindow) {
			_infoWindow.destroy();
			_infoWindow = undefined;
		}
	};
	
	this.showPickingDialogMapDialog = function(containerDiv,position) {
		this.closePickingDialogMapDialog(); //close the picking dialog if it is open
		var measurements = containerDiv.measure();
		_infoWindow = new OpenLayers.Popup.FramedCloud("_infoWindow", position, new OpenLayers.Size(300,300), '<div style="width: ' + measurements.outerWidth + 'px; height: ' + measurements.outerHeight + 'px;" id="picker_100">', undefined, true);

		_map.addPopup(_infoWindow);
		$('#picker_100').empty().append(containerDiv);
	};
	
	this.getUnderlyingMap = function() {
		return _map;
	};
	
	this.getMappingLayoutManager = function() {
		return _mappingManager;
	};
	
	this.getViewportDimensions = function() {
		var size = _map.getSize();
		return {
			height: size.h,
			width: size.w
		}
	};
	
	this.getViewportBBox = function() {
		var bbox = _map.getExtent().transform(_map.getProjectionObject(), new OpenLayers.Projection("EPSG:4326"));
		return {
			xmin: bbox.left,
			ymin: bbox.bottom,
			xmax: bbox.right,
			ymax: bbox.top
		}
	};
	
	this.initalize = function() {
		_map.render(_mappingManager.getMapDiv()[0]);
		if(!_me.hasBaseLayerType(options.baselayer)) {
			var replacementLayer = _me.getUnderlyingBaseLayerTypeArray()[0];
			interactiveMap.Logger.error('Invalid Selected Layer', 'The passed in layer : ' + options.baselayer + ' is unknown. Going to use ' + replacementLayer.getID() + ' instead.');
			_updateBaseLayer(replacementLayer);
		}
		else
			_updateBaseLayer(_me.getBaseLayerType(options.baselayer)); //set the base layer initially
		_mappingManager.append(_mappingManager.ControlsPosition.RIGHT_TOP, $('<div>').nbn_baseLayerSelector({map: _me})); //add the layer control now that the base layers have been created
	
		var extent = options.extent;
		_map.zoomToExtent(new OpenLayers.Bounds(extent.xmin, extent.ymin, extent.xmax, extent.ymax).transform(new OpenLayers.Projection("EPSG:4326"), _map.getProjectionObject()));
	};
	
	/*Add Scale widgets*/
	_mappingManager.append(_mappingManager.ControlsPosition.BOTTOM_LEFT, $('<div>').nbn_openLayersScaleWidget({openLayersMap: _map})); //pass in the open layers map of which the scale is based on
	
	this.ImageMapConstructor = nbn.mapping.openlayers.OpenLayersImageMap;
	
	/*Add event listeners*/
	this.addBaseLayerUpdateListener({
		update: function(type, baseLayer) {
			_mappingManager.setMappingForLayerType(baseLayer.mappingType); // update the mapping layout manager
		}
	});
	
	_map.events.register('zoomend', this, function() {
		_zoomAttribute.setZoom(_map.getZoom());
	});
	
	/*Define the Base layers and add them*/
	(function() {
		_me.addBaseLayerType(new nbn.mapping.openlayers.OpenLayersBingLayer({type : 'Shaded'}));
		_me.addBaseLayerType(new nbn.mapping.openlayers.OpenLayersBingLayer({type : 'Hybrid'}));
		_me.addBaseLayerType(new nbn.mapping.openlayers.OpenLayersBingLayer({type : 'Aerial'}));
		_me.addBaseLayerType((function() {
			var customOutlineLayer = new nbn.layer.ArcGISMap(nbn.util.ServerGeneratedLoadTimeConstants.gisServers, "arcgis/rest/services/general/CoastsAndVCs/MapServer/export",_me, {
				isBaseLayer: true,
				name: 'Outline',
				resolutions: EPSG_27700_RESOLUTIONS
			});
			var _filter = new nbn.layer.ArcGisMapFilter();
			_filter.setFilter({	visibleLayers:['0','1','2']	});
			customOutlineLayer.layer.projection = new OpenLayers.Projection("EPSG:27700"); //projection the layer should be added in
			customOutlineLayer.addLayerFilter(_filter);
			return new nbn.layer.BaseLayer(customOutlineLayer);
		})());
		_me.addBaseLayerType(new nbn.layer.BaseLayer(new nbn.mapping.openlayers.OpenLayersLayer(
				new OpenLayers.Layer.WMS("OS Map", nbn.util.ServerGeneratedLoadTimeConstants.gisServers + "OS-Modern", {
					layers: "MiniScale-NoGrid,OS250k,OS50k,OS25k", 
					format:"image/png"}, {
					isBaseLayer:true, 
					projection:new OpenLayers.Projection("EPSG:27700"), 
					resolutions: EPSG_27700_RESOLUTIONS
				}),{
					name: 'Ordnance Survey', 	//name displayed in the selection boxes
					copyright: '&copy; Crown copyright and database rights 2011 Ordnance Survey [100017955]' 		//Copyright statement, which gets appended to the copyright controller
				}
			),{ id:'OS' })//ID which will be used in getMapURL
		);
	})();
}
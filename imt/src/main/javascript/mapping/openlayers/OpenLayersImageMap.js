/**
*
* @author		:- Christopher Johnson
* @date			:- 9-March-2011
* @description	:- This JScript will wrap up the ArcGISMap and allow it to act like a Open Map Layer 
*/
window.nbn = window.nbn || {};
nbn.mapping = nbn.mapping || {};
nbn.mapping.openlayers = nbn.mapping.openlayers || {};

nbn.mapping.openlayers.OpenLayersImageMap = function(projectedImageMapTypeOptions, openLayersMap, options) {
	var _me = this;
	
	var _getCurrentVisibility = function() {
		return _me.getEnabled() && ($.isFunction(projectedImageMapTypeOptions.isToRender) ? projectedImageMapTypeOptions.isToRender() : true);
	};
	
	var _updateVisibility = function() {
		_me.layer.setVisibility(_getCurrentVisibility());
	};
	
	projectedImageMapTypeOptions  = $.isFunction(projectedImageMapTypeOptions) ? new projectedImageMapTypeOptions(function() {
		_updateVisibility();
		_me.layer.redraw();
	}) : projectedImageMapTypeOptions;
	
	projectedImageMapTypeOptions.tileSize = {
		height: 256,
		width: 256
	};
	
	$.extend(this,
		new nbn.util.ObservableAttribute('Name',options.name || ''), //create attributes, with defaults
		new nbn.util.ObservableAttribute('Copyright',options.copyright || ''), 
		new nbn.util.ObservableAttribute('Enabled',options.enabled || true), 
		new nbn.util.ObservableAttribute('Opacity',options.opacity || 1),
		projectedImageMapTypeOptions
	);
	
	this.layer = new OpenLayers.Layer.ZoomDelayedTMS("Name", "f", { 
		'type':'png',
		'getURL': function(bounds) {
			return projectedImageMapTypeOptions.getTileUrl({
				xmin: bounds.left,
				ymin: bounds.bottom,
				xmax: bounds.right,
				ymax: bounds.top
			}, this.map.getZoom(), {
				imageEPSG : openLayersMap.getProjection().substring(5),
				latLngEPSG: openLayersMap.getProjection().substring(5) 
			});
		},
		buffer:0,
		tileZoomDelay:1000,
		isBaseLayer: options.isBaseLayer === true,
		opacity : options.opacity,
		visibility: _getCurrentVisibility(),
		resolutions: options.resolutions
	});
	
	$.extend(this, (function() { //this object takes the form of a nbn.util.loading.LoadingConsolidator but is specifically designed to hook into the open layers controls
		var loadingObs = new nbn.util.Observable();
		var maxTileLoadingCount = 0;
		
		_me.layer.events.register('loadstart', this, function() {
			loadingObs.notifyListeners('startedLoading');
		});
		
		_me.layer.events.register('loadend', this, function() {
			loadingObs.notifyListeners('completedLoading');
			maxTileLoadingCount = 0;
		});	
		_me.layer.events.register('loadcancel', this, function() {
			loadingObs.notifyListeners('completedLoading');
			maxTileLoadingCount = 0;
		});		
		
		_me.layer.events.register('tileloaded', this, function() {
			maxTileLoadingCount = ((_me.layer.numLoadingTiles+1) > maxTileLoadingCount) ? _me.layer.numLoadingTiles+1 : maxTileLoadingCount;
			loadingObs.notifyListeners('updatedLoading',1-(_me.layer.numLoadingTiles/maxTileLoadingCount));
		});
		
		return { //adapt the observable object
			addLoadingListener: loadingObs.ObservableMethods.addListener,
			removeLoadingListener: loadingObs.ObservableMethods.removeListener
		};
	})());

	this.addEnabledUpdateListener({
		Enabled: function(newVal) {
			_updateVisibility();
		}
	});
	
	this.addOpacityUpdateListener({
		Opacity: function(newVal) {
			_me.layer.setOpacity(newVal);
		}
	});
};
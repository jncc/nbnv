/**
*
* @author	    :- Christopher Johnson
* @date		    :- 25th-Jan-2011
* @description	:- This JScript creates an interactive mapper
* @dependencies	:-
*	layout/MappingLayoutManager.js
*	layout/StaticGridLayoutManager.js
*	nbn.util.ObservableCollection
*/
$.namespace("nbn.mapping.InteractiveMappingClient", function(toDrawTo, options) {
	/*DEFINE DEFAULT OPTIONS*/
	options = $.extend(true, {
		map: {
			type: nbn.mapping.openlayers.OpenLayersMap,
			baselayer: 'Hybrid',
			extent: {
				xmin: -14.489099982674913,
				xmax: 7.87906407581859,
				ymin: 49.825193671965025,
				ymax: 59.45733404137668
			}
		},
		initialLayers: [{type:"Species"}]
	}, options);

	/*START CREATING GOOGLE MAP AND PROVIDING EXTERNAL METHODS*/
	var _me = this; //store a reference to me
	var optionsCreator = this.ConstructionFactory = new nbn.mapping.construction.WidgetOptionsCreator(this); //create a widget options creator for this interactive map

	$.extend(this,
		new nbn.util.ObservableCollection('NBNMapLayer'),
		new nbn.util.user.CookieLoginable()
	); //extend an observable collection
	
	/*CONSTRUCT THE NBN INTERACTIVE MAPPER*/
	this.Map = new options.map.type(options.map, this);

	this.LoginFilter = optionsCreator.createLoginFilter();
	this.Logger = optionsCreator.createLogger();

	var mappingLayoutManager = this.Map.getMappingLayoutManager(); //wrap it up in a mapping manger
	var gridLayoutManager = this.StaticGridLayoutManager = new nbn.layout.StaticGridLayoutManager(mappingLayoutManager); //wrap the mapping manger into a grid layout manager
	toDrawTo.append(gridLayoutManager.getLayoutContainer()); //draw the map to whereever

	if(this.Map.initalize) //does the map need to be explicty initalized
		this.Map.initalize();

	/*POSITION THE WIDGETS*/
	gridLayoutManager.append(gridLayoutManager.ControlsPosition.STATIC_GRID_FOOTER,optionsCreator.createCopyrightWidget());
	mappingLayoutManager.append(mappingLayoutManager.ControlsPosition.BOTTOM_CENTER,optionsCreator.createLoadingWidget());
	mappingLayoutManager.append(mappingLayoutManager.ControlsPosition.RIGHT_BOTTOM,optionsCreator.createLayersContainer());
	mappingLayoutManager.append(mappingLayoutManager.ControlsPosition.TOP_RIGHT,optionsCreator.createGetURLWidget());
	mappingLayoutManager.append(mappingLayoutManager.ControlsPosition.TOP_LEFT,optionsCreator.createLoginWidget());

	this.Map._registerNBNLayersManipulation(this);
	this.Map._registerPickingListener(this);
	
	/*PICKING CLOSING FUNCTIONALITY*/
	(function(interactiveMap) {
		var _updateEventsToListenTo = ['CurrentVisibleLayers', 'CurrentFilters', 'Identify', 'ToRenderLogic', 'MapService']; //event types to listen to
		var _layerUpdateObject = { update: interactiveMap.Map.closePickingDialogMapDialog }; //action to perform on update
		var _setListenToEvents = function(addOrRemove, layer) {
			for(var i in _updateEventsToListenTo)
				layer[addOrRemove + _updateEventsToListenTo[i] + 'UpdateListener'](_layerUpdateObject);
		};
		
		interactiveMap.addNBNMapLayerCollectionUpdateListener({
			add: 	function(collection, added) {	_setListenToEvents('add', added.layer);		},
			remove: function(collection, removed) {	_setListenToEvents('remove', removed.layer);}
		});
	})(_me);

	/*SET UP INITIAL MAPS*/
	for(var i in options.initialLayers) {
		var currLayerToCreate = options.initialLayers[i];
		try {
			var layerCreationFunction = optionsCreator['createNBN' + currLayerToCreate.type + 'Layer'];
			if(layerCreationFunction)
				_me.addNBNMapLayer(layerCreationFunction.call(optionsCreator,currLayerToCreate));
			else
				 this.Logger.error('Attempted To Create an initial layer of Unknown type',currLayerToCreate.type);
		}
		catch(err) {
			this.Logger.error('There was an error trying to set initial layer ' + i, err);
		}
	}

	/*VALIDATE ALL THE CONTROLS*/
	gridLayoutManager.validate();
});
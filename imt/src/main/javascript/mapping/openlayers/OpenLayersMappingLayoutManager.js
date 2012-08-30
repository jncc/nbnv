/**
*
* @author       :- Christopher Johnson
* @date         :- 24th-June-2011
* @description  :- This JScript enables controls to be drawn to specific areas on the map.
*	Elements appended through this mapping manager will be floated above the map.
*/
window.nbn = window.nbn || {};
nbn.mapping = nbn.mapping || {};
nbn.mapping.openlayers = nbn.mapping.openlayers || {};

nbn.mapping.openlayers.OpenLayersMappingLayoutManager = function(map) {
	var _previousMappingForLayerType;
	var _positions = {}, _containers = {};
	var _elementObserver = new nbn.util.Observable();
	
	var _mapDiv;
	var _container = $('<div>').append(_mapDiv = $('<div>').css({
		height: '100%',
		width: '100%',
		position: 'absolute'
	}));

	var _createControlDiv = function(position) {
		return $('<div>')
			.addClass(_positions[position] + '_CONTAINER')
			.appendTo(_container);
	};
	
	this.append = function(position, elementToAdd) {
		if(!elementToAdd.jquery) { //if it is not a jquery object, then it is potentially a notifyable object, deal with accordingly
			_elementObserver.ObservableMethods.addListener(elementToAdd);
			elementToAdd = elementToAdd.representation;
		}
		if(_containers[position] == undefined)
			_containers[position] = _createControlDiv(position);
		elementToAdd.addClass(_positions[position] + '_WIDGET');
		_containers[position].append(elementToAdd);
	};
	
	this.setMappingForLayerType = function(type) {
		if(_previousMappingForLayerType)
			_container.removeClass(_previousMappingForLayerType); //remove all mapping for layer type classes
		if(type) //if a class exists
			_container.addClass(_previousMappingForLayerType = 'MAPPING_TYPE-' + type); //then add it
	};
	
	this.removeMappingForLayerType = function() {
		return this.setMappingForLayerType();
	};
	
	this.getLayoutContainer = function() {
		return _container;
	};
	
	this.validate = function() {
		map.updateSize();
		_elementObserver.notifyListeners('validate', _container.height(), _container.width()); //notify self positioning elements
	};
	
	this.ControlsPosition = new function() {
		var drawablePositions = ['TOP_LEFT', 'RIGHT_TOP', 'TOP_RIGHT', 'RIGHT_BOTTOM', 'BOTTOM_CENTER', 'BOTTOM_LEFT'];
		for(var i in drawablePositions) {
			var currPosition = drawablePositions[i];
			this[currPosition] = i;
			_positions[i] = currPosition; //define css name
		}
	};
	
	this.getMapDiv = function() {
		return _mapDiv;
	};
};
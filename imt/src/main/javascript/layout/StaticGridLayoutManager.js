/**
*
* @author       :- Christopher Johnson
* @date         :- 24th-June-2011
* @description  :- This JScript creates a static grid manager which wraps up the mapping manager 
*	and provides four areas around it which elements can be added to.
*/
window.nbn = window.nbn || {};
nbn.layout = nbn.layout || {};

nbn.layout.StaticGridLayoutManager = function(centrePositionLayoutManager) {
	var _viewpoint;
	var _controlPositionMap = [];
	var _surroundingGridPositions = {};
	
	var _container = $('<div>').addClass('STATIC_GRID_CONTAINER')
		.append(_viewpoint = centrePositionLayoutManager.getLayoutContainer()
			.addClass('STATIC_GRID_VIEWPOINT')
		);
	
	this.getLayoutContainer = function() {
		return _container;
	};
	
	this.append = function(position, elementToAdd) {
		_controlPositionMap[position].append(elementToAdd); // use the mapping array to link back to the element to add to
	};

	this.validate = function(elementToValidate) { //either validate a specific element or all of them
		if(elementToValidate)
			_controlPositionMap[position].validate();
		else { //no element specified, validate the specific one
			for(var i in _surroundingGridPositions)
				_surroundingGridPositions[i].validate();
		}
	};
	
	this.ControlsPosition = new function() {
		var _creationFunctionManager = new function(_self) {
			function _storeControlPositionMap(name, mapTo) {
				_self[name] = _controlPositionMap.push(mapTo)-1;
			};
			
			this.createStaticGridAppendFunction = function(name,animationPropertiesGeneratorFunc,elementsToAnimate) {
				_storeControlPositionMap(name,_surroundingGridPositions[name] = {
					representation: $('<div>').addClass(name), //define the representation
					append: function(elementToAdd) {
						if(!$.contains(_container[0], this.representation[0])) //lazy add to the dom to reduce document size
							this.representation.appendTo(_container); 
						this.representation.append(elementToAdd.addClass(name + '_WIDGET'));
						this.validate();
					},					
					validate: function() {
						var animationProperties = animationPropertiesGeneratorFunc();
						for(var i in elementsToAnimate)
							elementsToAnimate[i].representation.css(animationProperties);
						_viewpoint.css(animationProperties); //always validate the viewpoint
						centrePositionLayoutManager.validate(); //validate the viewpoints mapping manager
					}
				});
			};
		}(this);
		
		_creationFunctionManager.createStaticGridAppendFunction('STATIC_GRID_LEFT',function() {
			return {left : _surroundingGridPositions.STATIC_GRID_LEFT.representation.width()};
		});
		
		_creationFunctionManager.createStaticGridAppendFunction('STATIC_GRID_RIGHT',function() {
			return {right :  _surroundingGridPositions.STATIC_GRID_RIGHT.representation.width()};
		});
		
		_creationFunctionManager.createStaticGridAppendFunction('STATIC_GRID_HEADER',function() {
			return {top : _surroundingGridPositions.STATIC_GRID_HEADER.representation.height()};
		},[_surroundingGridPositions.STATIC_GRID_LEFT,_surroundingGridPositions.STATIC_GRID_RIGHT]);
		
		_creationFunctionManager.createStaticGridAppendFunction('STATIC_GRID_FOOTER',function() {
			return {bottom : _surroundingGridPositions.STATIC_GRID_FOOTER.representation.height()};
		},[_surroundingGridPositions.STATIC_GRID_LEFT,_surroundingGridPositions.STATIC_GRID_RIGHT]);
	};
};

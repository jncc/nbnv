/**
*
* @author		:- Christopher Johnson
* @date			:- 10th-December-2010
* @description	:- This JScript file will create a picker window of the current state of a list of NBNLayers 
* @dependencies	:-
*	nbn.util.ObservableCollection of type NBNLayers
*	nbn.layer.ArcGISMap
*	Google Maps V3
*	jquery ui
*/

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.picker = nbn.layer.picker || {};

nbn.layer.picker.PickerInfoWindow = function(map, nbnMapLayers) {
	this.createOrUpdateInfoWindowIfApplicable = function(position) { //create the content of the Info window to put into the create info window
		var displayFlag = false;
		var containerDiv = $('<div>').nbn_dynamictabs();
		
		var _addDynamicTab = function(layer, pickerToAdd) {
			var currpickerInfo = pickerToAdd.getInfoWindowContents(position);//all infoable windows have the ability to return a contents to be added given a position
			if(currpickerInfo) {
				displayFlag=true;
				containerDiv.nbn_dynamictabs('add', (pickerToAdd.name) ? pickerToAdd.name : layer, currpickerInfo);
			}
		};
		
		nbnMapLayers.forEachNBNMapLayer(function(currLayer){
			if(currLayer.picker) { //does this layer have a picker?
				if($.isArray(currLayer.picker)) {
					for(var i in currLayer.picker)
						_addDynamicTab(currLayer.layer, currLayer.picker[i]);
				}
				else
					_addDynamicTab(currLayer.layer, currLayer.picker);
			}
		});
		containerDiv.tabs();
                
		if(displayFlag)
			map.showPickingDialogMapDialog(containerDiv,position);
	};
}
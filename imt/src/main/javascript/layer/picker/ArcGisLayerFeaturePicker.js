/**
*
* @author		:- Christopher Johnson
* @date			:- 21-Feb-2010
* @description	:- This JScript file defines a means for the common logic of feature picking to be easily expanded. 
*
*	This class will initiate the JSON call required for identifying features on a map, whilst that is being processed a 
*	loading screen will be displayed. 
*
*	Processing of the results is caried out by the featureProcessor function which will return a jquery element representing the results. 
*	On completion, the results will fade in.
*
* @usage		:- new nbn.layer.picker.ArcGisLayerFeaturePicker(layer, function() {
*		return $('<div'); //returning the results
*	});
* @dependencies	:-
*	nbn.layer.ArcGISMap
*/

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.picker = nbn.layer.picker || {};

nbn.layer.picker.ArcGisLayerFeaturePicker = function(layerToQuery, featureProcessor) {
	var _lastIdentifyCall;
	this.getInfoWindowContents = function(position) { //function required by PickerInfoWindow
		if(layerToQuery.isToRender() && layerToQuery.getEnabled()) {
			var infoContainer = $('<div>').addClass("nbn-picker-container");	 //create the container
			var loadingDiv = $('<div>').addClass('loading');
			infoContainer.append(loadingDiv);
			
			if(_lastIdentifyCall) //if there is a _lastIdentifyCall, abort it 
				_lastIdentifyCall.abort(); 
				
			_lastIdentifyCall = layerToQuery.identifyFeature(position, function (results) {
				if($.isFunction(featureProcessor.abort)) //if the feature processor has an abort function, call it
					featureProcessor.abort(); 
				featureProcessor.createPickerDiv(results, position, function(resultsDiv) {
					loadingDiv.fadeOut('slow',function() {//once returned then this is done loading
						$(this).remove();
						infoContainer.append(resultsDiv.fadeIn('slow'));
					});	
				});
			});
			return infoContainer;
		}
		else
			return false;
	};
};
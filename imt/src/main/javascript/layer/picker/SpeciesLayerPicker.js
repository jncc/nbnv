window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.picker = nbn.layer.picker || {};

nbn.layer.picker.SpeciesLayerPicker = function(layerToQuery) {
    
    /*
	* This function will convert the result of the ESRI Rest Identify call to a feature array 
	* which can be used by the Species Layer Picker Servlet. The values specfied below correspond to nbn.common.feature.FeatureType
	*/
    function createFeaturesFoundArray(idresults) { 
        return $.map(idresults.results, function(currResult) {
            var attributes = currResult.attributes;
            if(attributes.gridRef || attributes.GRIDREF)
                return 'GRID:' + attributes.gridRef || attributes.GRIDREF;
            else if (attributes.adminSiteKey)
                return 'SITEBOUNDARY:' + attributes.adminSiteKey;
        });
    }

    /*The following function defines the resource that should be called depending on the current mode of the species layer*/ 
    function getResource(tab) {
        var server = nbn.util.ServerGeneratedLoadTimeConstants.data_api;
        var resourceURLs = {SpeciesList: server + '/taxonObservations/species', DatasetList: server + '/taxonObservations/datasets', Records: server + '/taxonObservations/datasets/observations'};
        return resourceURLs[tab];
    }

    /*This function will return the api resource url qualified with the parameters for a given layer's current state*/
    function getResourceParameters(resultsFromIdentify) {
        var toReturn = {};
        toReturn.featureID = resultsFromIdentify;
        toReturn.spatialRelationship = 'overlap';
        switch(layerToQuery.getMode()) {
            case layerToQuery.Modes.SPECIES :
                toReturn.ptvk = (layerToQuery.getNBNSpeciesLayerFilters()).species;
                break;
            case layerToQuery.Modes.DESIGNATION :
                toReturn.designation = (layerToQuery.getNBNSpeciesLayerFilters()).desig;
                break;
            case layerToQuery.Modes.SINGLE_DATASET :
                toReturn.datasetKey = (layerToQuery.getNBNSpeciesLayerFilters()).datasets;
                break;
        }
    return toReturn;
    }

    function getResourceWithParams(resultsFromIdentify, tab){
        return getResource(tab) + nbn.util.ArrayTools.joinAndPrepend(nbn.util.ArrayTools.fromObject(getResourceParameters(resultsFromIdentify)),'&','?');
    }

    /*This function will take a javascript object which has a name and perhaps a link attribute and create a label from it*/
    function createLabel(toLabel) {
        return (!toLabel.link) ? toLabel.name : $('<a>').attr('target','_blank').attr('href',toLabel.link).html(toLabel.name);
    }

    /*This function will render a dataset object as html. Creates a link to metadata and shows the providing organisation*/ 
    function createDatasetLabel(dataset) {
        return $('<div>')
        .append($('<a>').attr('target','_blank').attr('href',dataset.href).html('Info'))
        .append(' - ')
        .append($('<strong>').html(dataset.organisationName))
        .append(': ' + dataset.title);
    }
	
    /*This function creates the html representation of a record object*/
    function createobservationLabel(observation) {
        var toReturn = $('<span>').addClass('nbn-picker-' + ((observation.absence) ? 'absence' : 'presence') + 'record');
        if (observation.absence)
            toReturn.append($('<span>').addClass('nbn-picker-observationType').append('Absence : '));
        if (observation.sensitive) 
            toReturn.append($('<span>').addClass('nbn-picker-sensitiveobservation').append('Sensitive observation - '));
        toReturn.append(observation.pTaxonName + ' - Date: ' + observation.startDate + ' (' + observation.dateType + ')');
//        if (observation.location !== 'Site name protected') 
        toReturn.append(' - Location: ' + observation.location + ((typeof observation.siteName != 'undefined')?' (' + observation.siteName + ')': ''));
//        if (observation.observationer !== '') 
//            toReturn.append(' - observationer: ' + observation.observationer);
//        if (observation.determiner !== '') 
//            toReturn.append(' - Determiner: ' + observation.determiner);
        return toReturn;
    }
	
    /*START DEFINITIONS OF TAB CREATION FUNCTIONS*/
    function createSpeciesTabDiv(pickerResults) {
        var toReturn = $('<div>'); //create a div to be returned to and turned into a tab
        toReturn.append(nbn.util.ArrayTools.toJQueryRenderedList(pickerResults, function(taxonWithQueryStats, index){
            return createLabel(taxonWithQueryStats.taxon);
        }));
        return toReturn;
    }
	
    function createDatasetsTabDiv(pickerResults) {
        var toReturn = $('<div>'); //create a div to be returned to and turned into a tab
        toReturn.append(nbn.util.ArrayTools.toJQueryRenderedList(pickerResults, function(datasetWithQueryStats, index){
            return $('<div>').append($('<div>').html(createDatasetLabel(datasetWithQueryStats.taxonDataset))); 
        }));
        return toReturn;
    }
	
    function createObservationsTabDiv(pickerResults) {
        var toReturn = $('<div>'); //create a div to be returned to and turned into a tab
        toReturn.append(nbn.util.ArrayTools.toJQueryRenderedList(pickerResults, function(dataset, index){
            var datasetTitle = $('<div>').append($('<div>').html(createDatasetLabel(dataset))); //append the dataset as a title for the observations
            return datasetTitle.append(nbn.util.ArrayTools.toJQueryRenderedList(dataset.observations, function(observation, obsIndex){
                return createobservationLabel(observation);
            }));
        }));
        return toReturn;
    }
    /*END DEFINITIONS OF TAB CREATION FUNCTIONS*/
	
    $.extend(this, new nbn.layer.picker.ArcGisLayerFeaturePicker(layerToQuery, {
        createPickerDiv: function(resultsFromIdentify, position, callback) {
            if(resultsFromIdentify.length!==0) {

                var errorDiv = $('<div>').html('An error occured whilst trying to obtain a response from the picker server');
                var jqxhrs = [];
                $.each(resultsFromIdentify, function(index, identifier){

                    //Three api calls are required to get the species, datasets and observation data - these are chained together
                    var toReturn = $('<div>').addClass('nbn-picker-speciesResults');
                    toReturn.nbn_dynamictabs();
                    jqxhrs.push($.getJSON(getResourceWithParams(identifier, 'Records'), function(pickerResults){
                        toReturn.nbn_dynamictabs('add','Records',createObservationsTabDiv(pickerResults));
                    }).error(function(){
                        toReturn.nbn_dynamictabs('add','Records',errorDiv);
                    }));
                    jqxhrs.push($.getJSON(getResourceWithParams(identifier, 'DatasetList'), function(pickerResults){
                            toReturn.nbn_dynamictabs('add','Datasets',createDatasetsTabDiv(pickerResults));
                    }).error(function(){
                        toReturn.nbn_dynamictabs('add','Records',errorDiv);
                    }));
                    jqxhrs.push($.getJSON(getResourceWithParams(identifier, 'SpeciesList'), function(pickerResults){
                        toReturn.nbn_dynamictabs('add','Species',createSpeciesTabDiv(pickerResults));
                    }).error(function(){
                        toReturn.nbn_dynamictabs('add','Records',errorDiv);
                    }));
                    $.when.apply(this, jqxhrs).done(function(){
                        toReturn.tabs();
                        callback(toReturn);
                    });
                
                });
                
            }
            else
                callback($('<div>').html('No Results here'));
        },
		
        abort: function() {
            if(this.__lastRequest__) 
                this.__lastRequest__.abort(); //if there is a last request then abort it
        }
    }));
}
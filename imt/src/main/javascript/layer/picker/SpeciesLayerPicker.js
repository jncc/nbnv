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
	
	/*The following function defines the servlet which should be called depending on the current mode of the species layer*/ 
	function getSpeciesPickerServlet() {
		switch(layerToQuery.getMode()) {
			case layerToQuery.Modes.SPECIES : return 'SingleSpeciesPickerServlet';
			case layerToQuery.Modes.DESIGNATION : return 'DesignationPickerServlet';
			case layerToQuery.Modes.SINGLE_DATASET : return 'DatasetPickerServlet';
		}
	}
	
	/*This function will give the servlet url qualified with the parameters for a given layers current state*/
	function getSpeciesPickerServletWithParams(resultsFromIdentify, sitesFound) {	
		var servletParameters = layerToQuery.getNBNSpeciesLayerFilters();
		servletParameters.sites = sitesFound; //append the sites found to the parameter object for the layer
                //Jon says: this should return a url with query string for the api
		return getSpeciesPickerServlet() + nbn.util.ArrayTools.joinAndPrepend(nbn.util.ArrayTools.fromObject(servletParameters),'&','?');
	}
	
	/*This function will take a javascript object which has a name and perhaps a link attribute and create a label from it*/
	function createLabel(toLabel) {
		return (!toLabel.link) ? toLabel.name : $('<a>').attr('target','_blank').attr('href',toLabel.link).html(toLabel.name);
	}
	
	/*This function will render a dataset object as html. Creates a link to metadata and shows the providing organisation*/ 
	function createDatasetLabel(dataset) {
		return $('<div>')
			.append($('<a>').attr('target','_blank').attr('href',dataset.link).html('Info'))
			.append(' - ')
			.append($('<strong>').html(dataset.organisation))
			.append(': ' + dataset.name);
	}
	
	/*This function creates the html representation of a record object*/
	function createRecordLabel(record) {
		var toReturn = $('<span>').addClass('nbn-picker-' + ((record.presence) ? 'presence' : 'absence') + 'Record');
		
		if (!record.presence)
			toReturn.append($('<span>').addClass('nbn-picker-recordType').append('Absence : '));
		if (record.sensitive) 
			toReturn.append($('<span>').addClass('nbn-picker-sensitiveRecord').append('Sensitive record - '));
		
		toReturn.append(record.species + ' - ' + record.date);
		if (record.site !== 'Site name protected') 
			toReturn.append(' - Site: ' + record.site);
		if (record.recorder !== '') 
			toReturn.append(' - Recorder: ' + record.recorder);
		if (record.determiner !== '') 
			toReturn.append(' - Determiner: ' + record.determiner);
		return toReturn;
	}
	
	/*START DEFINITIONS OF TAB CREATION FUNCTIONS*/
	function createSpeciesTabDiv(pickerResults) {
		var toReturn = $('<div>'); //create a div to be returned to and turned into a tab
		$.each(pickerResults.SITES, function(sitename, site) {
			toReturn.append(createLabel(site));
			if(site.SPECIES.length > 1)
				toReturn.append(' - (' + site.SPECIES.length + ' Species)');
			toReturn.append(nbn.util.ArrayTools.toJQueryRenderedList(site.SPECIES, function(nbnsysCode) {
				return createLabel(pickerResults.TAXON[nbnsysCode]);
			}));
		});
		return toReturn;
	}
	
	function createDatasetsTabDiv(pickerResults) {
		var toReturn = $('<div>'); //create a div to be returned to and turned into a tab
		$.each(pickerResults.SITES, function(sitename, site) {
			toReturn.append(createLabel(site));
			toReturn.append(nbn.util.ArrayTools.toJQueryRenderedList(site.DATASETS, function(records, datasetKey) {
				return createDatasetLabel(pickerResults.DATASETS[datasetKey]);
			}));
		});
		return toReturn;
	}
	
	function createRecordsTabDiv(pickerResults) {
		var toReturn = $('<div>'); //create a div to be returned to and turned into a tab
		$.each(pickerResults.SITES, function(sitename, site) {
			toReturn.append(createLabel(site)).append(' - (Maximum 250 Records Shown)');
			toReturn.append(nbn.util.ArrayTools.toJQueryRenderedList(site.DATASETS, function(records, datasetKey) {
				var datasetTitle = $('<div>').append($('<div>').html(createDatasetLabel(pickerResults.DATASETS[datasetKey]))); //append the dataset as a title for the records
				if(records.length) { //only return a response if there are records to show
					records = records.sort(function(a,b) { return pickerResults.RECORDS[b].presence - pickerResults.RECORDS[a].presence; }); //order the results so that presence and absence are grouped
					
					return datasetTitle.append(nbn.util.ArrayTools.toJQueryRenderedList(records, function(recordID) {
						return createRecordLabel(pickerResults.RECORDS[recordID]);
					}));
				}
				else if(!pickerResults.DATASETS[datasetKey].downloadRawData) //or leave a message if the user does does not have downloadRawData?
					return datasetTitle.append(nbn.util.ArrayTools.toJQueryRenderedList(["You do not have download raw data privilege for this dataset"],function(element) {
						return $('<em>').html(element); //convert the text to an em element
					}));
				else
					return false;
			}));
			if(site.additional && site.additional.recordComment) //Fix for NBNIV-553, could this be done in a better way?
				toReturn.append(createLabel(site.additional.recordComment).addClass('moreInfo'));
		});
		return toReturn;
	}
	/*END DEFINITIONS OF TAB CREATION FUNCTIONS*/
	
	$.extend(this, new nbn.layer.picker.ArcGisLayerFeaturePicker(layerToQuery, {
		createPickerDiv: function(resultsFromIdentify, position, callback) {
                    //Chris says: If i am right resultsFromIdentify should hold [TL45...]
                    //
                    //THe rest of this method should be about passing that idenifier list to
                    //the api with layer specfic arguments
                    //
//			var featuresFound = createFeaturesFoundArray(resultsFromIdentify);
			
			if(resultsFromIdentify.length!==0) {
                                //You shopu
console.log(getSpeciesPickerServletWithParams(resultsFromIdentify, resultsFromIdentify));
				this.__lastRequest = $.getJSON(getSpeciesPickerServletWithParams(resultsFromIdentify, resultsFromIdentify), function(pickerResults) {
					var toReturn = $('<div>').addClass('nbn-picker-speciesResults');
					toReturn.nbn_dynamictabs();
					toReturn.nbn_dynamictabs('add','Records',createRecordsTabDiv(pickerResults));
					toReturn.nbn_dynamictabs('add','Datasets',createDatasetsTabDiv(pickerResults));
					toReturn.nbn_dynamictabs('add','Species',createSpeciesTabDiv(pickerResults));
					toReturn.tabs();
					callback(toReturn);
				});
				this.__lastRequest.error(function() {callback($('<div>').html('An error occured whilst trying to obtain a response from the picker server'));});
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
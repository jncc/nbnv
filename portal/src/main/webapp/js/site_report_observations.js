(function ($) {

	var apiServer;

	function refreshObservationData(form) {
		$('#nbn-request-better-access').attr('href', getBetterAccessHref());
		$('#nbn-interactive-map').attr('href', getIMTHref());

		var $dataContainer = $('#nbn-observation-container');
		var featureID = form.attr('featureID');
		var ptvk = form.attr('ptvk');

		//Add a busy image to data container whilst getting data
		$dataContainer.empty();
		$dataContainer.append('<img src="/img/ajax-loader-medium.gif" class="nbn-centre-element">');

		var keyValuePairsFromForm = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(form);
		keyValuePairsFromForm['featureID'] = featureID;
		keyValuePairsFromForm['ptvk'] = ptvk;

		// Sort out the selected verification statuses, if undefined or empty 
		// then do nothing and display the no records found box
		if (keyValuePairsFromForm['verification'] !== undefined) {
			// Join array to list if verification is an array (mulitple selected)
			if ($.isArray(keyValuePairsFromForm['verification'])) {
				keyValuePairsFromForm['verification'] = keyValuePairsFromForm['verification'].join();
			}

			//Get data from api and add to container
			var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairsFromForm, false);
			var url = apiServer + '/taxonObservations/datasets/observations' + queryString;
			$.getJSON(url, function (data) {
				$dataContainer.empty();
				if (data.length > 0) {
					$.each(data, function (key, dataset) {
						var provider = getProvider(dataset.organisationID);
						// Disabled For Release
						//var $attributeDropDown = getAttributeDropDown(dataset.key, queryString);
						var $datasetContent = $('<div><div/>').addClass('tabbed');
						$datasetContent.append(getProviderHeading(dataset, provider));
						var $table = $('<table class="nbn-simple-table"></table>');
						var $row = $('<tr></tr>');
						$row.append($('<th></th>').text("Grid Reference"));
						$row.append($('<th></th>').text("Site name"));
						$row.append($('<th></th>').text("Start date"));
						$row.append($('<th></th>').text("End date"));
						$row.append($('<th></th>').text("Date type"));
						$row.append($('<th></th>').text("Recorder"));
						$row.append($('<th></th>').text("Determiner"));
						$row.append($('<th></th>').text("Sensitive"));
						$row.append($('<th></th>').text("Zero Abundance"));
						$row.append($('<th></th>').text("Full Version"));
						$row.append($('<th></th>').text("Verification Status"));
						// Disabled For Release
						//if($attributeDropDown){
						//    $row.append($attributeDropDown);
						//}
						$table.append($row);
						$.each(dataset.observations, function (key, observation) {
							var $row = $('<tr></tr>');
							$row.append($('<td></td>').text(nbn.portal.reports.utils.forms.getDefaultText(observation.location, 'Unavailable')));
							$row.append($('<td></td>').text(nbn.portal.reports.utils.forms.getDefaultText(observation.siteName, 'Unavailable')));
							$row.append($('<td></td>').text(nbn.portal.reports.utils.forms.getDateText(new Date(observation.startDate))));
							$row.append($('<td></td>').text(nbn.portal.reports.utils.forms.getDateText(new Date(observation.endDate))));
							$row.append($('<td></td>').text(observation.dateTypekey));
							$row.append($('<td></td>').text(nbn.portal.reports.utils.forms.getDefaultText(observation.recorder, 'Unavailable')));
							$row.append($('<td></td>').text(nbn.portal.reports.utils.forms.getDefaultText(observation.determiner, 'Unavailable')));
							$row.append($('<td></td>').text(observation.sensitive));
							$row.append($('<td></td>').text(observation.absence));
							$row.append($('<td></td>').text(observation.fullVersion));
							$row.append($('<td></td>').text(observation.verification));
							// Disabled For Release
							//if($attributeDropDown){
							//    $row.append($('<td id="' + observation.observationID + '"></td>').addClass('nbn-attribute-td'));
							//}
							$table.append($row);
						});
						$datasetContent.append($table);
						$dataContainer.append($datasetContent);
						//if($attributeDropDown){
						//    addAttributeData(dataset.key, $attributeDropDown.val(), queryString);
						//}
					});
				} else {
					$dataContainer.append(nbn.portal.reports.utils.forms.getNoRecordsFoundInfoBox());
				}
			});
		} else {
			$dataContainer.empty();
			$dataContainer.append(nbn.portal.reports.utils.forms.getNoRecordsFoundInfoBox());
		}
	}

	function getProviderHeading(dataset, provider) {
		var toReturn = '<h3>Records from dataset: <a href="/Datasets/' + dataset.key + '">' + dataset.title + '</a></h3>' +
				'<table id="nbn-tabbed-heading-table"><tr><td>Provider:</td><td>';
		if (provider.hasLogo) {
			toReturn += '<img src="' + apiServer + '/organisations/' + provider.id + '/logo "class="nbn-provider-table-logo">&nbsp&nbsp&nbsp;';
		}
		toReturn += '<a href="/Organisations/' + dataset.organisationID + '">' + provider.name + '</a></tr>' +
				'<tr><td>Your access:</td><td>';
		toReturn += getAccessPositions(dataset);
		'</tr></td>';
		return toReturn;
	}

	function getAccessPositions(taxonDataset) {
		var url = apiServer + '/taxonDatasets/' + taxonDataset.key + '/accessPositions';
		var toReturn = '';
		$.ajax({
			type: 'GET',
			url: url,
			dataType: 'json',
			success: function (data) {
				if (data.publicAccess === "None") {
					toReturn += '<ul><li>No public access';
				} else {
					toReturn += '<ul><li>You have public access to records at ' + data.publicAccess;
				}

				// Disabled as we no longer support public attributes
				//if(taxonDataset.publicAttribute){
				//    toReturn += ' with record attributes</li>';
				//}

				for (var i = 0; i < data.enhanced.length; i++) {
					toReturn += '<li>' + data.enhanced[i].owner + ' enhanced access: ' + data.enhanced[i].filterText + '</li>';
				}

				toReturn += '</ul>';
			},
			async: false
		});
		return toReturn;
	}

	function getAttributeDropDown(datasetKey, queryString) {
		var attributes = getDatasetAttributes(datasetKey);
		if (!$.isEmptyObject(attributes)) {
			var $select = $("<select id='nbn-site-observation-attribute-select'></select>")
					.change(function () {
						$('#nbn-attribute-dropdown-busy-image').attr('src', '/img/ajax-loader.gif');
						addAttributeData(datasetKey, $(this).val(), queryString);
					});
			$.each(attributes, function (index, attribute) {
				$select.append($("<option></option>")
						.attr("value", attribute.attributeID)
						.text(attribute.label));
			});
			return $('<th></th>').append($select);
		} else {
			return false;
		}
	}

	function getDatasetAttributes(datasetKey) {
		var url = apiServer + '/taxonDatasets/' + datasetKey + '/attributes';
		var toReturn = '';
		$.ajax({type: 'GET',
			url: url,
			dataType: 'json',
			success: function (data) {
				toReturn = data;
			},
			async: false
		});
		return toReturn;
	}

	function addAttributeData(datasetKey, attributeID, queryString) {
		var url = apiServer + '/taxonObservations/' + datasetKey + '/attributes/' + attributeID + queryString;
		$.ajax({
			type: 'GET',
			url: url,
			dataType: 'json',
			success: function (data) {
				$('.nbn-attribute-td').empty();
				$.each(data, function (index, observationAttribute) {
					$('#' + observationAttribute.observationID).text(observationAttribute.textValue);
				});
			},
			async: false
		});
	}

	function getProvider(providerID) {
		var url = apiServer + '/organisations/' + providerID;
		var toReturn;
		$.ajax({
			type: 'GET',
			url: url,
			dataType: 'json',
			success: function (data) {
				toReturn = data;
			},
			async: false
		});
		return toReturn;
	}

	function setupFormOnChange() {
		$('#nbn-site-report-form :input').change(function () {
			var $input = $(this);
			if (nbn.portal.reports.utils.forms.isSiteReportFormFieldValid($input)) {
				refreshObservationData($('#nbn-site-report-form'));
			}
		});
	}

	function doFirstVisitToPage() {
		nbn.portal.reports.utils.forms.setupVerificationCheckboxesURL();
		refreshObservationData($('#nbn-site-report-form'));
	}

	function getBetterAccessHref() {
		var form = $('#nbn-site-report-form');
		var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(form);
		return '/AccessRequest/Create?json={' +
				nbn.portal.reports.utils.forms.getSpatialFeatures(keyValuePairs, form.attr('gridSquare')) + ',' +
				'taxon:{tvk:\'' + form.attr('ptvk') + '\'},' +
				'dataset:{all:true},' +
				nbn.portal.reports.utils.forms.getYearJSON(keyValuePairs) +
				'}';
	}

	function getIMTHref() {
		var form = $('#nbn-site-report-form');
		var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(form);
		var url = '/imt?mode=SPECIES&species=' +
				form.attr('ptvk');
		if (keyValuePairs['startYear'] !== undefined &&
				keyValuePairs['startYear'] !== '' &&
				keyValuePairs['endYear'] !== undefined &&
				keyValuePairs['endYear'] !== '') {
			url = url + '&startyear=' +
					keyValuePairs['startYear'] +
					'&endyear=' +
					keyValuePairs['endYear'];
		}
		return url;
	}

	function setupDownloadRecordsLink() {
		$('#nbn-download-observations-button').click(function (e) {
			$('#nbn-download-terms').dialog({
				modal: true,
				width: 800,
				height: 450,
				buttons: {
					'Accept': function () {
						var form = $('#nbn-site-report-form');
						var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(form);
						window.location = '/Download?json={' +
								nbn.portal.reports.utils.forms.getSpatialFeatures(keyValuePairs, form.attr('gridSquare')) +
								',taxon:{tvk:\'' + form.attr('ptvk') + '\'}' +
								',dataset:{all:true},' +
								nbn.portal.reports.utils.forms.getYearJSON(keyValuePairs) + ',' +
								nbn.portal.reports.utils.forms.getVerificationJSON(keyValuePairs) +
								'}';
					},
					'Cancel': function () {
						$(this).dialog("close");
					}
				}
			});
			e.preventDefault();
		});
	}

	$(document).ready(function () {
		$('#nbn-download-terms').hide();
		apiServer = $('#nbn-site-report-form').attr('api-server');
		setupFormOnChange();
		setupDownloadRecordsLink();
		doFirstVisitToPage();
	});

})(jQuery);
(function ($) {

	function refreshGroupData(form) {
		// Update Better Access href
		$('#nbn-request-better-access').attr('href', nbn.portal.reports.utils.forms.getBetterAccessHref(form));
		
		var $dataContainer = $('#nbn-site-report-data-container');
		var featureID = form.attr('featureID');

		//Add title and busy image to data container whilst getting data
		var toAppend = '<h3>Groups</h3>';
		$dataContainer.empty();
		$dataContainer.append(toAppend);
		$dataContainer.append('<img src="/img/ajax-loader-medium.gif" class="nbn-centre-element">');

		//Get data from api and add to container
		var keyValuePairsFromForm = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(form);
		keyValuePairsFromForm['featureID'] = featureID;
		keyValuePairsFromForm['datasetKey'] = nbn.portal.reports.utils.datasetfields.getSelectedDatasetsJoined();

		// If we have more than one dataset selected then proceed otherwise skip 
		// call to api
		if (keyValuePairsFromForm['datasetKey'] !== undefined && keyValuePairsFromForm['datasetKey'].length > 0) {
			var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairsFromForm, false);
			var url = form.attr('api-server') + '/taxonObservations/groups' + queryString;
			$.getJSON(url, function (data) {
				if (data.length > 0) {
					toAppend += '<ul>';
					data.sort(function (a, b) {
						return ((a.taxonOutputGroup.name < b.taxonOutputGroup.name) ? -1 : ((a.taxonOutputGroup.name > b.taxonOutputGroup.name) ? 1 : 0));
					});
					$.each(data, function (key, val) {

						toAppend += '<li><a class="nbn-drilldown-link" href="/Reports/Sites/' + featureID + '/Groups/' + val.taxonOutputGroup.key + '/Species' + getLinkQueryString(keyValuePairsFromForm) + '">' + val.taxonOutputGroup.name + '</a>';
					});
					toAppend += '</ul>';
				} else {
					toAppend += nbn.portal.reports.utils.forms.getNoRecordsFoundInfoBox();
				}
				$dataContainer.empty();
				$($dataContainer).append(toAppend);
			});
		} else {
			$dataContainer.empty();
			toAppend += nbn.portal.reports.utils.forms.getNoRecordsFoundInfoBox();
			$($dataContainer).append(toAppend);
		}
	}

	function getLinkQueryString(keyValuePairsFromForm) {
		delete keyValuePairsFromForm['datasetKey'];
		var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairsFromForm, false);

		return queryString +
				'&selectedDatasets=' +
				nbn.portal.reports.utils.datasetfields.getSelectedDatasets();
	}


	function setupFormOnChange() {
		//The map should refresh when any form field is changed and has valid data
		//except when the nbn-select-datasets-auto check box is deselected
		$('#nbn-site-report-form :input').change(function () {
			var $input = $(this);
			if (nbn.portal.reports.utils.forms.isSiteReportFormFieldValid($input)) {
				refreshGroupData($('#nbn-site-report-form'));
			}
		});
	}

	function setupDownloadSpeciesButton() {
		$('#nbn-site-report-download-button').click(function (e) {
			$('#nbn-download-terms').dialog({
				modal: true,
				width: 800,
				height: 450,
				buttons: {
					'Accept': function () {
						var $form = $('#nbn-site-report-form');
						var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm($form);
						keyValuePairs['featureID'] = $form.attr("featureID");
						//keyValuePairs.taxonOutputGroup = $form.attr("taxonOutputGroupKey");                   
						keyValuePairs['datasetKey'] = nbn.portal.reports.utils.datasetfields.getSelectedDatasetsJoined();
						var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairs, false);
						var url = $form.attr('api-server') + '/taxonObservations/species/download/' + queryString;
						$(this).dialog("close");
						window.location = url;
					},
					'Cancel': function () {
						$(this).dialog("close");
					}
				}
			});
			e.preventDefault();
		});
	}

	function doFirstVisitToPage() {
		refreshObservationData($('#nbn-site-report-form'));
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
								nbn.portal.reports.utils.forms.getSpatialFeatures(keyValuePairs, form.attr('gridSquare')) + ',' +
								nbn.portal.reports.utils.datasetfields.getSelectedDatasetsJSON() + ',' +
								nbn.portal.reports.utils.forms.getYearJSON(keyValuePairs) + ',' +
								nbn.portal.reports.utils.forms.getTaxonFilter(keyValuePairs) +
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
		setupFormOnChange();
		setupDownloadSpeciesButton();
		setupDownloadRecordsLink();
		doFirstVisitToPage();
	});
})(jQuery);
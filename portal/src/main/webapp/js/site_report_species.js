(function($) {

    function refreshSpeciesData(form) {
        var $dataContainer = $('#nbn-site-report-data-container');
        var featureID = form.attr('featureID');
        var taxonOutputGroupKey = form.attr('taxonOutputGroupKey');

        //Add title and busy image to data container whilst getting data
        var toAppend = '<h3>Species</h3>';
        $dataContainer.empty();
        $dataContainer.append(toAppend);
        $dataContainer.append('<img src="/img/ajax-loader-medium.gif" class="nbn-centre-element">');

        //Get data from api and add to container
        var keyValuePairsFromForm = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(form);
        keyValuePairsFromForm['featureID'] = featureID;
        keyValuePairsFromForm['taxonOutputGroup'] = taxonOutputGroupKey;
        keyValuePairsFromForm['datasetKey'] = nbn.portal.reports.utils.datasetfields.getSelectedDatasetsJoined();
        
        // If we have more than one dataset selected then proceed otherwise skip 
        // call to api
        if (keyValuePairsFromForm['datasetKey'] !== undefined && keyValuePairsFromForm['datasetKey'].length > 0) {
            var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairsFromForm, false);
            var url = form.attr('api-server') + '/taxonObservations/species' + queryString;
            var numSpecies = 0;
            var datatableDisplayThreshold = 10;
            $.getJSON(url, function(data) {
                if (data.length > 0) {
                    numSpecies = data.length;
                    if (numSpecies > datatableDisplayThreshold) {
                        toAppend += '<table id="nbn-species-table" class="nbn-simple-table"><thead><tr><th>Sort</th></thead><tbody>';
                    } else {
                        toAppend += '<table id="nbn-species-table" class="nbn-simple-table"><tbody>';
                    }
                    $.each(data, function(key, val) {
                        toAppend += '<tr><td><a href="/Reports/Sites/' + featureID + '/Groups/' + taxonOutputGroupKey + '/Species/' + val.taxon.ptaxonVersionKey + '/Observations' + getLinkQueryString(keyValuePairsFromForm) + '">' + "<span class='nbn-taxon-name'>" + val.taxon.name + '</span>';
                        if (val.taxon.commonName)
                            toAppend += ' [' + val.taxon.commonName + ']';
                        toAppend += '</a></td></tr>';
                    });
                    toAppend += '</tbody></table>';
                } else {
                    toAppend += nbn.portal.reports.utils.forms.getNoRecordsFoundInfoBox();
                }
                $dataContainer.empty();
                $($dataContainer).append(toAppend);
                if (numSpecies > datatableDisplayThreshold) {
                    addDataTable();
                }
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
    
    function addDataTable(){
        $('#nbn-species-table').dataTable({
            "bJQueryUI": true,
            "sPaginationType": "full_numbers",
            "oLanguage": {
                "sLengthMenu": "Show _MENU_ species", 
                "sSearch": 'Search list',
                "sInfo": "Showing _START_ to _END_ of _TOTAL_ species",
                "sInfoFiltered": " (filtered from _MAX_ total species)"
            },
            "iDisplayLength": 25,
            "bSortClasses": false,
            "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]]
        });
    }
    
    function setupFormOnChange(){
        //The map should refresh when any form field is changed
        //except when the nbn-select-datasets-auto check box is deselected
        $('#nbn-site-report-form :input').change(function(){
            var $input = $(this);
            if(nbn.portal.reports.utils.forms.isSiteReportFormFieldValid($input)){
                refreshSpeciesData($('#nbn-site-report-form'));
            }
        });
    }
    
    function setupDownloadSpeciesButton(){
        $('#nbn-site-report-download-button').click(function(e){
            $('#nbn-download-terms').dialog({
                modal: true,
                width: 800,
                height: 450,
                buttons: {
                    'Accept': function(){
                        var $form = $('#nbn-site-report-form'); 
                        var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm($form);
                        keyValuePairs['featureID'] = $form.attr("featureID");
                        keyValuePairs['taxonOutputGroup'] = $form.attr("taxonOutputGroupKey");
                        keyValuePairs['datasetKey'] = nbn.portal.reports.utils.datasetfields.getSelectedDatasetsJoined();
                        var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairs, false);
                        var url = $form.attr('api-server') + '/taxonObservations/species/download/' + queryString;
                        $(this).dialog("close");
                        window.location = url;
                    },
                    'Cancel': function(){
                        $(this).dialog("close");
                    }
                }
            });
            e.preventDefault();
        });

    }
    
    function setupBetterAccessLink() {
        $('#nbn-request-better-access').click(function(e) {
            var form = $('#nbn-site-report-form');
            var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(form);
            window.location = '/AccessRequest/Create?json={' + 
                'taxon:{all:false,output:\'' + form.attr('taxonOutputGroupKey') + '\'},' +
                nbn.portal.reports.utils.forms.getSpatialFeatures(keyValuePairs, form.attr('gridSquare')) + ',' +
                // Disabled as creates requests for all public datasets explicitly
                // nbn.portal.reports.utils.datasetfields.getSelectedDatasetsJSON() + ',' +
                'dataset:{all:true},' +
                nbn.portal.reports.utils.forms.getYearJSON(keyValuePairs) + ',' +
                nbn.portal.reports.utils.forms.getTaxonFilter(keyValuePairs) + '}';
            e.preventDefault();
        });
    }
    
    function setupDownloadRecordsLink() {
        $('#nbn-download-observations-button').click(function(e) {
            $('#nbn-download-terms').dialog({
                modal: true,
                width: 800,
                height: 450,
                buttons: {
                    'Accept': function(){
                        var form = $('#nbn-site-report-form');
                        var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(form);
                        window.location = '/Download?json={' + 
                                'taxon:{all:false,output:\'' + form.attr('taxonOutputGroupKey') + '\'},' +
                                nbn.portal.reports.utils.forms.getSpatialFeatures(keyValuePairs, form.attr('gridSquare')) + ',' +
                                nbn.portal.reports.utils.datasetfields.getSelectedDatasetsJSON() + ',' +
                                nbn.portal.reports.utils.forms.getYearJSON(keyValuePairs) + ',' +
                                nbn.portal.reports.utils.forms.getTaxonFilter(keyValuePairs) +
                                '}';
                    },
                    'Cancel': function(){
                        $(this).dialog("close");
                    }
                }
            });
            e.preventDefault();
        });
    }
      
    function doFirstVisitToPage(){
        refreshSpeciesData($('#nbn-site-report-form'));
    }
    
    $(document).ready(function(){
        $('#nbn-download-terms').hide();
        setupFormOnChange();
        setupDownloadSpeciesButton();
        setupDownloadRecordsLink();
        setupBetterAccessLink();
        doFirstVisitToPage();
    });
})(jQuery);
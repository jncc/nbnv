(function($){
    
    function refreshGroupData(form){
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
        var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairsFromForm, false);
        var url = form.attr('api-server') + '/taxonObservations/groups' + queryString;
        $.getJSON(url, function(data){
            if(data.length > 0){
                toAppend += '<ul>';
                data.sort(function(a, b) { return ((a.taxonOutputGroup.name < b.taxonOutputGroup.name) ? -1 : ((a.taxonOutputGroup.name > b.taxonOutputGroup.name) ? 1 : 0)); });
                $.each(data, function(key, val){
                    
                    toAppend += '<li><a class="nbn-drilldown-link" href="/Reports/Sites/' + featureID + '/Groups/' + val.taxonOutputGroup.key + '/Species' + getLinkQueryString(keyValuePairsFromForm) + '">' + val.taxonOutputGroup.name + '</a>';
                });
                toAppend += '</ul>';
            }else{
                toAppend += nbn.portal.reports.utils.forms.getNoRecordsFoundInfoBox();
            }
            $dataContainer.empty();
            $($dataContainer).append(toAppend);
        });
    }
    
    function getLinkQueryString(keyValuePairsFromForm) {
        delete keyValuePairsFromForm['datasetKey'];
        var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairsFromForm, false);
        
        return queryString +
            '&selectedDatasets=' +  
            nbn.portal.reports.utils.datasetfields.getSelectedDatasets();
    }
    
    
    function setupFormOnChange(){
        //The map should refresh when any form field is changed and has valid data
        //except when the nbn-select-datasets-auto check box is deselected
        $('#nbn-site-report-form :input').change(function(){
            var $input = $(this);
            if(($input.attr('id')!='nbn-select-datasets-auto') || ($('#nbn-select-datasets-auto').is(':checked'))){
                if(nbn.portal.reports.utils.forms.isSiteReportFormFieldValid($input)){
                    //Requires jquery.dataset-selector-utils.js
                    nbn.portal.reports.utils.datasetfields.doDeselectDatasetKeys();
                    refreshGroupData($('#nbn-site-report-form'));
                    nbn.portal.reports.utils.datasetfields.doSelectDatasetKeys();
                }
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
                        // Get selected datasets workaround - TODO: Fix this properly
                        var datasets = nbn.portal.reports.utils.datasetfields.getSelectedDatasets();
                        nbn.portal.reports.utils.datasetfields.doDeselectDatasetKeys();
                        var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm($form);
                        keyValuePairs.featureID = $form.attr("featureID");
//                        keyValuePairs.taxonOutputGroup = $form.attr("taxonOutputGroupKey");
                        // Get selected datasets workaround - TODO: Fix this properly
                        keyValuePairs.datasetKey = datasets;
                        var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairs, false);
                        var url = $form.attr('api-server') + '/taxonObservations/species/download/' + queryString;
                        nbn.portal.reports.utils.datasetfields.doSelectDatasetKeys();
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

    function doFirstVisitToPage(){
        refreshGroupData($('#nbn-site-report-form'));
    }
      
    function setupBetterAccessLink() {
        $('#nbn-request-better-access').click(function(e) {
            var form = $('#nbn-site-report-form');
            var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(form);
            window.location = '/AccessRequest/Create?json={' + 
                    nbn.portal.reports.utils.forms.getSpatialFeatures(keyValuePairs, form.attr('gridSquare')) + ',' +
                    // Disabled as creates requests for all public datasets explicitly
                    // nbn.portal.reports.utils.datasetfields.getSelectedDatasetsJSON() + ',' +
                    '{dataset:{all:true}' +
                    nbn.portal.reports.utils.forms.getYearJSON(keyValuePairs) +
                    '}';
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
                                nbn.portal.reports.utils.forms.getSpatialFeatures(keyValuePairs, form.attr('gridSquare')) + ',' +
                                nbn.portal.reports.utils.datasetfields.getSelectedDatasetsJSON() + ',' +
                                nbn.portal.reports.utils.forms.getYearJSON(keyValuePairs) +
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
    

    
    $(document).ready(function(){
        $('#nbn-download-terms').hide();
        setupFormOnChange();
        setupDownloadSpeciesButton();
        setupBetterAccessLink();
        setupDownloadRecordsLink();
        doFirstVisitToPage();
    });
})(jQuery);
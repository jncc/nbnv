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
                $.each(data, function(key, val){
                    toAppend += '<li><a href="/Reports/Sites/' + featureID + '/Groups/' + val.taxonOutputGroup.key + '/Species">' + val.taxonOutputGroup.name + '</a>';
                });
                toAppend += '</ul>';
            }else{
                toAppend += nbn.portal.reports.utils.forms.getNoRecordsFoundInfoBox();
            }
            $dataContainer.empty();
            $($dataContainer).append(toAppend);
        });
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
                        nbn.portal.reports.utils.datasetfields.doDeselectDatasetKeys();
                        var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm($form);
                        keyValuePairs.featureID = $form.attr("featureID");
//                        keyValuePairs.taxonOutputGroup = $form.attr("taxonOutputGroupKey");
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
        $('#nbn-request-better-access').click(function() {
            var form = $('#nbn-site-report-form');
            var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(form);
            window.open('/AccessRequest/Create?json={' + 
                    getSpatialFeatures(keyValuePairs) + ',' +
                    nbn.portal.reports.utils.datasetfields.getSelectedDatasetsJSON() + ',' +
                    getYearRange(keyValuePairs) +
                    '}');
        });
    }
    
    function getYearRange(keyPairs) {
        if (keyPairs['startYear'] != undefined &&
                keyPairs['startYear'] != '' && 
                keyPairs['endYear'] != undefined && 
                keyPairs['endYear'] != '') {
            return 'year:{all:false:startYear:' + 
                    keyPairs['startYear'] + 
                    ',endYear:' + 
                    keyPairs['endYear'] + 
                    '}';
        } else {
            return 'year:{all:true}';
        }
    }
    
    function getSpatialFeatures(keyPairs) {
        return 'spatial:{all:false,match:\'' + keyPairs['spatialRelationship'] + '\',feature:\'' + $('#nbn-site-report-form').attr('featureid') + '\'}';
    }
    
    $(document).ready(function(){
        $('#nbn-download-terms').hide();
        setupFormOnChange();
        setupDownloadSpeciesButton();
        setupBetterAccessLink();
        doFirstVisitToPage();
    });
})(jQuery);
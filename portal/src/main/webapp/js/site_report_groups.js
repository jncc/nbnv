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
                if(nbn.portal.reports.utils.forms.isFormFieldValid($input)){
                    //Requires jquery.dataset-selector-utils.js
                    nbn.portal.reports.utils.datasetfields.doDeselectDatasetKeys();
                    refreshGroupData($('#nbn-site-report-form'));
                    nbn.portal.reports.utils.datasetfields.doSelectDatasetKeys();
                }
            }
        });
    }
    
    function doFirstVisitToPage(){
        refreshGroupData($('#nbn-site-report-form'));
    }
    
    $(document).ready(function(){
        setupFormOnChange();
        doFirstVisitToPage();
    });
})(jQuery);
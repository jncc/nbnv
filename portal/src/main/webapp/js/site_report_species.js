(function($){
    
    function refreshSpeciesData(form){
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
        var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairsFromForm, false);
        var url = form.attr('api-server') + '/taxonObservations/species' + queryString;
        $.getJSON(url, function(data){
            if(data.length > 0){
                toAppend += '<ul>';
                $.each(data, function(key, val){
                    toAppend += '<li><a href="/Reports/Sites/' + featureID + '/Groups/' + taxonOutputGroupKey + '/Species/' + val.taxon.taxonVersionKey + '">' + val.taxon.name + '</a>';
                });
                toAppend += '</ul>';
            }else{
                toAppend += nbn.portal.reports.utils.forms.getNoRecordsFoundInfoBox();
            }
            $dataContainer.empty();
            $($dataContainer).append(toAppend);
        });
    }
    
    $(document).ready(function(){
        
        var $siteReportForm = $('#nbn-site-report-form');
        nbn.portal.reports.site.initializeValidation();
        refreshSpeciesData($siteReportForm);

        $siteReportForm.submit(function(){
            //Requires jquery.dataset-selector-utils.js
            nbn.portal.reports.utils.DatasetFields.doDeselectDatasetKeys();
            var form = $(this);
            refreshSpeciesData(form);
            nbn.portal.reports.utils.DatasetFields.doSelectDatasetKeys();
            return false;
        });

    });
})(jQuery);
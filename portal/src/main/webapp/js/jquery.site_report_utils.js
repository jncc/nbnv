(function($){
    
    function initializeValidation(){
        $("#startYear").change(function(){
            validateYear($(this).val());
        });
        $("#endYear").change(function(){
            validateYear($(this).val());
        });
    }
    function validateYear(year){
        if(!isNumber(year) || year > new Date().getFullYear()){
            alert("You didn't enter a valid year, enter a value from 1600 to present");
        }
    }

    function isNumber(n) {
        return !isNaN(parseFloat(n)) && isFinite(n);
    }

    function refreshGroupData(form){
        var $dataContainer = $('#nbn-site-report-data-container');
        var toAppend = '<h3>Groups</h3>';
        $dataContainer.empty();
        $dataContainer.append(toAppend);
        $dataContainer.append('<img src="/img/ajax-loader-medium.gif" class="nbn-centre-element">');
        var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(form), false);
        var url = form.attr('api-server') + '/taxonObservations/groups' + queryString;
        $.getJSON(url, function(data){
            if(data.length > 0){
                toAppend += '<ul>';
                $.each(data, function(key, val){
                    toAppend += '<li>' + val.taxonOutputGroup.name;
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
        initializeValidation();
        refreshGroupData($siteReportForm);

        $siteReportForm.submit(function(){
            //Requires jquery.dataset-selector-utils.js
            nbn.portal.reports.utils.DatasetFields.doDeselectDatasetKeys();
            var form = $(this);
            refreshGroupData(form);
            nbn.portal.reports.utils.DatasetFields.doSelectDatasetKeys();
            return false;
        });

    });
})(jQuery);
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
    
    $(document).ready(function(){
        initializeValidation();

        $('#nbn-site-report-form').submit(function(){
            //Requires jquery.dataset-selector-utils.js
            nbn.portal.reports.utils.DatasetFields.doDeselectDatasetKeys();
            return true;
        });

    });
})(jQuery);
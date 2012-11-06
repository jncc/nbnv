(function($){
    
    
        namespace("nbn.portal.reports.site", {
        initializeValidation: function(){
            doInitializeValidation();
        }
    });

    function doInitializeValidation(){
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

})(jQuery);
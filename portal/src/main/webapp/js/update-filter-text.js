(function($){
    $(document).ready(function(){

        $("#startyear").change(function(){
            validateYear($(this).val());
        });
        
        $("#endyear").change(function(){
            validateYear($(this).val());
        });
        
        function validateYear(year){
            if(!isNumber(year)){
                alert("You didn't enter a valid year, enter a value from 1600 to present");
            }
        }
        
        function isNumber(n) {
            return !isNaN(parseFloat(n)) && isFinite(n);
        }        
        
    }); 
})(jQuery);



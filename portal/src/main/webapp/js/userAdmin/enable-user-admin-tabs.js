/* This javascript will implement JQuery tabs for a div with id=nbn-tabs*/
(function($){
    $(document).ready(function(){
        $('#nbn-tabs').tabs({
            spinner: 'Loading <img src="/img/ajax-loader.gif"/>',
            cache: true
        });
        applyTableEvenRowStyle();
        
        $('#modify-details-form').submit(function() {
            // Set this to constant just to make the model pass validation, 
            // never used though
            $('#password').val("0");
            // If the phone number is blank then set the text to "No number avail"
            if ($('#phone').val().trim() === '') {
                $('#phone').val('No number avail');
            }
            return true;
        });
        
        $('#change-password-form').submit(function() {
            $('#token').val('change');
        });
    });

    function applyTableEvenRowStyle(){
        $(".nbn-simple-table tr:even").addClass("ui-state-highlight");
    }
})(jQuery);

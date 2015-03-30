//This applies the business logic whereby 'record attributes' and 'recorder names'
//are dependent on the 'record resolution' selected by the user.
(function($) {
    
    $(document).ready(function() {
        $('input[name="resolution"]').change(function() {
            if($('input[name="resolution"]:checked').attr('value') === '100'){
               doDependentControls(false);
            }else{
               doDependentControls(true);
            }
        });
    });
    
    function doDependentControls(isDisabled){
        $('input[name="recordAtts"').prop('disabled', isDisabled);
        $('input[name="recorderNames"').prop('disabled', isDisabled);
    }
})(jQuery);
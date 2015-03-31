//This applies the business logic whereby 'record attributes' and 'recorder names'
//are dependent on the 'record resolution' selected by the user.
(function($) {
    
    $(document).ready(function() {
        $('input[name="resolution"]').change(updateControls());
        updateControls(); //run once on startup
    });
    
    function updateControls() {
        if($('input[name="resolution"]:checked').attr('value') === '100m'){
           doDependentControls(false);
        }else{
           doDependentControls(true);
        }
    }
    
    function doDependentControls(isDisabled){
        $('input[name="recordAtts"').prop('disabled', isDisabled);
        $('input[name="recorderNames"').prop('disabled', isDisabled);
    }
})(jQuery);
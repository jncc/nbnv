(function($){
    $(document).ready(function(){
        updateSelectedOrg($('#organisationID option:selected'));
        
        $('#organisationID').change(function() {
            updateSelectedOrg($('#organisationID option:selected').val());
        });
    });
    
    function updateSelectedOrg(id) {
        if (id > 0) {
            $('#selectedOrgID').text('Organisation ' + id + ' is selected');
        } else {
            $('#selectedOrgID').text('No Organisation Selected');
        }
    }
})(jQuery);
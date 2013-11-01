(function($){
    function submit() {
        $('#nbn-form-submit').attr('disabled','disabled');
        $("#nbn-waiting-note").show()
        $.ajax({ 
            type: 'POST',
            url: $('#nbn-metadata-update').attr('url'),
            data: $('#nbn-metadata-update').serialize(),
            success: function(result) {
                window.location = "/Datasets/" + $('#nbn-form-submit').data('dataset');
            },
            error: function(result) {
                $("#nbn-waiting-note").hide()
                alert("An unknown error occured while submitting these changes, please try again later");
            }
        });
        
        return false;
    }
    
    $(document).ready(function(){
        $('#nbn-form-submit').click(submit);
    });
})(jQuery);


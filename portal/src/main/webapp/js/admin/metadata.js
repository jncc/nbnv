(function($){
    function submit() {
        $.ajax({ 
            type: 'POST',
            url: $('#nbn-metadata-update').attr('url'),
            data: $('#nbn-metadata-update').serialize()
        });
    }
    
    $(document).ready(function(){
        $('#nbn-form-submit').click(submit);
    });
})(jQuery);


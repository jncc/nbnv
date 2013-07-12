(function($){
    function submit() {
        $.ajax({ 
            type: 'POST',
            url: $('#nbn-metadata-update').attr('url'),
            data: $('#nbn-metadata-update').serialize()
        });
        
        window.location = "/User/Admin";
        
        return false;
    }
    
    $(document).ready(function(){
        $('#nbn-form-submit').click(submit);
    });
})(jQuery);


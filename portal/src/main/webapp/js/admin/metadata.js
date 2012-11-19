(function($){
    function submit() {
        alert('here');
        $.ajax({ 
            type: 'POST',
            url: $('#nbn-metadata-update').attr('url'),
            data: $('#nbn-metadata-update').serialize()
        });
        
        return false;
    }
    
    $(document).ready(function(){
        $('#nbn-form-submit').click(submit);
    });
})(jQuery);


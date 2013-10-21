window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};

(function($) {   
    $(document).ready(function() {
        $('#nbn-designation-download-link').click(function(e) {
            $('#nbn-download-terms').dialog({
                modal: true,
                width: 800,
                height: 450,
                buttons: {
                    'Accept': function(){
                        var code = $('#nbn-designation-download-link').data('code');
                        window.location = '/Download?json={taxon:{all:false,designation:\'' + code + '\'}}';
                    },
                    'Cancel': function(){
                        $(this).dialog("close");
                    }
                }
            });
            
            e.preventDefault();
        });
    });
})(jQuery);



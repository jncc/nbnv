window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};

(function($) {   
    $(document).ready(function() {
        $('#nbn-download-notification-submit').click(function(e){
            var checked = false;
            if ($('#nbn-download-notification-checkbox').attr('checked') === 'checked') {
                checked = true;
            }
            
            $('#nbn-download-notification-working').empty()
                    .append($('<img>').attr('src', '/img/ajax-loader.gif'))
                    .append($('<p>').text('Working....'));
            $('#nbn-download-notification-checkbox').attr("disabled", true);
            
            $.ajax({
                url: nbn.nbnv.api + '/dataset/' + nbn.nbnv.dataset + '/userDownloadNotification?add=' +  checked,
                type: 'POST',
                success: function(data) {
                    window.reload;
                },
                failure: function(data) {
                    $('#nbn-download-notification-working').empty();
                    $('#nbn-download-notification-checkbox').removeAttr('disabled');
                    alert('Failed to change download notification settings for this dataset, please try again later');
                }
            });
            
            e.preventDefault();
        });
    });   
})(jQuery);
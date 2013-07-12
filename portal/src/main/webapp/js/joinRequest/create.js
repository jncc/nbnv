/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

(function($) {
    $(document).ready(function() {
        $('#nbn-org-join-request-submit').click(function(e) {
            if ($.trim($('#nbn-org-join-request-reason').val()) === "") {
                $('#nbn-org-join-request-dialog-extra').text(' without a message');
            } else {
                $('#nbn-org-join-request-dialog-extra').text('');
            }
            $('#nbn-org-join-request-dialog').dialog({
                resizable: false,
                width: 400,
                modal: true,
                buttons: {
                    'Send Request': function() {
                        displaySendingRequestDialog('Sending Request');
                        $.ajax({
                            type: 'PUT',
                            contentType: "application/json; charset=utf-8",
                            url: $('#nbn-org-join-request-submit').data('url'),
                            data: JSON.stringify({reason: $('#nbn-org-join-request-reason').val()}),
                            dataType: "json",
                            success: function() {
                                location.reload();
                            },
                            error: function() {
                                $('#nbn-org-join-request-dialog').dialog('close');
                                alert("ERROR: Could not create join request");
                            }
                        });
                    },
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                }
            });

        });
    });
})(jQuery);
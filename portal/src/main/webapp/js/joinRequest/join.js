(function($) {
    $(document).ready(function() {
        $('#nbn-org-join-request-submit').click(function(e) {
            if ($('#combobox').val() == -1) {
                alert("Please Select an Organisation!");
            } else {
                var org = $('#combobox').val();
                if ($('#nbn-org-join-request-reason').val().trim() === "") {
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
                                url: $('#nbn-org-join-request-submit').data('url') + org + '/join',
                                data: JSON.stringify({reason: $('#nbn-org-join-request-reason').val()}),
                                dataType: "json",
                                success: function() {
                                    window.location.href = "/Organisations/" + org + '/Join';
                                },
                                error: function(request, error) {
                                    $('#nbn-org-join-request-dialog').dialog('close');
                                    if (request.status == 303) {
                                        $('#nbn-org-join-request-exists-dialog').dialog({
                                            resizable: false,
                                            width: 400,
                                            modal: true,
                                            buttons: {
                                                "View Request": function () {
                                                    $('<div style="width:20px;height:20px;float:left;" class="ui-autocomplete-loading"></div>').insertBefore('#respond_div .ui-dialog-buttonpane button:first');
                                                    window.location.href = "/Organisations/" + org + "/Join";
                                                },
                                                "Cancel": function () {
                                                    $('#nbn-org-join-request-exists-dialog').dialog('close');
                                                }
                                            }
                                        });
                                    } else if (request.status == 400) {
                                        $('#nbn-org-member-already-dialog').dialog({
                                            resizable: false,
                                            width: 400,
                                            modal: true,
                                            buttons: {
                                                "OK": function () {
                                                    $('#nbn-org-member-already-dialog').dialog('close');
                                                }
                                            }
                                        });
                                    } else {
                                        $('#nbn-org-join-request-error-dialog').dialog({
                                            resizable: false,
                                            width: 400,
                                            modal: true,
                                            buttons: {
                                                "OK": function() {
                                                    $('#nbn-org-join-request-error-dialog').dialog('close');
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        },
                        Cancel: function() {
                            $(this).dialog('close');
                        }
                    }
                });
            }
        });
    });
})(jQuery);
(function($) {
    $(document).ready(function() {
        if (!$('#nbn-org-join-action').data('requestor')) {
            $('#nbn-org-join-accept').click(function() {
                showReasonDialog($('#nbn-org-join-action').data('id'), "accept", $(this).data("url"));
            });
            $('#nbn-org-join-decline').click(function() {
                showReasonDialog($('#nbn-org-join-action').data('id'), "decline", $(this).data("url"));
            });
        } else {
            $('#nbn-org-join-withdraw').click(function() {
                $('#nbn-org-join-withdraw-dialog').dialog({
                    resizable: false,
                    width: 400,
                    modal: true,
                    buttons: {
                        Ok: function() {
                            $.ajax({
                                type: 'POST',
                                contentType: 'application/json; charset=utf-8',
                                url: $('#nbn-org-join-withdraw').data('url'),
                                data: JSON.stringify({id: $('#nbn-org-join-withdraw').data('id'), responseType: 3}),
                                dataType: 'json',
                                success: function() {
                                    $('#nbn-org-join-withdraw').dialog('close');
                                    window.location = "/Organisations/JoinRequest/" + $('#nbn-org-join-withdraw').data('id');
                                },
                                error: function() {
                                    $('#nbn-org-join-withdraw-dialog').dialog('close');
                                    alert("ERROR: Could not withdraw join request");
                                }
                            });
                        },
                        Cancel: function() {
                            $(this).dialog("close");
                        }
                    }
                });
            });
        }

        function showReasonDialog(id, type, url) {
            $(".nbn-org-join-dialog-type").text(type);
            $("#nbn-org-join-reason-dialog").dialog({
                resizable: false,
                width: 550,
                modal: true,
                buttons: {
                    Ok: function() {
                        checkReasonDialog(url, id, type);
                    },
                    Cancel: function() {
                        $(this).dialog("close");
                    }
                }
            });
        }

        function checkReasonDialog(url, id, type) {
            var reason = $('#nbn-org-join-reason-text').val();
            if (reason.trim() === '') {
                $("#nbn-org-join-dialog-reason").text('with no reason attached?');
            } else {
                $("#nbn-org-join-dialog-reason").text('with the following reason?');
                $("#nbn-org-join-dialog-reason-text").text(reason);
            }
            
            $('#nbn-org-join-reason-check-dialog').dialog({
                resizable: false,
                width: 400,
                modal: true,
                buttons: {
                    Ok: function() {
                        $('#nbn-org-join-reason-dialog').dialog('close');
                        postResponse(url, id, type, reason);
                    },
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                }
            });
        }

        function postResponse(url, id, type, reason) {
            var reasonTypeId = 0;
            if (type === 'accept') {
                reasonTypeId = 1;
            } else {
                reasonTypeId = 2;
            }

            $.ajax({
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                url: url,
                data: JSON.stringify({id: id, reason: reason, responseType: reasonTypeId}),
                dataType: 'json',
                success: function() {
                    $('#nbn-org-join-reason-dialog').dialog('close');
                    location.reload();
                },
                error: function() {
                    $('#nbn-org-join-reason-dialog').dialog('close');
                    alert('ERROR: Could not reponsed to Join Request with ' + type);
                }
            });
        }
    });
})(jQuery);
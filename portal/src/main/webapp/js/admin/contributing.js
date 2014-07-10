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
        
        $('#nbn-add-contrib-org-submit').click(function(e) {
            var org = $("#nbn-add-contrib-org option:selected").text();
            $('#nbn-add-contrib-org-dialog-name').text(org);

            $('#nbn-add-contrib-org-submit-dialog').dialog({
                resizable: false,
                width: 400,
                modal: true,
                buttons: {
                    'Add Organisation': function() {
                        displaySendingRequestDialog('Adding Organisation');
                        $.ajax({
                            type: 'PUT',
                            contentType: "application/json; charset=utf-8",
                            url: $('#nbn-add-contrib-org-submit').data('url'),
                            data: JSON.stringify({orgID: $("#nbn-add-contrib-org option:selected").val()}),
                            dataType: "json",
                            success: function(result) {
                                if (result.result) {
                                    location.reload();
                                } else {
                                    $('#nbn-contrib-org-error-action').text('add ' + org);
                                    $('#nbn-contrib-org-error-reason').text(result.reason);
                                    $('#nbn-add-contrib-org-submit-dialog').dialog('close');
                                    $('#nbn-contrib-org-error-dialog').dialog({
                                        resizable: false,
                                        width: 400,
                                        modal: true,
                                        buttons: {
                                                'OK': function() {
                                                    $(this).dialog('close');
                                                }
                                        }
                                    });
                                }
                            },
                            error: function(result) {
                                $('#nbn-contrib-org-error-action').text('add ' + org);
                                $('#nbn-contrib-org-error-reason').text('Unknown Error has occurred, please try again later');
                                $('#nbn-add-contrib-org-submit-dialog').dialog('close');
                                $('#nbn-contrib-org-error-dialog').dialog({
                                    resizable: false,
                                    width: 400,
                                    modal: true,
                                    buttons: {
                                        'OK': function() {
                                            $(this).dialog('close');
                                        }
                                    }
                                });                                
                            }
                        });
                    },
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                }
            });
        });        
        
        $('.nbn-contributing-org-remove').click(function(e) {
            var name = $(this).data('name');
            var id = $(this).data('id');

            $('#nbn-org-remove-confirm-name').text(name);

            $("#nbn-remove-contrib-org-submit-dialog").dialog({
                resizable: false,
                width: 400,
                modal: true,
                buttons: {
                    "Remove Organisation": function() {
                        displaySendingRequestDialog('Removing Organisation');
                        $.ajax({
                            type: "DELETE",
                            contentType: "application/json; charset=utf-8",
                            url: $("#nbn-contributing-org-datatable").data("remove") + '/' + id,
                            dataType: "json",
                            success: function(result) {
                                if (result.result) {
                                    location.reload();
                                } else {
                                    $('#nbn-contrib-org-error-action').text('remove ' + org);
                                    $('#nbn-contrib-org-error-reason').text(result.reason);
                                    $('#nbn-remove-contrib-org-submit-dialog').dialog('close');
                                    $('#nbn-contrib-org-error-dialog').dialog({
                                        resizable: false,
                                        width: 400,
                                        modal: true,
                                        buttons: {
                                                'OK': function() {
                                                    $(this).dialog('close');
                                                }
                                        }
                                    });
                                }
                            },
                            error: function(result) {
                                $('#nbn-add-contrib-org-error-action').text('remove ' + name);
                                $('#nbn-add-contrib-org-error-reason').text('Unknown Error has occurred, please try again later');
                                $('#nbn-add-contrib-org-submit-dialog').dialog('close');
                                $('#nbn-add-contrib-org-error-dialog').dialog({
                                    resizable: false,
                                    width: 400,
                                    modal: true,
                                    buttons: {
                                        'OK': function() {
                                            $(this).dialog('close');
                                        }
                                    }
                                });   
                            }
                        });
                    },
                    Cancel: function() {
                        $(this).dialog("close");
                    }
                }
            });
        });
    });
    
    function replaceTerm(input, re) {
        return input.replace(re, '<span style="font-weight:bold;">' + '$&' + '</span>');
    }
})(jQuery);


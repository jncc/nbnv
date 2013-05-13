/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

(function($) {
    $('#nbn-org-join-accept').click(showConfirmDialog("accept", $(this).data(url)));
    $('#nbn-org-join-decline').click(showConfirmDialog("decline", $(this).data(url)));
    $('#nbn-org-join-withdraw').click(
        $("#nbn-org-join-confirm-admin").dialog({
            resizable: false,
            width: 400,
            modal: true,
            buttons: {
                Ok: function() {
                    $(this).dialog("close");
                    alert("Withdrew Request");
                },
                Cancel: function() {
                    $(this).dialog("close");
                }
            }
        })
    );

    function showCheckBlankReasonDialog(type, reason, url) {       
        $("#nbn-org-join-dialog-blank-type").text(type);
        
        $("#nbn-org-join-confirm-blank").dialog({
            resizable: false,
            width: 400,
            modal: true,
            buttons: {
                Ok: function() {
                    $("#nbn-org-join-confirm").dialog("close");
                    $(this).dialog("close");
                    postJoinResponse(type, reason, url);
                },
                Cancel: function() {
                    $(this).dialog("close");
                }
            }
        });
    }

    function showConfirmDialog(type, url) {
        var reason = $('#nbn-org-join-dialog-reason').val();

        $("#nbn-org-join-dialog-type").text(type);

        $("#nbn-org-join-confirm").dialog({
            resizable: false,
            width: 400,
            modal: true,
            buttons: {
                Ok: function() {
                    if (reason.trim() === "") {
                        showCheckBlankReasonDialog(type, reason, url);
                    } else {
                        $(this).dialog("close");
                    }

                },
                Cancel: function() {
                    $(this).dialog("close");
                }
            }
        });
    }
    
    function postJoinResponse(type, reason, url) {
        $.post(url, function(result) {
            
        }).fail(function(result) {
            
        });
    }
})(jQuery);
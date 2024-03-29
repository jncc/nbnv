window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};

(function($) {
    var uTable;

    $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
    $(document).ready(function() {
        $('.nbn-org-user-remove').click(function(e) {
            var name = $(this).data('name');
            var id = $(this).data('id');
            var email = $(this).data('email');

            $('#nbn-org-remove-confirm-name').text(name);
            $('#nbn-org-remove-confirm-email').text(email);
            
            $("#dialog-remove-confirm").dialog({
                resizable: false,
                width: 400,
                modal: true,
                buttons: {
                    "Remove User": function() {
                        displaySendingRequestDialog('Removing User');
                        $.ajax({
                            type: "POST",
                            contentType: "application/json; charset=utf-8",
                            url: $("#nbn-users-datatable").data("remove"),
                            data: JSON.stringify({userID: id}),
                            dataType: "json",
                            success: function(result) {
                                location.reload();
                            },
                            error: function(result) {
                                $("#dialog-remove-confirm").dialog("close");
                                alert("ERROR: Could not Remove " + id + " " + name);
                            }
                        });
                    },
                    Cancel: function() {
                        $(this).dialog("close");
                    }
                }
            });
            
            e.preventDefault();
        });

        $('.nbn-org-user-role').click(function(e) {
            var name = $(this).data('name');
            var id = $(this).data('id');
            var roleId = $(this).data('role');
            var email = $(this).data('email');

            $('#nbn-org-role-change-select').val(roleId).attr('selected', true);
            $('#nbn-org-role-change-name').text(name + '(' + email + ')');

            $("#dialog-role-choice").dialog({
                resizable: false,
                width: 400,
                modal: true,
                buttons: {
                    "Change Role": function() {
                        $(this).dialog("close");
                        showRoleConfirm(id, name);
                    },
                    Cancel: function() {
                        $(this).dialog("close");
                    }
                }
            });
            
            e.preventDefault();
        });

        uTable = $('#nbn-users-datatable').dataTable({
            "aaSorting": [[1, "asc"]],
            "bJQueryUI": true,
            "iDisplayLength": 10,
            "bSortClasses": false,
            "sPaginationType": "full_numbers",
            "aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
            "aoColumnDefs": [
                {"bVisible": false, "aTargets": [0]},
                {"bSortable": false, "aTargets": [3, 4]}
            ]
        });
        
        // Setup user information dialogs - External Dependency (dialog_utils.js)
        setupUsernameDialog(nbn.nbnv.api);   
    });

    function showRoleConfirm(id, name) {
        var roleId = $('#nbn-org-role-change-select option:selected').attr("value");
        var roleText = $('#nbn-org-role-change-select option:selected').text();

        $('#nbn-org-role-confirm-name').text(name);
        $('#nbn-org-role-confirm-type').text(roleText);

        $("#dialog-role-confirm").dialog({
            resizable: false,
            width: 400,
            modal: true,
            buttons: {
                "Change Role": function() {
                    displaySendingRequestDialog('Changing Role');
                    $.ajax({
                        type: "POST",
                        contentType: "application/json; charset=utf-8",
                        url: $("#nbn-users-datatable").data("modify"),
                        data: JSON.stringify({userID: id, role: roleId}),
                        dataType: "json",
                        success: function() {
                            location.reload();
                        },
                        error: function() {
                            $("#dialog-role-confirm").dialog("close");
                            alert("ERROR: Could not change Role of " + name + " to " + roleText);
                        }
                    });
                },
                Cancel: function() {
                    $(this).dialog("close");
                }
            }
        });
    }
})(jQuery);
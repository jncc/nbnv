/** 
 * This javascript is used to turn a list of users in a table into a Jquery plugin DataTable 
 * The first column will be invisible and The table id must be 'nbn-users-datatable'
 */

(function($) {
    var uTable;

    $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
    $(document).ready(function() {
//        $('#nbn-users-datatable tbody tr').click(function(e) {
//            if ($(this).hasClass('row_selected')) {
//                $(this).removeClass('row_selected');
//            } else {
//                uTable.$('tr.row_selected').removeClass('row_selected');
//                $(this).addClass('row_selected');
//            }
//        });

        uTable = $('#nbn-users-datatable').dataTable({
            "aaSorting": [[1, "asc"]],
            "bJQueryUI": true,
            "iDisplayLength": 10,
            "bSortClasses": false,
            "sPaginationType": "full_numbers",
            "aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
            "aoColumnDefs": [
                {"bVisible": false, "aTargets": [0]},
                {"bSortable": false, "aTargets": [3]}
            ]
        });

        $('.nbn-org-user-remove').click(function(e) {
            var name = $(this).data('name');
            var id = $(this).data('id');

            $('#nbn-org-remove-confirm-name').text(name);

            $("#dialog-remove-confirm").dialog({
                resizable: false,
                width: 400,
                modal: true,
                buttons: {
                    "Remove User": function() {
                        $(this).dialog("close");
                        alert("Removed " + id + " " + name);
                    },
                    Cancel: function() {
                        $(this).dialog("close");
                    }
                }
            });
        });

        $('.nbn-org-user-role').click(function(e) {
            var name = $(this).data('name');
            var id = $(this).data('id');
            var roleId = $(this).data('role');
            
            $('#nbn-org-role-change-select').val(roleId).attr('selected',true);

            $("#dialog-role-choice").dialog({
                resizable: false,
                width: 400,
                modal: true,
                buttons: {
                    "Change Role": function() {
                        $(this).dialog("close");
                        
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
                                    $(this).dialog("close");
                                    alert("Changed Role of " + id + " " + name + " to " + roleId + " " + roleText);
                                },
                                Cancel: function() {
                                    $(this).dialog("close");
                                }
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
})(jQuery);

function fnGetSelected(oTableLocal)
{
    return oTableLocal.$('tr.row_selected');
}
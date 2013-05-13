/** 
 * This javascript is used to turn a list of users in a table into a Jquery plugin DataTable 
 * The first column will be invisible and The table id must be 'nbn-users-datatable'
 */

(function($) {
    var rTable;

    $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
    $(document).ready(function() {
        rTable = $('#nbn-requests-datatable').dataTable({
            "aaSorting": [[1, "asc"]],
            "bJQueryUI": true,
            "iDisplayLength": 10,
            "bSortClasses": false,
            "sPaginationType": "full_numbers",
            "aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
            "aoColumnDefs": [
                {"bVisible": false, "aTargets": [0]}
            ],
            "oLanguage": {
                "sEmptyTable": "There are no outstanding requests to join this organisation"
            }
        });
    });
})(jQuery);

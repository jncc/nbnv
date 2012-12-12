/* This javascript is used to turn a list of datasets in a table into a Jquery plugin DataTable 
 * The second column of datasetKeys will be hidden but searchable, the first column will be the one sorted on by default
 * The table id must be 'nbn-datasets-datatable'
 */
(function($){
    $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
    $(document).ready(function(){
        $('#nbn-datasets-datatable').dataTable({
            "aaSorting": [[3, "desc"]],
            "bJQueryUI": true,
            "iDisplayLength": 25,
            "bSortClasses": false,
            "sPaginationType": "full_numbers",
            "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]],
            "aoColumnDefs": [
                {"bVisible": false, "aTargets": [0]},
                {"sWidth": "5%", "aTargets": [3,4]}
            ]
        });
    });
})(jQuery);
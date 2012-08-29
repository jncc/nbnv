/* This javascript will turn the input table to the Jquery plugin DataTable 
 * The second column will be hidden but searchable - eg useful for datasetKey, taxonVersionKey, etc
 * The table id must be 'nbn-generic-datatable'
 */
(function($){
    $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
    $(document).ready(function(){

        $('#nbn-generic-datatable').dataTable({
            "bJQueryUI": true,
            "aoColumnDefs": [
                {"bVisible": false, "aTargets": [1]}
            ]
        });
    });
})(jQuery);
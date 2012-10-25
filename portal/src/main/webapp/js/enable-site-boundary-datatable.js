/* This javascript is used to turn a list of site boundaries in a table into a Jquery plugin DataTable 
 * The table id must be 'nbn-site-boundary-datatable'
 */
(function($){
    $(document).ready(function(){
        $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
        $('#nbn-site-boundary-datatable').dataTable({
            "bJQueryUI": true,
            "iDisplayLength": 25,
            "sPaginationType": "full_numbers",
            "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]],
            "aoColumnDefs": [
                {"sWidth": "75%", "aTargets": [0]}
            ]
        });
        $('#nbn-site-boundary-datatable').width("100%");
    });
})(jQuery);

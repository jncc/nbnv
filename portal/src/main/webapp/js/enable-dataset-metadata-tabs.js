/* This javascript will implement JQuery tabs for a div with id=nbn-tabs
 * The html inside the div must be as specified in http://jqueryui.com/demos/tabs/ 
 * It will also wrap up a table as a JQueryUI plugin DataTable provided it has the id 'nbn-generic-table'
 * Dependencies:
 * - /js/jquery.dataTables.min.js
 * - http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css
 */
(function($){
    $(document).ready(function(){
        $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
        $('#nbn-tabs').bind('tabsload', function(event, ui){
            $(ui.panel).find('#nbn-generic-datatable').dataTable({
            "bJQueryUI": true,
            "aoColumnDefs": [
                {"bVisible": false, "aTargets": [1]}
            ]
            });
        });
        $('#nbn-tabs').tabs();
    });
})(jQuery);

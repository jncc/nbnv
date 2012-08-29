/* This javascript will implement JQuery tabs for a div with id=nbn-tabs
 * The html inside the div must be as specified in http://jqueryui.com/demos/tabs/ 
 */
(function($){
    $(document).ready(function(){
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

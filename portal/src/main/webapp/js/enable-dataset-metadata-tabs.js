/* This javascript will implement JQuery tabs for a div with id=nbn-tabs
 * The html inside the div must be as specified in http://jqueryui.com/demos/tabs/ 
 */
(function($){
    $(document).ready(function(){
        $('#nbn-tabs').tabs();
        $('#nbn-tabs').bind('tabsload', function(event, ui){
            $(ui.panel).find('#nbn-site-boundary-table').addClass('nbn-dataset-table');
            $(ui.panel).find('#nbn-site-boundary-table tr:even').addClass('ui-state-highlight');
            $(ui.panel).find('#nbn-site-boundary-table tr').hover(
                function(){
                    $(this).children('td').addClass('ui-state-hover');
                },
                function(){
                    $(this).children('td').removeClass('ui-state-hover');
                }
            );
        });
    });
})(jQuery);
/* This javascript will turn the input table to the Jquery plugin DataTable */
(function($){
    $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
    $(document).ready(function(){
        $('#nbn-datatable').dataTable({
            "bJQueryUI": true
        });
    });
})(jQuery);
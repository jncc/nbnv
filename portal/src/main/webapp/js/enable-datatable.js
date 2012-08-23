/* This javascript will turn the input table to the Jquery plugin DataTable */
(function($){
    $(document).ready(function(){
        $('#nbn-datatable').dataTable({
            "bJQueryUI": true
        });
    });
})(jQuery);
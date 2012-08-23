/**
 * This is used to add a simple alternating colour to a table
 * The table needs to use the class nbn-simple-table
 */
(function($){
    $(document).ready(function(){
        $(".nbn-simple-table tr:even").css("background-color","#F7F7F7");
    }); 
})(jQuery);

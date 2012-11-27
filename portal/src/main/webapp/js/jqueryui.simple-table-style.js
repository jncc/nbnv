/**
 * This is used to add a simple alternating colour to a table
 * The table needs to use the class 'nbn-simple-table'
 */
(function($){
    
    namespace("nbn.portal.style", {
        addSimpleTableStyle: function(){
            $(".nbn-simple-table tr:even").addClass("ui-state-highlight");
        }
    });
    
    $(document).ready(function(){
        nbn.portal.style.addSimpleTableStyle();
    }); 
    
})(jQuery);

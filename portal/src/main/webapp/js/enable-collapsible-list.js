/**
 * * This is mine and jons attempt at creating expandable lists
 */
(function($){
    $(document).ready(function(){
        $('.collapsible-list ul').hide();
        $('.collapsible-list h1').each(function(){
            $(this).parent().prepend($("<span>").addClass("icons-collapse"));
        });
        $('.collapsible-list h1').click(function(){
            $('~ span', this).toggleClass("icons-exapnd icons-collapse");
            $('~ ul', this).toggle('slow');
        });
    });
})(jQuery);
/**
 * The following JavaScript will enable collapsible-list functionality for well 
 * formed lists with a .collapsible-list class attached
 */
(function($){
    $(document).ready(function(){
        $('.collapsible-list ul').hide(); //first off. Hide all the sub lists
        
        $('.collapsible-list ul').each(function(){
            var list = $(this);
            list.parent().prepend(     //put the container before the sublist
                $("<span>")
                    .addClass("collapsible-list-icon icons-expand")
                    .click(function(){  //register a click listener which toggles the icon and list visibility
                        $('.collapsible-list-icon', this).toggleClass("icons-expand icons-collapse");
                        $('~ ul', this).stop().slideToggle('slow');
                    })
            );
        });
    });
})(jQuery);
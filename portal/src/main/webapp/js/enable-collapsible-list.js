/**
 * The following JavaScript will enable collapsible-list functionality for well 
 * formed lists with a .collapsible-list class attached
 */
(function($){
    $(document).ready(function(){
        $('.collapsible-list ul').hide(); //first off. Hide all the sub lists
        
        $('.collapsible-list h1').each(function(){
            var title = $(this);
            title.parent().prepend(     //put the container before the sublist
                $('<div>')              //create a div which contains an icon and the title
                    .append($("<span>").addClass("collapsible-list-icon icons-expand"))
                    .append(title)
                    .click(function(){  //register a click listener which toggles the icon and list visibility
                        $('.collapsible-list-icon', this).toggleClass("icons-expand icons-collapse");
                        $('+ ul', this).stop().slideToggle('slow');
                    })
            );
        });
    });
})(jQuery);
/**
 * * This is mine and jons attempt at creating expandable lists
 */
(function($){
    $(document).ready(function(){
        $('.expandableList ul').hide();
        $('.expandableList h1').click(function(){
            $('> ul', $(this).parent()).toggle('slow');
        });
    });
})(jQuery);
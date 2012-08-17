/**
 * The following JavaScript will enable map highlighting functionality for all
 * image maps
 */
(function($){
    $(document).ready(function() {
        $('img[usemap]').maphilight();
    });
})(jQuery);
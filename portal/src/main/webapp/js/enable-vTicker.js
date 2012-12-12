/**
 * The following JavaScript will enable news ticker functionality
 */
(function($){
    $(document).ready(function(){
        var newsTicker = $('.news-ticker');
        $('<div>').insertAfter(newsTicker).append(newsTicker).vTicker({ 
            speed: 500,
            pause: 5000,
            animation: 'fade',
            mousePause: true,
            showItems: 1
        });
    });
})(jQuery);
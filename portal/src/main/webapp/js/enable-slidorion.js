/**
 * The following JavaScript will enable slidorian functionality
 */
(function($){
    $(document).ready(function(){
        $('#slidorion').slidorion({
            effect: 'slideRandom',
            hoverPause: true,
            interval: 20000,
            speed: 800,
            controlNav: false,
            controlNavClass: 'nav'
        });
    });
})(jQuery);
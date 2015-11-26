(function($){
    function initAutocomplete() {
        var ac = $('#taxonautocomplete');
        
        ac.autocomplete({
                source: function(request, response) {
                    $.getJSON(ac.attr('api')+ '?q=' + request.term, function(data) {
                        response($.map(data.results, function(item) { item.value = item.name; return item; }));
                    });
                },
                select: function(event, ui) {
                    ac.val(ui.item.name);
                    window.location = ui.item.href;
                }
            });
            
        ac.data( "autocomplete" )._renderItem = function(ul, item) {
            var authority = item.authority ? item.authority : '';
            return $( "<li></li>" )
                .data( "item.autocomplete", item )
                .append( "<a><strong>" + item.searchMatchTitle + "</strong> " + authority + "<br>" + item.descript + "</a>" )
                .appendTo(ul);
            };

        ac.watermark('Taxon name', {className: 'watermark'});
    }
    
    $(document).ready(function(){
        initAutocomplete();
    });
        
})(jQuery);



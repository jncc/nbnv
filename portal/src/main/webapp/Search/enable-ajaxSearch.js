/**
 * The following JavaScript will enable ajax Search
 */
(function($){
    $(document).ready(function(){
        var searchForm = $('form.nbn-search').nbn_search({
            renderSearchResult:function(data){
                return "<h3>" + data.record_type + "</h3>" + data.name;
            },
            queried: function(evt, state) { //save state for deeplinking
                var path = '?' + state.formencodded;
                if (window.history.pushState)
                    window.history.pushState( state, path, path);
            }
        });
        
        window.onpopstate = function(e){  //Enable deeplinking
            if(e.state) {
                searchForm.nbn_search('setState',e.state); 
            } 
        }
    });
})(jQuery);
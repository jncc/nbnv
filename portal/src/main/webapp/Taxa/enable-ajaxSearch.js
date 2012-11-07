/**
 * The following JavaScript will enable ajax Search
 */
(function($){
    $(document).ready(function(){
        var searchForm = $('form.nbn-search').nbn_search({
            searchNode: '/api/taxa',
            renderSearchResult:function(data){
                return $('<div>')
                    .append($("<h3>")).html(data.name)
                    .append($("<ul>")
                        .append($("<li>").append($("<a>").html("Grid map for " + data.name).attr("href", "/Reports/Single_Species/" + data.pTaxonVersionKey + "/Grid_Map")))
                    );
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
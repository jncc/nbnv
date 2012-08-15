/**
 * The following JavaScript will enable ajax Search
 */
(function($){
    
    function renderResult(result) {
        return result.name;
    }
    
    
    function updatePagingLinks(nbnSearchForm) {}
    
    function updateCounts(nbnSearchForm, facetFields) {
        $(".facet-count", nbnSearchForm).html("(0)");// reset all facet fields
        $.each(facetFields, function(currFacetName, facetData) {
            $('.nbn-search-facet[rel="'+ currFacetName + '"] .facet-count', nbnSearchForm)
                .each(function() {
                    var currCount = $(this);
                    currCount.html("(" + facetData[currCount.attr('rel')] + ")");
                })
        })
    }
    
    function updateResults(nbnSearchForm) {
        var resultsDiv = $('.results', nbnSearchForm).empty();
        $.getJSON('/api/taxa', nbnSearchForm.serialize(), function(search) {
            updateCounts(nbnSearchForm, search.facetFields);
            $.each(search.results, function(i, data) {
                resultsDiv.append($('<li>').html(renderResult(data)));
            });
        });
    }
    
    $(document).ready(function(){
        $('form.nbn-search').each(function(){
            var me = $(this);
            $('input, select', me).change(function() { updateResults(me); });
            $('input[type="submit"]', me).remove();
        });
    });
})(jQuery);
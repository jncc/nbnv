/**
 * The following JavaScript will progressivly enhance the Search 
 * tag and provide ajax Search.
 * @author Christopher Johnson
 * @dependancy jquery.pagination.js, jQuery.query_string.js, jQuery, jQuery UI
 */
 (function( $, undefined ) {
    $.widget( "ui.nbn_search", {
        options: {
            renderSearchResult: function() {return "Result render not defined";}
        },
 
        _create: function() {
            var me = this;    
            this._state = {start: $.query_string().start || 0}; //get start value
            /*create dummy request response object so don't need to check it exists every search*/
            this._searchRequest = {abort: function(){return false;}};
            this._resultsDiv = $('.results', this.element);
            $('input, select', this.element).change(function() {me._state.start=0; me._querySearchNode();});
            $('input[type="submit"]', this.element).remove(); //remove the unnessersary search button
            this._querySearchNode(); //Ajaxify the search from the start
        },

        _createSearchURL : function() {return this.element.serialize() + "&start=" + this._state.start;},
        
        _querySearchNode: function() {
            var me=this, searchParams = me._createSearchURL();
            this._searchRequest.abort();
            this._searchRequest = $.getJSON(me.options.searchNode, searchParams , function(search) {
                //create and persist the state of this search form
                me.setState({ 
                    search: search,         facet: me._getFacetState(), 
                    start: me._state.start, formencodded : searchParams
                });
                me._trigger('queried', 0, me.getState());
            });
        },
        
        getState :function() {
            return this._state;
        },
        
        setState: function(state) {
            this._state = state; //store the state object locally
            this._updateCounts();
            this._updatePagingLinks();
            this._updateResults();
        },
        
        _updateResults: function() {
            var me=this;
            this._resultsDiv.empty();
            $.each(me._state.search.results, function(i, data) {
                me._resultsDiv.append($('<li>').html(me.options.renderSearchResult(data)));
            });
        },
        
        _getFacetState: function() {
            var toReturn=[];
            $('.nbn-search-facets input[type="checkbox"]', this.element).each(function(i, ele) {
                var me = $(ele);
                if(me.is(':checked'))
                    toReturn.push({ name: me.attr("name"), value:me.attr("value") });
            })
            return toReturn;
        },
        
        _updateFacets: function() {
            //reset checkboxes
            $('.nbn-search-facets input[type="checkbox"]', this.element).prop('checked', false);
            $.each(this._state.facet, function(i, curr) {
                $('.nbn-search-facets input'+
                    '[type="checkbox"][name="'+ curr.name + '"]' +
                    '[value="'+ curr.value + '"]', this.element
                ).prop('checked', true);
            });
        },
        
        _updateCounts: function() {
            var me=this;
            $(".facet-count", this.element).html("(0)");// reset all facet fields
            $.each(this._state.search.facetFields, function(currFacetName, facetData) {
                $('.nbn-search-facet[rel="'+ currFacetName + '"] .facet-count', me.element)
                    .each(function() {
                        var currCount = $(this);
                        currCount.html("(" + facetData[currCount.attr('rel')] + ")");
                    })
            });
        },
        
        _updatePagingLinks: function() {
            var me=this, rows = $('[name="rows"]',this.element).val(), currPage = this._state.start/rows;
            
            $('.paginator', this.element).pagination(this._state.search.header.numFound, {
                num_edge_entries:2,
                items_per_page: rows,
                current_page: currPage,
                prev_text:"&laquo; Previous",
                next_text:"Next &raquo;",
                callback: function(page) {
                    if(page != currPage) {
                        me._state.start = page * rows;
                        me._querySearchNode();
                    }
                    return false;
                }
            });
        }
    });
})( jQuery );
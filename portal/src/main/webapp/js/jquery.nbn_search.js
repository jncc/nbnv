/**
 * The following JavaScript will progressivly enhance the Search 
 * tag and provide ajax Search.
 * @author Christopher Johnson
 * @dependancy jquery.dataTables.js jQuery, jQuery UI
 */
 (function( $, undefined ) {
     function toObject(arr) {
        var toReturn = {};
        $.each(arr, function(i, val) {toReturn[val.name] = val.value;}); 
        return toReturn;
    }
    
    function processResults(callbackField, serverRes) {
        var toReturn = [];
        $.each(serverRes, function(i, val) {
            toReturn.push(['<a href="' + val[callbackField] + '">'+ val.name+'</a>']);
        });
        return toReturn;
    }
    
    $.widget( "ui.nbn_search", {
        _create: function() {
            var me = this, initialSearch = $('input[name="q"]', me.element).val();
            $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
            $('.controls, .results, .paginator', me.element).remove();
            $('<table>').addClass("nbn-simple-table").append($("<thead>")
                .append($('<tr>').append($('<th>').html("Taxon")))
            ).appendTo(me.element).dataTable( {
                "oSearch": {"sSearch": initialSearch},
                "iDisplayLength": 25,
                "bJQueryUI": true,
                "bProcessing": true,
                "bServerSide": true,
                "sPaginationType": "full_numbers",
                "sAjaxSource": me.element.attr('nbn-search-node'),
                "fnServerData": function( sUrl, aoData, fnCallback, oSettings ) {
                    var query = toObject(aoData);
                    oSettings.jqXHR = $.ajax( {
                        "url": sUrl,
                        "data": {
                            q: query.sSearch,
                            start: query.iDisplayStart,
                            rows: query.iDisplayLength
                        },
                        "success": function(data) {
                            fnCallback.call(this, {
                                iTotalDisplayRecords: data.header.numFound,
                                iTotalRecords: data.header.numFound,
                                sEcho: query.sEcho,
                                aaData:processResults("href", data.results)
                            });
                        },
                        "dataType": "jsonp",
                        "cache": false
                    } );
                }
            } );
        },
        
        _getFacetState: function() {
            var toReturn=[];
            $('.nbn-search-facets input[type="checkbox"]', this.element).each(function(i, ele) {
                var me = $(ele);
                if(me.is(':checked'))
                    toReturn.push({name: me.attr("name"), value:me.attr("value")});
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
        }
    });
})( jQuery );
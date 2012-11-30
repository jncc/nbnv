/**
 * The following JavaScript will progressivly enhance the Search 
 * tag and provide ajax Search.
 * @author Christopher Johnson
 * @dependancy jquery.dataTables.js jQuery, jQuery UI
 */
 (function( $, undefined ) {
    var SEARCH_NODE_ATTR = 'nbn-search-node',
        RESULT_LINK_ATTR = 'result-link',
        RESULT_ATTR = 'result-attr';
        
    /** 
     * The following utility function will transform dataTables aoData structure
     * (an array) to a standard js object for simple lookups
     */
    function toObject(arr) {
        var toReturn = {};
        $.each(arr, function(i, val) {toReturn[val.name] = val.value;}); 
        return toReturn;
    }

    $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
            
    $.widget( "ui.nbn_search", {
        _create: function() {
            var me = this, initialSearch = $('input[name="q"]', me.element).val();
            $('.controls, .paginator', me.element).remove(); //remove the elements which are going to be replace with datatable
            
            me._dataTable = $('.results', me.element).removeClass("results"); //maintain a reference to the data table (Remove old styling class)
            
            me._dataTable.dataTable( {
                "oSearch": {"sSearch": initialSearch},
                "iDisplayLength": 25,
                "bJQueryUI": true,
                "bProcessing": true,
                "bServerSide": true,
                "sPaginationType": "full_numbers",
                "sAjaxSource": me.element.attr(SEARCH_NODE_ATTR),
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
                                aaData:me._processResults(data.results)
                            });
                        },
                        "dataType": "jsonp",
                        "cache": false
                    } );
                }
            } );
        },
        
        _processResults: function processResults(serverRes) {
            var _me = this, toReturn = [];
            $.each(serverRes, function(i, val) {
                var row = [];
                $('thead th', _me.element).each(function() {
                    var ele = $(this), label = val[ele.attr(RESULT_ATTR)] || "";
                    row.push( 
                        ele.attr(RESULT_LINK_ATTR) //if the element has a label
                            ? '<a href="' + val[ele.attr(RESULT_LINK_ATTR)] + '">'+ label +'</a>'
                            : label
                    );
                });
                toReturn.push(row);
            });
            return toReturn;
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
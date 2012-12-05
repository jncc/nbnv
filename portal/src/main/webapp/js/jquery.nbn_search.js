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
        _initSpatialSearch: function(inputField) {
            var _me = this;
            map = new OpenLayers.Map({
                div: inputField.attr("map-div"),
                displayProjection: new OpenLayers.Projection("EPSG:4326"),
                layers: [
                    new OpenLayers.Layer.OSM({sphericalMercator:true}),
                    new OpenLayers.Layer.WMS("Site layer", inputField.attr("spatial-layer"), {
                        format: "image/png",
                        transparent: true
                    }, { isBaseLayer:false, projection: new OpenLayers.Projection("EPSG:3857") })
                ],
                eventListeners: {
                    moveend : function(evt) {
                        inputField.attr("value", evt.object.getExtent().transform(
                                    evt.object.getProjectionObject(),
                                    new OpenLayers.Projection("EPSG:4326")).toBBOX());
                        _me.refresh();
                    }            
                }
            });
            map.zoomToExtent(new OpenLayers.Bounds(
                -2695039.240359,6300039.134229,2216498.44845,7938849.020435));
        },
        
        _create: function() {
            var me = this, initialSearch = $('input[name="q"]', me.element).val();
            
            this._customFilters = $('.nbn-search [name]');  //store a list of the additional filters

            this._dataTable = $('.results', me.element).removeClass("results"); //maintain a reference to the data table (Remove old styling class)
            
            this._dataTable.dataTable( {
                "oSearch": {"sSearch": initialSearch},
                "bSort": false,
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
                        "data": me._generateSearchQuery(query),
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
            this._dataTable.prev().append($('.filters', this.element));
            
            $('input[spatial-layer]', this.element).each(function(i, ele) {//enable all spatial filters
                me._initSpatialSearch($(ele));}
            );
            
            
            this._customFilters.change(function() {me._dataTable.fnDraw();}); //register listeners to the custom filters
            
            $('.controls, .paginator', me.element).remove(); //remove the elements which are going to be replace with datatable
        },
        
        refresh : function() {  this._dataTable.fnDraw();    },
        
        _generateSearchQuery: function(query) {
            //generate the additional filters data object
            var additionalFilters = toObject(this._customFilters.map(function() {
                return {name: $(this).attr('name'), value: $(this).val()};
            }));
            //extend the query with these additional filters
            return $.extend(additionalFilters, {
                q: query.sSearch,
                start: query.iDisplayStart,
                rows: query.iDisplayLength
            });
        },
        
        _processResults: function processResults(serverRes) {
            var _me = this, toReturn = [];
            $.each(serverRes, function(i, val) {
                var row = [];
                $('thead th', _me.element).each(function() {
                    var ele = $(this), label = val[ele.attr(RESULT_ATTR)];
                    if(label == null) label = "";
                    row.push( 
                        ele.attr(RESULT_LINK_ATTR) //if the element has a label
                            ? '<a href="' + val[ele.attr(RESULT_LINK_ATTR)] + '">'+ label +'</a>'
                            : label
                    );
                });
                toReturn.push(row);
            });
            return toReturn;
        }
    });
})( jQuery );
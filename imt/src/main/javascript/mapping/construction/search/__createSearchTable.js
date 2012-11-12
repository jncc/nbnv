
(function($) {
    function toObject(arr) {
        var toReturn = {};
        $.each(arr, function(i, val) {
            toReturn[val.name] = val.value;
        }); 
        return toReturn;
    }

    function processResults(callbackField, serverRes) {
        var toReturn = [];
        $.each(serverRes, function(i, val) {
            toReturn.push(['<a href="' + val[callbackField] + '">'+ val.name+'</a>']);
        });
        return toReturn;
    }

    $.namespace("nbn.construction.search__createSearchTable", function(options, callback) {
        //define table variables
        var toReturn = $('<div>').css({"margin-bottom": 30}), table = $('<table>')
            .append($("<thead>")
                .append($('<tr>').append($('<th>').html(options.title)))
            ).appendTo(toReturn);

        //construct table
        table.dataTable( {
            "bProcessing": true,
            "bServerSide": true,
            "sPaginationType": "full_numbers",
            "sAjaxSource": nbn.util.ServerGeneratedLoadTimeConstants.data_api + "/search/" + options.searchSource,
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
                            aaData:processResults(options.callbackField, data.results)
                        });
                    },
                    "dataType": "jsonp",
                    "cache": false
                } );
            }
        } );

        //add a listener
        table.on('click', 'a', function() {
           callback($(this).attr('href'));
           return false; 
        });
        return toReturn;
    });
})(jQuery);
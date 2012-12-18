window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.picker = nbn.layer.picker || {};

nbn.layer.picker.SiteBoundaryPicker = function(layerToQuery) {
	$.extend(this, new nbn.layer.picker.ArcGisLayerFeaturePicker(layerToQuery, {
		createPickerDiv: function(resultsFromIdentify, position, callback) {
//console.log(resultsFromIdentify);
//resultsFromIdentify = ['GA000942E007'];//Wiltshire & Swindon Biological Records Centre

            if(resultsFromIdentify.length!==0) {
                var errorDiv = $('<div>').html('An error occured whilst trying to obtain a response from the picker server');
                var toReturn = $('<div>');
                var jqxhrs = [];
                $.each(resultsFromIdentify, function(index, identifier){
                    var url = nbn.util.ServerGeneratedLoadTimeConstants.data_api + '/features/' + identifier;
                    jqxhrs.push(
                        $.getJSON(url, function(siteBoundary){
                            $('<div>')
                                .append($('<a>' + siteBoundary.label + '</a>')
                                    .attr('href', siteBoundary.href)
                                    .attr('target', '_blank')
                                    .attr('alt', 'Site name')
                                )
                                .appendTo(toReturn); 
                        }).error(function(){
                            errorDiv.appendTo(toReturn)
                        })
                    );
                });
                $.when.apply(this, jqxhrs).done(function (){
                    toReturn.tabs();
                    callback(toReturn);
                });
            }else{
                toReturn.append($('<div>No Results here</div>'));
                callback(toReturn);
            }
        }
    }));
}

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.picker = nbn.layer.picker || {};

nbn.layer.picker.HabitatPicker = function(layerToQuery) {
    $.extend(this, new nbn.layer.picker.ArcGisLayerFeaturePicker(layerToQuery, {
        createPickerDiv: function(resultsFromIdentify, position, callback) {

//console.log(resultsFromIdentify);
//resultsFromIdentify = ['HL0000020101:0001394'];//Habitat feature from: Blanket Bog BAP Priority Habitat - England v2.1

            if(resultsFromIdentify.length!==0) {
                var errorDiv = $('<div>').html('An error occured whilst trying to obtain a response from the picker server');
                var toReturn = $('<div>');
                var jqxhrs = [];
                $.each(resultsFromIdentify, function(index, identifier){
                    var url = nbn.util.ServerGeneratedLoadTimeConstants.data_api + '/habitatFeatures/' + identifier;
                    jqxhrs.push(
                        $.getJSON(url, function(habitatFeature){
                            $('<div>')
                                .append('<ul>')
                                .append($('<li>')
                                    .append($('<a>' + habitatFeature.datasetTitle + '</a>')
                                        .attr('href', habitatFeature.datasetHref)
                                        .attr('target', '_blank')
                                        .attr('alt', 'Habitat dataset')
                                    )
                                )
                                .append($('<li>').html("Date uploaded: " + habitatFeature.formattedUploadDate))
                                .append($('<li>').html("Identifier: " + habitatFeature.identifier))
                                .append($('<li>').html("Dataset key: " + habitatFeature.formattedUploadDate))
                                .append($('<li>').html("Provider's feature key: " + habitatFeature.formattedUploadDate))
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

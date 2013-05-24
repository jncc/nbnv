window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.requestEditResult = function(endpoint) {
    this._endpoint = endpoint;

    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'result')
            .append($('<span>').addClass('filterheader').append('Request Actions'))
    };
    
    this._renderPanel = function() {
        return $('<div>')
            .append($('<p>')
                .attr('id', 'recordcounts')
            ).append($('<button>')
                .attr('id', 'changebtn')
                .text('Change')
            ).append($('<button>')
                .attr('id', 'cancelbtn')
                .text('Cancel')
                .click(function() {
                    window.location = '/AccessRequest/Admin';
                })
            );
    };

    this.setupData = function(json, dataset, datasetEndpoint) {
        var filter = {};
        $('#recordcounts').html('');
        $('#recordcounts').text('Loading request record coverage')
        
        if (!json.taxon.all) { 
            if (json.taxon.tvk) {
                filter.ptvk = json.taxon.tvk; 
            } else if (json.taxon.designation) {
                filter.designation = json.taxon.designation;
            } else if (json.taxon.output) {
                filter.taxonOutputGroup = json.taxon.output;
            }
        }
        
        if (!json.year.all) { filter.startYear = json.year.startYear; filter.endYear = json.year.endYear; }
        if (!json.spatial.all) { filter.featureID = json.spatial.feature; filter.spatialRelationship = json.spatial.matchType; }
        if (json.sensitive == 'sans') { filter.sensitive = 'true'; }
        filter.datasetKey = dataset;

        $.ajax({
            url: nbn.nbnv.api + datasetEndpoint,
            data: filter,
            success: function(datasets) { 
                $('#recordcounts').html('');
                $('#recordcounts').append('This request covers ' + datasets[0].querySpecificObservationCount + ' of ' + datasets[0].taxonDataset.recordCount + ' records in the dataset');
            }
        });
    };
    
    this._onEnter = function(json, id) {
        var _me = this;
        
        $('#changebtn').click(function() {
                    $.ajax({
                        type: "PUT"
                        , url: nbn.nbnv.api + _me._endpoint + '/' + id
                        , contentType: 'application/json'
                        , processData: false
                        , data: JSON.stringify(json)
                        , complete: function(data) {
                            window.location = '/AccessRequest/Admin';
                        }
                    })
                })
    };
    
    this._onExit = function() {
    };
};
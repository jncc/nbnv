window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.requestEditResult = function(endpoint, dataset, datasetEndpoint, grantDialog) {
    this._endpoint = endpoint;
    this._dataset = dataset;
    this._datasetEndpoint = datasetEndpoint;
    this._grantDialog = grantDialog;

    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'result')
            .append($('<span>').addClass('filterheader').append('Request Actions'));
    };
    
    this._renderPanel = function() {
        return $('<div>')
            .append($('<p>')
                .attr('id', 'recordcounts')
            ).append($('<button>')
                .attr('id', 'changebtn')
                .text('Change And Approve')
            ).append($('<button>')
                .attr('id', 'cancelbtn')
                .text('Cancel')
                .click(function() {
                    window.location = '/AccessRequest/Admin';
                })
            ).append($('<div>')
                .attr('id', 'editWaitingWindow')
                .append($('<span>')
                    .text('Editing Request'))
                .append($('<img>')
                    .attr('src', '/img/ajax-loader-medium.gif'))
                .dialog({ 
                    modal: true, 
                    autoOpen: false,
                    width: 400
                })
            );
    };

    this.setupData = function(json, dataset, datasetEndpoint) {
        var filter = {};
        $('#recordcounts').html('');
        $('#recordcounts').empty().append($('<img>').attr('src', '/img/ajax-loader.gif')).append('  Loading request record coverage');
        $('#changebtn').button('disable');
        
        if (!json.taxon.all) { 
            if (json.taxon.tvk) {
                filter.ptvk = json.taxon.tvk; 
            } else if (json.taxon.designation) {
                filter.designation = json.taxon.designation;
            } else if (json.taxon.output) {
                filter.taxonOutputGroup = json.taxon.output;
            } else if (json.taxon.orgSuppliedList) {
                filter.orgSuppliedList = json.taxon.orgSuppliedList;
            }
        }
        
        if (!json.year.all) { filter.startYear = json.year.startYear; filter.endYear = json.year.endYear; }
        if (!json.spatial.all) { 
            filter.spatialRelationship = json.spatial.matchType; 
            
            if (json.spatial.feature) {
                filter.featureID = json.spatial.feature; 
            } else if (json.spatial.gridRef) {
                filter.gridRef = json.spatial.gridRef;
            }
        }
        
        if (json.sensitive === 'sans') { filter.sensitive = 'true'; }
        filter.datasetKey = dataset;

        $.ajax({
            url: nbn.nbnv.api + datasetEndpoint,
            data: filter,
            success: function(datasets) { 
                $('#recordcounts').html('');
                $('#recordcounts').append('This request covers ' + datasets[0].querySpecificObservationCount + ' of ' + datasets[0].taxonDataset.recordCount + ' records in the dataset, ' + datasets[0].querySpecificSensitiveObservationCount + ' are sensitive records');
                $('#changebtn').button('enable');
            }
        });
    };
    
    this._onEnter = function(json, id) {
        var _me = this;
        
        $('#changebtn').click(function() {
            _me._grantDialog.show(id, json, _me._dataset, '/taxonObservations/datasets/' + _me._dataset + '/requestable', _me._endpoint);
        });
    };
    
    this._onExit = function() {
    };
};
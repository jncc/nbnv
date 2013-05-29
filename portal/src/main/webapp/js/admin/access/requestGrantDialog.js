window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.dialog = nbn.nbnv.ui.dialog || {};

nbn.nbnv.ui.dialog.requestGrantDialog = function() {
    this.requestID =  -1;
    this.div = null;
    this.reason = '';
    
    this._render = function() {
        var _me = this;
        
        this.div = $('<div>')
            .append($('<p>')
                .append('Granting a request gives access to the full version of the requested records to the user or organisation. It will also dispatch an email informing them of the granted access.')
            ).append($('<p>')
                .attr('id', 'granteffect')
            ).append($('<p>')
                .attr('id', 'grantanonwarn')
                .addClass('ui-state-error')
                .append('This request was sent without the user knowing about your dataset. If you reply, they will. So be careful m\'kay?')
                .hide()
            ).append($('<p>')
                .append('Grant reason:')
                .append($('<textarea>')
                    .attr('cols', '75')
                    .attr('rows', '15')
                    .watermark("Please enter reason here")
                    .change(function() {
                        _me.reason = $(this).val();
                    })
                )
            ).dialog({ 
                modal: true, 
                autoOpen: false,
                width: 650,
                buttons: { 
                    "Grant Request": function() {
                        alert(_me.requestID);
                        alert(_me.reason);
                        $(this).dialog("close"); 
                    }, Cancel: function() { 
                        $(this).dialog("close"); 
                    }
                }
            });
    };
    
    this.show = function(id, json, dataset, datasetEndpoint) {
        this.requestID = id;
        var filter = {};
        $('#granteffect').html('');
        $('#granteffect').text('Loading request record coverage');
        $('#grantanonwarn').hide();
        
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
                $('#granteffect').html('');
                $('#granteffect').append('This request covers ' + datasets[0].querySpecificObservationCount + ' of ' + datasets[0].taxonDataset.recordCount + ' records in the dataset, ' + datasets[0].querySpecificSensitiveObservationCount + ' are sensitive records');
                
                if (!json.spatial.all && datasets[0].querySpecificObservationCount == datasets[0].querySpecificSensitiveObservationCount) {
                    $('#grantanonwarn').show();
                } 
            }
        });

        this.div.dialog("open");
    };
}
window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.dialog = nbn.nbnv.ui.dialog || {};

nbn.nbnv.ui.dialog.requestDenyDialog = function() {
    this.requestID =  -1;
    this.div = null;
    this.reason = '';
    
    this._render = function() {
        var _me = this;
        
        this.div = $('<div>')
            .append($('<p>')
                .append('Denying a request dispatches an email informing them of the denied access request. If you want to silently deny a request, close it instead.')
            ).append($('<p>')
                .attr('id', 'denyeffect')
            ).append($('<p>')
                .attr('id', 'denyanonwarn')
                .addClass('ui-state-error')
                .append('This request was sent without the user knowing about your dataset. If you reply, they will. So be careful m\'kay?')
                .hide()
            ).append($('<p>')
                .append('Deny reason:')
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
                    "Deny Request": function() {
                        var filter = { action: "deny", reason: _me.reason };
                        
                        $.ajax({
                            type: 'POST',
                            url: nbn.nbnv.api + '/user/userAccesses/requests/' + _me.requestID,
                            data: filter,
                            success: function () { document.location.reload(true); }
                        });
                    }, Cancel: function() { 
                        $(this).dialog("close"); 
                    }
                }
            });
    };
    
    this.show = function(id, json, dataset, datasetEndpoint) {
        this.requestID = id;
        var filter = {};
        $('#denyeffect').html('');
        $('#denyeffect').text('Loading request record coverage');
        $('#denyanonwarn').hide();
        
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
                $('#denyeffect').html('');
                $('#denyeffect').append('This request covers ' + datasets[0].querySpecificObservationCount + ' of ' + datasets[0].taxonDataset.recordCount + ' records in the dataset, ' + datasets[0].querySpecificSensitiveObservationCount + ' are sensitive records');
                
                if (!json.spatial.all && datasets[0].querySpecificObservationCount == datasets[0].querySpecificSensitiveObservationCount) {
                    $('#denyanonwarn').show();
                } 
            }
        });

        this.div.dialog("open");
    };
}
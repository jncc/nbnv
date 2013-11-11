window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.dialog = nbn.nbnv.ui.dialog || {};

nbn.nbnv.ui.dialog.requestEditGrantDialog = function() {
    this.requestID =  -1;
    this.div = null;
    this.reason = '';
    this.timeLimit = null;
    this.orgReq = false;
    
    // Edit Parameters
    this.json = null;
    this.dataset = null;
    this.datasetEndpont = null;
    this.editEndpoint = null;
    
    
    this._render = function() {
        var _me = this;
        
        this.div = $('<div>')
            .attr('id', 'grantDialogDiv')
            .append($('<p>')
                .append('Granting a request gives access to the full version of the requested records to the user or organisation. It will also dispatch an email informing them of the granted access.')
            ).append($('<p>')
                .attr('id', 'granteffect')
            ).append($('<p>')
                .attr('id', 'grantanonwarn')
                .addClass('ui-state-error')
                .append('THIS REQUEST CONTAINS SENSITIVE RECORDS REQUESTED FOR SPECIFIC SITE(S). ACCEPTING THIS ACCESS REQUEST MAY REVEAL THE LOCATION OF THESE SENSITIVE RECORDS TO THE USER')
                .hide()
            ).append($('<p>')
                .attr('id', 'granttimelimit')
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
                    "Edit And Grant Request": function() {
                        displaySendingRequestDialog('Working...');
                        
                        var url = null;
                        var filter = {action: "grant", reason: _me.reason, json: _me.json, rawJSON: JSON.stringify(_me.json)};
                        
                        if (!_me.timeLimit._all) {
                            filter.expires = _me.timeLimit.getJson().time.date.format('DD/MM/YYYY');
                        }
                        
                        if (_me.orgReq) {
                            url = nbn.nbnv.api + '/organisation/organisationAccesses/requests/' + _me.requestID;
                        } else {
                            url = nbn.nbnv.api + '/user/userAccesses/requests/' + _me.requestID;
                        }
                        
                        $.ajax({
                            type: "PUT", 
                            url: url, 
                            contentType: 'application/json', 
                            processData: false, 
                            data: JSON.stringify(filter), 
                            success: function(data) {
                                document.location = '/AccessRequest/Admin';
                            },
                            error: function(data) {
                                alert('There was a problem editing or granting the request with id: ' + _me.requestID + '\nIf this error persists, please contact NBN support');
                                document.location.reload(true);
                            }
                        });                        
                    }, Cancel: function() { 
                        $(this).dialog("close"); 
                    }
                }
            });
    };
    
    this.show = function(id, json, dataset, datasetEndpoint, editEndpoint) {        
        $('.ui-dialog-buttonpane button:contains("Grant Request")').button('disable');
        
        this.json = json;
        this.dataset = dataset;
        this.datasetEndpoint = datasetEndpoint;
        this.editEndpoint = editEndpoint;
        
        this.requestID = id;
        this.timeLimit = new nbn.nbnv.ui.timeLimit(json);
        $('#granttimelimit').html('');
        $('#granttimelimit').append(this.timeLimit._renderPanel());
        var filter = {};
        $('#granteffect').html('');
        $('#granteffect').empty().append($('<img>').attr('src', '/img/ajax-loader.gif')).append('  Loading request record coverage');
        $('#grantanonwarn').hide();
        
        if ('organisationID' in json.reason && json.reason.organisationID !== -1) { this.orgReq = true; } else { this.orgReq = false; }
        
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
                $('#granteffect').html('');
                
                if (datasets.length > 0) {
                    $('#granteffect').append('This request covers ' + datasets[0].querySpecificObservationCount + ' of ' + datasets[0].taxonDataset.recordCount + ' records in the dataset, ' + datasets[0].querySpecificSensitiveObservationCount + ' are sensitive records');
                    
                    if (!json.spatial.all && datasets[0].querySpecificObservationCount === datasets[0].querySpecificSensitiveObservationCount) {
                        $('#grantanonwarn').show();
                    } 

                    $('.ui-dialog-buttonpane button:contains("Grant Request")').button('enable');
                } else {
                    $('#granteffect').html('<p style="color:red">No records would be granted by this request, we recommend you close this request</p>');
                }
            },
            error: function(retVal) {
                $('#granteffect').html('');
                $('#granteffect').append('Error while getting request statistics, if this error continues to occur please contact us');
            }
        });

        this.div.dialog("open");
    };
};
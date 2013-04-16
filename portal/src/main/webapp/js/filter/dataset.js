window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.filter = nbn.nbnv.ui.filter || {};

nbn.nbnv.ui.filter.dataset = function(json) {
    if (typeof(json) === 'undefined') { json = { dataset: { all: true } }; }
    
    this._all = true;
    this._datasets = [];
    this._fullCount = 0;
    
    if (!json.dataset.all) {
        this._all = false;
        this._datasets = json.dataset.datasets;
    }
    
    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'dataset')
            .append($('<span>').addClass('filterheader').append('Dataset Filter'))
            .append($('<span>').attr('id', 'datasetResult').addClass('resulttext'));
    };
    
    this._renderPanel = function() {
        var dataDiv = $('<div>');
        
        var datasetTable = $('<table>').attr('id', 'datasetfiltertable');
        
        dataDiv.append(datasetTable);
        
        return dataDiv;
    };

    this.setupTable = function (json) {
        var datasetTable = $('#datasetfiltertable');
        var _me = this;
        
        datasetTable.html('');
        datasetTable.append('Loading');
        
        var filter = {};
        if (!json.taxon.all) { filter.ptvk = json.taxon.tvk; }
        if (!json.year.all) { filter.startYear = json.year.startYear; filter.endYear = json.year.endYear; }
        if (!json.spatial.all) { filter.featureID = json.spatial.feature; filter.spatialRelationship = json.spatial.matchType; }
        if (json.sensitive == 'sans') { filter.sensitive = 'true'; }
        
        $.ajax({
            url: nbn.nbnv.api + '/taxonObservations/datasets/',
            data: filter,
            success: function(datasets) {
                datasetTable.html('');
                _me._fullCount = datasets.length;
                
                $.each(datasets, function(id, td) {
                    var dr = $('<tr>')
                        .append($('<td>')
                            .append($('<input>')
                                .attr('type', 'checkbox')
                                .attr('checked', 'true')
                                .attr('name', td.taxonDataset.key)
                                .change(function() { 
                                    if ($(this).is(':checked'))
                                        _me._addDataset($(this).attr('name'));
                                    else
                                        _me._dropDataset($(this).attr('name'));
                                })
                            )
                        ).append($('<td>')
                            .append($('<span>')
                                .addClass('dataset-label')
                                .attr('title', 'Use constraints - ' + (td.taxonDataset.useConstraints===''?'None':td.taxonDataset.useConstraints))
                                .append(td.taxonDataset.organisationName + ' - ' + td.taxonDataset.title + ' (' + td.querySpecificObservationCount + ' record(s))')
                                .click(function () {
                                    var win = window.open('/Datasets/' + td.taxonDataset.key, '_blank');
                                    win.focus();
                                }).hover(function () {
                                    $(this).addClass('ui-state-highlight');
                                }, function () {
                                    $(this).removeClass('ui-state-highlight');
                                })
                                .qtip({
                                    position: {
                                        my: 'left center',
                                        at: 'right center'
                                    }
                                })
                            )
                        );
                        
                    datasetTable.append(dr);
                    _me._addDataset(td.key);
                });
            }
        });
    };
    
    this._addDataset = function(dataset) {
        this._datasets.push(dataset);
        if (this._datasets.length == this._fullCount) { this._all = true; }
    };
    
    this._dropDataset = function(dataset) {
        this._datasets.splice(this._datasets.indexOf(dataset), 1);
        this._all = false;
    };
    
    this._onEnter = function() {
        $('#datasetResult').text('');
    };
    
    this._onExit = function() {
        var text = '';
        
        if (this._all) {
            text = 'All datasets'
        } else {
            text = 'Filter to ' + this._datasets.length + ' of ' + this._fullCount + ' datasets';
        }
        
        $('#datasetResult').text(text);
    };

    this.getJson = function() {
        if (this._all) {
            return { dataset : { all: true } };
        } else {
            return { dataset : { all: false, datasets : this._datasets } };
        }
    }
};

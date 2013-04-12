window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.filter = nbn.nbnv.ui.filter || {};

nbn.nbnv.ui.filter.dataset = function(json) {
    if (typeof(json) === 'undefined') { json = { dataset: { all: true } }; }
    
    this._all = true;
    this._datasets = [];
    
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
        var _me = this;
        
        var dataDiv = $('<div>');
        
        var datasetTable = $('<table>');
        
        $.ajax({
            url: nbn.nbnv.api + '/taxonDatasets',
            success: function(datasets) {
                $.each(datasets.slice(0, 15), function(id, td) {
                    var dr = $('<tr>')
                        .append($('<td>')
                            .append($('<input>')
                                .attr('type', 'checkbox')
                                .attr('checked', 'true')
                                .attr('name', td.key)
                                .change(function() { 
                                    if ($(this).is(':checked'))
                                        _me._addDataset($(this).attr('name'));
                                    else
                                        _me._dropDataset($(this).attr('name'));
                                })
                            )
                        ).append($('<td>')
                            .addClass('dataset-label')
                            .append(td.organisationName + ' - ' + td.title)
                        );
                        
                    datasetTable.append(dr);
                    _me._addDataset(td.key);
                });
            }
        });

        dataDiv.append(datasetTable);
        
        return dataDiv;
    };
    
    this._addDataset = function(dataset) {
        this._datasets.push(dataset);
    };
    
    this._dropDataset = function(dataset) {
        this._datasets.splice(this._datasets.indexOf(dataset), 1);
    };
    
    this._onEnter = function() {
        $('#datasetResult').text('');
    };
    
    this._onExit = function() {
        var text = '';
        
        if (this._all) {
            text = 'All datasets'
        } else {
            text = 'Filter to ' + this._datasets.length + ' datasets';
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

window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.requestPickDataset = function(json) {
    if (typeof(json.dataset) === 'undefined') { json.dataset = { all: true }; }

    this._dataset = '';
    this._datasetName = '';
    
    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'dataset')
            .append($('<span>').addClass('filterheader').append('Dataset'))
            .append($('<span>').attr('id', 'datasetResult').addClass('resulttext'));
    };
    
    this._renderPanel = function() {
        var _me = this;
        var dataDiv = $('<div>');

        var dataset = $('<select>')
            .addClass('selectMaxWidth')
            .change(function() {
                var value = $(this).find("option:selected").attr('value');
                _me._dataset = value;
                _me._datasetName = $(this).find("option:selected").text();
            });

        
        $.ajax({
            url: nbn.nbnv.api + '/datasets/adminable',
            success: function (data) {
                $.each(data, function (i, d) {
                    dataset.append($('<option>')
                        .text(d.title)
                        .attr('value', d.key)
                    );
                });
                
                dataset.change();
            }
        });
        
        dataDiv.append('Dataset: ').append(dataset);
        return dataDiv;
    };
    
    this._onEnter = function() {      
        $('#datasetResult').text('');
    };
    
    this._onExit = function() {
        $('#datasetResult').text(this._datasetName);
    };
    
    this.getJson = function() {
        return { dataset : { all: false, datasets : [ this._dataset ] } };
    };

    this.getError = function() {
        if (this._dataset === '') { return [ 'You must select a dataset' ]; }
        return [];
    };

};

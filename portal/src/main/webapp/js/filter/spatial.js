window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.filter = nbn.nbnv.ui.filter || {};

nbn.nbnv.ui.filter.spatial = function(json) {
    if (typeof(json)==='undefined') { json = { spatial: { all: true } }; }
    
    this._all = true;
    this._matchType = 'within';
    this._feature = -1;
    this._dataset = '';
    this._featureName = '';
    
    if (!json.spatial.all) {
        this._all = false;
        this._matchType = json.spatial.matchType;
        this._feature = json.spatial.feature;
        this._dataset = json.spatial.dataset;
    }
    
    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'spatial')
            .append($('<span>').addClass('filterheader').append('Spatial Filter'))
            .append($('<span>').attr('id', 'spatialResult').addClass('resulttext'));
    };
    
    this._renderPanel = function() {
        var _me = this;
        
        var dataDiv = $('<div>');
        
        var boundary = $('<select>')
            .addClass("sub-filter-spatial-select")
            .change(function() {
                _me._feature = $(this).find("option:selected").attr('value');
                _me._featureName = $(this).find("option:selected").text();
            });
            
        var boundaryTypes = $('<select>')
            .change(function() {
                var value = $(this).find("option:selected").attr('value');
                _me._dataset = value;
                
                $.ajax({
                    url: nbn.nbnv.api + '/siteBoundaryDatasets/' + value + '/siteBoundaries',
                    success: function(data) {
                        boundary.html('');
                        
                        $.each(data, function(id, sb) {
                            boundary.append($('<option>')
                                .text(sb.name)
                                .attr('value', sb.identifier)
                            );
                        });
                        
                        boundary.change();
                    }
                });
            });
            
        $.ajax({
            url: nbn.nbnv.api + '/siteBoundaryDatasets',
            success: function (datasets) {
                $.each(datasets, function(id, sbd) {
                    boundaryTypes.append($('<option>')
                        .text(sbd.title)
                        .attr('value', sbd.datasetKey)
                    );
                });
                
                boundaryTypes.change();
            }
        });

        var match = $('<select>')
            .append($('<option>').text("within").attr('value', 'within'))
            .append($('<option>').text("overlapping").attr('value', 'overlaps'))
            .change(function() {
                var value = $(this).find("option:selected").attr('value');
                _me._matchType = value;
            });

        var allRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'spatialfilterall')
                .attr('value', 'true')
                .change(function() {
                    if (this.checked) {
                        _me._all = true;
                        match.prop('disabled', true);
                        boundary.prop('disabled', true);
                        boundaryTypes.prop('disabled', true);
                    }
                })
            ).append('All records');

	var filterRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'spatialfilterall')
                .attr('value', 'false')
                .change(function() {
                    if (this.checked) {
                        _me._all = false;
                        match.prop('disabled', false);
                        boundary.prop('disabled', false);
                        boundaryTypes.prop('disabled', false);
                    }
                })
            ).append("Records that are ")
            .append(match)
            .append(' the boundary of ')
            .append($('<div>').append($('<span>').addClass('comboSpan').text('Dataset:')).append(boundaryTypes))
            .append($('<div>').append($('<span>').addClass('comboSpan').text('Site:')).append(boundary));

        if (this._all) {
            allRecords.children('input').attr('checked', 'checked').change();
        } else {
            filterRecords.children('input').attr('checked', 'checked').change();
        }
        
        dataDiv.append(allRecords).append(filterRecords);
        
        return dataDiv;
    };

    this._onEnter = function() {
        $('#spatialResult').text('');
    };
    
    this._onExit = function() {
        var text = '';
        
        if (this._all) {
            text = 'All years'
        } else {
            text = 'Records ' + this._matchType + ' ' + this._featureName;
        }
        
        $('#spatialResult').text(text);
    };
    
    this.getJson = function() {
        if (this._all) {
            return { spatial: { all: true }};
        } else {
            return { spatial: { all: false, matchType: this._matchType, feature: this._feature, dataset: this._dataset }};
        }
    };

};
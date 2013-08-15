window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.filter = nbn.nbnv.ui.filter || {};

nbn.nbnv.ui.filter.spatial = function(json) {
    if (typeof(json.spatial)==='undefined') { json.spatial = { all: true }; }
    
    this._all = true;
    this._matchType = 'within';
    this._feature = -1;
    this._dataset = '';
    this._featureName = '';
    
    if (!json.spatial.all) {
        this._all = false;
        this._matchType = json.spatial.match;
        this._feature = json.spatial.feature;
        this._dataset = json.spatial.dataset;
    }
    
    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'spatial')
            .append($('<span>').addClass('filterheader').append('Geographical Area'))
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
                            var option = $('<option>')
                                .text(sb.name)
                                .attr('value', sb.identifier)
                                
                            if (_me._feature == sb.identifier) {
                                option.attr("selected","selected");
                            }
                            
                            boundary.append(option);
                        });
                        
                        boundary.change();
                    }
                });
            });
            
        $.ajax({
            url: nbn.nbnv.api + '/siteBoundaryDatasets',
            success: function (datasets) {
                $.each(datasets, function(id, sbd) {
                    var option = $('<option>')
                        .text(sbd.title)
                        .attr('value', sbd.datasetKey);
                    
                    if (_me._dataset == sbd.datasetKey) {
                        option.attr("selected", "selected");
                    }
                    
                    boundaryTypes.append(option);
                });
                
                boundaryTypes.change();
            }
        });
        
        var match = $('<select>')
            .append($('<option>').text("within").attr('value', 'within'))
            .append($('<option>').text("overlapping and within").attr('value', 'overlap'))
            .change(function() {
                var value = $(this).find("option:selected").attr('value');
                _me._matchType = value;
            });

        match.val(this._matchType);
        match.change();

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
                        match.val(0);
                        boundaryTypes.val(0);
                        boundaryTypes.change();
                    }
                })
            ).append('All areas');

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
            .append($('<div>').append($('<span>').addClass('comboSpan').text('Site List:')).append(boundaryTypes))
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
        var _me = this;
        
        if (this._all) {
            text = 'All areas'
        } else if (this._featureName == '') {
            $.ajax({
                url: nbn.nbnv.api + '/features/' + this._feature,
                success: function (data) {
                    $('#spatialResult').text('Records ' + _me._matchType + ' ' + data.label);
                }
            });
        } else {
            text = 'Records ' + this._matchType + ' ' + this._featureName;
        }
        
        $('#spatialResult').text(text);
    };
    
    this.getJson = function() {
        if (this._all) {
            return { spatial: { all: true }};
        } else {
            return { spatial: { all: false, match: this._matchType, feature: this._feature, dataset: this._dataset }};
        }
    };
    
    this.getQueryString = function() {
        if (this._all) {
            return '';
        }
        return "spatialRelationship=" + this._matchType + ",siteKey=" + this._feature;
    };

    this.getError = function() {
        return [];
    };
};
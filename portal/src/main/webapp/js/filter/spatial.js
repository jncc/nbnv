window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.filter = nbn.nbnv.ui.filter || {};

nbn.nbnv.ui.filter.spatial = function(json) {
    if (typeof(json.spatial)==='undefined') { json.spatial = { all: true }; }
    
    this._all = true;
    this._siteFilter = true;
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
            .attr('id', 'boundarySelect')
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
        
        var matchGrid = $('<select>')
            .append($('<option>').text("within").attr('value', 'within'))
            .append($('<option>').text("overlapping and within").attr('value', 'overlap'))
            .change(function() {
                var value = $(this).find("option:selected").attr('value');
                _me._matchType = value;
            });
            
        matchGrid.val(this._matchType);
        matchGrid.change();
        
        var gridRefSelector = $('<input>')
                .attr('type', 'text')
                .attr('id', 'gridRefSelector')
            ;
        
        var gridRef = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'spatialfilterall')
                .attr('value', 'true')
                .change(function() {
                    if (this.checked) {
                        _me._all = false;
                        _me._siteFilter = false;
                        disableSiteSelectors(match, boundary, boundaryTypes);
                        matchGrid.prop('disabled', false);
                        gridRefSelector.prop('disabled', false);
                    }
                })
            ).append("Records that are ")
            .append(matchGrid)
            .append(' the Grid Reference ')
            .append($('<div>').append($('<span>').addClass('comboSpan').text('Grid Reference:')).append(gridRefSelector));
    
        var allRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'spatialfilterall')
                .attr('value', 'true')
                .change(function() {
                    if (this.checked) {
                        _me._all = true;
                        disableSiteSelectors(match, boundary, boundaryTypes);
                        disableGridRefSelectors(matchGrid, gridRefSelector);
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
                        _me._siteFilter = true;
                        disableGridRefSelectors(matchGrid, gridRefSelector);
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
            if (this._dataset == undefined || 
                    this._dataset == '' || 
                    (this._feature != '' && this._dataset == this._feature)) {
                gridRef.children('input').attr('checked', 'checked').change();
                gridRefSelector.val(this._feature);
            } else {
                filterRecords.children('input').attr('checked', 'checked').change();
            }
        }
        
        dataDiv.append(allRecords).append(filterRecords).append(gridRef);
        
        return dataDiv;
    };

    this._onEnter = function() {
        $('#spatialResult').text('');
    };
    
    this._onExit = function() {
        var text = '';
        var _me = this;
        
        if (!this._all && !this._siteFilter) {
            this._feature = $('#gridRefSelector').val();
            this._featureName = $('#gridRefSelector').val();
        }
        
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
            if (_me._siteFilter) {
                _me._feature = $('#boundarySelect').find("option:selected").attr('value');;
            } else {
                _me.feature = $('#gridRefSelector').val();
            }
            text = 'Records ' + this._matchType + ' ' + this._featureName;
        }
        
        $('#spatialResult').text(text);
    };
    
    this._postRender = function() {
        $('#gridRefSelector').autocomplete({
                source: nbn.nbnv.api + "/gridMapSquares/search?resolution=10km",
                minLength: 3,
                select: function(event, ui) {
                    event.preventDefault();
                    this._all = false;
                    this._feature = ui.item.gridRef;
                    this._featureName = ui.item.gridRef;

                    $('#gridSquareSelector').val(ui.item.gridRef);
                    $('#gridSquareSelector').text(ui.item.gridRef);
                }
            })
            .data('autocomplete')._renderItem = function(ul, item) {
                return $('<li></li>')
                        .data('item.autocomplete', item)
                        .append('<a><strong style="font-size: small;">' + item.gridRef + '</strong></a>')
                        .appendTo(ul);
        };
    };
    
    this.getJson = function() {
        if (this._all) {
            return { spatial: { all: true }};
        } else if (this._siteFilter) {
            return { spatial: { all: false, match: this._matchType, feature: this._feature, dataset: this._dataset }};
        }
        return { spatial: { all: false, match: this._matchType, gridRef: $('#gridRefSelector').val() }};
    };

    this.getError = function() {
        if (!this._siteFilter && !this._all) {
            this._feature = $('#gridRefSelector').val();
            if (!(new RegExp('^[HJNOST][A-Z](\\d\\d)$','i').test(this._feature) || 
                    new RegExp('^[A-HJ-Z](\\d\\d)$','i').test(this._feature) || 
                    new RegExp('^W[AV](\\d\\d)$','i').test(this._feature))) {
                return ["Grid reference is not a valid 10km UKGB, OSNI or Channel Islands grid square"];
            }
        }
        
        return [];
    };
    
    function disableSiteSelectors(match, boundary, boundaryTypes) {
        match.prop('disabled', true);
        boundary.prop('disabled', true);
        boundaryTypes.prop('disabled', true);
        match.val(0);
        boundaryTypes.val(0);
        boundaryTypes.change();
    }
    
    function disableGridRefSelectors(matchGrid, gridSelector) {
        matchGrid.prop('disabled', true);
        matchGrid.val(0);
        gridSelector.prop('disabled', true);
    } 
};
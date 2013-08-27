window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.filter = nbn.nbnv.ui.filter || {};

nbn.nbnv.ui.filter.dataset = function(json) {
    if (typeof(json.dataset) === 'undefined') { json.dataset = { all: true }; }
    
    this._all = true;
    this._datasets = [];
    this._fullCount = -1;
    this._mode = 'all';
    this._secret = true;
    this._useSecret = false;
    
    if (!json.dataset.all) {
        this._all = false;
        this._datasets = json.dataset.datasets;
        this._secret = json.dataset.secret;
        
        if (this._datasets.length == 1) { this._mode = 'single'; } else { this._mode = 'filter'; }
    }
    
    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'dataset')
            .append($('<span>').addClass('filterheader').append('Datasets'))
            .append($('<span>').attr('id', 'datasetResult').addClass('resulttext'));
    };
    
    this._renderPanel = function() {
        var _me = this;
        var dataDiv = $('<div>');
        
        var datasetTable = $('<table>').attr('id', 'datasetfiltertable').addClass('results');
        $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';

        var sensitiveInfo = $('<div>')
            .append($('<p>')
                .append('Datasets relevant to your search area may not appear in the list of datasets you can request enhanced access to. This is to protect the location of any sensitive records.')
            ).append($('<p>')
                .append('You may request access to these additional sensitive datasets by ticking this box. A request for access will then be sent to the relevant dataset administrators. Please note there may not be any additional sensitive datasets and you may not receive a reply regarding this additional request.')
            ).dialog({ 
                modal: true, 
                autoOpen: false,
                buttons: {
                    "OK" : function() { sensitiveInfo.dialog("close"); }
                }
            });
            
        var datasetAutoComplete = $('<input>').addClass('selectMaxWidth')
            .autocomplete({
                source: function(request, response) {
                    $.getJSON(nbn.nbnv.api + '/search/taxonDatasets?q=' + request.term, function(data) {
                        response($.map(data.results, function(item) { item.value = item.title; return item; }))
                    });
                },
                select: function(event, ui) {
                    _me._datasets = [ui.item.key];
                    datasetAutoComplete.val(ui.item.title);
                }
            });
        
        datasetAutoComplete.data( "autocomplete" )._renderItem = function(ul, item) {
            return $( "<li></li>" )
                .data( "item.autocomplete", item )
                .append( "<a><i>" + item.searchMatchTitle + "</i><br>" + item.organisationName + "</a>" )
                .appendTo(ul);
            };

        if (this._mode == 'single') {
            $.ajax({
                url: nbn.nbnv.api + '/datasets/' + _me._datasets[0],
                success: function(data) {
                    datasetAutoComplete.val(data.title);
                }
            });
        }

        var secret = $('<div>')
            .addClass('queryBlock')
            .attr('id', 'datasetfiltersecretblock')
            .append($('<input>')
                .attr('type', 'checkbox')
                .attr('name', 'datasetfiltersecret')
                .attr('value', 'secret')
                .change(function() {
                    _me._secret = this.checked;
                })
            ).append('Include additional sensitive datasets. ')
            .append($('<a>')
                .text('Why are they excluded from the above list of datasets?')
                .attr('href', '#')
                .click(function() { sensitiveInfo.dialog("open"); })
            );
                
        var allRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'datasetfilterall')
                .attr('value', 'all')
                .change(function() {
                    if (this.checked) {
                        var dTable = $('#datasetfiltertable').dataTable();
                        _me._all = true;
                        _me._mode = 'all';
                        datasetAutoComplete.prop('disabled', true);
                        datasetAutoComplete.val('');

                        if (dTable.data() != null) {
                            $('#dsfilter-sa').prop('disabled', true);
                            $('#dsfilter-da').prop('disabled', true);
                            dTable.$('tr').find(":checkbox").prop('disabled', false);
                            dTable.$('tr').find(":checkbox").prop('checked', true);                        
                        }
                    }
                })
            ).append('All datasets');


	var singleRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'datasetfilterall')
                .attr('value', 'single')
                .change(function() {
                    if (this.checked) {
                        var dTable = $('#datasetfiltertable').dataTable();
                        _me._all = false;
                        _me._mode = 'single';
                        datasetAutoComplete.prop('disabled', false);
                        
                        if (dTable.data() != null) {
                            $('#dsfilter-sa').prop('disabled', true);
                            $('#dsfilter-da').prop('disabled', true);
                            dTable.$('tr').find(":checkbox").prop('disabled', true);
                            dTable.$('tr').find(":checkbox").prop('checked', true);                        
                        }
                    }
                })
            ).append('Single Dataset ').append(datasetAutoComplete);
                
	var filterRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'datasetfilterall')
                .attr('value', 'filter')
                .change(function() {
                    if (this.checked) {
                        var dTable = $('#datasetfiltertable').dataTable();
                        _me._all = false;
                        _me._mode = 'filter';
                        datasetAutoComplete.prop('disabled', true);
                        datasetAutoComplete.val('');
                        
                        if (dTable.data() != null) {
                            $('#dsfilter-sa').prop('disabled', false);
                            $('#dsfilter-da').prop('disabled', false);
                            dTable.$('tr').find(":checkbox").prop('disabled', false);
                            dTable.$('tr').find(":checkbox").prop('checked', true);                        
                        }
                    }
                })
            ).append("Dataset List ")
            .append(datasetTable);
        
        if (this._mode == 'all') {
            allRecords.children('input').attr('checked', 'checked').change();
        } else if (this._mode == 'filter') {
            filterRecords.children('input').attr('checked', 'checked').change();
        } else if (this._mode == 'single') {
            singleRecords.children('input').attr('checked', 'checked').change();
        }
        
        if (this._secret) {
            secret.find('input').attr('checked', 'checked').change();
        }

        dataDiv.append(allRecords).append(singleRecords).append(filterRecords).append(secret);
        
        return dataDiv;
    };

    this.setupTable = function (json, endpoint) {
        var datasetTable = $('#datasetfiltertable');

        var _me = this;
        var dataf = this._datasets;
        this._datasets = [];
        
        datasetTable.html('');
        datasetTable.append('Loading');
        
        var filter = {};
        
        if (json.spatial.all) {
            $('#datasetfiltersecretblock').hide();
            this._useSecret = false;
        } else {
            $('#datasetfiltersecretblock').show();
            this._useSecret = true;
        }
        
        if (json.taxon.all && json.spatial.all) {
            datasetTable.html('');
            datasetTable
                .append($('<tr>')
                    .append($('<td>')
                        .append($('<span>')
                            .addClass('comboSpan')
                            .append('&nbsp;')
                        )
                    ).append($('<td>')
                        .append($('<span>')
                            .addClass('ui-state-highlight')
                            .append('Please apply a taxon or spatial filter to choose datasets.')
                        )
                    )
                );
            $("input:radio[name='datasetfilterall'][value='filter']").prop('disabled', true);
            return;
        }
        $("input:radio[name='datasetfilterall'][value='filter']").prop('disabled', false);
                
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

        $.ajax({
            url: nbn.nbnv.api + endpoint,
            data: filter,
            success: function(datasets) {
                datasetTable.html('');
                datasetTable.append($('<thead>')
                    .append($('<tr>')
                        .append($('<th>').append('Use'))
                        .append($('<th>').append('Dataset'))
                        .append($('<th>').append('Records'))
                    )
                );
                    
                var tbody = $('<tbody>');
                    
                    
                _me._fullCount = datasets.length;
                
                datasets.sort(function (a, b) {
                    if (a.taxonDataset.organisationName < b.taxonDataset.organisationName)
                        return -1;
                    if (a.taxonDataset.organisationName > b.taxonDataset.organisationName)
                        return 1;
                    if (a.taxonDataset.title < b.taxonDataset.title)
                        return -1;
                    if (a.taxonDataset.title > b.taxonDataset.title)
                        return 1;

                    return 0;
                })
                
                $.each(datasets, function(id, td) {
                    var cb = $('<input>')
                                .attr('type', 'checkbox')
                                .attr('name', td.taxonDataset.key)
                                .change(function() { 
                                    if ($(this).is(':checked'))
                                        _me._addDataset($(this).attr('name'));
                                    else
                                        _me._dropDataset($(this).attr('name'));
                                });
                    
                    if (_me._all || $.inArray(td.taxonDataset.key, dataf) > -1) {
                        cb.attr("checked", "true");
                    }
                    
                    if (_me._mode != filter) {
                        cb.prop('disabled', true);
                    }
                    
                    var dr = $('<tr>')
                        .append($('<td>')
                            .append(cb)
                        ).append($('<td>')
                            .append($('<span>')
                                .addClass('dataset-label')
                                .attr('title', 'Use constraints - ' + (td.taxonDataset.useConstraints===''?'None':td.taxonDataset.useConstraints))
                                .append(td.taxonDataset.organisationName + ' - ' + td.taxonDataset.title)
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
                        ).append($('<td>')
                            .append(td.querySpecificObservationCount + (td.querySpecificSensitiveObservationCount > 0?' (' + td.querySpecificSensitiveObservationCount + ' sensitive)':''))
                        );
                        
                    tbody.append(dr);
                    
                    if (_me._all || $.inArray(td.taxonDataset.key, dataf) > -1) {
                        _me._addDataset(td.taxonDataset.key);
                    }
                });
                
                datasetTable.append(tbody);
                datasetTable.dataTable({
                    "aaSorting": [[1, "asc"]],
                    "bAutoWidth": true,
                    "bFilter": false,
                    "bDestroy": true,
                    "bJQueryUI": true,
                    "iDisplayLength": 25,
                    "bSortClasses": false,
                    "sDom": '<"dsfilter-toolbar">frtip',
                    "sPaginationType": "full_numbers",
                    "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]]
                });
                
                $("div.dsfilter-toolbar").append($('<button>')
                        .text('Select All')
                        .attr('id', 'dsfilter-sa')
                        .prop('disabled', true)
                        .click(function() {
                            var dTable = $('#datasetfiltertable').dataTable();
                            dTable.$('tr').find(":checkbox").prop('checked', true);                        
                        })
                    ).append($('<button>')
                        .text('Deselect All')
                        .attr('id', 'dsfilter-da')
                        .prop('disabled', true)
                        .click(function() {
                            var dTable = $('#datasetfiltertable').dataTable();
                            dTable.$('tr').find(":checkbox").prop('checked', false);                        
                        })
                    );

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
        } else if (this._fullCount == -1) {
            text = 'Filter to ' + this._datasets.length + ' datasets';
        } else {
            text = 'Filter to ' + this._datasets.length + ' of ' + this._fullCount + ' datasets';
        }
        
        $('#datasetResult').text(text);
    };

    this.getJson = function() {
        if (this._all) {
            if (this._useSecret) {
                return { dataset : { all: true, secret: this._secret } };
            }
            return { dataset : { all: true } };
        } else {
            if (this._useSecret) {
                return { dataset : { all: false, datasets : this._datasets, secret: this._secret } };
            }
            return { dataset : { all: false, datasets : this._datasets } };
        }
    }

    this.getError = function() {
        if (!this._all && this._datasets.length < 1) { return [ 'You must select at least one dataset' ]; }
        return [];
    };
};

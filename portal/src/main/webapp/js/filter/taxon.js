window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.filter = nbn.nbnv.ui.filter || {};

nbn.nbnv.ui.filter.taxon = function(json) {
    if (typeof(json.taxon)==='undefined') { json.taxon = { all: true }; }
    
    this._all = true;
    this._tvk = '';
    this._desigCode = '';
    this._outputGroupKey = '';
    this._taxonName = '';
    this._designation = '';
    this._outputGroup = '';
    this._orgSuppliedList = -1;
    this._orgSuppliedListText = '';
    
    var mode = 'all';
    
    if (!json.taxon.all) {
        this._all = false;

        if (json.taxon.tvk) {
            mode = 'taxon';
            this._tvk = json.taxon.tvk; 
        } else if (json.taxon.designation) {
            mode = 'desig';
            this._desigCode = json.taxon.designation;
        } else if (json.taxon.output) {
            mode = 'output';
            this._outputGroupKey = json.taxon.output;
        } else if (json.taxon.orgSuppliedList) {
            mode = 'org';
            this._orgSuppliedList = json.taxon.orgSuppliedList;
        }
    }
    
    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'taxon')
            .append($('<span>').addClass('filterheader').append('Species'))
            .append($('<span>').attr('id', 'taxonResult').addClass('resulttext'));
    };
    
    this._renderPanel = function() {
        var _me = this;
        
        var dataDiv = $('<div>');
        
        var speciesAutoComplete = $('<input>')
            .autocomplete({
                source: function(request, response) {
                    $.getJSON(nbn.nbnv.api + '/taxa?q=' + request.term, function(data) {
                        response($.map(data.results, function(item) { item.value = item.name; return item; }))
                    });
                },
                select: function(event, ui) {
                    _me._taxonName = ui.item.name;
                    _me._tvk = ui.item.ptaxonVersionKey;
                    speciesAutoComplete.val(ui.item.name);
                }
            });
        
        speciesAutoComplete.data( "autocomplete" )._renderItem = function(ul, item) {
            return $( "<li></li>" )
                .data( "item.autocomplete", item )
                .append( '<a><span class="taxonTerm">' + item.searchMatchTitle + "</span><br>" + item.descript + "</a>" )
                .appendTo(ul);
            };

        var desig = $('<select>')
            .addClass('selectMaxWidth')
            .change(function() {
                var value = $(this).find("option:selected").attr('value');
                _me._desigCode = value;
                _me._designation = $(this).find("option:selected").text();
            });

        
        $.ajax({
            url: nbn.nbnv.api + '/designations',
            success: function (data) {
                $.each(data, function (i, d) {
                    var opt = $('<option>')
                        .text(d.name)
                        .attr('value', d.code)
                    
                    if (_me._desigCode == d.code) { opt.attr('selected', 'selected'); }
                    
                    desig.append(opt);
                });
                
                desig.change();
            }
        });
        
        var org = $('<select>')
            .addClass('selectMaxWidth')
            .change(function() {
                var selected = $(this).find("option:selected");
                _me._orgSuppliedList = selected.attr('value');
                _me._orgSuppliedListText = selected.text();
            });

        
        $.ajax({
            url: nbn.nbnv.api + '/organisationList',
            success: function (data) {
                $.each(data, function (i, d) {
                    var opt = $('<option>')
                        .text(d.name)
                        .attr('value', d.id)

                    if (_me._orgSuppliedList == d.id) { opt.attr('selected', 'selected'); }
                    
                    org.append(opt);
                });
                
                org.change();
            }
        });
        
        var output = $('<select>')
            .addClass('selectMaxWidth')
            .change(function() {
                var value = $(this).find("option:selected").attr('value');
                _me._outputGroupKey = value;
                _me._outputGroup = $(this).find("option:selected").text();
            });
        
        $.ajax({
            url: nbn.nbnv.api + '/taxonOutputGroups',
            success: function (data) {
                $.each(data, function (i, grp) {
                    var opt = $('<option>')
                        .text(grp.name)
                        .attr('value', grp.key)
                        
                    if (_me._outputGroupKey == grp.key) { opt.attr('selected', 'selected'); }
                    
                    output.append(opt);
                });
                
                output.change();
            }
        });
        
        if (this._tvk != '') {
            $.ajax({
                url: nbn.nbnv.api + '/taxa/' + _me._tvk,
                success: function(data) {
                    speciesAutoComplete.val(data.name);
                    _me._taxonName = data.name;
                }
            });
        }
        
        var allRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'taxonfilterall')
                .attr('value', 'all')
                .change(function() {
                    if (this.checked) {
                        mode = 'all';
                        _me._all = true;
                        output.prop('disabled', true);
                        desig.prop('disabled', true);
                        org.prop('disabled', true);
                        speciesAutoComplete.prop('disabled', true);
                        speciesAutoComplete.val('');
                        output.val(0);
                        desig.val(0);
                    }
                })
            ).append('All species');

	var taxonFilterRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'taxonfilterall')
                .attr('value', 'taxon')
                .change(function() {
                    if (this.checked) {
                        mode = 'taxon';
                        _me._all = false;
                        output.prop('disabled', true);
                        desig.prop('disabled', true);
                        org.prop('disabled', true);
                        speciesAutoComplete.prop('disabled', false);
                        output.val(0);
                        desig.val(0);
                    }
                })
            ).append("Single species or taxon ")
            .append(speciesAutoComplete);

        var desigFilterRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'taxonfilterall')
                .attr('value', 'designation')
                .change(function() {
                    if (this.checked) {
                        mode = 'desig';
                        _me._all = false;
                        output.prop('disabled', true);
                        desig.prop('disabled', false);
                        org.prop('disabled', true);
                        speciesAutoComplete.prop('disabled', true);
                        speciesAutoComplete.val('');
                        output.val(0);
                    }
                })
            ).append("Designated List ")
            .append(desig);
    
        var orgFilterRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'taxonfilterall')
                .attr('value', 'orgList')
                .change(function() {
                    if (this.checked) {
                        mode = 'org';
                        _me._all = false;
                        output.prop('disabled', true);
                        desig.prop('disabled', true)
                        org.prop('disabled', false);
                        speciesAutoComplete.prop('disabled', true);
                        speciesAutoComplete.val('');
                        output.val(0);
                    }
                })
            ).append("Organisation List ")
            .append(org);
        
        var outputFilterRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'taxonfilterall')
                .attr('value', 'output')
                .change(function() {
                    if (this.checked) {
                        mode = 'output';
                        _me._all = false;
                        output.prop('disabled', false);
                        desig.prop('disabled', true);
                        org.prop('disabled', true);
                        speciesAutoComplete.prop('disabled', true);
                        speciesAutoComplete.val('');
                        desig.val(0);
                    }
                })
            ).append("Species Group ")
            .append(output);
        
        if (mode == 'all') {
            allRecords.children('input').attr('checked', 'checked').change();
        } else if (mode == 'taxon') {
            taxonFilterRecords.children('input').attr('checked', 'checked').change();
        } else if (mode == 'desig') {
            desigFilterRecords.children('input').attr('checked', 'checked').change();
        } else if (mode === 'org') {
            orgFilterRecords.children('input').attr('checked', 'checked').change();
        } else if (mode == 'output') {
            outputFilterRecords.children('input').attr('checked', 'checked').change();
        }
        
        dataDiv.append(allRecords).append(taxonFilterRecords).append(desigFilterRecords).append(orgFilterRecords).append(outputFilterRecords);

        return dataDiv;
    };
    
    this._onEnter = function() {
        $('#taxonResult').text('');
    };
    
    this._onExit = function() {
        var text = '';
        var _me = this;
        
        if (mode == 'all') {
            text = 'All species'
        } else if (mode == 'taxon') {
            if (this._taxonName == '') {
                $.ajax({
                    url: nbn.nbnv.api + '/taxa/' + _me._tvk,
                    success: function(data) {
                        $('#taxonResult').text(data.name + ' records');  
                    }
                });
            } else {
                text = this._taxonName + ' records';
            }
        } else if (mode == 'desig') {
            if (this._designation == '') {
                $.ajax({
                    url: nbn.nbnv.api + '/designations/' + _me._desigCode,
                    success: function(data) {
                        $('#taxonResult').text(data.name + ' records');  
                    }
                });
            } else {
                text = this._designation + ' records';
            }  
        } else if (mode === 'org') {
            if (this._orgSuppliedListText == '') {
                $.ajax({
                    url: nbn.nbnv.api + '/organisationList/' + _me._orgSuppliedList,
                    success: function(data) {
                        $('#taxonResult').text(data.name + ' records');  
                    }
                });
            } else {
                text = this._orgSuppliedListText + ' records';
            }  
        } else if (mode == 'output') {
            if (this._outputGroup == '') {
                $.ajax({
                    url: nbn.nbnv.api + '/taxonOutputGroups/' + _me._outputGroupKey,
                    success: function(data) {
                        $('#taxonResult').text(data.name + ' records');  
                    }
                });
            } else {
                text = this._outputGroup + ' records';
            }
        }
        
        $('#taxonResult').text(text);
    };
    
    this.getJson = function() {
        if (mode == 'all') {
            return { taxon: { all: true }};
        } else if (mode == 'taxon') {
            return { taxon: { all: false, tvk: this._tvk }};
        } else if (mode == 'desig') {
            return { taxon: { all: false, designation: this._desigCode }};
        } else if (mode == 'org') {
            return { taxon: { all : false, orgSuppliedList: this._orgSuppliedList }};
        } else if (mode == 'output') {
            return { taxon: { all: false, output: this._outputGroupKey }};
        } else {
            return { taxon: { all: false }};
        }
    };
    
    this.getError = function() {
        var e = [];
        
        if (mode == 'taxon' && this._tvk == '') { e.push('You must specify a taxon in the taxon filter'); }
        
        return e;
    };
};
window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.filter = nbn.nbnv.ui.filter || {};

nbn.nbnv.ui.filter.taxon = function(json) {
    if (typeof(json.taxon)==='undefined') { json.taxon = { all: true }; }
    
    this._all = true;
    this._tvk = '';
    this._taxonName = '';
    
    if (!json.taxon.all) {
        this._all = false;
        this._tvk = json.taxon.tvk;
    }
    
    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'taxon')
            .append($('<span>').addClass('filterheader').append('Taxon Filter'))
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
            var authority = item.authority ? item.authority : '';
            return $( "<li></li>" )
                .data( "item.autocomplete", item )
                .append( "<a><strong>" + item.searchMatchTitle + "</strong> " + authority + "<br>" + item.descript + "</a>" )
                .appendTo(ul);
            };

        var allRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'taxonfilterall')
                .attr('value', 'true')
                .change(function() {
                    if (this.checked) {
                        _me._all = true;
                        speciesAutoComplete.prop('disabled', true);
                    }
                })
            ).append('All records');

	var filterRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'taxonfilterall')
                .attr('value', 'false')
                .change(function() {
                    if (this.checked) {
                        _me._all = false;
                        speciesAutoComplete.prop('disabled', false);
                    }
                })
            ).append("Records that are ")
            .append(speciesAutoComplete);

        if (this._all) {
            allRecords.children('input').attr('checked', 'checked').change();
        } else {
            filterRecords.children('input').attr('checked', 'checked').change();
        }
        
        dataDiv.append(allRecords).append(filterRecords);

        return dataDiv;
    };
    
    this._onEnter = function() {
        $('#taxonResult').text('');
    };
    
    this._onExit = function() {
        var text = '';
        
        if (this._all) {
            text = 'All species'
        } else {
            text = this._taxonName + ' records';
        }
        
        $('#taxonResult').text(text);
    };
    
    this.getJson = function() {
        if (this._all) {
            return { taxon: { all: true }};
        } else {
            return { taxon: { all: false, tvk: this._tvk }};
        }
    };
};
window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.filter = nbn.nbnv.ui.filter || {};

nbn.nbnv.ui.filter.year = function(json) {
    if (typeof(json.year)==='undefined') { json.year = { all: true }; }
    
    this._all = true;
    this._startYear = 1600;
    this._endYear = new Date().getFullYear();
    
    if (!json.year.all) {
        this._all = false;
        this._startYear = json.year.startYear;
        this._endYear = json.year.endYear;
    }

    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'year')
            .append($('<span>').addClass('filterheader').append('Year Filter'))
            .append($('<span>').attr('id', 'yearResult').addClass('resulttext'));
    };
    
    this._renderPanel = function() {
        var _me = this;
        
        var data = $('<div>');
        
        var startInput = $('<input>')
            .attr('type', "text").attr('length', "8").attr('name', "yearstart").val(this._startYear)
            .change(function() {
                _me._startYear = $(this).val();
            });
        
        var endInput = $('<input>')
            .attr('type', "text").attr('length', "8").attr('name', "yearend").val(this._endYear)
            .change(function() {
                _me._endYear = $(this).val();
            });
                                
        var allRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'yearfilterall')
                .attr('value', 'true')
                .change(function() {
                    if (this.checked) {
                        _me._all = true;
                        startInput.prop('disabled', true);
                        endInput.prop('disabled', true);
                    }
                })
            ).append('All records');

	var filterRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'yearfilterall')
                .attr('value', 'false')
                .change(function() {
                    if (this.checked) {
                        _me._all = false;
                        startInput.prop('disabled', false);
                        endInput.prop('disabled', false);
                    }
                })
            ).append("Records between the years ")
            .append(startInput)
            .append(" and ")
            .append(endInput);
        
        if (this._all) {
            allRecords.children('input').attr('checked', 'checked').change();
        } else {
            filterRecords.children('input').attr('checked', 'checked').change();
        }
        
        data.append(allRecords).append(filterRecords);
        
        return data;
    };
    
    this._onEnter = function() {
        $('#yearResult').text('');
    };
    
    this._onExit = function() {
        var text = '';
        
        if (this._all) {
            text = 'All years'
        } else {
            text = 'Records between ' + this._startYear + ' and ' + this._endYear;
        }
        
        $('#yearResult').text(text);
    };
    
    this.getJson = function() {
        if (this._all) {
            return { year: { all: true } };
        } else {
            return { year: { all: false, startYear: this._startYear, endYear: this._endYear }}
        }
    };
};
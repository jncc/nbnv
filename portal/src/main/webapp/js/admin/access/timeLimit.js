window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.timeLimit = function(json) {
    if (typeof(json.time) === 'undefined') { json.time = { all: true }; }
    
    this._all = true;
    this._date = moment().add('year', 1);
    
    if (!json.time.all) {
        this._all = false;
        this._date = moment(json.time.date);
    }
    
    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'timeLimit')
            .append($('<span>').addClass('filterheader').append('Request Access Until'))
            .append($('<span>').attr('id', 'timeLimitResult').addClass('resulttext'));
    };
    
    this._renderPanel = function() {
        var _me = this;
    
        var dataDiv = $('<div>');
        
        var datePicker = $('<input>')
                .attr('type', 'text')
                .attr('id', 'datepicker')
                .datepicker({ 
                    dateFormat: 'dd/mm/yy',
                    changeMonth: true,
                    changeYear: true
                })
                .change(function () {
                    _me._date = moment(this.value, 'DD/MM/YYYY');
                });
        
        datePicker.val(this._date.format('DD/MM/YYYY'));        
        
        var allRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'datefilterall')
                .attr('value', 'true')
                .change(function() {
                    if (this.checked) {
                        _me._all = true;
                        datePicker.prop('disabled', true);
                    }
                })
            ).append('Indefinitely');

	var filterRecords = $('<div>')
            .append($('<input>')
                .attr('type', 'radio')
                .attr('name', 'datefilterall')
                .attr('value', 'false')
                .change(function() {
                    if (this.checked) {
                        _me._all = false;
                        datePicker.prop('disabled', false);
                    }
                })
            ).append('Until: ')
            .append(datePicker);
                
        if (this._all) {
            allRecords.children('input').attr('checked', 'checked').change();
        } else {
            filterRecords.children('input').attr('checked', 'checked').change();
        }
        
        dataDiv.append(allRecords).append(filterRecords);

        return dataDiv;

    };

    this._onEnter = function() {
        $('#timeLimitResult').text('');
    };
    
    this._onExit = function() {
        var text = '';
        
        if (this._all) {
            text = 'Indefinitely'
        } else {
            text = this._date.format('DD/MM/YYYY');
        }
        
        $('#timeLimitResult').text(text);
    };
    
    this.getJson = function() {
        if (this._all) {
            return { time: { all: true }};
        } else {
            return { time: { all: false, date: this._date }};
        }
    };
    
    this.getError = function() {
        if (!this._all && !moment(this._date).isAfter(moment())) { return ['Time limit must be in the future']; }
        return [];
    };
};
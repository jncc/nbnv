window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.filter = nbn.nbnv.ui.filter || {};

nbn.nbnv.ui.filter.sensitive = function(json) {
    if (typeof(json)==='undefined') { json = { sensitive: 'ns' }; }
    
    this._value = json.sensitive;
    
    this._renderHeader = function() {
        return $('<h3>')
            .append($('<span>').addClass('filterheader').append('Sensitive Filter'))
            .append($('<span>').attr('id', 'sensitiveResult').addClass('resulttext'));
    };
    
    this._renderPanel = function() {
        var _me = this;
        
        var select = $('<select>')
            .append($('<option>').text("non-sensitive").attr('value', 'ns'))
            .append($('<option>').text("sensitive and non-sensitive").attr('value', 'sans'))
            .change(function() {
                _me._value = $(this).find('option:selected').attr('value');
            });
        
        select.val(this._value);
        select.change();
        
        var data = $('<div>')
            .append("All ")
            .append(select).append(" records");
        
        return data;
    };
    
    this._onEnter = function() {
        $('#sensitiveResult').text('');
    };
    
    this._onExit = function() {
        var text = '';
        
        if (this._value == 'ns') {
            text = 'Non-sensitive records';
        } else if (this._value == 'sans') {
            text = 'Sensitive and non-sensitive records';
        }
        
        $('#sensitiveResult').text(text);
    };
    
    this.getJson = function() {
        return { sensitive: this._value };
    };
};

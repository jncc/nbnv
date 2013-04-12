window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.requestResult = function() {
    this._renderHeader = function() {
        return $('<h3>')
            .append($('<span>').addClass('filterheader').append('Confirm Access Request'));
    };
    
    this._renderPanel = function() {
        var data = $('<div>');
        
        data.append($('<div>').append('TODO'))
            .append($('<button>').text('Request'));
            
        return data;
    };
};
window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.timeLimit = function() {
    this._renderHeader = function() {
        return $('<h3>')
            .append($('<span>').addClass('filterheader').append('Request Access Until'));
    };
    
    this._renderPanel = function() {
        return $('<div>').append('TODO');
    };
};
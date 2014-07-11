window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.requestDetails = function(json, username) {
    if (typeof(json.reason) === 'undefined') { json.reason = { purpose: 1, details: '', organisationID: -1 }; }

    var purposes = { 1: 'Personal interest'
                    , 2: 'Educational purposes'
                    , 3: 'Research and scientific analysis'
                    , 4: 'Media publication'
                    , 5: 'Conservation NGO work'
                    , 6: 'Professional land management'
                    , 7: 'Data provision and interpretation services (commercial)'
                    , 8: 'Data provision and interpretation services (non-profit)'
                    , 9: 'Statutory work' };

    this._asID = json.reason.organisationID;
    this._purpose = json.reason.purpose;
    this._details = json.reason.details;
    this._username = username;

    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'reason')
            .append($('<span>').addClass('filterheader').append('Access Request Details'));
    };
    
    this._renderPanel = function() {
        var _me = this;
        
        return $('<div>').append($('<table>')
            .append($('<tr>')
                .append($('<th>')
                    .append('Request From:')
                ).append($('<td>')
                    .addClass('selectMaxWidth')
                    .append(this._username)
                )
            ).append($('<tr>')
                .append($('<th>')
                    .append('Purpose:')
                ).append($('<td>')
                    .addClass('selectMaxWidth')                    
                    .append(purposes[this._purpose])
                )
            ).append($('<tr>')
                .append($('<th>')
                    .append('Details:')
                ).append($('<td>')
                    .addClass('selectMaxWidth')
                    .append(this._details)
                )
            ));
    };

    this._onEnter = function() {
    };
    
    this._onExit = function() {
    };
    
    this.getJson = function() {
        if (this._asID > -1)
            return { reason: { purpose: this._purpose, details: (this._details), organisationID: this._asID }};
        
        return { reason: { purpose: this._purpose, details: (this._details) }};
    };

    this.getError = function() {
        return [];
    };

};
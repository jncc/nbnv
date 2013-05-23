window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.requestResult = function() {
    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'result')
            .append($('<span>').addClass('filterheader').append('Confirm Access Request'));
    };
    
    this._renderPanel = function(clickfn) {
        return $('<div>')
            .append($('<div>')
                .attr('id', 'resulterrortext')
                .addClass('ui-state-error')
            ).append($('<p>')
                .attr('id', 'resultpermtext')
            ).append($('<button>')
                .text('Submit')
                .attr('id', 'resultsubmitbtn')
                .click(clickfn)
            );
    };
    
    this._onEnter = function(permissionNeeded, error) {
        var data = $('#resultpermtext');
        data.html('');
        
        if (permissionNeeded) {
            data.append("You have requested full access to the records detailed above and permission to use these records for commercial or research purposes.  If your request is granted, this constitutes written permission to use the data as described.  Please ensure your use of the data complies with the ")
                .append($('<a>').attr('href', '/Terms').text('NBN Gateway Terms and Conditions.'))
                .append(", for example by crediting data providers in any report or publication.  Click 'Submit' to send this request to all dataset administrators.  You will be notified by e-mail when your request has been dealt with.");
        } else {
            data.append("You have requested full access to the records detailed above.  Click 'Submit' to send this request to all dataset administrators.  You will be notified by e-mail when your request has been dealt with.  Please ensure your use of the data complies with the ")
                .append($('<a>').attr('href', '/Terms').text('NBN Gateway Terms and Conditions.'));
        }
        
        var errorDiv = $('#resulterrortext');
        errorDiv.html('');
        errorDiv.append($('<li>').text('Errors:'));
        
        if (error.length > 0) {
            data.hide();
            $.each(error, function (i, e) {
                $('#resulterrortext').append($('<li>').append(e));
            });
            errorDiv.show();
            $('#resultsubmitbtn').prop('disabled', true);
        } else {
            errorDiv.hide();
            data.show();
            $('#resultsubmitbtn').prop('disabled', false);
        }

    }
};
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
            data.append('<p>You are requesting full access to the records as detailed above and permission to use these records for commercial or research purposes. If your request is granted, this constitutes written permission to use the data as described.</p>')
                .append('<p><span style="font-weight:bold;">Please check that you have used the appropriate filters within each of the above sections to only request access to the records that you require.</span> On clicking submit your request will then be sent to the relevant dataset administrators.</p>')
                .append('<p>Please ensure your use of the data complies with the <a href="/Terms">NBN Gateway Terms and Conditions</a>, for example by crediting data providers in any report or publication.</p>');
        } else {
            data.append('<p>You are requesting full access to the records as detailed above.</p>')
                .append('<p><span style="font-weight:bold;">Please check that you have used the appropriate filters within each of the above sections to only request access to the records that you require.</span> On clicking submit your request will then be sent to the relevant dataset administrators.</p>')
                .append('<p>Please ensure your use of the data complies with the <a href="/Terms">NBN Gateway Terms and Conditions</a></p>');
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
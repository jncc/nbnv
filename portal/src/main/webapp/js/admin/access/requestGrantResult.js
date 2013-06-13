window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.requestGrantResult = function() {
    this._renderHeader = function() {
        return $('<h3>').attr('filtertype', 'result')
            .append($('<span>').addClass('filterheader').append('Confirm Access Grant'));
    };
    
    this._renderPanel = function(clickfn) {
        return $('<div>')
            .append($('<div>')
                .attr('id', 'resulterrortext')
                .addClass('ui-state-error')
            ).append($('<p>')
                .attr('id', 'resultpermtext')
            ).append($('<button>')
                .text('Grant')
                .attr('id', 'resultsubmitbtn')
                .click(clickfn)
            );
    };
    
    this._onEnter = function(error) {
        var data = $('#resultpermtext');
        data.html('');
        
        data.append("You are granting full access to the records detailed above.  They will be notified by e-mail when the request has been dealt with.")
        
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
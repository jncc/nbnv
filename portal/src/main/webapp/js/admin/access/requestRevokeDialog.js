window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.dialog = nbn.nbnv.ui.dialog || {};

nbn.nbnv.ui.dialog.requestRevokeDialog = function() {
    this.requestID =  -1;
    this.div = null;
    this.reason = '';
    
    this._render = function() {
        var _me = this;
        
        this.div = $('<div>').append($('<p>')
            .append('Revoking access removes access to the full version records covered by this request (unless remaining granted by a different request) to a user or organisation. It also sends an email to the user advising them of the change in access.')
        ).append($('<p>')
            .append('Revoke reason:')
            .append($('<textarea>')
                .attr('cols', '75')
                .attr('rows', '15')
                .watermark("Please enter reason here")
                .change(function() {
                    _me.reason = $(this).val();
                })
            )
        ).dialog({ 
            modal: true, 
            autoOpen: false,
            width: 650,
            buttons: { 
                "Revoke Request": function() {
                        var filter = { action: "revoke", reason: _me.reason }
                        
                        $.ajax({
                            type: 'POST',
                            url: nbn.nbnv.api + '/user/userAccesses/requests/' + _me.requestID,
                            data: filter,
                            success: function () { document.location.reload(true); }
                        });
                }, Cancel: function() { 
                    $(this).dialog("close"); 
                }
            }
        });
    };
    
    this.show = function(id) {
        this.requestID = id;
        this.div.dialog("open");
    };
}
window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.dialog = nbn.nbnv.ui.dialog || {};

nbn.nbnv.ui.dialog.requestCloseDialog = function() {
    this.requestID =  -1;
    this.div = null;
    
    this._render = function() {
        var _me = this;
        
        this.div = $('<div>').append($('<p>')
            .append('Closing a request removes it from the system, it does not grant access, and sends no response by email. This is used when a request has been handled outside of the NBN Gateway.')
        ).dialog({ 
            modal: true, 
            autoOpen: false,
            buttons: { 
                "Close Request": function() {
                        var filter = { action: "close", reason: "via portal" };
                        
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
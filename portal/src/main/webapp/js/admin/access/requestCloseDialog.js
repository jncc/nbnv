window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.dialog = nbn.nbnv.ui.dialog || {};

nbn.nbnv.ui.dialog.requestCloseDialog = function() {
    this.requestID =  -1;
    this.div = null;
    this.orgReq = false;
    
    this._render = function() {
        var _me = this;
        
        this.div = $('<div>').append($('<p>')
            .append('Closing a request removes it from the NBN Gateway, it does not grant access and does not send a response to the user by email. CLOSING A REQUEST SHOULD BE USED WHEN THE REQUEST CONTAINS SENSITIVE RECORDS FOR SPECIFIC SITE(S), as well as when requests have been handled outside of the NBN Gateway')
        ).dialog({ 
            modal: true, 
            autoOpen: false,
            buttons: { 
                "Close Request": function() {
                        displaySendingRequestDialog('Working...');
                        var filter = { action: "close", reason: "via portal" };
                        
                        var url;
                        
                        if (_me.orgReq) {
                            url = nbn.nbnv.api + '/organisation/organisationAccesses/requests/' + _me.requestID;
                        } else {
                            url = nbn.nbnv.api + '/user/userAccesses/requests/' + _me.requestID;
                        }

                        $.ajax({
                            type: 'POST',
                            url: url,
                            data: filter,
                            success: function () { document.location.reload(true); },
                            error: function () { alert("Problem with request id: " + _me.requestID); document.location.reload(true);  }                        
                        });
                }, Cancel: function() { 
                    $(this).dialog("close"); 
                }
            }
        });
    };
    
    this.show = function(id, json) {
        this.requestID = id;
        if ('organisationID' in json.reason && json.reason.organisationID != -1) { this.orgReq = true; } else { this.orgReq = false; }
        this.div.dialog("open");
        
    };
}
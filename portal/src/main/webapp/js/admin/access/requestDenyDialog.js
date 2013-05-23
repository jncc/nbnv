window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.dialog = nbn.nbnv.ui.dialog || {};

nbn.nbnv.ui.dialog.requestDenyDialog = function() {
    this.requestID =  -1;
    this.div = null;
    this.reason = '';
    
    this._render = function() {
        var _me = this;
        
        this.div = $('<div>').append($('<p>')
            .append('Denying a request dispatches an email informing them of the denied access request. If you want to silently deny a request, close it instead.')
        ).append($('<p>')
            .append('Deny reason:')
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
                "Deny Request": function() {
                    alert(_me.requestID);
                    alert(_me.reason);
                    $(this).dialog("close"); 
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
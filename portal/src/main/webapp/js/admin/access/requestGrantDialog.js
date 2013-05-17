window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};
nbn.nbnv.ui.dialog = nbn.nbnv.ui.dialog || {};

nbn.nbnv.ui.dialog.requestGrantDialog = function() {
    this.requestID =  -1;
    this.div = null;
    this.reason = '';
    
    this._render = function() {
        var _me = this;
        
        this.div = $('<div>').append($('<p>')
            .append('Granting a request gives access to the full version of the requested records to the user or organisation. It will also dispatch an email informing them of the granted access.')
        ).append($('<p>')
            .append('Grant reason:')
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
                "Grant Request": function() {
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
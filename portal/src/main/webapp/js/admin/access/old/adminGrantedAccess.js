window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.adminGrantedAccess = function() {
    var dataset = "";
    
    this.setDatasetKey = function(d) {
        dataset = d;
    };
    
    this._render = function() {
        var _me = this;
        
        var data = $('<div>');
            //.addClass("portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all");
        
        $.ajax({
            type: "POST",
            url: 'DatasetPrivilege',
            data: "dataset=" + dataset,
            success: function(datasetInfo) {
                if (datasetInfo.accessPrivileges.length == 0) {
                        data.append($('<div>')
                                .addClass('portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all')
                                .append("No access privileges granted yet")
                            );
                } else {
                    $.each(datasetInfo.accessPrivileges, function (id, priv) {
                        data.append($('<div>')
                                .addClass("portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                                .append($('<div>')
                                    .addClass("portlet-header ui-corner-all ui-widget-header")
                                    .append($("<span>").addClass("ui-icon ui-icon-person"))
                                    .append(priv.user)
                                ).append($('<div>')
                                    .addClass("portlet-content admin-granted")
                                    .append(nbn.nbnv.ui.util.requestJsonToText($.parseJSON(priv.json)))
                                ).append($('<div>')
                                    .addClass("admin-controls-granted")
                                    .append($('<div>')
                                        .addClass('ui-widget ui-widget-content ui-helper-clearfix ui-corner-all')
                                        .append($('<span>')
                                            .addClass('ui-icon ui-icon-closethick')
                                        ).attr('title', 'Deny')
                                        .click(function() {
                                            $.ajax({
                                                type: "POST",
                                                url: "AdminAccessRequest",
                                                data: "action=deny&uafID=" + priv.uafID,
                                                success: function() {
                                                    $('#request-' + priv.uafID).hide(); 
                                                    location.reload();
                                                },
                                                failure: function() {}
                                            })
                                        }).hover(function() {
                                            $(this).addClass('ui-state-hover');
                                        }, function() {
                                            $(this).removeClass('ui-state-hover');
                                        })
                                    ).append($('<div>')
                                        .addClass('ui-widget ui-widget-content ui-helper-clearfix ui-corner-all ui-state-disabled')
                                        .append($('<span>')
                                            .addClass('ui-icon ui-icon-pencil')
                                       ).attr('title', 'Edit')
                                       .click(function() {

                                        }).hover(function() {
                                            $(this).addClass('ui-state-disabled');
                                        }, function() {
                                            //$(this).removeClass('ui-state-disabled');
                                        })
                                    )
                                )
                            );    
                    });
                }
            }
        });

        return data;
    };
    
};

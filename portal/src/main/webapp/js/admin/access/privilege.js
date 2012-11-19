window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.privilegeFull = function() {
    var dataset = "";
    
    this.setDataset = function(d) {
        dataset = d;
    };
    
    this._render = function() {
        var _me = this;
        
        var data = $('<div>')
            .addClass("portlet privilege ui-widget ui-widget-content ui-helper-clearfix ui-corner-all");
        
        $.ajax({
            url: nbn.nbnv.api + '/taxonDatasets/' + dataset,
            success: function(datasetInfo) {
                data.append($('<div>')
                    .addClass("portlet-header privilege-dataset ui-corner-all")
                    .append($('<div>')
                        .addClass("logo")
                        .append($('<img>')
                            .attr('src', datasetInfo.logo)
                        )
                    ).append($('<div>').append(datasetInfo.organisationName + " - " + datasetInfo.title))
                );
                
                data.append($('<div>')
                    .addClass("portlet privilege-access ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .append($('<div>')
                        .addClass("portlet-header ui-corner-all ui-widget-header ui-state-active")
                        .append($("<span>").addClass("ui-icon ui-icon-person"))
                        .append($('<b>').append("Public access"))
                    ).append($('<div>')
                        .addClass("portlet-content")
                        .text('Public records are blurred to 10km')
                    )
                );
                    /*
                $.each(datasetInfo.accessPrivileges, function (id, priv) {
                    data.append($('<div>')
                        .addClass("portlet privilege-access ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                        .append($('<div>')
                            .addClass("portlet-header ui-corner-all ui-widget-header")
                            .append($("<span>").addClass("ui-icon ui-icon-person"))
                            .append(priv.user)
                        ).append($('<div>')
                            .addClass("portlet-content")
                            .append('Enhanced access: ' + nbn.nbnv.ui.util.requestJsonToText($.parseJSON(priv.json)))
                        )
                    );    
                });
                */
                data.append($('<div>')
                    .addClass("portlet privilege-access ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .append($('<div>')
                        .addClass("portlet-header privilege-constraint ui-corner-all")
                        .text("Use Constraints")
                    ).append($('<div>')
                        .addClass("portlet-content")
                        .text(datasetInfo.useConstraints)
                    )
                ).append($('<div>')
                    .addClass("portlet privilege-access ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .append($('<div>')
                        .addClass("portlet-header privilege-constraint ui-corner-all")
                        .text("Access Constraints")
                    ).append($('<div>')
                        .addClass("portlet-content")
                        .text(datasetInfo.accessConstraints)
                    )
                );
            }
        });

        return data;
    };
    
};

nbn.nbnv.ui.privilegeAccess = function() {
    var dataset = "";
    
    this.setDataset = function(d) {
        dataset = d;
    };
    
    this._render = function() {
        var _me = this;
        var data = $('<div>')
            .addClass("portlet privilege ui-widget ui-widget-content ui-helper-clearfix ui-corner-all");
        
        $.ajax({
            url: nbn.nbnv.api + '/datasets/' + dataset,
            success: function(datasetInfo) {
                data.append($('<div>')
                    .addClass("portlet-header privilege-dataset ui-corner-all")
                    .append($('<div>')
                        .addClass("logo")
                        .append($('<img>')
                            .attr('src', datasetInfo.logo)
                        )
                    ).append($('<div>').append(datasetInfo.organisationName + " - " + datasetInfo.title))
                ).append($('<div>')
                    .addClass("portlet privilege-access ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .append($('<div>')
                        .addClass("portlet-header privilege-constraint ui-corner-all")
                        .text("Access Constraints")
                    ).append($('<div>')
                        .addClass("portlet-content")
                        .text(datasetInfo.accessConstraints)
                    )
                );
            }
        });
        
        return data;
    }
}
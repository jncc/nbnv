window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.adminRequests = function() {
    var dataset;
    var reqs = [];
    
    this.setDatasetKey = function(datasetKey) {
        dataset = datasetKey;
    };
    
    this._render = function() {
        var requests = $('<div>');
        
        $.ajax({
            type: "POST",
            url: 'AccessRequests',
            data: "dataset=" + dataset,
            success: function(data) {
                if (data.length == 0) {
                    requests.append($('<div>')
                                        .addClass('portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all')
                                        .append("No outstanding admin requests")
                                    );
                } else {
                    $.each(data, function(i, reqJSON) {
                        var ara = new nbn.nbnv.ui.adminRequestedAccess();
                        ara.setJSON(reqJSON);
                        reqs.push(ara);
                        requests.append(ara._render());
                    });
                }
            }
        });
        
        return requests;
    };
};

nbn.nbnv.ui.adminRequestedAccess = function() {
    var setupJSON;
    
    this.setJSON = function(json) {
        setupJSON = json;
    };
    
    this._render = function() {
        var data = $('<div>')
                    .attr('id', 'request-' + setupJSON.uafID)
                    .addClass('portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all')
                    .append($('<div>')
                        .addClass('portlet-header ui-state-error ui-widget-header ui-corner-all')
                        .append($('<span>')
                            .addClass('ui-icon ui-icon-person')
                        ).append(setupJSON.user)
                    ).append($('<div>')
                        .addClass('admin-request')
                        .append(nbn.nbnv.ui.util.requestJsonToText(setupJSON.filter))
                        .append($('<div>')
                            .addClass('admin-request-reason ui-widget ui-widget-content ui-helper-clearfix ui-corner-all')
                            .append($('<div>')
                                .addClass('admin-request-reason-type')
                                .append('<i>I am a <b>' + setupJSON.role + '</b>, and request access for the purpose of <b>' + setupJSON.purpose + '</b></i>')
                            ).append($('<div>')
                                .append(setupJSON.reason)
                            )
                        )
                    ).append($('<div>')
                        .addClass('admin-controls-request')
                        .append($('<div>')
                            .addClass('ui-widget ui-widget-content ui-helper-clearfix ui-corner-all')
                            .append($('<span>')
                                .addClass('ui-icon ui-icon-check')
                            ).attr('title', 'Accept')
                            .click(function() {
                                $.ajax({
                                    type: "POST",
                                    url: "AdminAccessRequest",
                                    data: "action=accept&uafID=" + setupJSON.uafID,
                                    success: function() {
                                        $('#request-'  + setupJSON.uafID).hide();
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
                            .addClass('ui-widget ui-widget-content ui-helper-clearfix ui-corner-all')
                            .append($('<span>')
                                .addClass('ui-icon ui-icon-closethick')
                            ).attr('title', 'Deny')
                            .click(function() {
                                $.ajax({
                                    type: "POST",
                                    url: "AdminAccessRequest",
                                    data: "action=deny&uafID=" + setupJSON.uafID,
                                    success: function() {
                                        $('#request-'  + setupJSON.uafID).hide(); 
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
                                $(this).removeClass('ui-state-disabled');
                            })
                        )
                    );
        
        return data;
    };
};

/*	<div class="portlet">
		<div class="portlet-header requested">John Smith</div>
		<div class="portlet-content-request">All non-sensitive records for <b><i>Carychium minimum</i></b> within <b>North Devon (VC 4)</b> between <b>1990</b> and <b>2010</b>
			<div class="portlet-reason">
				<div style="text-align: center; padding-bottom: 0.3em;"><i>I am a <b>Ecologist</b>, and request access for the purpose of <b>Conservation</b></i></div>
				<div>I am conducting a survey of Carychium minimum within North Devon to locate areas for habitat preservation.</div>
			</div>
		</div>
		<div class="portlet-controls-request"><span class="button-edit">Edit</span><span class="button-accept">Accept</span><span class="button-delete">Delete</span></div>

	</div> */
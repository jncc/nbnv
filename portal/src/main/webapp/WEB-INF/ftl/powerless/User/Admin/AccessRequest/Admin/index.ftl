<#assign userRequests=json.readURL("${api}/user/userAccesses/requests/admin") />

<@template.master title="Requests for Datasets"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/proto-contols.css"]>

    <h1>Pending Access Requests</h1>
    <table>
        <#list userRequests as r>
        <tr>
            <td>
                <@userRequest request=r />
            </td>
        </tr>
        </#list>
    </table>
</@template.master>

<#macro userRequest request>
    <#assign ruser=json.readURL("${api}/user/${request.userID}") />
    <#assign rdataset=json.readURL("${api}/datasets/${request.datasetKey}") />
    <div id="request-${request.filter.id}" class="portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all">
        <div class="portlet-header ui-state-error ui-widget-header ui-corner-all">
            <span class="ui-icon ui-icon-person"></span>${ruser.forename} ${ruser.surname}
        </div>
        <div class="admin-request">
            <div>
                ${rdataset.title}
            </div>
            <div>
                ${request.filter.filterText}
            </div>
            <div class="admin-request-reason ui-widget ui-widget-content ui-helper-clearfix ui-corner-all">
                <div class="admin-request-reason-type">
                    <i>I am a <b>${request.requestRoleID}</b>, and request access for the purpose of <b>${request.requestTypeID}</b></i>
                </div>
                <div>
                    ${request.requestReason}
                </div>
            </div>
        </div>
        <div class="admin-controls-request">
            <div class="ui-widget ui-widget-content ui-helper-clearfix ui-corner-all">
                <span class="ui-icon ui-icon-check" title="Accept"></span>
            </div>
            <div class="ui-widget ui-widget-content ui-helper-clearfix ui-corner-all">
                <span class="ui-icon ui-icon-closethick" title="Deny"></span>
            </div>
            <div class="ui-widget ui-widget-content ui-helper-clearfix ui-corner-all">
                <span class="ui-icon ui-icon-pencil" title="Edit"></span>
            </div>
        </div>

    </div>
<!--
                    ).append($('<div>')
                        .addClass('admin-controls-request')
                        .append($('<div>')
                            .addClass('
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
                    ); -->
</#macro>
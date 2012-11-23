<#macro userRequest request controls=true>
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
        <#if controls == true>
            <div class="admin-controls-request">
                <div class="ui-widget ui-widget-content ui-helper-clearfix ui-corner-all">
                    <a href="/User/Admin/AccessRequest/Admin/${request.filter.id}/Accept"><span class="ui-icon ui-icon-check" title="Accept"></span></a>
                </div>
                <div class="ui-widget ui-widget-content ui-helper-clearfix ui-corner-all">
                    <span class="ui-icon ui-icon-closethick" title="Deny"></span>
                </div>
                <div class="ui-widget ui-widget-content ui-helper-clearfix ui-corner-all">
                    <span class="ui-icon ui-icon-pencil" title="Edit"></span>
                </div>
            </div>
        </#if>
    </div>
</#macro>
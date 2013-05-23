<@template.master title="NBN Gateway - Organisation Join Request"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js/","/js/joinRequest/create.js","/js/joinRequest/join_org_utils.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/organisation.css"]>

    <#assign organisationId="${URLParameters.organisation}">
    <#assign organisation=json.readURL("${api}/organisations/${organisationId}")>
    <#assign user=json.readURL("${api}/user")>
    <#assign isMember=json.readURL("${api}/organisationMemberships/${organisationId}/${user.id?string('0')}/isMember")>
    <#assign request=json.readURL("${api}/organisationMemberships/${organisationId}/join")>

    <#if isMember>
        <h1>Already a member of ${organisation.name}</h1>
        <p>You are already a member of this organisation, please click the link below to view the organisation page</p>
        <a href="/Organisations/${organisationId}">Click Here</a>
    <#elseif request.responseTypeID == -1> 
       <h1>Create Join Request for ${organisation.name}</h1>

        <p>Please give a reason for wanting to join this organisation below;<p>
        <textarea id="nbn-org-join-request-reason" rows="10" cols="50"></textarea>
        <input id="nbn-org-join-request-submit" type="button" value="Submit Request" data-url="${api}/organisationMemberships/${organisationId}/join"/>

        <div id="nbn-org-join-request-dialog" title="Submit Request" style="display:none;">
            <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>Are you sure you want to submit this join request<span id="nbn-org-join-request-dialog-extra"></span>?</p>
        </div>
    <#else>
        <#include "/organisationJoinRequest.ftl" />
    </#if>
</@template.master>
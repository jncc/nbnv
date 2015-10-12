<@template.master title="NBN Gateway - Organisation Join Request"
    javascripts=["/js/joinRequest/create.js","/js/joinRequest/join_org_utils.js","/js/dialog_spinner.js"]
    csss=["/css/organisation.css","/css/dialog-spinner.css"]>

    <#assign organisationId="${.data_model['organisationID']}">
    <#assign organisation=json.readURL("${api}/organisations/${organisationId}")>
    <#assign user=json.readURL("${api}/user")>
    <#assign request=json.readURL("${api}/organisationMemberships/request/${.data_model['requestID']?c}")>

    <#include "/organisationJoinRequest.ftl" />
</@template.master>
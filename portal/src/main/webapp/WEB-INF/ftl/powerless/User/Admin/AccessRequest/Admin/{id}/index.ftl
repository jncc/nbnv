<#assign id="${URLParameters.id}">
<#assign r=json.readURL("${api}/user/userAccesses/requests/admin/${id}") />

<@template.master title="Requests for Datasets"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/proto-contols.css"]>

    <h1>Pending Access Request</h1>
    <@accessrequest_utils.userRequest request=r />
</@template.master>

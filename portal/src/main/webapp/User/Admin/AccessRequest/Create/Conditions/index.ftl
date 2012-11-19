<#assign data="${RequestParameters.json}" />

<@template.master title="Conditions"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js","/js/admin/access/warning.js","/js/admin/access/util/requestJsonToText.js","/js/admin/access/privileges.js","/js/admin/access/privilege.js","/js/admin/access/requestedAccess.js","/js/admin/access/accessRequest.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/proto-contols.css"]>
    <h1>Access Request Submitted</h1>
    <div style="padding-bottom: 40px;">TODO: Blurb on access request process, what happens next etc</div>
    <div>DEBUG: ${data}</div>
</@template.master>

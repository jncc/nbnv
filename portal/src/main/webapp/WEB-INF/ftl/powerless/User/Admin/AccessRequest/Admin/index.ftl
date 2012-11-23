<#assign userRequests=json.readURL("${api}/user/userAccesses/requests/admin") />

<@template.master title="Requests for Datasets"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/proto-contols.css"]>

    <h1>Pending Access Requests</h1>
    <table>
        <#list userRequests as r>
        <tr>
            <td>
                <@accessrequest_utils.userRequest request=r />
            </td>
        </tr>
        </#list>
    </table>
</@template.master>

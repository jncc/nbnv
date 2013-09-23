<#assign user=json.readURL("${api}/user/${URLParameters.userID}")>
<#assign userOrgs=json.readURL("${api}/user/${URLParameters.userID}/organisations")>

<@template.master title="NBN Gateway - User"
    csss=["/css/dataset-metadata.css"]>
    <h1>User Details</h1>
    <table class="nbn-dataset-table nbn-simple-table nbn-metadata-dataset-table">
        <tr>
            <th>Name</th>
            <td>${user.forename} ${user.surname}</td>
        </tr>
        <tr>
            <th>Email</th>
            <td>${user.email}</td>
        </tr>
        <tr>
            <th>Organisation Memberships</th>
            <td>
                <#if userOrgs?has_content>
                <ul>
                    <#list userOrgs as org>
                    <li><a href="/Organisations/${org.id}">${org.name}</a></li>
                    </#list>
                </ul>
                <#else>
                None
                </#if>
            </td>
        </tr>
    </table>
</@template.master>
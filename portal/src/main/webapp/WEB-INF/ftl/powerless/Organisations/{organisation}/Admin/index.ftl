<@template.master title="NBN Gateway - Organisations Administration"
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/organisation.css"]>

    <#assign organisationId="${URLParameters.organisation}">
    <#assign organisation=json.readURL("${api}/organisations/${organisationId}")>
    <#assign users=json.readURL("${api}/organisationMembership/${organisationId}")>
    <#assign joins=json.readURL("${api}/organisationJoinRequests/${organisationId}")>


    <h1>${organisation.name}</h1>
    <div class="tabbed nbn-organisation-tabbed">
        <h3>Summary</h3>
        <img class="nbn-align-img-right" src="${api}/organisations/${organisation.id}/logo" />${organisation.summary}
    </div>
    <div class="tabbed nbn-organisation-tabbed">
        <h3>Contact details</h3>
        <div id="nbn-organisation-contact-details">
            <form id="nbn-metadata-update" url="${api}/datasets/${dataset.key}">
                <table class="nbn-dataset-table nbn-simple-table">
                    <tr>
                        <th>Contact Name</th>
                        <td><input type="text" name="contactName" value="${organisation.contactName}" /></td>
                    </tr>
                    <tr>
                        <th>Contact Email</th>
                        <td><input type="text" name="contactEmail" value="${organisation.contactEmail}" /></td>
                    </tr>
                    <tr>
                        <th>Address</th>
                        <td><input type="text" name="address" value="${organisation.address}" /></td>
                    </tr>
                    <tr>
                        <th>Postcode</th>
                        <td><input type="text" name="postCode" value="${organisation.postCode}" /></td>
                    </tr>
                    <tr>
                        <th>Website</th>
                        <td><input type="text" name="website" value="${organisation.website}" /></td>
                    </tr>
                </table>

                <button id="nbn-form-submit"> Update </button>
            </form>
        </div>
    </div>
</@template.master>

<#macro parseUsers userList>
    <#assign users = []>
    <#list userList as obj>
        <#assign users = users + [{"key": obj.user.id, "name": obj.user.firstName + " " + obj.user.lastName, "role": obj.role}]/>
    </#list>
    <@userTable users "Users associated with this organisation" />
</#macro>

<#macro userTable users title>
    <div class="tabbed nbn-organisation-tabbed">
        <h3>${title}</h3>
        <table class="nbn-simple-table"><tr><th>Title</th><th>Role</th></tr>
            <#list users as user>
                <tr><td class="nbn-org-datasets-title-td"><a href="/User/${user.key}">${user.name}</a></td><td class="nbn-org-datasets-desc-td">${user.role}</td></tr>
            </#list>
        </table>
    </div>
</#macro>

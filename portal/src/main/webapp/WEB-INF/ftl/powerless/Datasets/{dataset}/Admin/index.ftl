<#assign full=json.readURL("${api}/datasets/${URLParameters.dataset}/edit")>
<#assign admins=json.readURL("${api}/datasets/${URLParameters.dataset}/admins")>

<h3>Add a user as dataset administrator</h2>
<input id="nbn-dataset-add-admin" type="text" data-url="${api}/user/search?dataset=${URLParameters.dataset}" />
<input id="nbn-dataset-add-admin-id" type="hidden" />
<input id="nbn-dataset-add-admin-submit" type="button" data-url="${api}/datasets/${URLParameters.dataset}/addAdmin" value="Add User As Dataset Admin" />
<br />
<h3>Remove a dataset administrator</h2>
<@parseUsers userList=admins />

<div id="dialog-dataset-admin" title="Are you sure?" style="display:none;">
    <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span><span id="dialog-dataset-admin-text"></span></p>
</div>

<div id="dialog-dataset-admin-success" title="Successfully completed operation" style="display:none;">
    <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span><span id="dialog-dataset-admin-success-text"></span></p>
</div>

<h3>Other Dataset Administration Actions</h3>
<a href="/Datasets/${full.key}/Edit">Edit Dataset Metadata</a><br/>
<a href="/Datasets/${full.key}/Surveys/Edit">Edit Survey Metadata</a><br/>
<a href="/Datasets/${full.key}/Attributes/Edit">Edit Attribute Metadata</a><br/>
<a href="/AccessRequest/History/${full.key}">View Dataset Access History</a><br/>
<a href="/Datasets/${full.key}/Contributing">Add / Remove Contributing Organisations</a><br/>
<a href="/Datasets/${full.key}#ui-tabs-<#if full.typeName == "Taxon">5<#else>1</#if>">Add / Remove Dataset Administrators</a>                        

<#macro parseUsers userList>
    <#assign users = []>
    <#list userList as obj>
        <#assign users = users + [{"key": obj.user.id, "name": obj.user.forename + " " + obj.user.surname}]/>
    </#list>
    <@userTable users "Dataset Administrators" />
</#macro>

<#macro userTable users title>
    <table id="nbn-dataset-admin" class="nbn-simple-table" data-remove="${api}/datasets/${URLParameters.dataset}/removeAdmin">
        <thead>
            <tr>
                <th>ID</th>
                <th>User</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td class="nbn-dataset-admin-id-td">${user.key?string("0")}</td>
                <td class="nbn-dataset-admin-name-td">${user.name}</td>
                <td class="nbn-dataset-admin-remove-td">
                    <a href="#" class="nbn-dataset-admin-remove" style="float:right;" data-id="${user.key?string("0")}" data-name="${user.name}">Revoke Dataset Administration Rights</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</#macro>

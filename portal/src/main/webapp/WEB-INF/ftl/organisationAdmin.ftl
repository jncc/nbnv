<@template.master title="NBN Gateway - Organisations Administration"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js/","/js/enable-dataset-metadata-tabs.js","/js/jquery.dataTables.min.js","/js/orgAdmin/enable-users-datatable.js","/js/orgAdmin/enable-join-datatable.js","/js/orgAdmin/enable-direct-add-user.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/organisation.css","/css/org-admin.css"]>

    <#assign organisationId="${.data_model['organisationID']}">
    <#assign organisation=json.readURL("${api}/organisations/${organisationId}")>
    <#assign users=json.readURL("${api}/organisationMemberships/${organisationId}")>

    <h1>${organisation.name}</h1>
    <div id="nbn-tabs">
        <ul>
            <li><a href="#tabs-1">User Management</a></li>
            <li><a href="#tabs-2">Add a User</a></li>
            <li><a href="#tabs-3">Join Requests</a></li>
        </ul>
        <div id="tabs-1">
            <@parseUsers userList=users />
        </div>
        <div id="tabs-2">
            <input id="nbn-org-add-user" type="text" data-url="${api}/user/search" />
            <input id="nbn-org-add-user-id" type="hidden" />
            <input id="nbn-org-add-user-submit" type="button" data-url="${api}/organisationMemberships/${organisationId}/addUser" value="Add Selected User" />
        </div>
        <div id="tabs-3">
            <@parseRequests userList=[] />
        </div>
    </div>

    <div id="dialog-remove-confirm" title="Remove this user from the organisation?" style="display:none;">
        <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>Are you sure you want to remove <span id="nbn-org-remove-confirm-name"></span> from the organisation?</p>
    </div>

    <div id="dialog-role-choice" title="Change this users role" style="display:none;">
        <p>Please select a new role for <span id="nbn-org-role-change-name"></span> from the dropdown list</p>
        <select id="nbn-org-role-change-select">
            <option value="1">Member</option>
            <option value="2">Administrator</option>
        </select>
    </div>

    <div id="dialog-role-confirm" title="Change this users role" style="display:none;">
        <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>Are you sure you want to change <span id="nbn-org-role-confirm-name"></span>'s role to <span id="nbn-org-role-confirm-type"></span>?</p>
    </div>

    <div id="nbn-org-add-user-dialog" title="Add user to organisation" style="display:none;">
        <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>Are you sure you want to add <span id="nbn-org-add-user-dialog-name"></span> to this organisation?</p>
    </div>
    
</@template.master>

<#macro parseUsers userList>
    <#assign users = []>
    <#list userList as obj>
        <#assign users = users + [{"key": obj.user.id, "name": obj.user.forename + " " + obj.user.surname, "role": obj.role}]/>
    </#list>
    <@userTable users "Users associated with this organisation" />
</#macro>

<#macro userTable users title>
    <table id="nbn-users-datatable" class="nbn-simple-table" data-modify="${api}/organisationMemberships/${organisationId}/modifyUserRole" data-remove="${api}/organisationMemberships/${organisationId}/removeUser">
        <thead>
            <tr>
                <th>ID</th>
                <th>User</th>
                <th>Role</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td class="nbn-org-user-id-td">${user.key?string("0")}</td>
                <td class="nbn-org-user-name-td">${user.name}</td>
                <td class="nbn-org-user-role-td">
                    <#if user.role == "administrator">Administrator<#elseif user.role == "lead">Lead<#else>Member</#if>
                </td>
                <td class="nbn-org-user-modify-td">
                    <img data-id="${user.key?string("0")}" data-name="${user.name}" class="nbn-org-user-remove" src="/img/delete.png" style="float:right;" title="Remove User" />
                    <img data-id="${user.key?string("0")}" data-name="${user.name}" data-role="<#if user.role == "administrator">2<#elseif user.role == "lead">3<#else>1</#if>" class="nbn-org-user-role" src="/img/role.png" style="float:right;" title="Change User Role" /> 
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</#macro>

<#macro parseRequests userList>
        <#assign users = []>
        <#list userList as obj>
            <#assign users = users + [{"key": obj.user.id, "name": obj.user.forename + " " + obj.user.surname, "text": obj.desc}]/>
        </#list>
        <@requestsTable users "Requests to join this organisation" />
</#macro>

<#macro requestsTable users title>
    <table id="nbn-requests-datatable" class="nbn-simple-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>User</th>
                <th>Request Message</th>
            </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td class="nbn-org-user-join-id-td">${user.key?string("0")}</td>
                <td class="nbn-org-user-join-name-td">${user.name}</td>
                <td class="nbn-org-user-join-text-td">${user.text}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</#macro>

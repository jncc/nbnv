<@template.master title="NBN Gateway - Organisations Administration"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js/","/js/enable-dataset-metadata-tabs.js","/js/jquery.dataTables.min.js","/js/orgAdmin/enable-users-datatable.js","/js/orgAdmin/enable-join-datatable.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/organisation.css","/css/org-admin.css"]>

    <#assign organisationId="${.data_model['organisationID']}">
    <#assign organisation=json.readURL("${api}/organisations/${organisationId}")>
    <#assign users=json.readURL("${api}/organisationMemberships/${organisationId}")>

    <h1>${organisation.name}</h1>
    <div id="nbn-tabs">
        <ul>
            <li><a href="#tabs-1">Summary</a></li>
            <li><a href="#tabs-2">Contact Details</a></li>
            <li><a href="#tabs-3">User Management</a></li>
        </ul>
        <div id="tabs-1">
            <div>
                <img class="nbn-align-img-right" src="${api}/organisations/${organisation.id}/logo" />${organisation.summary}
            </div>
        </div>
        <div id="tabs-2">
            <form id="nbn-metadata-update" url="${api}/organisation/${organisation.id}">
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
                        <td><input type="text" name="postCode" value="${organisation.postcode}" /></td>
                    </tr>
                    <tr>
                        <th>Website</th>
                        <td><input type="text" name="website" value="${organisation.website}" /></td>
                    </tr>
                </table>

                <button id="nbn-form-submit"> Update </button>
            </form>
        </div>
        <div id="tabs-3">
            <@parseUsers userList=users />
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
            <option value="3">Lead</option>
        </select>
    </div>

    <div id="dialog-role-confirm" title="Change this users role" style="display:none;">
        <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>Are you sure you want to change <span id="nbn-org-role-confirm-name"></span>'s role to <span id="nbn-org-role-confirm-type"></span>?</p>
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
    <div class="tabbed nbn-organisation-tabbed">
        <h3>${title}</h3>
        <table id="nbn-users-datatable" class="nbn-simple-table">
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
                    <td class="nbn-org-user-delete-td">
                        <img data-id="${user.key?string("0")}" data-name="${user.name}" class="nbn-org-user-remove" src="/img/delete.png" style="float:right;" title="Remove User" />
                        <img data-id="${user.key?string("0")}" data-name="${user.name}" data-role="<#if user.role == "administrator">2<#elseif user.role == "lead">3<#else>1</#if>" class="nbn-org-user-role" src="/img/role.png" style="float:right;" title="Change User Role" /> 
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</#macro>

<#macro parseRequests userList>
        <#assign users = []>
        <#list userList as obj>
            <#assign users = users + [{"key": obj.user.id, "name": obj.user.forename + " " + obj.user.surname, "text": obj.desc}]/>
        </#list>
        <@requestsTable users "Requests to join this organisation" />
</#macro>

<#macro requestsTable users title>
    <div class="tabbed nbn-organisation-tabbed">
        <h3>${title}</h3>
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
    </div>
</#macro>

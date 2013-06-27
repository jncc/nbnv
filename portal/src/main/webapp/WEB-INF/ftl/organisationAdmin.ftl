<@template.master title="NBN Gateway - Organisations Administration"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js/","/js/enable-dataset-metadata-tabs.js","/js/jquery.dataTables.min.js","/js/orgAdmin/enable-users-datatable.js","/js/orgAdmin/enable-join-datatable.js","/js/orgAdmin/enable-direct-add-user.js","/js/jquery.validate.min.js","/js/orgAdmin/enable-metadata-edit.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/organisation.css","/css/org-admin.css"]>

    <#assign organisationId="${.data_model['organisationID']}">
    <#assign organisation=json.readURL("${api}/organisations/${organisationId}/metadata")>
    <#assign users=json.readURL("${api}/organisationMemberships/${organisationId}")>
    <#assign joinRequests=json.readURL("${api}/organisationMemberships/${organisationId}/requests")>

    <h1>${organisation.name}</h1>
    <div id="nbn-tabs">
        <ul>
            <li><a href="#tabs-1">Current Members</a></li>
            <li><a href="#tabs-2">Add a Member</a></li>
            <li><a href="#tabs-3">Membership Requests <b>(${joinRequests?size})</b></a></li>
            <li><a href="#tabs-4">Update Organisation Details</a></li>
        </ul>
        <div id="tabs-1">
            <@parseUsers userList=users />
        </div>
        <div id="tabs-2">
            <input id="nbn-org-add-user" type="text" data-url="${api}/user/search?organisation=${organisationId}" />
            <input id="nbn-org-add-user-id" type="hidden" />
            <input id="nbn-org-add-user-submit" type="button" data-url="${api}/organisationMemberships/${organisationId}/addUser" value="Add Selected User" />
        </div>
        <div id="tabs-3">
            <@parseRequests requestList=joinRequests />
        </div>
        <div id="tabs-4">
            <form id="nbn-org-metadata-edit" action="${api}/organisations/${organisationId}/metadata">
                <table>
                    <tr>
                        <th>Name</th>
                        <td><input type="text" name="name" value="${organisation.name}" /></td>
                    </tr>
                    <tr>
                        <th>Abbreviation</th>
                        <td><input type="text" name="abbreviation" value="<#if organisation.abbreviation??>${organisation.abbreviation}<#else></#if>" /></td>
                    </td>
                    <tr>
                        <th>Summary</th>    
                        <td><textarea name="summary"><#if organisation.summary??>${organisation.summary}<#else></#if></textarea></td>
                    </tr>
                    <tr>
                        <th>Address</th>
                        <td><textarea name="address"><#if organisation.address??>${organisation.address}<#else></#if></textarea></td>
                    </tr>
                    <tr>
                        <th>Postcode</th>
                        <td><input type="text" name="postcode" value="<#if organisation.postcode??>${organisation.postcode}<#else></#if>" /></td>
                    </tr>
                    <tr>
                        <th>Phone</th>
                        <td><input type="text" name="phone" value="<#if organisation.phone??>${organisation.phone}<#else></#if>" /></td>
                    </tr>
                    <tr>
                        <th>Website</th>
                        <td><input type="text" name="website" value="<#if organisation.website??>${organisation.website}<#else></#if>" /></td>
                    </tr>
                    <tr>
                        <th>Contact Name</th>
                        <td><input type="text" name="contactName" value="<#if organisation.contactName??>${organisation.contactName}<#else></#if>" /></td>
                    </tr>
                    <tr>
                        <th>Contact Email</th>
                        <td><input type="text" name="contactEmail" value="<#if organisation.contactEmail??>${organisation.contactEmail}<#else></#if>" /></td>
                    </tr>
                </table>
                <input id="nbn-org-metadata-update-submit" type="submit" value="Change Organisation Details" />
                <div id="nbn-waiting-ticker" style="display:none; float: right;"><p>Warning it may take some time for these changes to propagate to live site <img src="/img/ajax-loader.gif" /></p></div>
            </form>
        </div>
    </div>

    <div id="dialog-remove-confirm" title="Revoke this users membership?" style="display:none;">
        <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>Are you sure you want to revoke <span id="nbn-org-remove-confirm-name"></span>'s membership?</p>
    </div>

    <div id="dialog-role-choice" title="Change Member's Role" style="display:none;">
        <p>Please select a new role for <span id="nbn-org-role-change-name"></span> from the dropdown list</p>
        <select id="nbn-org-role-change-select">
            <option value="1">Member</option>
            <option value="2">Administrator</option>
        </select>
    </div>

    <div id="dialog-role-confirm" title="Change Member's Role" style="display:none;">
        <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>Are you sure you want to change <span id="nbn-org-role-confirm-name"></span>'s role to <span id="nbn-org-role-confirm-type"></span>?</p>
    </div>

    <div id="nbn-org-add-user-dialog" title="Add Member to Organisation" style="display:none;">
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
                    <a href="#" class="nbn-org-user-role" style="float:right;" data-name="${user.name}" data-id="${user.key?string("0")}" data-role="<#if user.role == "administrator">2<#elseif user.role == "lead">3<#else>1</#if>">Change Member's Role</a> 
                </td>
                <td class="nbn-org-user-remove-td">
                    <a href="#" class="nbn-org-user-remove" style="float:right;" data-id="${user.key?string("0")}" data-name="${user.name}">Revoke Membership</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</#macro>

<#macro parseRequests requestList>
        <#assign requests = []>
        <#list requestList as obj>
            <#assign requests = requests + [{"key": obj.id, "name": obj.user.forename + " " + obj.user.surname, "text": obj.requestReason, "date": obj.requestDate}]/>
        </#list>
        <@requestsTable requests "Requests to join this organisation" />
</#macro>

<#macro requestsTable requests title>
    <table id="nbn-requests-datatable" class="nbn-simple-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>User</th>
                <th>Request Message</th>
                <th>Request Date</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
        <#list requests as request>
            <tr>
                <td class="nbn-org-user-join-id-td">${request.key?string("0")}</td>
                <td class="nbn-org-user-join-name-td">${request.name}</td>
                <td class="nbn-org-user-join-request-text-td">${request.text}</td>
                <td class="nbn-org-user-join-request-date-td">${request.date?number_to_date}</td>
                <td class="nbn-org-user-join-request-view-td"><a href="/Organisations/JoinRequest/${request.key?string("0")}">Action this request</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</#macro>

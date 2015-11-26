<#assign dataset=json.readURL("${api}/datasets/${URLParameters.dataset}/edit")>

<@template.master title="Contributing Organisations for ${dataset.title}"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js","/js/jquery.dataTables.min.js","/js/admin/contributing.js","/js/dialog_spinner.js"] 
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css", "/css/admin-controls.css", "/css/dialog-spinner.css"]>

    <#assign organisations=json.readURL("${api}/organisations")>

    <h1>Manage Contributing Organisations for ${dataset.title}</h1>
    <div id="nbn-contributing-org-add_tab" class="tabbed">
        <h3>Add Contributing Organisation</h3>
        <select id="nbn-add-contrib-org">
            <#list organisations?sort_by("name") as org>
            <option value="${org.id}">${org.name}</option>
            </#list>
        </select>
        <input id="nbn-add-contrib-org-submit" type="button" data-url="${api}/datasets/${dataset.key}/contributing" value="Add Contributing Organisation" />
    </div>
    <div id="nbn-contributing-org-remove_tab" class="tabbed">
        <h3>Remove Contributing Organisation</h3>
        <@parseOrgs orgList=dataset.contributingOrganisations />
    </div>
    
    <div id="nbn-add-contrib-org-submit-dialog" style="display:none">
        <p>Are you sure you would like to add <span id="nbn-add-contrib-org-submit-dialog-name"></span> to the list of Contributing Organisations?</p>
        <p><b>Warning:</b> It may take some time for these changes to take effect for Public viewing</p>
    </div>

    <div id="nbn-remove-contrib-org-submit-dialog" style="display:none">
        <p>Are you sure you would like to remove <span id="nbn-remove-contrib-org-submit-dialog-name"></span> to the list of Contributing Organisations?</p>
        <p><b>Warning:</b> It may take some time for these changes to take effect for Public viewing</p>
    </div>

    <div id="nbn-contrib-org-error-dialog" style="display:none">
        <p>Could not <span id="nbn-contrib-org-error-action"></span> as a contributing organisation, the following message was returned:</p>
        <p><span id="nbn-contrib-org-error-reason"></span></p>
    </div>

</@template.master>

<#macro parseOrgs orgList>
    <#assign orgs = []>
    <#list orgList as obj>
        <#assign orgs = orgs + [{"id": obj.id, "name": obj.name}]/>
    </#list>
    <@orgTable orgs "Contributing Organisations" />
</#macro>

<#macro orgTable orgs title>
    <table id="nbn-contributing-org-datatable" class="nbn-simple-table" data-remove="${api}/datasets/${dataset.key}/contributing">
        <thead>
            <tr>
                <th>Organisation</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
        <#list orgs as org>
            <tr>
                <td class="nbn-org-name-td">${org.name}</td>
                <td class="nbn-org-remove-td">
                    <a href="#" class="nbn-contributing-org-remove" style="float:right;" data-id="${org.id?string("0")}" data-name="${org.name}">Remove Contributing Organisation</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</#macro>
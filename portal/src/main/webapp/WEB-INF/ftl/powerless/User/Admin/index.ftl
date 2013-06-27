<#assign user = json.readURL("${api}/user")/>
<#assign datasets = json.readURL("${api}/user/adminDatasets")/>
<#assign orgs = json.readURL("${api}/user/organisations")/>
<#assign orgAs = json.readURL("${api}/user/adminOrganisations")/>

<@template.master title="National Biodiversity Network Gateway"
    javascripts=["/js/jquery.dataTables.min.js"]
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"]>

    <script>
        //nbn.nbnv.api = '${api}';

        $(function(){
            $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
            $('.sTable').dataTable({
                "aaSorting": [[0, "asc"]],
                "bAutoWidth": true,
                "bFilter": false,
                "bJQueryUI": true,
                "iDisplayLength": 10,
                "bSortClasses": false,
                "sPaginationType": "full_numbers",
                "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]]
            });
        });
    </script>

    <h1>Administration Options for ${user.forename} ${user.surname}</h1>
    <div>
        <@userAdmin user=user />
        <#if datasets?has_content>
            <@datasetAdmin datasets=datasets />
        </#if>
        <#if orgs?has_content>
            <@organisations organisations=orgs admin=orgAs />
        </#if>
    </div>
</@template.master>

<#macro organisations organisations admin>
    <div class="tabbed">
        <h3>Organisations</h3>
        <table class="sTable">
            <thead>
                <tr>
                    <th>Organisation</th>
                    <#if admin?has_content><th>Action</th></#if>
                </tr>
            </thead>
            <tbody>
            <#list organisations as r>
                <tr>
                    <td>${r.name}</td>
                    <#assign t=0 /><#list admin as a><#if a.id==r.id><#assign t=1 /></#if></#list>
                    <#if t==1>
                        <td>
                            <a href="/Organisations/${r.id?c}/Admin">Admin Organisation</a><br/>
                            <a href="/AccessRequest/Organisations/${r.id?c}">View Organisation Access Requests</a>
                        </td>
                    </#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</#macro>
<#macro userAdmin user>
    <div class="tabbed">
        <h3>User Settings</h3>
        <table>
            <tr><td>Username:</td><td>${user.username}</td></tr>
            <tr><td>Email:</td><td>${user.email}</td></tr>
            <tr><td><a href="/User/Modify">Modify your user details</a></td><td><a href="/User/Modify#tabs-2">Change password</a></td><td><a href="/User/Modify#tabs-3">Change Email Subscriptions</a></td></tr>
        </table>
    </div>
</#macro>

<#macro datasetAdmin datasets>
    <div class="tabbed">
        <h3>Administrate Datasets</h3>
        <div><a href="AccessRequest/Admin">Interact with Access Permissions</a></div>
        <div>View Download Log</div>
        <table class="sTable">
            <thead>
                <tr>
                    <th>Dataset</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
            <#list datasets as r>
                <tr>
                    <td>${r.title}</td>
                    <td>
                        <a href="/Datasets/${r.key}/Edit">Edit Dataset Metadata</a><br/>
                        <a href="/Datasets/${r.key}/Surveys/Edit">Edit Survey Metadata</a><br/>
                        <a href="/Datasets/${r.key}/Attributes/Edit">Edit Attribute Metadata</a><br/>
                        <a href="/AccessRequest/History/${r.key}">Access Permissions History</a>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</#macro>

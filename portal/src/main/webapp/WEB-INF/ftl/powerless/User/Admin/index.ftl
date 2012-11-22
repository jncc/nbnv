<#assign user = json.readURL("${api}/user")/>
<#assign datasets = json.readURL("${api}/user/adminDatasets")/>

<@template.master title="National Biodiversity Network Gateway">
    <h1>Administration Options for ${user.forename} ${user.surname}</h1>
    <div>
        <@userAdmin user=user />
        <#if datasets?has_content>
            <@datasetAdmin datasets=datasets />
        </#if>
    </div>
</@template.master>

<#macro userAdmin user>
    <div class="tabbed">
        <h3>User Settings</h3>
        <table>
            <tr><td>Username:</td><td>${user.username}</td></tr>
            <tr><td>Email:</td><td>${user.email}</td></tr>
            <tr><td>Id:</td><td>${user.id}</td></tr>
            <tr><td>TODO: Editable bits go here...</td></tr>
        </table>
    </div>
</#macro>

<#macro datasetAdmin datasets>
    <div class="tabbed">
        <h3>Administrate Datasets</h3>
        <table>
            <tr><td><a href="Admin/Metadata">Alter Dataset Metadata</a></td></tr>
            <tr><td>Interact with Access Permissions</td></tr>
            <tr><td>View Download Log</td></tr>
        </table>
    </div>
</#macro>

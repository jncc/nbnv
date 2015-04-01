<#assign user = json.readURL("${api}/user")/>
<#assign datasets = json.readURL("${api}/user/adminDatasets")/>
<#assign orgs = json.readURL("${api}/user/organisations")/>
<#assign orgAs = json.readURL("${api}/user/adminOrganisations")/>
<#assign orgRs = json.readURL("${api}/organisationMemberships/requests")/>

<@template.master title="National Biodiversity Network Gateway"
    javascripts=["/js/jquery.dataTables.min.js"]
    csss=["//ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"]>

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
    <p>Welcome to your Account Page. Follow the links to update your account details, request enhanced access to datasets and apply for membership to organisations. Links are provided for administrators to manage their datasets and organisations. </p>
    <div>
        <@userAdmin user=user />
        <#if datasets?has_content>
            <@datasetAdmin datasets=datasets />
        </#if>
        <@organisations organisations=orgs admin=orgAs requests=orgRs />
    </div>
</@template.master>

<#macro organisations organisations admin requests>
    <div class="tabbed">
        <h3>Your Organisation Membership</h3>
        <#if requests?has_content>
        <h4>Pending Membership Requests</h4>
        <table class="sTable">
            <thead>
                <tr>
                    <th>Organisation</th>
                    <th>Request Reason</th>
                    <th>Request Date</th>
                </tr>
            </thead>
            <tbody>
            <#list requests as r>
                <tr>
                    <td><a href="/Organisations/${r.organisation.id?c}">${r.organisation.name}</a></td>
                    <td>${r.requestReason}</td>
                    <td>${r.requestDate}</td>
                </tr>
            </#list>
            </tbody>
        </table>
        </#if>
        <h4>Organisation Membership</h4>
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
                    <td><a href="/Organisations/${r.id?c}">${r.name}</a></td>
                    <#assign t=0 /><#list admin as a><#if a.id==r.id><#assign t=1 /></#if></#list>
                    <#if t==1>
                        <td>
                            <a href="/Organisations/${r.id?c}/Admin#tabs-4">Edit Organisation Details</a><br/>
                            <a href="/Organisations/${r.id?c}/Admin">Manage Organisation Membership</a><br/>
                            <a href="/AccessRequest/Organisations/${r.id?c}">View Organisation Access Requests</a>
                        </td>
                    <#elseif admin?has_content>
                        <td>&nbsp;</td>
                    </#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</#macro>

<#macro userAdmin user>
    <div class="tabbed">
        <h3>Your Account</h3>
        <table>
            <tr><td>Username:</td><td>${user.username}</td></tr>
            <tr><td>Email:</td><td>${user.email}</td></tr>
            <tr><td><a href="/User/Modify">Change Your User Details</a></td><td><a href="/User/Modify#tabs-2">Change Password</a></td><td><a href="http://www.nbn.org.uk/News/Latest-news/Sign-up-to-receive-eNews.aspx">Subscribe to NBN eNews</a></td></tr>
            <tr><td><a href="/AccessRequest/Create">Create New Access Request</a></td><td><a href="/AccessRequest">View Your Access Requests</a></td><td><a href="/Organisations/Join">Join an Organisation</a></td></tr>
        </table>
    </div>
</#macro>

<#macro datasetAdmin datasets>
    <div class="tabbed">
        <h3>Manage Your Datasets</h3>
        <p><a href="/AccessRequest/Admin">Manage Access Requests</a></p>
        <p><a href="/AccessRequest/Create/Grant">Create New Dataset Access</a></p>
        <p><a href="/Reports/Download">View Download Log For Your Datasets</a></p>
        <p><a href="/Import">Manage dataset uploads</a></p>
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
                    <td><a href="/Datasets/${r.key}">${r.title}</a></td>
                    <td>
                        <a href="/Datasets/${r.key}/Edit">Edit Dataset Metadata</a><br/>
                        <a href="/Datasets/${r.key}/Surveys/Edit">Edit Survey Metadata</a><br/>
                        <a href="/Datasets/${r.key}/Attributes/Edit">Edit Attribute Metadata</a><br/>
                        <a href="/AccessRequest/History/${r.key}">View Dataset Access History</a><br/>
                        <a href="/Datasets/${r.key}/Contributing">Add / Remove Contributing Organisations</a><br/>
                        <a href="/Datasets/${r.key}/Admin">Add / Remove Dataset Administrators</a><br/>  
                        <a href="/Datasets/${r.key}/Notifications">Turn On / Off Download Notifications</a><br/>
                        <a href="/Reports/Download/${r.key}">Download Log</a><br/>
                        <a href="/Reports/ApiViews/${r.key}">View and REST services Log</a><br/>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</#macro>

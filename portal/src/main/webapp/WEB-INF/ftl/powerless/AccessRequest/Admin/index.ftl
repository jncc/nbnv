<#assign oUserRequests=json.readURL("${api}/user/userAccesses/requests/admin/pending") />
<#assign gUserRequests=json.readURL("${api}/user/userAccesses/requests/admin/granted") />
<#assign dUserRequests=json.readURL("${api}/user/userAccesses/requests/admin/denied") />
<#assign oOrgRequests=json.readURL("${api}/organisation/organisationAccesses/requests/admin/pending") />
<#assign gOrgRequests=json.readURL("${api}/organisation/organisationAccesses/requests/admin/granted") />
<#assign dOrgRequests=json.readURL("${api}/organisation/organisationAccesses/requests/admin/denied") />

<@template.master title="Requests for Datasets"
    javascripts=["/js/jquery.dataTables.min.js"
    ,"/js/jquery.watermark.min.js"
    ,"/js/admin/access/util/moment.min.js"
    ,"/js/admin/access/timeLimit.js"
    ,"/js/admin/access/requestCloseDialog.js"
    ,"/js/admin/access/requestGrantDialog.js"
    ,"/js/admin/access/requestDenyDialog.js"
    ,"/js/admin/access/requestRevokeDialog.js"
    ,"/js/admin/access/requestInfoDialogs.js"
    ,"/js/dialog_spinner.js"
    ]
    csss=["//ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css","/css/dialog-spinner.css"]>

    <script>
        nbn.nbnv.api = '${api}';

        var close;
        var grant;
        var deny;
        var revoke;
        var jsonCache = {};

        $(function(){
            <#list oUserRequests as r>
                jsonCache[${r.filter.id?c}] = ${r.filter.filterJSON}
            </#list>
            <#list oOrgRequests as r>
                jsonCache[${r.filter.id?c}] = ${r.filter.filterJSON}
            </#list>
            <#list gUserRequests as r>
                jsonCache[${r.filter.id?c}] = ${r.filter.filterJSON}
            </#list>
            <#list gOrgRequests as r>
                jsonCache[${r.filter.id?c}] = ${r.filter.filterJSON}
            </#list>

            close = new nbn.nbnv.ui.dialog.requestCloseDialog();
            close._render();
            $('.closelink').click(function() { close.show($(this).attr("request"), jsonCache[$(this).attr("request")]); return false; });
            grant = new nbn.nbnv.ui.dialog.requestGrantDialog();
            grant._render();
            $('.grantlink').click(function() { grant.show($(this).attr("request"), jsonCache[$(this).attr("request")], $(this).attr("dataset"), '/taxonObservations/datasets/' + $(this).attr("dataset") + '/requestable'); return false; });
            deny = new nbn.nbnv.ui.dialog.requestDenyDialog();
            deny._render();
            $('.denylink').click(function() { deny.show($(this).attr("request"), jsonCache[$(this).attr("request")], $(this).attr("dataset"), '/taxonObservations/datasets/' + $(this).attr("dataset") + '/requestable'); return false; });
            revoke = new nbn.nbnv.ui.dialog.requestRevokeDialog();
            revoke._render();
            $('.revokelink').click(function() { revoke.show($(this).attr("request"), jsonCache[$(this).attr("request")]); return false;});

            $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
            $('.presults').dataTable({
                "aaSorting": [[4, "desc"]],
                "bAutoWidth": true,
                "bFilter": false,
                "bJQueryUI": true,
                "iDisplayLength": 25,
                "bSortClasses": false,
                "sPaginationType": "full_numbers",
                "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]],
                "aoColumns": [
                    {"sWidth": "15%"}, 
                    {"sWidth": "20%"}, 
                    {"sWidth": "21%"}, 
                    {"sWidth": "25%"}, 
                    {"sWidth": "7%"}, 
                    {"sWidth": "5%"}
                ]
            });

            $('.results').dataTable({
                "aaSorting": [[5, "desc"]],
                "bAutoWidth": true,
                "bFilter": false,
                "bJQueryUI": true,
                "iDisplayLength": 25,
                "bSortClasses": false,
                "sPaginationType": "full_numbers",
                "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]],
                "aoColumns": [
                    {"sWidth":"11%"},
                    {"sWidth":"15%"},
                    {"sWidth":"15%"},
                    {"sWidth":"7%", "iDataSort": 9},
                    {"sWidth":"15%"},
                    {"sWidth":"7%"},
                    {"sWidth":"15%"},
                    {"sWidth":"7%"},
                    {"sWidth":"5%"},
                    {"bVisible" : false }                    
                ]
            });

            $('.dresults').dataTable({
                "aaSorting": [[4, "desc"]],
                "bAutoWidth": true,
                "bFilter": false,
                "bJQueryUI": true,
                "iDisplayLength": 25,
                "bSortClasses": false,
                "sPaginationType": "full_numbers",
                "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]],
                "aoColumns": [
                    {"sWidth": "15%"}, 
                    {"sWidth": "18%"}, 
                    {"sWidth": "15%"}, 
                    {"sWidth": "15%"}, 
                    {"sWidth": "7%"}, 
                    {"sWidth": "15%"},
                    {"sWidth": "7%"}
                ]
            });
        });
    </script>
    <h1>Manage Access Requests</h1>
    <p>Pending and granted access requests to your datasets are displayed in the tables below. Follow the links to manage these requests.</p>
    <h1>Updating Public Access</h1>
    <p>The current level of public access to your dataset is displayed in the Access and Constraints tab of the dataset metadata. Public access can be changed by reloading the dataset onto the NBN Gateway. Please contact access@nbn.org.uk who will help with this. If you require an urgent change to the level of public access then public access to the dataset can be removed whilst the dataset is reloaded at the new level of public access.</p>
    <h1>Pending Access Requests</h1>
    <table class="presults">
        <thead>
            <tr>
                <th>User</th>
                <th>Dataset</th>
                <th>Data Request</th>
                <th>Request Reason</th>
                <th>Request Date</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <#list oUserRequests as r>
            <tr>
                <td>
                    <a class="nbn-request-username" title="View User Information" href="" data-email="${r.user.email}" data-id="${r.user.id?c}">${r.user.forename} ${r.user.surname}</a>
                </td>
                <td>
                    <a href=${r.dataset.href}>${r.dataset.title}</a>
                </td>
                <td>
                    ${r.filter.filterText}
                </td>
                <td>
                    ${r.requestReason}
                </td>
                <td>
                    ${r.requestDate}
                </td>
                <td>
                    <a class="grantlink" href="#" request="${r.filter.id?c}" dataset="${r.dataset.key}">Grant</a>
                    <a class="denylink" href="#" request="${r.filter.id?c}" dataset="${r.dataset.key}">Deny</a>
                    <a href="/AccessRequest/Edit/User/${r.filter.id?c}">Edit</a>
                    <a class="closelink" href="#" request="${r.filter.id?c}">Close</a>
                </td>
            </tr>
            </#list>
            <#list oOrgRequests as r>
            <tr>
                <td>
                    <a class="nbn-request-orgname" data-id="${r.organisation.id?c}" title="View Organisation Information" href="">${r.organisation.name}</a>
                </td>
                <td>
                    <a href=${r.dataset.href}>${r.dataset.title}</a>
                </td>
                <td>
                    ${r.filter.filterText}
                </td>
                <td>
                    ${r.requestReason}
                </td>
                <td>
                    ${r.requestDate}
                </td>
                <td>
                    <a class="grantlink" href="#" request="${r.filter.id?c}" dataset="${r.dataset.key}">Grant</a>
                    <a class="denylink" href="#" request="${r.filter.id?c}" dataset="${r.dataset.key}">Deny</a>
                    <a href="/AccessRequest/Edit/Organisation/${r.filter.id?c}">Edit</a>
                    <a class="closelink" href="#" request="${r.filter.id?c}">Close</a>
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
    <h1>Granted Access Requests</h1>
    <table class="results">
        <thead>
            <tr>
                <th style="width: 15%">User</th>
                <th>Dataset</th>
                <th>Data Request</th>
                <th>Request Expires</th>
                <th>Request Reason</th>
                <th>Request Date</th>
                <th>Response Reason</th>
                <th>Response Date</th>
                <th>Action</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <#list gUserRequests as r>
            <tr>
                <td>
                    <a class="nbn-request-username" title="View User Information" href="" data-email="${r.user.email}" data-id="${r.user.id?c}">${r.user.forename} ${r.user.surname}</a>
                </td>
                <td>
                    <a href=${r.dataset.href}>${r.dataset.title}</a>
                </td>
                <td>
                    ${r.filter.filterText}
                </td>
                <td>
                    <#if r.accessExpires??>${r.accessExpires}<#else>Indefinitely</#if>
                </td>
                <td>
                    ${r.requestReason}
                </td>
                <td>
                    ${r.requestDate}
                </td>
                <td>
                    ${r.responseReason}
                </td>
                <td>
                    ${r.responseDate}
                </td>
                <td>
                    <a class="revokelink" href="#" request="${r.filter.id?c}">Revoke</a>
                </td>
                <td><#if r.accessExpires??>${r.accessExpires}</#if></td>
            </tr>
            </#list>
            <#list gOrgRequests as r>
            <tr>
                <td>
                    <a class="nbn-request-orgname" data-id="${r.organisation.id?c}" title="View Organisation Information" href="">${r.organisation.name}</a>
                </td>
                <td>
                    <a href=${r.dataset.href}>${r.dataset.title}</a>
                </td>
                <td>
                    ${r.filter.filterText}
                </td>
                <td>
                    <#if r.accessExpires??>${r.accessExpires}<#else>Indefinitely</#if>
                </td>
                <td>
                    ${r.requestReason}
                </td>
                <td>
                    ${r.requestDate}
                </td>
                <td>
                    ${r.responseReason}
                </td>
                <td>
                    ${r.responseDate}
                </td>
                <td>
                    <a class="revokelink" href="#" request="${r.filter.id?c}">Revoke</a>
                </td>
                <td><#if r.accessExpires??>${r.accessExpires}</#if></td>
            </tr>
            </#list>
        </tbody>
    </table>
    <h1>Denied Access Requests</h1>
    <table class="dresults">
        <thead>
            <tr>
                <th style="width: 15%">User</th>
                <th>Dataset</th>
                <th>Data Request</th>
                <th>Request Reason</th>
                <th>Request Date</th>
                <th>Response Reason</th>
                <th>Response Date</th>
            </tr>
        </thead>
        <tbody>
            <#list dUserRequests as r>
            <tr>
                <td>
                    <a class="nbn-request-username" title="View User Information" href="" data-email="${r.user.email}" data-id="${r.user.id?c}">${r.user.forename} ${r.user.surname}</a>
                </td>
                <td>
                    <a href=${r.dataset.href}>${r.dataset.title}</a>
                </td>
                <td>
                    ${r.filter.filterText}
                </td>
                <td>
                    ${r.requestReason}
                </td>
                <td>
                    ${r.requestDate}
                </td>
                <td>
                    ${r.responseReason}
                </td>
                <td>
                    ${r.responseDate}
                </td>
            </tr>
            </#list>
            <#list dOrgRequests as r>
            <tr>
                <td>
                    <a class="nbn-request-orgname" data-id="${r.organisation.id?c}" title="View Organisation Information" href="">${r.organisation.name}</a>
                </td>
                <td>
                    <a href=${r.dataset.href}>${r.dataset.title}</a>
                </td>
                <td>
                    ${r.filter.filterText}
                </td>
                <td>
                    ${r.requestReason}
                </td>
                <td>
                    ${r.requestDate}
                </td>
                <td>
                    ${r.responseReason}
                </td>
                <td>
                    ${r.responseDate}
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
    <div id="nbn-further-info-user" title="User Information" style="display:none">
        <table class="nbn-dataset-table">
            <tr>
                <td>User Name:</td>
                <td id="nbn-further-info-user-name"></td>
            </tr>
            <tr>
                <td>Email:</td>
                <td id="nbn-further-info-user-email"></td>
            </tr>
            <tr><td></td><td></td></tr>
            <tr>
                <td id="nbn-further-info-user-org-desc">Organisation Memberships:</td>
                <td id="nbn-further-info-user-orgs"></td>
            </tr>
        </table>        
    </div>
    <div id="nbn-further-info-org" title="Organisation Information" style="display:none">
    </div>
</@template.master>

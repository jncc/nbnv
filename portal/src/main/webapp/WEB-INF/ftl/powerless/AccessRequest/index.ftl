<#assign gUserRequests=json.readURL("${api}/user/userAccesses/requests/granted") />
<#assign gOrgRequests=json.readURL("${api}/organisation/organisationAccesses/requests/granted") />
<#assign pUserRequests=json.readURL("${api}/user/userAccesses/requests/pending") />
<#assign pOrgRequests=json.readURL("${api}/organisation/organisationAccesses/requests/pending") />

<@template.master title="Requests for Datasets"
    javascripts=["/js/jquery.dataTables.min.js"]
    csss=[]>

    <script>
        $(function(){
            $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
            $('#pending').dataTable({
                "aaSorting": [[5, "desc"]],
                "bAutoWidth": true,
                "bFilter": false,
                "bJQueryUI": true,
                "iDisplayLength": 25,
                "bSortClasses": false,
                "sPaginationType": "full_numbers",
                "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]],
                "aoColumns": [
                    { "sWidth": "20%" },
                    { "sWidth": "22%" },
                    { "sWidth": "22%" },
                    { "sWidth": "7%", "iDataSort": 6 },
                    { "sWidth": "22%" },
                    { "sWidth": "7%" },
                    { "bVisible" : false }
                ]
            });
            $('#granted').dataTable({
                "aaSorting": [[5, "desc"]],
                "bAutoWidth": true,
                "bFilter": false,
                "bJQueryUI": true,
                "iDisplayLength": 25,
                "bSortClasses": false,
                "sPaginationType": "full_numbers",
                "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]],
                "aoColumns": [
                    { "sWidth": "15%" },
                    { "sWidth": "16%" },
                    { "sWidth": "16%" },
                    { "sWidth": "7%", "iDataSort": 8 },
                    { "sWidth": "16%" },
                    { "sWidth": "7%" },
                    { "sWidth": "16%" },
                    { "sWidth": "7%" },
                    { "bVisible" : false }
                ]
            });
        });
    </script>
    <h1>Your Access Requests</h1>
    <p>Your pending and granted enhanced access requests are displayed in the tables below.</p>
    <h1>Pending Access Requests</h1>
    <table id="pending" class="results">
        <thead>
            <tr>
                <th style="width: 15%">User</th>
                <th>Dataset</th>
                <th>Data Request</th>
                <th>Request Expires</th>
                <th>Request Reason</th>
                <th>Request Date</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <#list pUserRequests as r>
            <tr>
                <td>
                    ${r.user.forename} ${r.user.surname}
                </td>
                <td>
                    ${r.dataset.title}
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
                <td><#if r.accessExpires??>${r.accessExpires}</#if></td>
            </tr>
            </#list>
            <#list pOrgRequests as r>
            <tr>
                <td>
                    ${r.organisation.name}
                </td>
                <td>
                    ${r.dataset.title}
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
                <td><#if r.accessExpires??>${r.accessExpires}</#if></td>
            </tr>
            </#list>
        </tbody>
    </table>
    <h1>Granted Access Requests</h1>
    <table id="granted" class="results">
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
                <th></th>
            </tr>
        </thead>
        <tbody>
            <#list gUserRequests as r>
            <tr>
                <td>
                    ${r.user.forename} ${r.user.surname}
                </td>
                <td>
                    ${r.dataset.title}
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
                <td><#if r.accessExpires??>${r.accessExpires}</#if></td>
            </tr>
            </#list>
            <#list gOrgRequests as r>
            <tr>
                <td>
                    ${r.organisation.name}
                </td>
                <td>
                    ${r.dataset.title}
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
                <td><#if r.accessExpires??>${r.accessExpires}</#if></td>
            </tr>
            </#list>
        </tbody>
    </table>
</@template.master>

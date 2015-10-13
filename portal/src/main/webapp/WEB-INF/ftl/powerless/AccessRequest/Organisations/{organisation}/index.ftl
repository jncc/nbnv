<#assign org="${URLParameters.organisation}" />
<#assign gOrgRequests=json.readURL("${api}/organisation/organisationAccesses/${org}/requests/granted") />
<#assign pOrgRequests=json.readURL("${api}/organisation/organisationAccesses/${org}/requests/pending") />

<@template.master title="Requests for Datasets"
    javascripts=["/js/jquery.dataTables.min.js"]
    csss=[]>

    <script>
        $(function(){
            $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
            $('.results').dataTable({
                "aaSorting": [[4, "desc"]],
                "bAutoWidth": true,
                "bFilter": false,
                "bJQueryUI": true,
                "iDisplayLength": 25,
                "bSortClasses": false,
                "sPaginationType": "full_numbers",
                "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]]
            });
        });
    </script>
    <h1>Pending Access Requests</h1>
    <table class="results">
        <thead>
            <tr>
                <th style="width: 15%">User</th>
                <th>Dataset</th>
                <th>Data Request</th>
                <th>Request Reason</th>
                <th>Request Date</th>
            </tr>
        </thead>
        <tbody>
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
                    ${r.requestReason}
                </td>
                <td>
                    ${r.requestDate}
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
                <th>Request Reason</th>
                <th>Request Date</th>
                <th>Response Reason</th>
                <th>Response Date</th>
            </tr>
        </thead>
        <tbody>
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
</@template.master>

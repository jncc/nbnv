<#assign userRequests=json.readURL("${api}/user/userAccesses/requests/admin") />

<@template.master title="Requests for Datasets"
    javascripts=["/js/jquery.dataTables.min.js","/js/jquery-ui-1.8.23.custom.min.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/jquery.qtip.min.css"]>

    <script>
        $(function(){
            $('#requesttable').dataTable({
                "iDisplayLength": 5,
                "bJQueryUI": true,
                "bProcessing": true,
                "sPaginationType": "full_numbers"
            });
        });
    </script>
    <h1>Pending Access Requests</h1>
    <table id="requesttable" class="results">
        <thead>
            <tr>
                <th>User</th>
                <th>Dataset</th>
                <th>Data Request</th>
                <th>Request Reason</th>
                <th>Request Date</th>
            </tr>
        </thead>
        <tbody>
            <#list userRequests as r>
            <tr>
                <td>
                    ${r.userID}
                </td>
                <td>
                    ${r.datasetKey}
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
</@template.master>

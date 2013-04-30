<#assign userRequests=json.readURL("${api}/user/userAccesses/requests/admin") />

<@template.master title="Requests for Datasets"
    javascripts=["/js/jquery.dataTables.min.js"]
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"]>

    <script>
        $(function(){
            $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
            $('#requesttable').dataTable({
                "aaSorting": [[4, "desc"]],
                "bAutoWidth": true,
                "bFilter": false,
                "bJQueryUI": true,
                "iDisplayLength": 25,
                "bSortClasses": false,
                "sPaginationType": "full_numbers",
                "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]],
            });
        });
    </script>
    <h1>Pending Access Requests</h1>
    <table id="requesttable" class="nbn-dataset-table">
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
                    ${r.user.forename} ${r.user.surname}
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
</@template.master>

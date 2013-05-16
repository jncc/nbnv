<#assign oUserRequests=json.readURL("${api}/user/userAccesses/requests/admin/pending") />
<#assign gUserRequests=json.readURL("${api}/user/userAccesses/requests/admin/granted") />
<#assign dUserRequests=json.readURL("${api}/user/userAccesses/requests/admin/denied") />

<@template.master title="Requests for Datasets"
    javascripts=["/js/jquery.dataTables.min.js"]
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"]>

    <script>
        $(function(){
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
                "aoColumns": [{"sWidth": "15%"}, {"sWidth": "20%"}, {"sWidth": "25%"}, {"sWidth": "28%"}, {"sWidth": "7%"}, {"sWidth": "5%"}]
            });

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
                <td>
                    Grant
                    Deny
                    Edit
                    Close
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
    <h1>Denied Access Requests</h1>
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
            <#list dUserRequests as r>
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

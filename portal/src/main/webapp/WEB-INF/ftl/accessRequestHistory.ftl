<#assign oHistory=json.readURL("${api}/organisation/organisationAccesses/requests/history/${model}") />
<#assign uHistory=json.readURL("${api}/user/userAccesses/requests/history/${model}") />

<@template.master title="Requests for Datasets"
    javascripts=["/js/jquery.dataTables.min.js"]
    csss=["//ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"]>

    <script>
        //nbn.nbnv.api = '${api}';

        $(function(){
            $.fn.dataTableExt.oJUIClasses.sStripeOdd = 'ui-state-highlight';
            $('.presults').dataTable({
                "aaSorting": [[1, "desc"]],
                "bAutoWidth": true,
                "bFilter": false,
                "bJQueryUI": true,
                "iDisplayLength": 50,
                "bSortClasses": false,
                "sPaginationType": "full_numbers",
                "aLengthMenu": [[10,25,50,100,-1],[10,25,50,100,"All"]],
                "aoColumns": [{"sWidth": "15%"}, {"sWidth": "20%"}, {"sWidth": "28%"}, {"sWidth": "7%"}, {"sWidth": "30%"}]
            });
        });
    </script>
    <h1>Access Request History</h1>
    <table class="presults">
        <thead>
            <tr>
                <th>User</th>
                <th>Date</th>
                <th>Action</th>
                <th>Data Request ID</th>
                <th>Data Request</th>
            </tr>
        </thead>
        <tbody>
            <#list uHistory as r>
            <tr>
                <td>
                    ${r.actioner.forename} ${r.actioner.surname}
                </td>
                <td>
                    ${r.timestamp}
                </td>
                <td>
                    ${r.action}
                </td>
                <td>
                    ${r.request.filter.id?c}
                </td>
                <td>
                    ${r.request.filter.filterText}
                </td>
            </tr>
            </#list>
            <#list oHistory as r>
            <tr>
                <td>
                    ${r.actioner.forename} ${r.actioner.surname}
                </td>
                <td>
                    ${r.timestamp}
                </td>
                <td>
                    ${r.action}
                </td>
                <td>
                    ${r.request.filter.id?c}
                </td>
                <td>
                    ${r.request.filter.filterText}
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
</@template.master>
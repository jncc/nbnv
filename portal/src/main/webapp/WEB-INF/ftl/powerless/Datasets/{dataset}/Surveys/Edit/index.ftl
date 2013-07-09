<#assign datasetId="${URLParameters.dataset}">
<#assign surveys=json.readURL("${api}/taxonDatasets/${datasetId}/surveys")>
<#assign isAdmin=json.readURL("${api}/datasets/${datasetId}/isAdmin")>

<@template.master title="Edit Surveys"
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

    <h1>Edit Surveys</h1>
    <div>
        <table class="sTable">
            <thead>
                <tr>
                    <th>Survey</th>
                    <th>Key</th>
                    <th>Description</th>
                    <#if isAdmin><th>Action</th></#if>
                </tr>
            </thead>
            <tbody>
            <#list surveys as r>
                <tr>
                    <td>${r.title!'No title'}</td>
                    <td>${r.providerKey}</td>
                    <td>${r.description!'No description'}</td>
                    <#if isAdmin><td><a href="${r.id?c}/Edit">Alter metadata</a></td></#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</@template.master>
<#assign datasetId="${URLParameters.dataset}">
<#assign attributes=json.readURL("${api}/taxonDatasets/${datasetId}/attributes")>
<#assign isAdmin=json.readURL("${api}/datasets/${datasetId}/isAdmin")>

<@template.master title="Edit Attributes"
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

    <h1>Edit Attributes</h1>
    <div>
        <table class="sTable">
            <thead>
                <tr>
                    <th>Attribute</th>
                    <th>Description</th>
                    <#if isAdmin><th>Action</th></#if>
                </tr>
            </thead>
            <tbody>
            <#list attributes as r>
                <tr>
                    <td>${r.label}</td>
                    <td>${r.description}</td>
                    <#if isAdmin><td><a href="${r.attributeID}">Edit metadata</a></td></#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</@template.master>
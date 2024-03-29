<#assign datasetId="${URLParameters.dataset}">
<#assign taxa=json.readURL("${api}/taxonDatasets/${datasetId}/taxa")>
<table id="nbn-species-datatable" class="nbn-dataset-table">
    <thead>
        <tr>
            <th>Species name</th>
            <th>Taxon version key</th>
            <th>Species group name</th>
            <th>Number of observations</th>
        </tr>
    </thead>
    <tbody>
        <#list taxa as taxon>
            <tr>
                <td><a class="nbn-datatable-anchor" href="/Taxa/${taxon.taxonVersionKey}">${taxon_utils.getLongName(taxon)}</a></td>
                <td>${taxon.taxonVersionKey}</td>
                <td>${taxon.taxonOutputGroupName}</td>
                <td>${taxon.observationCount?c}</td>
            </tr>
        </#list>
    </tbody>
</table>

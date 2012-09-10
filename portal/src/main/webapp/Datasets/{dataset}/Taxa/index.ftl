<#assign datasetId="${URLParameters.dataset}">
<#assign taxonDataset=json.readURL("${api}/taxonDatasets/${datasetId}")>
<#assign taxa=taxonDataset.taxa>
<table id="nbn-species-datatable">
    <thead>
        <tr>
            <th>Species name</th>
            <th>Taxon version key</th>
            <th>Species group name</th>
        </tr>
    </thead>
    <tbody>
        <#list taxa as taxon>
            <tr>
                <td><a href="#"><@taxon_utils.short_name taxon=taxon/></a></td>
                <td>${taxon.taxonVersionKey}</td>
                <td>${taxon.outputGroupName}</td>
            </tr>
        </#list>
    </tbody>
</table>

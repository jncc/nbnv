<#macro speciesListForDataset>

<#assign datasetId="${URLParameters.dataset}">
<#assign taxonDataset=json.readURL("${api}/taxonDatasets/${datasetId}")>
<#assign taxa=taxonDataset.taxa>

<table id="nbn-generic-datatable">
    <thead>
        <tr>
            <th>Taxon version key</th>
            <th>Species name</th>
        </tr>
    </thead>
    <tbody>
        <#list taxa as taxon>
            <tr>
                <td><a href="#">${taxon.taxonVersionKey}</a></td>
                <td><a href="#">${taxon.name}</a></td>
            </tr>
        </#list>
    </tbody>
</table>

</#macro>
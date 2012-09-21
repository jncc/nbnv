<#assign tenkmGridRef=URLParameters.tenkmGridRef>
<#assign records=json.readURL("${api}/taxonObservations?gridRef=${tenkmGridRef}",RequestParameters)>
<#assign metaDatasets=json.readURL("${api}/taxonObservations/datasets?gridRef=${tenkmGridRef}",RequestParameters)>

<@template.master title="10km report for ${tenkmGridRef}">
    <h1>10km Square ${tenkmGridRef}</h1>
    <table>
    <tr><th>Records</th></tr>
    <#list records as record>
        <tr><td>${record.observationID}</td><td>${record.taxonVersionKey}</td></tr>
    </#list>
    </table>
    
    <@dataset.dataset_table metaDatasets=metaDatasets/>

</@template.master>

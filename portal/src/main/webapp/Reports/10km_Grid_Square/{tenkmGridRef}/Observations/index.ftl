<#assign tenkmGridRef=URLParameters.tenkmGridRef>
<#assign records=json.readURL("${api}/taxonObservations?gridRef=${tenkmGridRef}",RequestParameters)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers?gridRef=${tenkmGridRef}",RequestParameters)>

<@template.master title="10km report for ${tenkmGridRef}">
    <h1>10km Square ${tenkmGridRef}</h1>
    <table class="nbn-coloured-table">
    <tr><th>Records</th></tr>
    <#list records as record>
        <tr><td>${record.observationID}</td><td>${record.taxonVersionKey}</td></tr>
    </#list>
    </table>
    
    <@dataset.dataset_table providersWithQueryStats=providersWithQueryStats/>

</@template.master>

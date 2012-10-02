<#assign tenkmGridRef=URLParameters.gridRef>
<#assign requestParametersExtended = RequestParameters + {"gridRef":[tenkmGridRef],"ptvk":[URLParameters.ptvk]}>
<#assign records=json.readURL("${api}/taxonObservations",requestParametersExtended)>
<#assign taxon=json.readURL("${api}/taxa/${URLParameters.ptvk}")>

<@template.master title="10km report for ${tenkmGridRef}">
    <h1>Records for <@taxon_utils.short_name taxon=taxon/> in ${tenkmGridRef}</h1>
    <table class="nbn-coloured-table">
    <tr>
        <th class="nbn-th-left">Site name</th>
        <th>Gridref</th>
        <th>Date accuracy</th>
        <th>Date type</th>
        <th>Sensitive</th>
        <th class="nbn-th-right">Absence</th>
    </tr>
    <#list records as record>
        <tr>
            <td class="nbn-td-left">${record.siteName!"N/A"}</td>
            <td>${record.gridRef}</td>
            <td>${record.startDate} to ${record.endDate}</td>
            <td>${record.dateType}</td>
            <td>${record.sensitive?string}</td>
            <td class="nbn-td-right">${record.absence?string}</td>
        </tr>
    </#list>
    </table>
    
</@template.master>

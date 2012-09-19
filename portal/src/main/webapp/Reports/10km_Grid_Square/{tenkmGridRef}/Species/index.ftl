<#assign taxa=json.readURL("${api}/taxonObservations/species?gridRef=${URLParameters.tenkmGridRef}")>

<@template.master title="10km report for ${URLParameters.tenkmGridRef}">
    <h1>10km Square ${URLParameters.tenkmGridRef}</h1>

    <table>
    <tr><th>Species</th></tr>
    <#list taxa as taxon>
        <tr><td>${taxon.name}</td></tr>
    </#list>
    </table>
</@template.master>

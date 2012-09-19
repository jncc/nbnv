<#assign taxonOutputGroups=json.readURL("${api}/taxonObservations/groups?gridRef=${URLParameters.tenkmGridRef}")>

<@template.master title="10km report for ${URLParameters.tenkmGridRef}">
    <h1>10km Square ${URLParameters.tenkmGridRef}</h1>

    <table>
    <tr><th>Taxon Group</th></tr>
    <#list taxonOutputGroups as taxonOutputGroup>
        <tr><td>${taxonOutputGroup.taxonGroupName}</td></tr>
    </#list>
    </table>
</@template.master>

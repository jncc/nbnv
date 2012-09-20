<#assign tenkmGridRef=URLParameters.tenkmGridRef>
<#assign taxonOutputGroups=json.readURL("${api}/taxonObservations/groups?gridRef=${tenkmGridRef}",RequestParameters)>

<@template.master title="10km report for tenkmGridRef">
    <h1>10km Square ${URLParameters.tenkmGridRef}</h1>

    <table>
    <tr><th>Taxon Group</th></tr>
    <#list taxonOutputGroups as taxonOutputGroup>
        <tr><td><a href="../${tenkmGridRef}/Species?taxonOutputGroup=${taxonOutputGroup.taxonGroupKey}">${taxonOutputGroup.taxonGroupName}</a></td></tr>
    </#list>
    </table>
</@template.master>

<#assign tenkmGridRef=URLParameters.tenkmGridRef>
<#assign taxonGroupKey=RequestParameters.taxonOutputGroup>
<#assign taxa=json.readURL("${api}/taxonObservations/species?gridRef=${tenkmGridRef}",RequestParameters)>

<@template.master title="10km report for ${tenkmGridRef}">
    <h1>10km Square ${tenkmGridRef}</h1>
    <table>
    <tr><th>Species</th></tr>
    <#list taxa as taxon>
        <tr><td><a href="../${tenkmGridRef}/Data?ptvk=${taxon.prefnameTaxonVersionKey}">${taxon.name}</a></td></tr>
    </#list>
    </table>
</@template.master>

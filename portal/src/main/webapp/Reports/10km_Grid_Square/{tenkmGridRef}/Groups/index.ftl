<#assign tenkmGridRef=URLParameters.tenkmGridRef>
<#assign taxonOutputGroups=json.readURL("${api}/taxonObservations/groups?gridRef=${tenkmGridRef}",RequestParameters)>
<#assign datasets=json.readURL("${api}/taxonObservations/datasets?gridRef=${tenkmGridRef}",RequestParameters)>

<@template.master title="10km report for tenkmGridRef">
    <h1>10km Square ${URLParameters.tenkmGridRef}</h1>

<h2>Groups</h2>
    <div>
        <table>
            <tr><th>Taxon Group</th></tr>
            <#list taxonOutputGroups as taxonOutputGroup>
                <tr><td><a href="../${tenkmGridRef}/Species?taxonOutputGroup=${taxonOutputGroup.taxonGroupKey}">${taxonOutputGroup.taxonGroupName}</a></td></tr>
            </#list>
        </table>
    </div>

<h2>Datasets</h2>
    <div>
        <table>
            <tr><th>Dataset</th></tr>
            <#list datasets as dataset>
                <tr><td><a href="/Datasets/${dataset.datasetKey}">${dataset.name}</a></td></tr>
            </#list>
        </table>
    </div>
</@template.master>

<#assign tenkmGridRef=URLParameters.tenkmGridRef>
<#assign metaTaxonOutputGroups=json.readURL("${api}/taxonObservations/groups?gridRef=${tenkmGridRef}",RequestParameters)>
<#assign metaDatasets=json.readURL("${api}/taxonObservations/datasets?gridRef=${tenkmGridRef}",RequestParameters)>

<@template.master title="10km report for tenkmGridRef">
    <h1>10km Square ${URLParameters.tenkmGridRef}</h1>
    <div>
        <table class="nbn-simple-table">
            <tr><th>Species groups</th></tr>
            <#list metaTaxonOutputGroups as metaTaxonOutputGroup>
                <tr><td><a href="../${tenkmGridRef}/Species?taxonOutputGroup=${metaTaxonOutputGroup.taxonGroupKey}">${metaTaxonOutputGroup.taxonOutputGroup.taxonGroupName}</a>(${metaTaxonOutputGroup.querySpecificSpeciesCount} species)</td></tr>
            </#list>
        </table>
    </div>
        
    <@dataset.dataset_table metaDatasets=metaDatasets/>

</@template.master>

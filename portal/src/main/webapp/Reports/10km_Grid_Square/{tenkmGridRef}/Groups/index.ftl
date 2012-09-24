<#assign tenkmGridRef=URLParameters.tenkmGridRef>
<#assign metaTaxonOutputGroups=json.readURL("${api}/taxonObservations/groups?gridRef=${tenkmGridRef}",RequestParameters)>
<#assign metaDatasets=json.readURL("${api}/taxonObservations/datasets?gridRef=${tenkmGridRef}",RequestParameters)>

<@template.master title="10km report for tenkmGridRef">
    <h1>10km Square ${URLParameters.tenkmGridRef}</h1>

    <div class="nbn-report-data-container">
        <table class="nbn-simple-table">
            <tr><th class="nbn-th-left nbn-th-right">Species groups for ${tenkmGridRef} (number of species)</th></tr>
            <#list metaTaxonOutputGroups as metaTaxonOutputGroup>
                <tr><td class="nbn-td-left nbn-td-right"><a href="../${tenkmGridRef}/Species?taxonOutputGroup=${metaTaxonOutputGroup.taxonGroupKey}">${metaTaxonOutputGroup.taxonOutputGroup.taxonGroupName}</a>(${metaTaxonOutputGroup.querySpecificSpeciesCount})</td></tr>
            </#list>
        </table>
    </div>
        
    <@dataset.dataset_table metaDatasets=metaDatasets/>

</@template.master>

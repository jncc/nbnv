<#assign tenkmGridRef=URLParameters.tenkmGridRef>
<#assign taxonOutputGroupsWithQueryStats=json.readURL("${api}/taxonObservations/groups?gridRef=${tenkmGridRef}",RequestParameters)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers?gridRef=${tenkmGridRef}",RequestParameters)>

<@template.master title="10km report for tenkmGridRef">
    <h1>10km Square ${URLParameters.tenkmGridRef}</h1>

    <div class="nbn-report-data-container">
        <table class="nbn-simple-table">
            <tr><th class="nbn-th-left nbn-th-right">Species groups for ${tenkmGridRef} (number of species)</th></tr>
            <#list taxonOutputGroupsWithQueryStats as taxonOutputGroupWithQueryStats>
                <tr><td class="nbn-td-left nbn-td-right"><a href="../${tenkmGridRef}/Species?taxonOutputGroup=${taxonOutputGroupWithQueryStats.taxonGroupKey}">${taxonOutputGroupWithQueryStats.taxonOutputGroup.taxonGroupName}</a>(${taxonOutputGroupWithQueryStats.querySpecificSpeciesCount})</td></tr>
            </#list>
        </table>
    </div>
        
    <@dataset.dataset_table providersWithQueryStats=providersWithQueryStats/>

</@template.master>

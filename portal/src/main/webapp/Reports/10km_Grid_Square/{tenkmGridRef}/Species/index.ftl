<#assign tenkmGridRef=URLParameters.tenkmGridRef>
<#assign taxonGroupKey=RequestParameters.taxonOutputGroup>
<#assign taxaWithQueryStats=json.readURL("${api}/taxonObservations/species?gridRef=${tenkmGridRef}",RequestParameters)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers?gridRef=${tenkmGridRef}",RequestParameters)>

<@template.master title="10km report for ${tenkmGridRef}">
    <h1>10km Square ${tenkmGridRef}</h1>

    <div class="nbn-report-data-container">
        <table class="nbn-simple-table">
        <tr><th class="nbn-th-left nbn-th-right">Species recorded in ${tenkmGridRef} (number of records)</th></tr>
        <#list taxaWithQueryStats as taxonWithQueryStats>
            <tr><td class="nbn-td-left nbn-td-right"><a href="../${tenkmGridRef}/Observations?ptvk=${taxonWithQueryStats.taxon.prefnameTaxonVersionKey}">${taxonWithQueryStats.taxon.name}</a> (${taxonWithQueryStats.querySpecificObservationCount})</td></tr>
        </#list>
        </table>
    </div>

    <@dataset.dataset_table providersWithQueryStats=providersWithQueryStats/>

</@template.master>

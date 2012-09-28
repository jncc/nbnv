<#assign tenkmGridRef=URLParameters.tenkmGridRef>
<#assign taxonGroupKey=RequestParameters.taxonOutputGroup>
<#assign taxaWithQueryStats=json.readURL("${api}/taxonObservations/species?gridRef=${tenkmGridRef}",RequestParameters)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers?gridRef=${tenkmGridRef}",RequestParameters)>

<@template.master title="10km report for ${tenkmGridRef}">
    <h1>Species with records for 10km Square ${tenkmGridRef}</h1>

    <@report_utils.site_report_filters/>

    <div class="nbn-report-data-container">
        <table class="nbn-coloured-table">
        <tr><th class="nbn-th-left nbn-th-right">Species recorded in ${tenkmGridRef} (number of records)</th></tr>
        <#list taxaWithQueryStats as taxonWithQueryStats>
            <tr><td class="nbn-td-left nbn-td-right"><a href="../${tenkmGridRef}/Observations?ptvk=${taxonWithQueryStats.taxon.prefnameTaxonVersionKey}">${taxonWithQueryStats.taxon.name}</a> (${taxonWithQueryStats.querySpecificObservationCount})</td></tr>
        </#list>
        </table>
    </div>

    <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats/>

</@template.master>

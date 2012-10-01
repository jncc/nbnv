<#assign tenkmGridRef=URLParameters.gridRef>
<#assign taxonOutputGroup=URLParameters.taxonOutputGroup>
<#assign requestParametersExtended = RequestParameters + {"gridRef":[tenkmGridRef],"taxonOutputGroup":[taxonOutputGroup]}>
<#assign taxaWithQueryStats=json.readURL("${api}/taxonObservations/species",requestParametersExtended)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>

<@template.master title="10km report for ${tenkmGridRef}">
    <h1>Species with records for 10km Square ${tenkmGridRef}</h1>

    <@report_utils.site_report_filters requestParameters=RequestParameters/>

    <div class="nbn-report-data-container">
        <table class="nbn-coloured-table">
        <tr><th class="nbn-th-left nbn-th-right">Species recorded in ${tenkmGridRef} (number of records)</th></tr>
        <#list taxaWithQueryStats as taxonWithQueryStats>
            <tr><td class="nbn-td-left nbn-td-right"><a href="/Reports/10km_Grid_Square/${tenkmGridRef}/Groups/${taxonOutputGroup}/Species/${taxonWithQueryStats.taxon.prefnameTaxonVersionKey}/Observations">${taxonWithQueryStats.taxon.name}</a> (${taxonWithQueryStats.querySpecificObservationCount})</td></tr>
        </#list>
        </table>
    </div>

    <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>

</@template.master>

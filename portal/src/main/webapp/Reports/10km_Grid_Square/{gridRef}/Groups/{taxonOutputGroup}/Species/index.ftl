<#assign tenkmGridRef=URLParameters.gridRef>
<#assign taxonOutputGroupKey=URLParameters.taxonOutputGroup>
<#assign requestParametersExtended = RequestParameters + {"gridRef":[tenkmGridRef],"taxonOutputGroup":[taxonOutputGroupKey]}>
<#assign taxaWithQueryStats=json.readURL("${api}/taxonObservations/species",requestParametersExtended)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign taxonOutputGroup=json.readURL("${api}/taxonOutputGroups/${taxonOutputGroupKey}")>

<@template.master title="10km report for ${tenkmGridRef}" javascripts=["/js/site-report-form-validation.js","/js/jquery.dataset-selector-utils.js"]>
    <h1>${taxonOutputGroup.taxonGroupName?cap_first} species with records for the 10km square ${tenkmGridRef} from <@report_utils.yearRangeText requestParameters=RequestParameters/></h1>

    <@report_utils.site_report_filters requestParameters=RequestParameters args={"taxonOutputGroup":taxonOutputGroup} location=tenkmGridRef isSiteBoundaryReport=false/>

    <div class="nbn-report-data-container">
        <table class="nbn-coloured-table">
        <tr><th class="nbn-th-left nbn-th-right">Species recorded in ${tenkmGridRef} (number of records)</th></tr>
        <#list taxaWithQueryStats as taxonWithQueryStats>
            <tr><td class="nbn-td-left nbn-td-right"><a href="/Reports/10km_Grid_Square/${tenkmGridRef}/Groups/${taxonOutputGroupKey}/Species/${taxonWithQueryStats.taxon.prefnameTaxonVersionKey}/Observations"><@taxon_utils.short_name taxon=taxonWithQueryStats.taxon/></a> (${taxonWithQueryStats.querySpecificObservationCount})</td></tr>
        </#list>
        </table>
    </div>

    <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>

</@template.master>

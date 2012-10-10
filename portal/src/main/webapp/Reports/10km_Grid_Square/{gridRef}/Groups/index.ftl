<#assign tenkmGridRef=URLParameters.gridRef>
<#assign requestParametersExtended = RequestParameters + {"gridRef":[tenkmGridRef]}>
<#assign taxonOutputGroupsWithQueryStats=json.readURL("${api}/taxonObservations/groups",requestParametersExtended)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>

<@template.master title="10km report for tenkmGridRef" javascripts=["/js/site-report-form-validation.js","/js/jquery.dataset-selector-utils.js"]>
    <h1>Species groups with records for the 10km square ${tenkmGridRef} from <@report_utils.yearRangeText requestParameters=RequestParameters/></h1>
    <form action="" method="post">
    <@report_utils.site_report_filters requestParameters=RequestParameters location=tenkmGridRef  isSiteBoundaryReport=false/>
    <div class="nbn-report-data-container">
        <table class="nbn-coloured-table">
            <tr><th class="nbn-th-left nbn-th-right">Species groups (number of species)</th></tr>
            <#list taxonOutputGroupsWithQueryStats as taxonOutputGroupWithQueryStats>
                <tr><td class="nbn-td-left nbn-td-right"><a href="/Reports/10km_Grid_Square/${tenkmGridRef}/Groups/${taxonOutputGroupWithQueryStats.taxonGroupKey}/Species${report_utils.getQueryParameterText(RequestParameters)}">${taxonOutputGroupWithQueryStats.taxonOutputGroup.taxonGroupName}</a>(${taxonOutputGroupWithQueryStats.querySpecificSpeciesCount})</td></tr>
            </#list>
        </table>
    </div>
    <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>
    </form>

</@template.master>

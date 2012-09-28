<#assign tenkmGridRef=URLParameters.tenkmGridRef>
<#assign taxonOutputGroupsWithQueryStats=json.readURL("${api}/taxonObservations/groups?gridRef=${tenkmGridRef}",RequestParameters)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers?gridRef=${tenkmGridRef}",RequestParameters)>

<@template.master title="10km report for tenkmGridRef" javascripts=["/js/update_filter_text.js"]>
    <h1>Species groups with records for 10km Square ${URLParameters.tenkmGridRef}</h1>

    <form action="" method="post">

    <@report_utils.site_report_filters requestParameters=RequestParameters/>

    <div class="nbn-report-data-container">

        <table class="nbn-coloured-table">
            <tr><th class="nbn-th-left nbn-th-right">Species groups (number of species)</th></tr>
            <#list taxonOutputGroupsWithQueryStats as taxonOutputGroupWithQueryStats>
                <tr><td class="nbn-td-left nbn-td-right"><a href="../${tenkmGridRef}/Species?taxonOutputGroup=${taxonOutputGroupWithQueryStats.taxonGroupKey}">${taxonOutputGroupWithQueryStats.taxonOutputGroup.taxonGroupName}</a>(${taxonOutputGroupWithQueryStats.querySpecificSpeciesCount})</td></tr>
            </#list>
        </table>
    </div>
        
    <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats/>

    </form>

</@template.master>

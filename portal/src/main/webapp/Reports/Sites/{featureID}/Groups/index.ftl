<#assign featureID=URLParameters.featureID>
<#assign requestParametersExtended = RequestParameters + {"featureID":[featureID]}>
<#assign taxonOutputGroupsWithQueryStats=json.readURL("${api}/taxonObservations/groups",requestParametersExtended)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign site=json.readURL("${api}/features/${featureID}")>

<@template.master title="Site report for..." javascripts=["/js/site-report-form-validation.js"]>
    <h1>Species groups with records for '${site.label}' from <@report_utils.yearRangeText requestParameters=RequestParameters/></h1>
    <form action="" method="post">

        <@report_utils.site_report_filters requestParameters=RequestParameters location=site.label/>

        <div class="nbn-report-data-container">

            <table class="nbn-coloured-table">
                <tr><th class="nbn-th-left nbn-th-right">Species groups (number of species)</th></tr>
                <#list taxonOutputGroupsWithQueryStats as taxonOutputGroupWithQueryStats>
                    <tr><td class="nbn-td-left nbn-td-right"><a href="/Reports/Sites/${featureID}/Groups/${taxonOutputGroupWithQueryStats.taxonGroupKey}/Species">${taxonOutputGroupWithQueryStats.taxonOutputGroup.taxonGroupName}</a>(${taxonOutputGroupWithQueryStats.querySpecificSpeciesCount})</td></tr>
                </#list>
            </table>
        </div>

        <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>

    </form>

</@template.master>

<#assign featureID=URLParameters.featureID>
<#assign requestParametersExtended = RequestParameters + {"featureID":[featureID]}>
<#assign taxonOutputGroupsWithQueryStats=json.readURL("${api}/taxonObservations/groups",requestParametersExtended)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign site=json.readURL("${api}/features/${featureID}")>
<#assign title="Species groups with records for '${site.label}' from ${report_utils.getYearRangeText(RequestParameters)}">
<#assign is10kmReport=(site.type="GridSquare")>

<@template.master title="NBN Site Report" javascripts=["/js/site-report-form-validation.js","/js/jquery.dataset-selector-utils.js"]>
    <h1>${title}</h1>
    <form action="" method="post" id="${report_utils.getSiteFormId()}">
        <@report_utils.site_report_filters requestParameters=RequestParameters location=site.label  isSpatialRelationshipNeeded=true/>
        <div class="nbn-report-data-container">
            <#if taxonOutputGroupsWithQueryStats?has_content>
                <table class="nbn-coloured-table">
                    <tr><th class="nbn-th-left nbn-th-right">Species groups (number of species)</th></tr>
                    <#list taxonOutputGroupsWithQueryStats as taxonOutputGroupWithQueryStats>
                        <tr><td class="nbn-td-left nbn-td-right"><a href="/Reports/Sites/${featureID}/Groups/${taxonOutputGroupWithQueryStats.taxonOutputGroupKey}/Species${report_utils.getQueryString(RequestParameters)}">${taxonOutputGroupWithQueryStats.taxonOutputGroup.name}</a>(${taxonOutputGroupWithQueryStats.querySpecificSpeciesCount})</td></tr>
                    </#list>
                </table>
            <#else>
                <@report_utils.noRecordsInfoBox/>
            </#if>
        </div>
        <@report_utils.siteImage locationName=site.label locationID=featureID imageURL=report_utils.getSiteBoundaryImageURL(featureID,!is10kmReport)/>
        <#if providersWithQueryStats?has_content>
            <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>
        </#if>
    </form>
</@template.master>

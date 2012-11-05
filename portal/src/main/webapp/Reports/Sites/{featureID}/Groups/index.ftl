<#assign featureID=URLParameters.featureID>
<#assign requestParametersExtended = RequestParameters + {"featureID":[featureID]}>
<#assign taxonOutputGroupsWithQueryStats=json.readURL("${api}/taxonObservations/groups",requestParametersExtended)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign site=json.readURL("${api}/features/${featureID}")>
<#assign title="Species groups with records for '${site.label}' from ${report_utils.getYearRangeText(RequestParameters)}">
<#assign is10kmReport=(site.type="GridSquare")>

<@template.master title="NBN Site Report" 
    csss=["/css/site-report.css"]
    javascripts=["/js/jquery.site_report_utils.js","/js/jquery.dataset-selector-utils.js"]>
    <h1>${title}</h1>
    <form action="/Reports/Sites/${featureID}/Groups" method="post" id="${report_utils.getSiteFormId()}">
        <@report_utils.site_report_filters requestParameters=RequestParameters location=site.label  isSpatialRelationshipNeeded=true/>
        <#if taxonOutputGroupsWithQueryStats?has_content>
            <div class="tabbed" id="nbn-site-report-data-container">
                <h3>Species groups (number of species)</h3>
                <ul>
                    <#list taxonOutputGroupsWithQueryStats as taxonOutputGroupWithQueryStats>
                        <li><a href="/Reports/Sites/${featureID}/Groups/${taxonOutputGroupWithQueryStats.taxonOutputGroupKey}/Species${report_utils.getQueryString(RequestParameters)}">${taxonOutputGroupWithQueryStats.taxonOutputGroup.name}</a>(${taxonOutputGroupWithQueryStats.querySpecificSpeciesCount})</li>
                    </#list>
                </ul>
            </div>
        <#else>
            <@report_utils.noRecordsInfoBox/>
        </#if>
        <@report_utils.siteImage locationName=site.label locationID=featureID imageURL=report_utils.getSiteBoundaryImageURL(featureID,!is10kmReport)/>
        <#if providersWithQueryStats?has_content>
            <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>
        </#if>
    </form>
</@template.master>

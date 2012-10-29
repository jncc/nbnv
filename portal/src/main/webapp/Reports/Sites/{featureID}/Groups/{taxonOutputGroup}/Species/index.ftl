<#assign featureID=URLParameters.featureID>
<#assign taxonOutputGroupKey=URLParameters.taxonOutputGroup>
<#assign requestParametersExtended = RequestParameters + {"featureID":[featureID],"taxonOutputGroup":[taxonOutputGroupKey]}>
<#assign taxaWithQueryStats=json.readURL("${api}/taxonObservations/species",requestParametersExtended)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign taxonOutputGroup=json.readURL("${api}/taxonOutputGroups/${taxonOutputGroupKey}")>
<#assign site=json.readURL("${api}/features/${featureID}")>
<#assign title=taxonOutputGroup.name?cap_first + " species with records for '${site.label}' from ${report_utils.getYearRangeText(RequestParameters)}">
<#assign is10kmReport=(site.type="GridSquare")>

<@template.master title="NBN Site Report" javascripts=["/js/site-report-form-validation.js","/js/jquery.dataset-selector-utils.js"]>
    <h1>${title}</h1>
    <form action="" method="post" id="${report_utils.getSiteFormId()}">
        <@report_utils.site_report_filters requestParameters=RequestParameters args={"taxonOutputGroup":taxonOutputGroup} location=site.label isSpatialRelationshipNeeded=true/>
        <div class="nbn-report-data-container">
            <#if taxaWithQueryStats?has_content>
                <div class="tabbed">
                    <h3>Species recorded (number of records)</h3>
                    <ul>
                        <#list taxaWithQueryStats as taxonWithQueryStats>
                            <li><a href="/Reports/Sites/${featureID}/Groups/${taxonOutputGroup.key}/Species/${taxonWithQueryStats.taxon.ptaxonVersionKey}/Observations${report_utils.getQueryString(RequestParameters)}">${taxon_utils.getShortName(taxonWithQueryStats.taxon)}</a> (${taxonWithQueryStats.querySpecificObservationCount})</li>
                        </#list>
                    </ul>
                </div>
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

<#assign featureID=URLParameters.featureID>
<#assign taxonOutputGroupKey=URLParameters.taxonOutputGroup>
<#assign requestParametersExtended = RequestParameters + {"featureID":[featureID],"taxonOutputGroup":[taxonOutputGroupKey]}>
<#--<#assign taxaWithQueryStats=json.readURL("${api}/taxonObservations/species",requestParametersExtended)>-->
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign taxonOutputGroup=json.readURL("${api}/taxonOutputGroups/${taxonOutputGroupKey}")>
<#assign site=json.readURL("${api}/features/${featureID}")>
<#assign title=taxonOutputGroup.name?cap_first + " species with records for '${site.label}'">
<#assign is10kmReport=(site.type="GridSquare")>

<@template.master title="NBN Site Report"
    csss=["/css/site-report.css"]
    javascripts=["/js/report_utils.js","/js/site_report_utils.js","/js/site_report_species.js","/js/jquery.dataset-selector-utils.js"]>

    <h1>${title}</h1>
    <form action="/Reports/Sites/${featureID}/Groups/${taxonOutputGroupKey}/Species" method="post" id="${report_utils.getSiteFormId()}" api-server="${api}" featureID="${featureID}" taxonOutputGroupKey="${taxonOutputGroupKey}">
        <@report_utils.site_report_filters requestParameters=RequestParameters args={"taxonOutputGroup":taxonOutputGroup} location=site.label isSpatialRelationshipNeeded=true/>
            <div class="tabbed" id="nbn-site-report-data-container"></div>
        <@report_utils.siteImage locationName=site.label locationID=featureID imageURL=report_utils.getSiteBoundaryImageURL(featureID,!is10kmReport)/>
        <#if providersWithQueryStats?has_content>
            <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>
        </#if>
    </form>
</@template.master>

<#assign featureID=URLParameters.featureID>
<#assign requestParametersExtended = RequestParameters + {"featureID":[featureID]}>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign site=json.readURL("${api}/features/${featureID}")>
<#assign title="Species groups with records for '${site.label}'">
<#assign is10kmReport=(site.type="GridSquare")>

<@template.master title="NBN Site Report" 
    csss=["/css/site-report.css"]
    javascripts=["/js/report_utils.js","/js/site_report_utils.js","/js/site_report_groups.js","/js/jquery.dataset-selector-utils.js"]>
    <h1>${title}</h1>
    <form id="nbn-site-report-form" api-server="${api}" featureID="${featureID}">
        <@report_utils.site_report_filters requestParameters=RequestParameters location=site.label  isSpatialRelationshipNeeded=true/>
        <div class="tabbed" id="nbn-site-report-data-container"></div>
        <@report_utils.siteImage locationName=site.label locationID=featureID imageURL=report_utils.getSiteBoundaryImageURL(featureID,!is10kmReport)/>
        <#if providersWithQueryStats?has_content>
            <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>
        </#if>
    </form>
</@template.master>

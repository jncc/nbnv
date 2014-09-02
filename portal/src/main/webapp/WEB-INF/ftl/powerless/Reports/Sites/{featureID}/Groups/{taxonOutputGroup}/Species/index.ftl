<#assign featureID=URLParameters.featureID>
<#assign taxonOutputGroupKey=URLParameters.taxonOutputGroup>
<#assign requestParametersExtended = RequestParameters + {"featureID":[featureID],"taxonOutputGroup":[taxonOutputGroupKey]}>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign unavailableDatasets=json.readURL("${api}/taxonObservations/unavailableDatasets",requestParametersExtended)>
<#assign taxonOutputGroup=json.readURL("${api}/taxonOutputGroups/${taxonOutputGroupKey}")>
<#assign site=json.readURL("${api}/features/${featureID}")>
<#assign title=taxonOutputGroup.name?cap_first + " species with records for '${site.label}'">
<#assign is10kmReport=(site.type="GridSquare")>

<@template.master title="NBN Site Report"
    csss=["/css/report.css","/css/site-report.css","/css/smoothness/jquery-ui-1.8.23.custom.css"]
    javascripts=["/js/report_utils.js","/js/site_report_species.js","/js/jquery.dataset-selector-utils.js","/js/jquery.dataTables.min.js"]>

    <h1>${title}</h1>
    <form id="nbn-site-report-form" api-server="${api}" featureID="${featureID}" taxonOutputGroupKey="${taxonOutputGroupKey}" gridSquare="${is10kmReport?string}">
        <@report_utils.site_report_filters requestParameters=RequestParameters args={"taxonOutputGroup":taxonOutputGroup} location=site.label isSpatialRelationshipNeeded=true isDownloadButtonNeeded=true isRequestBetterAccessLinkNeeded=true isVerificationNeeded=true/>
        <div class="tabbed" id="nbn-site-report-data-container"></div>
        <@report_utils.siteImage locationName=site.label locationID=featureID imageURL=report_utils.getSiteBoundaryImageURL(featureID,!is10kmReport)/>
        <@mdcontent.smallCaveat/>
        <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>
    </form>
    <br><br>
    <@report_utils.unavailable_datasets unavailableDatasets=unavailableDatasets/>
    <@report_utils.downloadTermsDialogue/>
</@template.master>

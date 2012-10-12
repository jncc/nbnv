<#assign featureID=URLParameters.featureID>
<#assign requestParametersExtended = RequestParameters + {"featureID":[featureID]}>
<#assign taxonOutputGroupsWithQueryStats=json.readURL("${api}/taxonObservations/groups",requestParametersExtended)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign site=json.readURL("${api}/features/${featureID}")>

<@template.master title="Site report for..." javascripts=["/js/site-report-form-validation.js","/js/jquery.dataset-selector-utils.js"]>
    <#assign title="Species groups with records for '${site.label}' from ${report_utils.getYearRangeText(RequestParameters)}">
    <@location_report_content.groups title=title locationName=site.label locationID=featureID is10kmReport=false taxonOutputGroupsWithQueryStats=taxonOutputGroupsWithQueryStats providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>
</@template.master>
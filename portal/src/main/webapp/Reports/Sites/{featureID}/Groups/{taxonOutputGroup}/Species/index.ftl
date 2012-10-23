<#assign featureID=URLParameters.featureID>
<#assign taxonOutputGroupKey=URLParameters.taxonOutputGroup>
<#assign requestParametersExtended = RequestParameters + {"featureID":[featureID],"taxonOutputGroup":[taxonOutputGroupKey]}>
<#assign taxaWithQueryStats=json.readURL("${api}/taxonObservations/species",requestParametersExtended)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign taxonOutputGroup=json.readURL("${api}/taxonOutputGroups/${taxonOutputGroupKey}")>
<#assign site=json.readURL("${api}/features/${featureID}")>

<@template.master title="NBN Site Report" javascripts=["/js/site-report-form-validation.js","/js/jquery.dataset-selector-utils.js"]>
    <#assign title=taxonOutputGroup.name?cap_first + " species with records for '${site.label}' from ${report_utils.getYearRangeText(RequestParameters)}">
    <@location_report_content.species title=title locationName=site.label locationID=featureID is10kmReport=false taxaWithQueryStats=taxaWithQueryStats providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters taxonOutputGroup=taxonOutputGroup/>
</@template.master>

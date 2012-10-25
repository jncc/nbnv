<#assign featureID=URLParameters.featureID>
<#assign requestParametersExtended = RequestParameters + {"featureID":[featureID],"ptvk":[URLParameters.ptvk]}>
<#assign datasets=json.readURL("${api}/taxonObservations/datasets/observations",requestParametersExtended)>
<#assign taxon=json.readURL("${api}/taxa/${URLParameters.ptvk}")>
<#assign site=json.readURL("${api}/features/${featureID}")>

<@template.master title="NBN Site Report" javascripts=["/js/site-report-form-validation.js"]>
    <#assign title="Records for ${taxon_utils.getShortName(taxon)} in '${site.label}' from ${report_utils.getYearRangeText(RequestParameters)}">
    <@location_report_content.observations title=title locationName=site.label locationID=featureID datasets=datasets requestParameters=RequestParameters taxon=taxon is10kmReport=false/>
</@template.master>

<#assign tenkmGridRef=URLParameters.gridRef>
<#assign requestParametersExtended = RequestParameters + {"featureID":[tenkmGridRef],"ptvk":[URLParameters.ptvk]}>
<#assign datasets=json.readURL("${api}/taxonObservations/datasets/observations",requestParametersExtended)>
<#assign taxon=json.readURL("${api}/taxa/${URLParameters.ptvk}")>

<@template.master title="10km report for ${tenkmGridRef}" javascripts=["/js/site-report-form-validation.js"]>
    <#assign title="Records for ${taxon_utils.getShortName(taxon)} in the 10km square ${tenkmGridRef} from ${report_utils.getYearRangeText(RequestParameters)}">
    <@location_report_content.observations title=title locationName=tenkmGridRef locationID=tenkmGridRef datasets=datasets requestParameters=RequestParameters taxon=taxon is10kmReport=true/>
</@template.master>

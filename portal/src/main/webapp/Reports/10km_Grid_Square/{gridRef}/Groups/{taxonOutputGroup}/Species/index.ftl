<#assign tenkmGridRef=URLParameters.gridRef>
<#assign taxonOutputGroupKey=URLParameters.taxonOutputGroup>
<#assign requestParametersExtended = RequestParameters + {"gridRef":[tenkmGridRef],"taxonOutputGroup":[taxonOutputGroupKey]}>
<#assign taxaWithQueryStats=json.readURL("${api}/taxonObservations/species",requestParametersExtended)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign taxonOutputGroup=json.readURL("${api}/taxonOutputGroups/${taxonOutputGroupKey}")>

<@template.master title="10km report for ${tenkmGridRef}" javascripts=["/js/site-report-form-validation.js","/js/jquery.dataset-selector-utils.js"]>
<#assign title=taxonOutputGroup.name?cap_first + " species with records for the 10km square ${tenkmGridRef} from ${report_utils.getYearRangeText(RequestParameters)}">
<@location_report_content.species title=title locationName=tenkmGridRef locationID=tenkmGridRef is10kmReport=true taxaWithQueryStats=taxaWithQueryStats providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters taxonOutputGroup=taxonOutputGroup/>
</@template.master>

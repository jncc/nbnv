<#assign tenkmGridRef=URLParameters.gridRef>
<#assign requestParametersExtended = RequestParameters + {"gridRef":[tenkmGridRef]}>
<#assign taxonOutputGroupsWithQueryStats=json.readURL("${api}/taxonObservations/groups",requestParametersExtended)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>


<@template.master title="10km report for tenkmGridRef" javascripts=["/js/site-report-form-validation.js","/js/jquery.dataset-selector-utils.js"]>
    <#assign title="Species groups with records for the 10km square ${tenkmGridRef} from ${report_utils.yearRangeText(RequestParameters)}">
    <@location_report_content.groups title=title locationName=tenkmGridRef locationID=tenkmGridRef is10kmReport=true taxonOutputGroupsWithQueryStats=taxonOutputGroupsWithQueryStats providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>
</@template.master>

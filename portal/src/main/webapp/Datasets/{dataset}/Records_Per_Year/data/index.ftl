<#assign datasetId="${URLParameters.dataset}">
<#assign recordsPerYears=json.readURL("${api}/taxonDatasets/${datasetId}/recordsPerYear")>
<#assign first=true>
<#assign seperator=''>
[[
<#list recordsPerYears as recordsPerYear>
${seperator}[${recordsPerYear.year},${recordsPerYear.recordCount}]
<#if first=true>
<#assign seperator=','>
</#if>
</#list>
]]

<#assign datasetId="${URLParameters.dataset}">
<#assign recordsPerYears=json.readURL("${api}/taxonDatasets/${datasetId}/recordsPerYear")>
<div id="nbn-record-chart" style="height:400px;width:100%;"/>
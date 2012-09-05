<#assign datasetId="${URLParameters.dataset}">
<#assign dataset=json.readURL("${api}/datasets/${datasetId}")>
<#assign recordsPerYears=json.readURL("${api}/taxonDatasets/${datasetId}/recordsPerYear")>
<#assign recordsPerDateTypes=json.readURL("${api}/taxonDatasets/${datasetId}/recordsPerDateType")>
<div id="nbn-temporal-chart" datasetkey=${datasetId} "/>
<div id="nbn-chart-tooltip">Tip: zoom to data by drawing a rectangle on the chart (hold down mouse button and drag), double click to reset.</div>
<table class="nbn-dataset-table nbn-simple-table">
    <tr>
        <th>Data provider's comments</th>
        <td colspan=2>${dataset.temporalCoverage}</td>
    </tr>
    <tr>
        <th>Date range of all records</th>
        <td colspan=2><span id='nbn-dataset-startyear'/> - <span id='nbn-dataset-endyear'/></td>
    </tr>
    <tr>
        <th>&nbsp;</th>
        <td colspan=2>&nbsp;</td>
    </tr>
    <tr>
        <th rowspan=${recordsPerDateTypes?size + 1}>Summary of temporal precision</th>
        <th>Temporal precision</th>
        <th>Number of records</th>
    </tr>
    <#list recordsPerDateTypes as recordsPerDateType>
        <tr>
            <td>${recordsPerDateType.dateTypeName}</td>
            <td>${recordsPerDateType.recordCount}</td>
        </tr>
    </#list>
</table>


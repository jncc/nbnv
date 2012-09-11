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

<ul class="collapsible-list nbn-temporal">
    <li>
        <h1>Show/hide table of records per year</h1>
        <ul>
            <li>
                <#assign numRows=39>
                <#assign isNewColum=false>
                <table class="nbn-temporal-table">
                    <tr>
                        <#list recordsPerYears as recordsPerYear>
                            <#if (recordsPerYear_index % numRows) == 0>
                                <td>
                                    <table class="nbn-temporal-table">
                                        <tr>
                                            <th class="ui-state-highlight">Year</th>
                                            <th class="ui-state-highlight">Records</th>
                                        </tr>
                            </#if>
                                        <tr>
                                            <td class="ui-state-highlight">${recordsPerYear.year}</td>
                                            <td class="ui-state-highlight">${recordsPerYear.recordCount}</td>
                                        </tr>
                            <#if (recordsPerYear_index + 1) % numRows == 0>
                                    </table>
                                </td>
                            </#if>
                        </#list>
                    </tr>
                </table>
            </li>
        </ul>
    </li>
</ul>

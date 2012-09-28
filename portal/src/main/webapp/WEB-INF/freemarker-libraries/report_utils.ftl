<#macro dataset_table providersWithQueryStats requestParameters>
    <table class="nbn-coloured-table">
        <tr><th class="nbn-th-left nbn-th-right" colspan="3">Data providers and their datasets that contribute to this page (number of records)</th></tr>
        <#list providersWithQueryStats as providerWithQueryStats>
            <tr class="nbn-emphasise-row">
                <td class="nbn-td-left"><img src="${api}/organisations/${providerWithQueryStats.organisationID}/logo" class="nbn-provider-table-logo"></td>
                <td class="nbn-td-right" colspan="2"><a href="/Provider/${providerWithQueryStats.organisationID}">${providerWithQueryStats.organisation.name}</a> (${providerWithQueryStats.querySpecificObservationCount})</td>
            </tr>
            <#assign datasetsWithQueryStats=providerWithQueryStats.datasetsWithQueryStats>
            <#list datasetsWithQueryStats as datasetWithQueryStats>
                <tr class="nbn-de-emphasise-row">
                    <#assign checked = requestParameters.datasetKey?seq_contains(datasetWithQueryStats.datasetKey)?string("checked","")>
                    <td class="nbn-td-left" ><input type="checkbox" name="datasetKey" value="${datasetWithQueryStats.datasetKey}" ${checked}></td>
                    <td><a href="/Datasets/${datasetWithQueryStats.datasetKey}">${datasetWithQueryStats.dataset.name}</a> (${datasetWithQueryStats.querySpecificObservationCount})</td>
                    <td  class="nbn-td-right" >Access to this dataset Fusce in leo massa, nec ullamcorper dui. Aliquam auctor iaculis sapien, et scelerisque mi iaculis in. Donec nibh libero, aliquet vitae cursus in, mattis vel augue. Nulla facilisi. Aenean porttitor.</td>
                </tr>
            </#list>
        </#list>
    </table>
</#macro>

<#macro site_report_filters requestParameters>
    <#assign startYear=requestParameters.startYear?has_content?string(requestParameters.startYear[0]!"","1600")>
    <#assign endYear=requestParameters.endYear?has_content?string(requestParameters.endYear[0]!"",.now?string("yyyy"))>
    <#assign designations=json.readURL("${api}/designations")>
    <div class="nbn-report-data-container">
        <table class="nbn-coloured-table">
            <tr>
                <th class="nbn-th-left nbn-th-right" colspan="2">
                    Current filter options
                </th>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-filter-name">Designation:</td><td class="nbn-td-right">None</td>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-filter-name">Year range:</td><td class="nbn-td-right">1600 to 2012</td>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-filter-name">Datasets:</td><td class="nbn-td-right">all available</td>
            </tr>
            <tr>
                <th class="nbn-td-left nbn-td-right" colspan="2">
                    Change your filter options
                </th>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-filter-name">Designation: </td>
                <td class="nbn-td-right">
                    <select name="designation" id="designation" class="nbn-designation-dropdown">
                        <option selected="selected" value="">None selected</option>
                        <#list designations as designation>
                            <#assign selected = requestParameters.designation?seq_contains(designation.code)?string("selected","")>
                            <option value="${designation.code}" ${selected}>${designation.name}</option>
                        </#list>
                    </select><br/>
                </td>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-filter-name">Year range:</td>
                <td class="nbn-td-right"><input name="startYear" id="startYear" type="textbox" value="${startYear}" class="nbn-year-input"/>
                                         to <input name="endYear" id="endYear" type="textbox" value="${endYear}" class="nbn-year-input"/></td>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-filter-name">Datasets:</td><td class="nbn-td-right">Use dataset table below</td>
            </tr>
            <tr>
                <td class="nbn-td-left"></td>
                <td class="nbn-td-right"><input type="submit"/></td>
            </tr>
        </table>
    </div>
</#macro>

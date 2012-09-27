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
                    <td class="nbn-td-left" ><input type="checkbox"></td>
                    <td><a href="/Datasets/${datasetWithQueryStats.datasetKey}">${datasetWithQueryStats.dataset.name}</a> (${datasetWithQueryStats.querySpecificObservationCount})</td>
                    <td  class="nbn-td-right" >Access to this dataset Fusce in leo massa, nec ullamcorper dui. Aliquam auctor iaculis sapien, et scelerisque mi iaculis in. Donec nibh libero, aliquet vitae cursus in, mattis vel augue. Nulla facilisi. Aenean porttitor.</td>
                </tr>
            </#list>
        </#list>
    </table>
</#macro>

<#macro site_report_filters>
    <#assign designations=json.readURL("${api}/designations")>
    <div class="nbn-report-data-container">
        <table class="nbn-coloured-table">
            <tr>
                <th class="nbn-th-left nbn-th-right" colspan="2">
                    Current filter options
                </th>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-filter-name">Designation:</td><td class="nbn-td-right">MCZ Species of Conservation Importance</td>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-filter-name">Year range:</td><td class="nbn-td-right">from 1600 to 2012</td>
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
                            <select name="desig" id="desig" class="nbn-designation-dropdown">
                                <option selected="selected" value="-1">None selected</option>
                                <#list designations as designation>
                                    <option value="${designation.designationID}">${designation.name}</option>
                                </#list>
                            </select><br/>
                </td>
            </tr>
            <tr>
                <td class="nbn-td-left nbn-filter-name">Year range:</td>
                <td class="nbn-td-right">from <input id="startyear" type="textbox" value="1600" class="nbn-year-input"/>
                                         to <input id="endyear" type="textbox" value="2010" class="nbn-year-input"/></td>
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

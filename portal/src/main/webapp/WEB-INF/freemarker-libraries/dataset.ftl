<#macro dataset_table providersWithQueryStats>
    <table class="nbn-coloured-table">
        <tr><th class="nbn-th-left nbn-th-right" colspan="3">Data providers and their datasets contributing to this report (number of records)</th></tr>
        <#list providersWithQueryStats as providerWithQueryStats>
            <tr class="nbn-emphasise-row">
                <td class="nbn-td-left"><img src="http://localhost:8084/api/organisations/${providerWithQueryStats.organisationID}/logo" class="nbn-provider-table-logo"></td>
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

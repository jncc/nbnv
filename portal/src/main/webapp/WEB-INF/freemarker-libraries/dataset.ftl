<#macro dataset_table providersWithQueryStats>
    <table class="nbn-simple-table">
        <tr><th class="nbn-th-left nbn-th-right">Datasets (number of records)</th></tr>
        <#list providersWithQueryStats as providerWithQueryStats>
            <tr><td class="nbn-td-left nbn-td-right">Provider: <a href="/Provider/${providerWithQueryStats.organisationID}">${providerWithQueryStats.organisation.name}</a> (${providerWithQueryStats.querySpecificObservationCount})</td></tr>
            <#assign datasetsWithQueryStats=providerWithQueryStats.datasetsWithQueryStats>
            <#list datasetsWithQueryStats as datasetWithQueryStats>
                <tr><td class="nbn-td-left nbn-td-right">Dataset: <a href="/Datasets/${datasetWithQueryStats.datasetKey}">${datasetWithQueryStats.dataset.name}</a> (${datasetWithQueryStats.querySpecificObservationCount})</td></tr>
            </#list>
        </#list>
    </table>
</#macro>

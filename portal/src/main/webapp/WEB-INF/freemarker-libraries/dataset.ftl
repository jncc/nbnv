<#macro dataset_table metaDatasets>
        <table class="nbn-simple-table">
            <tr><th class="nbn-th-left nbn-th-right">Datasets (number of records)</th></tr>
            <#list metaDatasets as metaDataset>
                <tr><td class="nbn-td-left nbn-td-right"><a href="/Datasets/${metaDataset.datasetKey}">${metaDataset.dataset.name}</a> (${metaDataset.querySpecificObservationCount})</td></tr>
            </#list>
        </table>
</#macro>

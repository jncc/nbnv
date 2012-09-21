<#macro dataset_table metaDatasets>
        <table class="nbn-simple-table">
            <tr><th>Datasets</th></tr>
            <#list metaDatasets as metaDataset>
                <tr><td><a href="/Datasets/${metaDataset.datasetKey}">${metaDataset.dataset.name}</a> (${metaDataset.querySpecificObservationCount} records)</td></tr>
            </#list>
        </table>
</#macro>

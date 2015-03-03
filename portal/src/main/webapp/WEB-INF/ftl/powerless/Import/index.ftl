<#assign statuses = json.readURL("${api}/taxonDatasets/adminable/import")/>

<@template.master title="NBN import">
  <h1>DASHBOARD!</h1>
  <#list statuses as status>
    <div>
      <h2>${status.dataset.key} - ${status.dataset.title}</h2>
      <#if status.importStatus.isOnQueue>
        Dataset Queued
      </#if>
      <#if status.importStatus.isProcessing>
        Dataset Processing
      </#if>

      <#list status.importStatus.history?keys as timestamp>
        <h3>${timestamp}</h3>
        <#if status.importStatus.history[timestamp].success>
          Import was successful
        </#if>

        <#list status.importStatus.history[timestamp].validationErrors as error>
           Broken Rule - ${error.rule}
           Message     - ${error.message}
           Record Key  - ${error.recordKey}
        </#list>
      </#list>
    </div>
  </#list>
</@template.master>
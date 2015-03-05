<#assign statuses = json.readURL("${api}/taxonDatasets/adminable/import")/>

<@template.master title="NBN import">
  <h1>DASHBOARD!</h1>
  <#list statuses as status>
    <div>
      <h2>${status.dataset.key} - ${status.dataset.title}</h2>
      <ul>
        <#if status.importStatus.isOnQueue>
          <li>Dataset Queued</li>
        </#if>
        <#if status.importStatus.isProcessing>
          <li>Dataset Processing</li>
        </#if>

        <#list status.importStatus.history as importStatus>
          <li class="${importStatus.success?string('import-success', 'import-fail')}">
            ${importStatus.timestamp}

            <table>
              <tr><th>Record Key</th><th>Rule</th><th>Message</th></tr>
              <#list importStatus.validationErrors as error>
                <tr>
                  <td>${error.recordKey}</td>
                  <td>${error.rule}</td>
                  <td>${error.message}</td>
                </tr>
              </#list>
            </table>
          </li>
        </#list>
      </ul>
    </div>
  </#list>
</@template.master>

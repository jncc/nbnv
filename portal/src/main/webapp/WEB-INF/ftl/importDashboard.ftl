<@template.master title="NBN import">
  <h1>DASHBOARD!</h1>
  <#list statuses as status>
    <div class="tabbed">
      <h3>${status.dataset.key} - ${status.dataset.title}</h3>
      <ul>
        <#if status.importStatus.isOnQueue>
          <li>Dataset Queued</li>
        </#if>
        <#if status.importStatus.isProcessing>
          <li>Dataset Processing</li>
        </#if>

        <#list status.importStatus.history as importStatus>
          <#if importStatus.success>
            <li class="import-success">${importStatus.time?datetime}</li>
          <#else>
            <li class="import-fail">
              ${importStatus.time?datetime}
              <#if importStatus.validationErrors?has_content>
                <table class="nbn-simple-table">
                  <tr><th>Record Key</th><th>Rule</th><th>Message</th></tr>
                  <#list importStatus.validationErrors as error>
                    <tr>
                      <td>${error.recordKey}</td>
                      <td>${error.rule}</td>
                      <td>${error.message}</td>
                    </tr>
                  </#list>
                </table>
              </#if>
            </li>
          </#if>
        </#list>
      </ul>
    </div>
  </#list>
</@template.master>

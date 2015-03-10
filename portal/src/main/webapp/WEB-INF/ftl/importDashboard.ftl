<#-- 
    Creates an form which will perform some operation against the /Import
    endpoint. The form will contain only hidden fields and a visible submit 
    button.
-->
<#macro importForm label op datasetKey timestamp="">
  <form method="POST" action="/Import" enctype="application/x-www-form-urlencoded">
    <input type="hidden" name="op" value="${op}">
    <input type="hidden" name="datasetKey" value="${datasetKey}">
    <#if timestamp?has_content>
      <input type="hidden" name="timestamp" value="${timestamp}">
    </#if>
    <input type="submit" value="${label}">
  </form>
</#macro>

<@template.master title="NBN import">
  <h1>DASHBOARD!</h1>
  <#if error??><div class="message error">${error!}</div></#if>
  <#if message??><div class="message">${message!}</div></#if>
  <#list statuses as status>
    <div class="tabbed">
      <h3>${status.dataset.key} - ${status.dataset.title}</h3>
      <ul>
        <#if status.importStatus.isOnQueue>
          <li>
            Dataset Queued
            <@importForm "Delete" "delete" status.dataset.key/>
          </li>
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
                <@importForm "Import valid records" "importValid" status.dataset.key importStatus.timestamp/>
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
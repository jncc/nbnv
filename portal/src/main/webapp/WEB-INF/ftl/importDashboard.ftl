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

<#macro importControls>
  <div class="import-controls"><#nested></div>
</#macro>

<#macro importResult icon="" status="" value="" timestamp="">
  <li class="import-result import-result-${status}">
    <div class="import-status">
      <span class="ui-icon ui-icon-${icon}"></span>
      <span class="import-message">
        ${value} <#if timestamp?has_content><i> - ${timestamp?datetime}</i></#if>
      </span>
    </div>
    <#nested>
  </li>
</#macro>

<@template.master title="NBN import" csss=["//ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"]>
  <h1>DASHBOARD!</h1>
  <#if error??><div class="message error">${error!}</div></#if>
  <#if message??><div class="message info">${message!}</div></#if>

  <a href="/Import/Replace">Replace</a>

  <#list statuses as status>
    <div class="tabbed">
      <h3>${status.dataset.key} - ${status.dataset.title}</h3>
      <ul>
        <#if status.importStatus.isOnQueue>
          <@importResult icon="clock" status="inprogress" value="Dataset Queued">
            <@importControls><@importForm "Delete" "delete" status.dataset.key/></@importControls>
          </@importResult>
        </#if>

        <#if status.importStatus.isProcessing>
          <@importResult icon="arrowthickstop-1-n" status="inprogress" value="Dataset Processing"></@importResult>
        </#if>

        <#list status.importStatus.history as importStatus>
          <#if importStatus.state.name() == "SUCCESS">
            <@importResult icon="check" status="success" timestamp=importStatus.time value="Import completed successfully"></@importResult>
          <#elseif importStatus.state.name() == "VALIDATION_ERRORS">
            <@importResult icon="notice" status="warning" timestamp=importStatus.time value="Import failed with validation errors">
              <@importControls><@importForm "Import valid records" "importValid" status.dataset.key importStatus.timestamp/></@importControls>
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
            </@importResult>
	  <#elseif importStatus.state.name() == "BAD_FILE">
            <@importResult icon="alert" status="error" timestamp=importStatus.time value="The uploaded file could not be imported">
              <table class="nbn-simple-table">
                <tr><th>Data Issue</th><th>Rule</th><th>Message</th></tr>
                <#list importStatus.validationErrors as error>
                  <tr>
                    <td>${error.recordKey}</td>
                    <td>${error.rule}</td>
                    <td>${error.message}</td>
                  </tr>
                </#list>
              </table>
            </@importResult>
          <#elseif importStatus.state.name() == "MISSING_SENSITIVE_COLUMN">
            <@importResult icon="notice" status="warning" timestamp=importStatus.time value="Sensitive column is missing">
              <@importControls>
                <@importForm "Import all as Sensitive" "sensitiveTrue" status.dataset.key importStatus.timestamp/>
                <@importForm "Import all as Non-Sensitive" "sensitiveFalse" status.dataset.key importStatus.timestamp/>
              </@importControls>
            </@importResult>
          <#else>
            <@importResult icon="alert" status="error" timestamp=importStatus.time value="Import failed. Please contact the NBN Gateway team to help resolve this"></@importResult>
          </#if>
        </#list>
      </ul>
    </div>
  </#list>
</@template.master>

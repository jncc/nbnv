<@template.master title="NBN import">

  <h1>New dataset</h1>
  <#if status??>
    <div class="message error">${status!}</div>
  </#if>
  <fieldset>
    <form method="post" enctype="multipart/form-data" action="/Import/New">
      <#-- WARNING - This form is processed by the DatasetImporterController. It
           expects the fields in this order -->
      <input type="hidden" name="key" value="${datasetKey}">
      <p>
        <input type="file" name="file">
      </p>
      <p>
        <input type="submit" value="Import New Dataset">
        <a href="/Import">Back to Dashboard</a>
      </p>
    </form>
  </fieldset>
</@template.master>

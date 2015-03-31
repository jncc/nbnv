<@template.master title="NBN import">

  <h1>New taxon dataset - dataset upload</h1>
  <p>
    Use this form to upload your new taxon dataset.  Once it has uploaded, check the <a href="/Import">dashboard</a> to see where it is in the queue and whether there any problem records.
  </p>
  <#if status??>
    <div class="message error">${status!}</div>
  </#if>
  <fieldset>
    <form method="post" enctype="multipart/form-data" action="/Import/New">
      <#-- WARNING - This form is processed by the DatasetImporterController. It
           expects the fields in this order -->
      <input type="hidden" name="datasetKey" value="${datasetKey}">
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

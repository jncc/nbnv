<@template.master title="NBN import">

  <h1>Replace a taxon dataset</h1>
  <p>
    Use this form to upload your replacement taxon dataset.  Once it has uploaded, check the <a href="/Import">dashboard</a> to see where it is in the queue and whether there are any problem records.
  </p>
  <#if status??>
    <div class="message error">${status!}</div>
  </#if>
  <fieldset>
    <form method="post" enctype="multipart/form-data" action="/Import/Replace">
      <#-- WARNING - This form is processed by the DatasetImporterController. It
           expects the fields in this order -->
      <p>
        <select name="datasetKey">
          <#list datasets as dataset>
           <option value="${dataset.key}">${dataset.key}: ${dataset.title}</option>
          </#list>
        </select>
      </p>
      <p>
        <input type="file" name="file">
      </p>
      <p>
        <input type="submit" value="Import Replacement Dataset">
        <a href="/Import">Back to Dashboard</a>
      </p>
    </form>
  </fieldset>

</@template.master>

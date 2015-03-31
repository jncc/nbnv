<#assign datasets = json.readURL("${api}/user/adminDatasets")/>

<@template.master title="NBN import">

  <h1>Replace a dataset</h1>
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

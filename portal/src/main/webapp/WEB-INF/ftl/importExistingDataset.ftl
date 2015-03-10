<#assign user = json.readURL("${api}/user")/>
<#assign datasets = json.readURL("${api}/user/adminDatasets")/>

<@template.master title="NBN import">

  <h1>${isReplace?string("Replace", "Append")} a dataset</h1>
  <#if status??>
    <div class="message error">${status!}</div>
  </#if>
  <fieldset>
    <form method="post" enctype="multipart/form-data" action="/Import/Existing">
      <#-- WARNING - This form is processed by the DatasetImporterController. It
           expects the fields in this order -->
      <input type="hidden" name="isReplace" value="${isReplace?string("true", "false")}">
      <p>
        <select name="key">
          <#list datasets as dataset>
           <option value="${dataset.key}">${dataset.key}: ${dataset.title}</option>
          </#list>
        </select>
      </p>
      <p>
        <input type="file" name="file">
      </p>
      <p>
        <input type="submit">
        <a href="/Import">Back</a>
      </p>
    </form>
  </fieldset>

</@template.master>

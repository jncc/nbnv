<#assign user = json.readURL("${api}/user")/>
<#assign datasets = json.readURL("${api}/user/adminDatasets")/>

<@template.master title="NBN import">

  <h1>${isReplace?string("Replace", "Append")} a dataset</h1>
  <#if status??>
    <div class="message error">${status!}</div>
  </#if>
  <fieldset>
    <form method="post" enctype="multipart/form-data" action="/Import/Existing">
    <p>
      <input type="file" name="file">
    </p>
    <p>
      <select name="key">
        <#list datasets as dataset>
         <option value="${dataset.key}">${dataset.key}: ${dataset.title}</option>
        </#list>
      </select>
    </p>
    <p>
      <input type="hidden" name="isReplace" value="${isReplace?string("true", "false")}">
      <input type="submit">
    </p>
    </form>
  </fieldset>

</@template.master>

<#assign user = json.readURL("${api}/user")/>
<#assign datasets = json.readURL("${api}/user/adminDatasets")/>

<@template.master title="NBN import">

  <h1>Replace a dataset</h1>

  <fieldset>
    <form method="post" enctype="multipart/form-data" action="/replace">
    <p>
      <input type="file" name="file">
    </p>
    <#if datasets?has_content>
      <p>
        <select name="key">
          <option value="0">Choose dataset to replace...</option>
          <#list datasets as dataset>
           <option value="${dataset.key}">${dataset.key}: ${dataset.title}</option>
          </#list>
        </select>
      </p>
      <p>
        <input type="submit">
      </p>
    </#if>
    </form>
  </fieldset>

</@template.master>

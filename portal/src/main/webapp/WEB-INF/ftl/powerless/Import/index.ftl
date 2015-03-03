<#assign user = json.readURL("${api}/user")/>
<#assign datasets = json.readURL("${api}/user/adminDatasets")/>

<@template.master title="NBN import">

  <h1>DASHBOARD!</h1>
  <#if datasets?has_content>
    <div>
      <select id="datasets">
        <#list datasets as dataset>
        </#list>
      </select>
    </div>
  </#if>

</@template.master>

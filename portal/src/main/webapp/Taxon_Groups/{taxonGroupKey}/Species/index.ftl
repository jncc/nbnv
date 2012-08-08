<#include "/WEB-INF/templates/master.ftl">
<#include "/WEB-INF/templates/search.ftl">

<@master title="NBN Gateway - Taxa for ${URLParameters.taxonGroupKey}">
<<<<<<< HEAD
=======
    <#assign search=json.readURL("${api}/taxonNavigationGroups/${URLParameters.taxonGroupKey}/species", RequestParameters)/>
>>>>>>> ab619f4a3005bb4673ddb453f01f12eff9fc34bd
    
    <@search 
        url="${api}/taxonGroups/${URLParameters.taxonGroupKey}/species" 
        query=RequestParameters
        ; result
    >
        ${result.name}
    </@search>

</@master>
<#include "/WEB-INF/templates/master.ftl">
<#include "/WEB-INF/templates/search.ftl">

<@master title="NBN Gateway - Taxa for ${URLParameters.taxonGroupKey}">
    <@search 
        url="${api}/taxonNavigationGroups/${URLParameters.taxonGroupKey}/species" 
        query=RequestParameters
        ; result
    >
        ${result.name}
    </@search>

</@master>
<#include "/WEB-INF/templates/master.ftl">

<@master title="NBN Gateway - Taxa for ${URLParameters.taxonGroupKey}">
    <#assign search=json.readURL("${api}/taxonNavigationGroups/${URLParameters.taxonGroupKey}/species", RequestParameters)/>
    

    <ul>
        <#list search.results as result>
            <li>${result.name}</li>
        </#list>
    </ul>

    <#assign limit=(RequestParameters.limit!"20")?number/>
    <#assign nextOffset=(RequestParameters.offset!"0")?number + limit/>

    <#if (nextOffset < search.numFound)>    
        <a href="/Taxon_Groups/${URLParameters.taxonGroupKey}/Species?offset=${nextOffset}&limit=${limit}">Next</a>
    </#if>
</@master>
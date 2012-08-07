<#include "/WEB-INF/templates/master.ftl">

<@master title="NBN Gateway - Taxa for ${URLParameters.nhmkey}">
    <#assign search=json.readURL("${api}/taxonGroups/${URLParameters.nhmkey}/species", RequestParameters)/>
    

    <ul>
        <#list search.results as result>
            <li>${result.name}</li>
        </#list>
    </ul>
    
    <a href="/Taxon_Groups/${URLParameters.nhmkey}?offset=${RequestParameters.limit!20?number + RequestParameters.offset!0?number}&limit=${RequestParameters.limit!20}">Next</a>
</@master>
<#-- 
    The following template defines the structure of a Search Widget which can 
    be powered by NBN Gateway API standardised search resources
-->

<#function queryString hash>
    <#list hash?keys as currKey>
        <#if currKey_index==0>
            <#assign toReturn = "?${currKey}=${hash[currKey]}"/>
        <#else>
            <#assign toReturn = "${toReturn}&${currKey}=${hash[currKey]}"/>
        </#if>
    </#list>
    <#return toReturn/>
</#function>

<#macro page page rows>
    <a href="${queryString(RequestParameters + {"start":(page-1)*rows})}" class="previous">${page}</a>
</#macro>

<#function pageNumbers amountToShow totalPages currentPage>
    <#if totalPages <= amountToShow>
        <#return 1..totalPages>
    <#else>
        <#local leftStart = currentPage-((amountToShow/2)?floor)+1/>
        <#local rightFinish = (currentPage+(amountToShow/2)?ceiling)/>
        <#if (leftStart < 1)>
            <#return 1..(rightFinish - leftStart + 1)/>
        <#elseif (rightFinish > totalPages)>
            <#return (leftStart - (rightFinish - totalPages))..totalPages/>
        <#else>
            <#return leftStart..rightFinish/>
        </#if>
    </#if>
</#function>

<#macro _searchPaging search>
    <#--<#assign prevStart=(search.header.start - search.header.rows)/>-->
    <#assign currPage=(search.header.start/search.header.rows)+1/>
    <#assign amountOfPages=(search.header.numFound/search.header.rows)?ceiling/>
    <#--<#assign nextStart=(search.header.start + search.header.rows)/>-->

    <#list pageNumbers(9, amountOfPages, currPage) as pageNumber>
        <#if currPage != pageNumber>
            <@page pageNumber search.header.rows/>
        <#else>
            <${pageNumber}>
        </#if>
    </#list>
</#macro>

<#macro search url query>
    <#assign search=json.readURL(url, query)/>
    <div class="nbn-search">
        <ol class="results">
            <#list search.results as result>
                <li><#nested result></li>
            </#list>
        </ol>
        <@_searchPaging search/>
    </div>
</#macro>
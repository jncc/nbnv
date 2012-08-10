<#-- 
    The following template defines the structure of a Search Widget which can 
    be powered by NBN Gateway API standardised search resources
-->

<#function queryString parameterMap>
    <#list parameterMap?keys as parameter>
        <#list parameterMap[parameter] as parameterVal>
            <#-- Check if the we are dealing with the first output parameter -->
            <#if parameter_index==0 && parameterVal_index==0>
                <#assign toReturn = "?${parameter}=${parameterVal}"/>
            <#else>  
                <#assign toReturn = "${toReturn}&${parameter}=${parameterVal}"/>
            </#if>
        </#list>
    </#list>
    <#return toReturn/>
</#function>

<#macro page page rows>
    <a href="${queryString(RequestParameters + {"start":[(page-1)*rows]})}" class="previous">${page}</a>
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

<#macro facet category data={}>
    <#list data?keys as facetVal>
        <li>
            <#if data[facetVal].subCategories?? >
                <h1>${facetVal} (${data[facetVal].totalCount})
                    <input type="checkbox" name="fq" value="${data[facetVal].filterQuery}" ${RequestParameters.fq?seq_contains(data[facetVal].filterQuery)?string('checked="checked"','')}/>
                </h1>
                <ul><@facet category data[facetVal].subCategories/></ul>
            <#else>
                ${facetVal} (${data[facetVal].totalCount}) <input type="checkbox" name="fq" value="${data[facetVal].filterQuery}" ${RequestParameters.fq?seq_contains(data[facetVal].filterQuery)?string('checked="checked"','')}/>
            </#if>
            
        </li>
    </#list>
</#macro>

<#macro facets data>
    <div class="nbn-search-facets">
        <#list data?keys as currFacet>
            <h1>${currFacet}</h1>
            <ul class="collapsible-list"><@facet currFacet data[currFacet]/></ul>
        </#list>
        <input type="submit" value="Filter"/>
    </div>
</#macro>

<#macro search url query>
    <#assign search=json.readURL(url, query)/>
    <div class="nbn-search">
        
        <form>
            <@facets search.facetFields/>
            <ol class="results">
                <#list search.results as result>
                    <li><#nested result></li>
                </#list>
            </ol>
            <div class="pagnation"><@_searchPaging search/></div>
        </form>
    </div>
</#macro>
<#-- 
    The following template defines the utilities required to generate pagination
    decorations.

    @depends On core.ftl
-->

<#macro __page page rows label=page>
    <a href="${core.queryString(RequestParameters + {"start":[(page-1)*rows]})}" class="page">${label}</a>
</#macro>

<#function __pageNumbers amountToShow totalPages currentPage>
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


<#macro __listPages pageNumbers rows>
    <#list pageNumbers as pageNumber><@__page pageNumber rows/></#list>
</#macro>

<#macro pages search 
        page_numbers=9 
        pages_outside_leading_range=2 
        pages_outside_trailing_range=2>

    <#assign currPage=(search.header.start/search.header.rows)+1/>
    <#assign amountOfPages=(search.header.numFound/search.header.rows)?ceiling/>
    <#assign adjacentPages=__pageNumbers(page_numbers, amountOfPages, currPage)/>
    

    <#if (adjacentPages?first > pages_outside_leading_range)>
        <@__listPages 1..pages_outside_leading_range search.header.rows/>
        <span class="gap">...</span>
    </#if>
    <#list adjacentPages as pageNumber>
        <#if currPage != pageNumber>
            <@__page pageNumber search.header.rows/>
        <#else>
            <span class="page current">${pageNumber}</span>
        </#if>
    </#list>

    <#if (adjacentPages?last < (amountOfPages-pages_outside_trailing_range))>
        <span class="gap">...</span>
        <@__listPages (amountOfPages-pages_outside_trailing_range)..amountOfPages search.header.rows/>
    </#if>
</#macro>

<#macro previous search label>
    <#assign prevStart=(search.header.start - search.header.rows)/>
    <#if (prevStart >= 0)>
        <a href="${core.queryString(RequestParameters + {"start":[prevStart]})}">${label}</a>
    <#else>
        <span class="disabled">${label}</span>
    </#if>
</#macro>

<#macro next search label>
    <#assign nextStart=(search.header.start + search.header.rows)/>
    <#if (nextStart <= search.header.numFound)>
        <a href="${core.queryString(RequestParameters + {"start":[nextStart]})}">${label}</a>
    <#else>
        <span class="disabled">${label}</span>
    </#if>
</#macro>

<#macro paginator search 
        page_numbers=9 
        pages_outside_leading_range=2 
        pages_outside_trailing_range=2
        show_previous_link=true
        show_next_link=true>

    <div class="paginator">
        <#if show_previous_link><@previous search "&laquo; Previous"/></#if>
        <@pages search page_numbers pages_outside_leading_range pages_outside_trailing_range/>
        <#if show_next_link><@next search "Next &raquo;"/></#if>
    </div>
</#macro>
<#-- 
    The following template defines the structure of a Search Widget which can 
    be powered by NBN Gateway API standardised search resources
-->


<#macro __filterInputBox currentFacet>
    <input 
        type="checkbox" 
        name="${currentFacet.name}" 
        value="${currentFacet.id}"
        ${RequestParameters[currentFacet.name]?seq_contains(currentFacet.id)?string('checked="checked"','')}
    />
</#macro>

<#macro __facet name data counts>
    <#list data as currentFacet>
        <li>
            <#if currentFacet.children?? >
                <h1>${currentFacet.name} (${counts[currentFacet.id].totalCount})
                    <@__filterInputBox currentFacet/>
                </h1>
                <ul><@__facet name currentFacet.children counts/></ul>
            <#else>
                ${currentFacet.name} (${counts[currentFacet.id].totalCount}) 
                <@__filterInputBox currentFacet/>
            </#if>
        </li>
    </#list>
</#macro>

<#macro __facets facets counts>
    <div class="nbn-search-facets">
        <#list facets?keys as currFacet>
            <div class="nbn-search-facet">
                <h1>${currFacet}</h1>
                <ul class="collapsible-list"><@__facet currFacet facets[currFacet] counts[currFacet]/></ul>
            </div>
        </#list>
    </div>
</#macro>

<#macro search url query facets>
    <#assign search=json.readURL(url, query)/>
    <div class="nbn-search">
        
        <form>
            
            <@__facets facets search.facetFields/>
            <div class="controls">
                Show - <@pagination.show/> 
                <input type="submit" value="Filter"/>
            </div>
            <ol class="results">
                <#list search.results as result>
                    <li><#nested result></li>
                </#list>
            </ol>
            
        </form>
    </div>
    <@pagination.paginator search/>
</#macro>
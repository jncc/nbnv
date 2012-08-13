<#-- 
    The following template defines the structure of a Search Widget which can 
    be powered by NBN Gateway API standardised search resources
-->


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
            <@pagination.paginator search/>
        </form>
    </div>
</#macro>
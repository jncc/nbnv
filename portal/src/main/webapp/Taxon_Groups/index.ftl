<#include "/WEB-INF/templates/master.ftl">

<#macro loadTaxonGroup taxonGroupKey>
    <#assign details=json.readURL("${api}/taxonGroups/${taxonGroupKey}")/>
    <#if details.children?has_content>
        <h1>${details.taxonGroupName}</h1>
        <ul>
            <#list details.children as childTaxonGroup>
                <li><@loadTaxonGroup taxonGroupKey="${childTaxonGroup.taxonGroupKey}"/></li>
            </#list>
        </ul>
    <#else>
        <a href="/Taxon_Groups/Taxa/?taxonGroup=${details.taxonGroupKey}">${details.taxonGroupName}</a>
    </#if>
</#macro>

<@master title="NBN Gateway - Taxon Groups">
    <ul class="collapsible-list">
        <#list json.readURL("${api}/taxonGroup/topLevels") as taxonGroup>
            <li><@loadTaxonGroup taxonGroupKey="${taxonGroup.taxonGroupKey}"/></li>
        </#list>
    </ul>
</@master>
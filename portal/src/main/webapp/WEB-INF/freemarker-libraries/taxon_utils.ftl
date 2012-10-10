<#macro long_name taxon>
    <span class="nbn-taxon-name">${taxon.name}</span>
    <#if taxon.authority?has_content>
        ${taxon.authority}
    </#if>
    <#if taxon.commonName?has_content>
        ${taxon.commonName}
    </#if>
</#macro>

<#function getShortName taxon>
    <#assign toReturn="<span class='nbn-taxon-name'>${taxon.name}</span>">
    <#if taxon.commonName?has_content>
        <#assign toReturn=toReturn + " " + taxon.commonName>
    </#if>
    <#return toReturn>
</#function>
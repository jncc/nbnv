<#function getLongName taxon>
    <#assign toReturn="<span class='nbn-taxon-name'>${taxon.name}</span>">
    <#if taxon.authority?has_content>
        <#assign toReturn = toReturn + " ${taxon.authority}">
    </#if>
    <#if taxon.commonName?has_content>
        <#assign toReturn = toReturn + " [${taxon.commonName}]">
    </#if>
    <#return toReturn>
</#function>

<#function getShortName taxon>
    <#assign toReturn="<span class='nbn-taxon-name'>${taxon.name}</span>">
    <#if taxon.commonName?has_content>
        <#assign toReturn = toReturn + " [${taxon.commonName}]">
    </#if>
    <#return toReturn>
</#function>
<#macro long_name taxon>
    <span class="nbn-taxon-name">${taxon.name}</span>
    <#if taxon.taxonAuthority?has_content>
        ${taxon.taxonAuthority}
    </#if>
    <#if taxon.commonName?has_content>
        ${taxon.commonName}
    </#if>
</#macro>

<#macro short_name taxon>
    <span class="nbn-taxon-name">${taxon.name}</span>
    <#if taxon.commonName?has_content>
        ${taxon.commonName}
    </#if>
</#macro>
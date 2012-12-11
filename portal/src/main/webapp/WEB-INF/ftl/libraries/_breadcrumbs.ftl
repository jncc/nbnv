<#-- The following freemarker template will render a list of breadcrumbs from 
    the Sequence which has been made globally available (breadcrumbs)
-->
<ol>
    <#list breadcrumbs as breadcrumb>
        <li>
            <#if breadcrumb.link?? && breadcrumb_has_next>
                <a href="${breadcrumb.link}">${breadcrumb.name}</a>
            <#else>
                ${breadcrumb.name}
            </#if>
        </li>
    </#list>
</ol>
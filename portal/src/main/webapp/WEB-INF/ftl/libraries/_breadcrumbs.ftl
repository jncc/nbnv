<#-- The following freemarker template will render a list of breadcrumbs from 
    the Sequence which has been made globally available (breadcrumbs)
-->
<ol>
    <#-- Only show breadcrumbs when this is not a top level page -->
    <#if (breadcrumbs?size > 1)>
        <span>You are here: </span>
        <#list breadcrumbs as breadcrumb>
            <li>
                <#if breadcrumb.link?? && breadcrumb_has_next>
                    <a href="${breadcrumb.link}">${breadcrumb.name}</a>
                <#else>
                    ${breadcrumb.name}
                </#if>
            </li>
        </#list>
    </#if>
</ol>
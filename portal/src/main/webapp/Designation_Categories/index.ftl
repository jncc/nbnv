<#include "/WEB-INF/templates/master.ftl">

<@master title="NBN Gateway - Designation Categories">
    
    <#assign designationCategories=json.readURL("${api}/designationCategories")>

    <div id="nbn-designation-content">
        <h1>Designation Categories</h1>

        <p class="nbn-navigation">Browse these designation categories to find the designations you need.
        Select individual designations to obtain more information about them and associated species.</p>

        <ul class="collapsible-list" id="nbn-designation-categories-tree">
            <#list designationCategories as currEntry>
                <li>
                    <h1><span class="nbn-designation-category-heading-strong">${currEntry.label}:</span> ${currEntry.description}</h1>
                    <ul>
                        <#assign designationCategory=json.readURL("${api}/designationCategories/${currEntry.designationCategoryID}/designations")>
                        <#list designationCategory as currDesignation>
                            <li class="nbn-designation-nested-list"><a title="${currDesignation.description!}" href="/Designations/${currDesignation.designationID}">${currDesignation.name}</a> : ${currDesignation.label}</li>
                        </#list>
                    </ul>
                </li>
            </#list>
        </ul>

        <p class="nbn-designation-footer">All designation information on the NBN Gateway is collated and supplied by the <a href="http://www.jncc.gov.uk/page-5546">Joint Nature Conservation Committee (JNCC)</a></p>
    </div>
</@master>
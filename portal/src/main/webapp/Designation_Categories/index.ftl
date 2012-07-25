<#include "/WEB-INF/templates/master.ftl">

<@master title="NBN Gateway - Designation Categories">

    <h1>Designation Categories</h1>

    <p>Browse these designation categories to find the designations you need.
    Select individual designations to obtain more information about them and associated species.</p>
    
    <#assign designationCategories=json.readURL("${api}/designationCategories")>
    
    <ul class="collapsible-list">
        <#list designationCategories as currEntry>
            <li>
                <h1>${currEntry.label} - (${currEntry.description})</h1>
                <ul>
                    <#assign designationCategory=json.readURL("${api}/designationCategories/${currEntry.designationCategoryID}/designations")>
                    <#list designationCategory as currDesignation>
                        <li><a title="${currDesignation.description!}" href="/Designation?desig=${currDesignation.designationID}">${currDesignation.name}</a> : ${currDesignation.label}</li>
                    </#list>
                </ul>
            </li>
        </#list>
    </ul>

    <p>All designation information on the NBN Gateway is collated and supplied by the <a href="http://www.jncc.gov.uk/page-5546">Joint Nature Conservation Committee (JNCC)</a></p>
</@master>
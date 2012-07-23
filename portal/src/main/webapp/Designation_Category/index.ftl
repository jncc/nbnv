<#include "/WEB-INF/templates/master.ftl">

<@master title="National Biodiversity Network Gateway">

    <@markdown file="content.md" />
    
    <#assign designationCategories=json.readURL("${api}designationCategories")>
    
    <ul class="expandableList">
        <#list designationCategories as currEntry>
            <li>
                <h1>${currEntry.label}</h1>
                <ul>
                <#assign designationCategory=json.readURL("${api}designationCategories/${currEntry.designationCategoryID}/designations")>
                <#list designationCategory as currDesignation>
                    <li><a href="/Designation?desig=${currDesignation.designationCategoryID}">${currDesignation.name}</a></li>
                </#list>
                </ul>
            </li>
        </#list>
    </ul>
</@master>
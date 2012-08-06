<#include "/WEB-INF/templates/master.ftl">

<@master title="NBN Gateway - Designation Categories">
    
    <#assign siteCategories=json.readURL("${api}/siteBoundaryCategories")>

    <div id="nbn-designation-content">
        <h1>Site Datasets</h1>
        <p class="nbn-navigation">Browse sites to create a site report. 
            The site report includes a map of the site and a list of species recorded in the site.</p>
        <ul class="collapsible-list" id="nbn-designation-categories-tree">
            <#list siteCategories as siteCategory>
                <li class="collapsible-list">
                    <h1><span class="nbn-designation-category-heading-strong">${siteCategory.name}</span></h1>
                    <ul>
                        <#list siteCategory.siteBoundaryDatasets as siteDataset>
                                <li class="nbn-designation-nested-list"><a href="#">${siteDataset.datasetTitle}</a></li>
                        </#list>
                    </ul>
                </li>
            </#list>
        </ul>
    </div>

</@master>
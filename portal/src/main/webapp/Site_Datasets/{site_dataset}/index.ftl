<#include "/WEB-INF/templates/master.ftl">

<@master title="NBN Gateway - Designation Categories">

    <#assign siteDatasetKey="${URLParameters.site_dataset}">
    <#assign siteDataset=json.readURL("${api}/siteBoundaryDatasets/${siteDatasetKey}")>
    <#assign siteBoundaries=json.readURL("${api}/siteBoundaryDatasets/${siteDatasetKey}/siteBoundaries")>

    <div id="nbn-designation-content">
        <h4>${siteDataset.name}</h4>
        <ul id="nbn-designation-categories-tree">
            <#list siteBoundaries as siteBoundary>
                <li class="collapsible-list">
                    <a href="/Sites/#">${siteBoundary.name}</a>
                </li>
            </#list>
        </ul>
    </div>

</@master>
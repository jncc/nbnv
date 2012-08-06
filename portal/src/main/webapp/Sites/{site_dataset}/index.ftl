<#include "/WEB-INF/templates/master.ftl">

<@master title="NBN Gateway - Designation Categories">

    <#assign siteId="${URLParameters.site}">
    <#assign sites=json.readURL("${api}/siteDatasets/${site}")>

    <div id="nbn-designation-content">
        <h1>Site Datasets</h1>
        <p class="nbn-navigation">Browse sites to create a site report. 
            The site report includes a map of the site and a list of species recorded in the site.</p>
        <ul id="nbn-designation-categories-tree">
            <#list siteDatasets as currEntry>
                <li class="collapsible-list">
                    <a href="/Sites">${currEntry.geoLayerName}</a>
                </li>
            </#list>
        </ul>
    </div>

</@master>
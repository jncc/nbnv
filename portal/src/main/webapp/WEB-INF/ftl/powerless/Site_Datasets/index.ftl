<@template.master title="NBN Gateway - Site browser" csss=["/css/site-browser.css"]>
    
    <#assign siteBoundaryCategories=json.readURL("${api}/siteBoundaryCategories")>

    <div id="nbn-designation-content">
        <h1>10km grid square and site boundary browser</h1>
        <div class="nbn-navigation">
            <p>Browse to a 10km grid square or site boundary to view a report of taxa found there.
                Either click on a 100km square or expand a site boundary category to start your navigation.</p>
        </div>
        <div id="nbn-100km-selector-container">
            <@image_map.hundredKM/>
        </div>
        <div id="nbn-site-dataset-browser-container">
            <@siteDatasetBrowser/>
        </div>
    </div>

</@template.master>

<#macro siteDatasetBrowser>
    <ul class="collapsible-list" id="nbn-designation-categories-tree">
        <#list siteBoundaryCategories as siteCategory>
            <li class="collapsible-list">
                <h1><span class="nbn-designation-category-heading-strong">${siteCategory.name}</span></h1>
                <ul>
                    <#list siteCategory.siteBoundaryDatasets as siteDataset>
                            <li class="nbn-designation-nested-list"><a href="/Site_Datasets/${siteDataset.datasetKey}">${siteDataset.title}</a></li>
                    </#list>
                </ul>
            </li>
        </#list>
    </ul>
</#macro>

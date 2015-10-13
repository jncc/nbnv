<#assign dataset=json.readURL("${api}/datasets/${URLParameters.site_dataset}")>

<@template.master title="NBN Gateway - Site Boundaries"
    csss=[] 
    javascripts=["/js/OpenLayers.js",
                "/js/jquery.dataTables.min.js",
                "/js/jquery.nbn_search.js",
                "/js/enable-search.js"]>

    <h1>Site boundaries for the dataset '${dataset.title}'</h1>

    <div id="site-search-map" style="height:335px; width:100%;"></div>
    <@search.search 
        url="${api}/search/siteDatasets/${dataset.key}"
        defaultRows=10
        display=[
            {"title":"Site Boundary Name", "attr":"label", "link":"href"},
            {"title":"Site Key", "attr":"identifier"}
        ]
        query=RequestParameters
        filters=[{
            "type": "spatial",
            "id" : "bbox",
            "map" : "site-search-map",
            "layer": "${gis}/SiteBoundaryDatasets?LAYERS=${dataset.key}"
        }]
        />
</@template.master>

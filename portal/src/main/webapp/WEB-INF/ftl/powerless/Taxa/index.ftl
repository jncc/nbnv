<@template.master title="NBN Gateway - Taxon Search" 
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css","/css/browsetaxon.css"] 
    javascripts=["/js/jquery.dataTables.min.js",
                "/js/jquery.nbn_search.js",
                "/js/jquery.watermark.min.js",
                "/js/enable-search.js",
                "/js/browse_species.js"]>
    <@markdown file="introduction.md"/>

    <div class="taxonpagesection">
        <@search.search 
            url="${api}/taxa"
            display=[
                {"title":"Name", "attr":"searchMatchTitle", "link":"href"}
                {"title":"Preferred Name", "attr":"extendedName", "width":"85%"},
                {"title":"Record Count", "attr":"gatewayRecordCount"}
            ]
            query=RequestParameters 
            filters=[{
                "name": "Groups",	  	
                "id":"taxonOutputGroupKey",
                "data":json.readURL("${api}/taxonOutputGroups")
            }]/>
    </div>
</@template.master>
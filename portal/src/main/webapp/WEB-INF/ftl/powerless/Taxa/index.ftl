<@template.master title="NBN Gateway - Taxon Search" 
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"] 
    javascripts=["/js/jquery.dataTables.min.js",
                "/js/jquery.nbn_search.js",
                "/js/enable-search.js"]>
    <@markdown file="introduction.md"/>

    <@search.search 
        url="${api}/taxa"
        display=[
            {"title":"Taxon", "attr":"searchMatchTitle", "link":"href", "width":"80%"},
            {"title":"Record Count", "attr":"gatewayRecordCount"}
        ]
        query=RequestParameters 
        filters=[{
            "name": "Groups",	  	
            "id":"taxonOutputGroupKey",
            "data":json.readURL("${api}/taxonOutputGroups")
        }]/>
</@template.master>
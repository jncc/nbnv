<@template.master title="NBN Gateway - Taxon Search" 
    csss=[] 
    javascripts=["/js/jquery.dataTables.min.js",
                "/js/jquery.nbn_search.js",
                "/js/enable-search.js"]>
    <@markdown file="introduction.md"/>

    <@search.search 
        url="${api}/search"
        display=[
            {"title":"Result", "attr":"searchMatchTitle", "link":"href"},
            {"title":"Info", "attr":"descript", "width":"60%"},
            {"title":"Type", "attr":"entityType", "width":"15%"}
        ]
        query=RequestParameters/>
</@template.master>
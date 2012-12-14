<@template.master title="NBN Gateway - Taxon Search" 
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"] 
    javascripts=["/js/jquery.dataTables.min.js",
                "/js/jquery.nbn_search.js",
                "/js/enable-search.js"]>
    <@markdown file="introduction.md"/>

    <@search.search 
        url="${api}/search"
        display=[
            {"title":"Result", "attr":"searchMatchTitle", "link":"href"},
            {"title":"Type", "attr":"entityType"}
        ]
        query=RequestParameters/>
</@template.master>
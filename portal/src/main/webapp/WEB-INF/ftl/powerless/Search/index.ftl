<@template.master title="NBN Gateway - Taxon Search" 
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"] 
    javascripts=["/js/jquery.dataTables.min.js",
                "/js/jquery.nbn_search.js",
                "/js/enable-search.js"]>
    <@markdown>
#General Gateway Search Demonstration

Welcome to a mockup of viewing and searching gateway concepts. Please be aware that this 
is not a finished product.
    </@markdown>

    <@search.search 
        url="${api}/search"
        display=[
            {"title":"Result", "attr":"name", "link":"href"}
        ]
        query=RequestParameters/>
</@template.master>
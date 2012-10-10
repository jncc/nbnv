<@template.master title="NBN Gateway - Taxon Search" javascripts=["enable-ajaxSearch.js"]>
    <@markdown>
#Taxon Search Demonstration

Welcome to a mockup of viewing and searching taxons. Please be aware that this 
is not a finished product.
    </@markdown>

    <@search.search 
        url="${api}/taxa" 
        query=RequestParameters 
        facets=[{
                "name": "Taxon Navigation Group",
                "id":"category",
                "data":json.readURL("${api}/taxonNavigationGroups")
            }, {
                "id":"lang",
                "name": "Language",
                "data": [
                    {"id":"en", "name":"English"},
                    {"id":"la", "name":"Scientific"},
                    {"id":"cy", "name":"Welsh"},
                    {"id":"gd", "name":"Scottish Gaelic"},
                    {"id":"En", "name":"Invalidly imported English"}
                ]
        }]; result>
        ${taxon_utils.getShortName(result)}
    </@search.search>
</@template.master>
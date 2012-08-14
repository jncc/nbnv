<@template.master title="NBN Gateway - Taxon Seach">
    <@markdown>
#Taxon Search Demonstration

Welcome to a mockup of viewing and searching taxons. Please be aware that this 
is not a finished product.
    </@markdown>

    <@search.search 
        url="${api}/taxa" 
        query=RequestParameters 
        facets=[{
            "id":"category",
            "data":json.readURL("${api}/taxonNavigationGroups")
        }, "lang"]
        ; result
    >
        <@taxon_utils.short_name taxon=result/>
    </@search.search>
</@template.master>
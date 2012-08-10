<@template.master title="NBN Gateway - Taxon Seach">
    <@search.search 
        url="${api}/species" 
        query=RequestParameters
        ; result
    >
        ${result.name}
    </@search.search>
</@template.master>
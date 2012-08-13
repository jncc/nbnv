<@template.master title="NBN Gateway - Taxon Seach">
    <@markdown>
#Taxon Search Demonstration

Welcome to a mockup of viewing and searching taxons. Please be aware that this 
is not a finished product.
    </@markdown>



    <@search.search 
        url="${api}/species" 
        query=RequestParameters 
        facets={ 
            "category": [{
                "name":"bob",
                "id": 'NHMSYS0000080012',
                "children": [{
                    "name":"bob",
                    "id": 'NHMSYS0000080012'
                }]
            }]
        }
        ; result
    >
        ${result.name}
    </@search.search>
</@template.master>
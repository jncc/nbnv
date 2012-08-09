<@template.master title="NBN Gateway - Taxa for ${URLParameters.taxonGroupKey}">
    <@search.search 
        url="${api}/taxonNavigationGroups/${URLParameters.taxonGroupKey}/species" 
        query=RequestParameters
        ; result
    >
        ${result.name}
    </@search.search>

</@template.master>
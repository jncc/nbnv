<@template.master title="NBN Gateway - Taxon Search" javascripts=["enable-ajaxSearch.js"]>
    <@markdown>
#Taxon Search Demonstration

Welcome to a mockup of viewing and searching taxons. Please be aware that this 
is not a finished product.
    </@markdown>

    <@search.search 
        url="${api}/taxa" 
        query=RequestParameters 
        facets=[]; result>
        ${taxon_utils.getShortName(result)}
    </@search.search>
</@template.master>
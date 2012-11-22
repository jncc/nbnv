<@template.master title="NBN Gateway - Taxon Search" javascripts=["/js/enable-taxaSearch.js"]>
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
	  	"render" : "combo",
               "data":json.readURL("${api}/taxonNavigationGroups/topLevels")
	  	
            }]; result>
        ${taxon_utils.getShortName(result)}
    </@search.search>
</@template.master>
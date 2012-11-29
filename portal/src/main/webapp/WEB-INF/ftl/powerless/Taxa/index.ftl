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
        <a href="${result.href}">${taxon_utils.getShortName(result)}</a>
    </@search.search>
</@template.master>
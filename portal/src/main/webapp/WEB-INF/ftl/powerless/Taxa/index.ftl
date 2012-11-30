<@template.master title="NBN Gateway - Taxon Search" 
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"] 
    javascripts=["/js/jquery.dataTables.min.js",
                "/js/jquery.nbn_search.js",
                "/js/enable-search.js"]>
    <@markdown>
#Taxon Search Demonstration

Welcome to a mockup of viewing and searching taxons. Please be aware that this 
is not a finished product.
    </@markdown>

    <@search.search 
        url="${api}/taxa"
        display=[
            {"title":"Taxon", "attr":"name", "link":"href"},
            {"title":"Common Name", "attr":"commonName"},
            {"title":"Authority", "attr":"authority"},
            {"title":"Output Group", "attr":"taxonOutputGroupName"},
            {"title":"Rank", "attr":"rank"}
        ]
        query=RequestParameters 
        filters=[{
            "name": "Output Groups",	  	
            "id":"taxonOutputGroupKey",
            "data":json.readURL("${api}/taxonOutputGroups")
        }]/>
</@template.master>
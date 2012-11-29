<@template.master title="NBN Gateway - Taxon Search" javascripts=["/js/enable-search.js"]>
    <@markdown>
#General Gateway Search Demonstration

Welcome to a mockup of viewing and searching gateway concepts. Please be aware that this 
is not a finished product.
    </@markdown>

    <@search.search url="${api}/search" query=RequestParameters; result>
        <a href="${result.href}">${result.name!""}${result.title!""}</a>
    </@search.search>
</@template.master>
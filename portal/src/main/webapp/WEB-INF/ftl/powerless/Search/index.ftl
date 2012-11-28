<@template.master title="NBN Gateway - Taxon Search" javascripts=["/js/enable-search.js"]>
    <@markdown>
#General Gateway Search Demonstration

Welcome to a mockup of viewing and searching gateway concepts. Please be aware that this 
is not a finished product.
    </@markdown>

    <@search.search url="${api}/search" query=RequestParameters; result>
        <a href="${result.portal_href}">${result.name}</a>
    </@search.search>
</@template.master>
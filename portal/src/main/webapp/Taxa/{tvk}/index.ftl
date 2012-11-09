<#assign taxon=json.readURL("${api}/taxa/${URLParameters.tvk}")>

<@template.master title="NBN Gateway - Taxon">
    <h1>${taxon.taxonVersionKey}</h1>
</@template.master>
<#assign tenkmGridRef=URLParameters.tenkmGridRef>
<#assign taxonGroupKey=RequestParameters.taxonOutputGroup>
<#assign metaTaxa=json.readURL("${api}/taxonObservations/species?gridRef=${tenkmGridRef}",RequestParameters)>
<#assign metaDatasets=json.readURL("${api}/taxonObservations/datasets?gridRef=${tenkmGridRef}",RequestParameters)>

<@template.master title="10km report for ${tenkmGridRef}">
    <h1>10km Square ${tenkmGridRef}</h1>
    <table class="nbn-simple-table">
    <tr><th>Species</th></tr>
    <#list metaTaxa as metaTaxon>
        <tr><td><a href="../${tenkmGridRef}/Data?ptvk=${metaTaxon.taxon.prefnameTaxonVersionKey}">${metaTaxon.taxon.name}</a> (${metaTaxon.querySpecificObservationCount} observations)</td></tr>
    </#list>
    </table>
    
    <@dataset.dataset_table metaDatasets=metaDatasets/>

</@template.master>

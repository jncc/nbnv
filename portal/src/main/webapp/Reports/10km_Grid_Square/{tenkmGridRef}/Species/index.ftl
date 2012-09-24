<#assign tenkmGridRef=URLParameters.tenkmGridRef>
<#assign taxonGroupKey=RequestParameters.taxonOutputGroup>
<#assign metaTaxa=json.readURL("${api}/taxonObservations/species?gridRef=${tenkmGridRef}",RequestParameters)>
<#assign metaDatasets=json.readURL("${api}/taxonObservations/datasets?gridRef=${tenkmGridRef}",RequestParameters)>

<@template.master title="10km report for ${tenkmGridRef}">
    <h1>10km Square ${tenkmGridRef}</h1>

    <div class="nbn-report-data-container">
        <table class="nbn-simple-table">
        <tr><th class="nbn-th-left nbn-th-right">Species recorded in ${tenkmGridRef} (number of records)</th></tr>
        <#list metaTaxa as metaTaxon>
            <tr><td class="nbn-td-left nbn-td-right"><a href="../${tenkmGridRef}/Data?ptvk=${metaTaxon.taxon.prefnameTaxonVersionKey}">${metaTaxon.taxon.name}</a> (${metaTaxon.querySpecificObservationCount})</td></tr>
        </#list>
        </table>
    </div>

    <@dataset.dataset_table metaDatasets=metaDatasets/>

</@template.master>

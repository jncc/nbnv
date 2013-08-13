<#assign featureID=URLParameters.featureID>
<#assign requestParametersExtended = RequestParameters + {"featureID":[featureID],"ptvk":[URLParameters.ptvk]}>
<#assign datasets=json.readURL("${api}/taxonObservations/datasets/observations",requestParametersExtended)>
<#assign taxon=json.readURL("${api}/taxa/${URLParameters.ptvk}")>
<#assign site=json.readURL("${api}/features/${featureID}")>
<#assign startYear=RequestParameters.startYear?has_content?string(RequestParameters.startYear[0]!"1600","1600")>
<#assign endYear=RequestParameters.endYear?has_content?string(RequestParameters.endYear[0]!.now?string("yyyy"),.now?string("yyyy"))>
<#assign spatialRelationship=RequestParameters.spatialRelationship?has_content?string(RequestParameters.spatialRelationship[0]!"overlap","overlap")>
<#assign title="Records for ${taxon_utils.getLongName(taxon)} in ${site.label}">
<#assign is10kmReport=(site.type="GridSquare")>
<#assign unavailableDatasets=json.readURL("${api}/taxonObservations/unavailableDatasets",requestParametersExtended)>

<@template.master title="NBN Site Report" 
    csss=["/css/report.css","/css/site-report.css","/css/smoothness/jquery-ui-1.8.23.custom.css"]
    javascripts=["/js/report_utils.js","/js/site_report_observations.js"]>
    <h1>${title}</h1>
    <form id="nbn-site-report-form" featureID="${featureID}" ptvk="${URLParameters.ptvk}" api-server="${api}">
        <@report_utils.site_report_filters requestParameters=RequestParameters args={"taxon":taxon} location=site.label isSpatialRelationshipNeeded=true isDesignationNeeded=false isDatasetNeeded=false/>
        <div class="tabbed" id="nbn-site-report-filter-container">
            <h3>Other Options</h3>
            <a id="nbn-request-better-access" href="#">Request Better Access</a>
            <a id="nbn-interactive-map" href="#">View on IMT</a>
        </div>
        <@report_utils.siteImage locationName=site.label locationID=featureID imageURL=report_utils.getSiteSpeciesImageURL(featureID, taxon.ptaxonVersionKey, startYear, endYear, datasets, spatialRelationship, !is10kmReport)/>
    </form>
    <div id="nbn-observation-container"></div>
    <br><br>
    <@report_utils.unavailable_datasets unavailableDatasets=unavailableDatasets/>
</@template.master>

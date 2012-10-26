<#assign featureID=URLParameters.featureID>
<#assign requestParametersExtended = RequestParameters + {"featureID":[featureID],"ptvk":[URLParameters.ptvk]}>
<#assign datasets=json.readURL("${api}/taxonObservations/datasets/observations",requestParametersExtended)>
<#assign taxon=json.readURL("${api}/taxa/${URLParameters.ptvk}")>
<#assign site=json.readURL("${api}/features/${featureID}")>
<#assign startYear=RequestParameters.startYear?has_content?string(RequestParameters.startYear[0]!"1600","1600")>
<#assign endYear=RequestParameters.endYear?has_content?string(RequestParameters.endYear[0]!.now?string("yyyy"),.now?string("yyyy"))>
<#assign spatialRelationship=RequestParameters.spatialRelationship?has_content?string(RequestParameters.spatialRelationship[0]!"overlap","overlap")>
<#assign title="Records for ${taxon_utils.getShortName(taxon)} in '${site.label}' from ${report_utils.getYearRangeText(RequestParameters)}">
<#assign is10kmReport=(site.type="GridSquare")>

<@template.master title="NBN Site Report" javascripts=["/js/site-report-form-validation.js"]>
    <h1>${title}</h1>
    <form action="" method="post" id="${report_utils.getSiteFormId()}">
        <@report_utils.site_report_filters requestParameters=RequestParameters args={"taxon":taxon} location=site.label isSpatialRelationshipNeeded=false isDesignationNeeded=false isDatasetNeeded=false/>
        <@report_utils.siteImage locationName=site.label locationID=featureID imageURL=report_utils.getSiteSpeciesImageURL(featureID, taxon.ptaxonVersionKey, startYear, endYear, datasets, spatialRelationship, !is10kmReport)/>
    </form>
    <#if datasets?has_content>
        <#list datasets as dataset>
            <#assign provider=json.readURL("${api}/organisations/${dataset.organisationID}")>
            <table class="nbn-coloured-table">
                <tr>
                    <th colspan="2" class="nbn-th-left">Dataset:</th>
                    <th colspan="4" class="nbn-th-right"><a href="/Datasets/${dataset.key}">${dataset.title}</a></th>
                </tr>
                <tr>
                    <th colspan="2" class="nbn-td-left">Provider:</th>
                    <th colspan="4" class="nbn-td-right"><a href="/Organisations/${provider.id}">${provider.name}</a></th>
                </tr>
                <tr>
                    <th colspan="2" class="nbn-td-left">Your access:</th>
                    <th colspan="4" class="nbn-td-right">Access to this dataset Fusce in leo massa, nec ullamcorper dui. Aliquam auctor iaculis sapien, et scelerisque mi iaculis in. Donec nibh libero, aliquet vitae cursus in, mattis vel augue. Nulla facilisi. Aenean porttitor.</a></th>
                </tr>
                <tr class="nbn-emphasise-row">
                    <td class="nbn-td-left">Site name</td>
                    <td>Location</td>
                    <td>Date range</td>
                    <td>Date type</td>
                    <td>Sensitive</td>
                    <td class="nbn-td-right">Absence</td>
                </tr>
                <#list dataset.observations as observation>
                    <tr>
                        <td class="nbn-td-left">${observation.siteName!"Not available"}</td>
                        <td>${observation.identifier}</td>
                        <td>${observation.startDate} to ${observation.endDate}</td>
                        <td>${observation.dateTypekey}</td>
                        <td>${observation.sensitive?string}</td>
                        <td class="nbn-td-right">${observation.absence?string}</td>
                    </tr>
                </#list>
            </table>
        </#list>
    <#else>
        <@report_utils.noRecordsInfoBox/>
    </#if>
</@template.master>

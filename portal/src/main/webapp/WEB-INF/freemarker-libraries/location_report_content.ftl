<#macro groups title locationName locationID is10kmReport taxonOutputGroupsWithQueryStats providersWithQueryStats requestParameters>
    <h1>${title}</h1>
    <form action="" method="post" id="${getSiteFormId()}">
        <@report_utils.site_report_filters requestParameters=requestParameters location=locationName  isSpatialRelationshipNeeded=!is10kmReport/>
        <div class="nbn-report-data-container">
            <#if taxonOutputGroupsWithQueryStats?has_content>
                <table class="nbn-coloured-table">
                    <tr><th class="nbn-th-left nbn-th-right">Species groups (number of species)</th></tr>
                    <#list taxonOutputGroupsWithQueryStats as taxonOutputGroupWithQueryStats>
                        <tr><td class="nbn-td-left nbn-td-right"><a href="/Reports/${getReportTypeTextForURL(is10kmReport)}/${locationID}/Groups/${taxonOutputGroupWithQueryStats.taxonOutputGroupKey}/Species${report_utils.getQueryParameterText(requestParameters)}">${taxonOutputGroupWithQueryStats.taxonOutputGroup.name}</a>(${taxonOutputGroupWithQueryStats.querySpecificSpeciesCount})</td></tr>
                    </#list>
                </table>
            <#else>
                <@noRecordsInfoBox/>
            </#if>
        </div>
        <@report_utils.siteImage locationName=locationName locationID=locationID imageURL=report_utils.getSiteBoundaryImageURL(locationID,!is10kmReport)/>
        <#if providersWithQueryStats?has_content>
            <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=requestParameters/>
        </#if>
    </form>
</#macro>

<#macro species title locationName locationID is10kmReport taxaWithQueryStats providersWithQueryStats requestParameters taxonOutputGroup>
    <h1>${title}</h1>
    <form action="" method="post" id="${getSiteFormId()}">
        <@report_utils.site_report_filters requestParameters=requestParameters args={"taxonOutputGroup":taxonOutputGroup} location=locationName isSpatialRelationshipNeeded=!is10kmReport/>
        <div class="nbn-report-data-container">
            <#if taxaWithQueryStats?has_content>
                <table class="nbn-coloured-table">
                <tr><th class="nbn-th-left nbn-th-right">Species recorded (number of records)</th></tr>
                <#list taxaWithQueryStats as taxonWithQueryStats>
                    <tr><td class="nbn-td-left nbn-td-right"><a href="/Reports/${getReportTypeTextForURL(is10kmReport)}/${locationID}/Groups/${taxonOutputGroup.key}/Species/${taxonWithQueryStats.taxon.ptaxonVersionKey}/Observations${report_utils.getQueryParameterText(requestParameters)}">${taxon_utils.getShortName(taxonWithQueryStats.taxon)}</a> (${taxonWithQueryStats.querySpecificObservationCount})</td></tr>
                </#list>
                </table>
            <#else>
                <@noRecordsInfoBox/>
            </#if>
        </div>
        <@report_utils.siteImage locationName=locationName locationID=locationID imageURL=report_utils.getSiteBoundaryImageURL(locationID,!is10kmReport)/>
        <#if providersWithQueryStats?has_content>
            <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>
        </#if>
    </form>
</#macro>

<#macro observations title locationName locationID datasets requestParameters taxon is10kmReport>
    <#assign startYear=requestParameters.startYear?has_content?string(requestParameters.startYear[0]!"1600","1600")>
    <#assign endYear=requestParameters.endYear?has_content?string(requestParameters.endYear[0]!.now?string("yyyy"),.now?string("yyyy"))>
    <#assign spatialRelationship=requestParameters.spatialRelationship?has_content?string(requestParameters.spatialRelationship[0]!"overlap","overlap")>
    <h1>${title}</h1>
    <form action="" method="post" id="${getSiteFormId()}">
        <@report_utils.site_report_filters requestParameters=requestParameters args={"taxon":taxon} location=locationName isSpatialRelationshipNeeded=false isDesignationNeeded=false isDatasetNeeded=false/>
        <@report_utils.siteImage locationName=locationName locationID=locationID imageURL=report_utils.getSiteSpeciesImageURL(locationID, taxon.ptaxonVersionKey, startYear, endYear, datasets, spatialRelationship, !is10kmReport)/>
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
                    <td>Gridref</td>
                    <td>Date range</td>
                    <td>Date type</td>
                    <td>Sensitive</td>
                    <td class="nbn-td-right">Absence</td>
                </tr>
                <#list dataset.observations as observation>
                    <tr>
                        <td class="nbn-td-left">${observation.siteName!"Not available"}</td>
                        <td>${observation.gridRef}</td>
                        <td>${observation.startDate} to ${observation.endDate}</td>
                        <td>${observation.dateTypekey}</td>
                        <td>${observation.sensitive?string}</td>
                        <td class="nbn-td-right">${observation.absence?string}</td>
                    </tr>
                </#list>
            </table>
        </#list>
    <#else>
        <@noRecordsInfoBox/>
    </#if>
</#macro>

<#function getReportTypeTextForURL is10kmReport>
    <#assign toReturn="Sites">
    <#if is10kmReport>
        <#assign toReturn="10km_Grid_Square">
    </#if>
    <#return toReturn>
</#function>

<#function getSiteFormId>
    <#return "nbn-site-report-form">
</#function>

<#macro noRecordsInfoBox>
    <div class="nbn-information-panel">No records were found for your current filter options</div>
</#macro>
<#macro groups title locationName locationID is10kmReport taxonOutputGroupsWithQueryStats providersWithQueryStats requestParameters>
    <h1>${title}</h1>
    <form action="" method="post" id="${getSiteFormId()}">
        <@report_utils.site_report_filters requestParameters=requestParameters location=locationName  isSpatialRelationshipNeeded=!is10kmReport/>
        <div class="nbn-report-data-container">
            <table class="nbn-coloured-table">
                <tr><th class="nbn-th-left nbn-th-right">Species groups (number of species)</th></tr>
                <#list taxonOutputGroupsWithQueryStats as taxonOutputGroupWithQueryStats>
                    <tr><td class="nbn-td-left nbn-td-right"><a href="/Reports/${getReportTypeTextForURL(is10kmReport)}/${locationID}/Groups/${taxonOutputGroupWithQueryStats.taxonGroupKey}/Species${report_utils.getQueryParameterText(requestParameters)}">${taxonOutputGroupWithQueryStats.taxonOutputGroup.taxonGroupName}</a>(${taxonOutputGroupWithQueryStats.querySpecificSpeciesCount})</td></tr>
                </#list>
            </table>
        </div>
        <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=requestParameters/>
    </form>
</#macro>

<#macro species title locationName locationID is10kmReport taxaWithQueryStats providersWithQueryStats requestParameters taxonOutputGroup>
    <h1>${title}</h1>
    <form action="" method="post" id="${getSiteFormId()}">
        <@report_utils.site_report_filters requestParameters=requestParameters args={"taxonOutputGroup":taxonOutputGroup} location=locationName isSpatialRelationshipNeeded=!is10kmReport/>
        <div class="nbn-report-data-container">
            <table class="nbn-coloured-table">
            <tr><th class="nbn-th-left nbn-th-right">Species recorded (number of records)</th></tr>
            <#list taxaWithQueryStats as taxonWithQueryStats>
                <tr><td class="nbn-td-left nbn-td-right"><a href="/Reports/${getReportTypeTextForURL(is10kmReport)}/${locationID}/Groups/${taxonOutputGroup.taxonGroupKey}/Species/${taxonWithQueryStats.taxon.prefnameTaxonVersionKey}/Observations${report_utils.getQueryParameterText(requestParameters)}">${taxon_utils.getShortName(taxonWithQueryStats.taxon)}</a> (${taxonWithQueryStats.querySpecificObservationCount})</td></tr>
            </#list>
            </table>
        </div>
        <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>
    </form>
</#macro>


<#macro observations title locationName locationID datasets requestParameters taxon>
    <h1>${title}</h1>
    <form action="" method="post" id="${getSiteFormId()}">
        <@report_utils.site_report_filters requestParameters=requestParameters args={"taxon":taxon} location=locationName isSpatialRelationshipNeeded=false isDesignationNeeded=false isDatasetNeeded=false/>
    </form>
    <#list datasets as dataset>
        <#assign provider=json.readURL("${api}/organisations/${dataset.organisationID}")>
        <table class="nbn-coloured-table">
            <tr>
                <th colspan="2" class="nbn-th-left">Dataset:</th>
                <th colspan="4" class="nbn-th-right"><a href="/Datasets/${dataset.datasetKey}">${dataset.name}</a></th>
            </tr>
            <tr>
                <th colspan="2" class="nbn-td-left">Provider:</th>
                <th colspan="4" class="nbn-td-right"><a href="/Organisations/${provider.organisationID}">${provider.name}</a></th>
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
                    <td class="nbn-td-left">${observation.siteName!"N/A"}</td>
                    <td>${observation.gridRef}</td>
                    <td>${observation.startDate} to ${observation.endDate}</td>
                    <td>${observation.dateType}</td>
                    <td>${observation.sensitive?string}</td>
                    <td class="nbn-td-right">${observation.absence?string}</td>
                </tr>
            </#list>
        </table>
    </#list>
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
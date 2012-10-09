<#assign featureID=URLParameters.featureID>
<#assign taxonOutputGroupKey=URLParameters.taxonOutputGroup>
<#assign requestParametersExtended = RequestParameters + {"featureID":[featureID],"taxonOutputGroup":[taxonOutputGroupKey]}>
<#assign taxaWithQueryStats=json.readURL("${api}/taxonObservations/species",requestParametersExtended)>
<#assign providersWithQueryStats=json.readURL("${api}/taxonObservations/providers",requestParametersExtended)>
<#assign taxonOutputGroup=json.readURL("${api}/taxonOutputGroups/${taxonOutputGroupKey}")>
<#assign site=json.readURL("${api}/features/${featureID}")>

<@template.master title="Site report for..." javascripts=["/js/site-report-form-validation.js","/js/jquery.dataset-selector-utils.js"]>
    <h1>${taxonOutputGroup.taxonGroupName?cap_first} species with records for ${site.label} from <@report_utils.yearRangeText requestParameters=RequestParameters/></h1>
    <form action="" method="post" id="nbn-site-report-form">
        <@report_utils.site_report_filters requestParameters=RequestParameters args={"taxonOutputGroup":taxonOutputGroup} location=site.label  isSiteBoundaryReport=true/>
        <div class="nbn-report-data-container">
            <table class="nbn-coloured-table">
            <tr><th class="nbn-th-left nbn-th-right">Species recorded in... (number of records)</th></tr>
            <#list taxaWithQueryStats as taxonWithQueryStats>
                <tr><td class="nbn-td-left nbn-td-right"><a href="/Reports/Sites/${featureID}/Groups/${taxonOutputGroupKey}/Species/${taxonWithQueryStats.taxon.prefnameTaxonVersionKey}/Observations"><@taxon_utils.short_name taxon=taxonWithQueryStats.taxon/></a> (${taxonWithQueryStats.querySpecificObservationCount})</td></tr>
            </#list>
            </table>
        </div>
        <@report_utils.dataset_table providersWithQueryStats=providersWithQueryStats requestParameters=RequestParameters/>
    </form>
</@template.master>

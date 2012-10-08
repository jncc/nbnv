<#assign featureID=URLParameters.featureID>
<#assign requestParametersExtended = RequestParameters + {"featureID":[featureID],"ptvk":[URLParameters.ptvk]}>
<#assign datasets=json.readURL("${api}/taxonObservations/datasets/observations",requestParametersExtended)>
<#assign taxon=json.readURL("${api}/taxa/${URLParameters.ptvk}")>
<#assign site=json.readURL("${api}/features/${featureID}")>

<@template.master title="Site report for ${featureID}">
    <h1>Records for <@taxon_utils.short_name taxon=taxon/> in '${site.label}' from <@report_utils.yearRangeText requestParameters=RequestParameters/></h1>

    <@report_utils.site_report_filters requestParameters=RequestParameters args={"taxon":taxon} location=site.label/>

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
                <td>Date accuracy</td>
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
    
</@template.master>

<@template.master title="NBN Gateway - Organisations" 
    javascripts=["/js/jqueryui.simple-table-style.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css"]>

    <#assign organisationId="${URLParameters.organisation}">
    <#assign organisation=json.readURL("${api}/organisations/${organisationId}")>
    <#assign datasets=json.readURL("${api}/organisations/${organisationId}/datasets")>

    <h1>${organisation.name}</h1>
    <table class="nbn-organisation-table nbn-simple-table">
        <tr>
            <th>Summary</th>
        </tr>
        <tr>
            <td><img class="nbn-align-img-right" src="${api}/organisations/${organisation.id}/logo" />${organisation.summary}</td>
        </tr>
        <tr>
            <th>Contact details</th>
        </tr>
        <tr>
            <td>
                <div id="nbn-organisation-contact-details">
                    <#if organisation.contactName?has_content>${organisation.contactName}</br></#if>
                    <#if organisation.contactEmail?has_content>${organisation.contactEmail}</br></br></#if>
                    <#if organisation.address?has_content>${organisation.address}</br></#if>
                    <#if organisation.postCode?has_content>${organisation.postCode}</br></#if>
                    <#if organisation.website?has_content></br>${organisation.website}</#if>
                </div>
            </td>
        </tr>
        <@parseDatasets datasetList=datasets />
    </table>

<#macro parseDatasets datasetList>
    <#assign taxonDatasetInfo = "">
    <#assign habitatDatasetInfo = "">
    <#assign siteBoundaryDatasetInfo = "">
    <#list datasetList as dataset>
        <#assign datasetLinkFragment = "<li><a href='/Datasets/${dataset.key}'>${dataset.key}</a></li>">
        
        <#if dataset.typeName == "Taxon">
            <#if taxonDatasetInfo?length == 0>
                <#assign taxonDatasetInfo = " ${taxonDatasetInfo}<tr><th>Species datasets</th></tr><tr><td><ul> ">
            </#if>
            <#assign taxonDatasetInfo = "${taxonDatasetInfo} ${datasetLinkFragment}">
        <#elseif dataset.typeName == "Habitat">
            <#if habitatDatasetInfo?length == 0>
                <#assign habitatDatasetInfo = " ${habitatDatasetInfo}<tr><th>Habitat datasets</th></tr><tr><td><ul> ">
            </#if>
            <#assign habitatDatasetInfo = "${habitatDatasetInfo} ${datasetLinkFragment}">
        <#elseif dataset.typeName == "Site Boundary">
            <#if siteBoundaryDatasetInfo?length == 0>
                <#assign siteBoundaryDatasetInfo = " ${siteBoundaryDatasetInfo}<tr><th>Site boundary datasets</th></tr><tr><td><ul> ">
            </#if>
            <#assign siteBoundaryDatasetInfo = "${siteBoundaryDatasetInfo} ${datasetLinkFragment}">
        </#if>

    </#list>
    <#if taxonDatasetInfo?length != 0>
        <#assign taxonDatasetInfo = "${taxonDatasetInfo} </td></tr></ul>">
        ${taxonDatasetInfo}
    </#if>
    <#if habitatDatasetInfo?length != 0>
        <#assign habitatDatasetInfo = "${habitatDatasetInfo} </ul>">
        ${habitatDatasetInfo}
    </#if>
    <#if siteBoundaryDatasetInfo?length != 0>
        <#assign siteBoundaryDatasetInfo = "${siteBoundaryDatasetInfo} </ul>">
        ${siteBoundaryDatasetInfo}
    </#if>
</#macro>

</@template.master>

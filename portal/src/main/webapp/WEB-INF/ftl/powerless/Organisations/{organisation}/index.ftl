<@template.master title="NBN Gateway - Organisations" 
    javascripts=["/js/jqueryui.simple-table-style.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/organisation.css"]>

    <#assign organisationId="${URLParameters.organisation}">
    <#assign organisation=json.readURL("${api}/organisations/${organisationId}")>
    <#assign datasets=json.readURL("${api}/organisations/${organisationId}/datasets")>

    <h1>${organisation.name}</h1>
    <div class="tabbed nbn-organisation-tabbed">
        <h3>Summary</h3>
        <img class="nbn-align-img-right" src="${api}/organisations/${organisation.id}/logo" />${organisation.summary}
    </div>
    <div class="tabbed nbn-organisation-tabbed">
        <h3>Contact details</h3>
                <div id="nbn-organisation-contact-details">
                    <#if organisation.contactName?has_content>${organisation.contactName}</br></#if>
                    <#if organisation.contactEmail?has_content>${organisation.contactEmail}</br></br></#if>
                    <#if organisation.address?has_content>${organisation.address}</br></#if>
                    <#if organisation.postCode?has_content>${organisation.postCode}</br></#if>
                    <#if organisation.website?has_content></br>${organisation.website}</#if>
                </div>
    </div>
    <@parseDatasets datasetList=datasets />

</@template.master>

<#macro parseDatasets datasetList>
    <#assign taxonDatasets = []>
    <#assign habitatDatasets = []>
    <#assign siteBoundaryDatasets = []>
    <#list datasetList as dataset>
        <#if dataset.typeName == "Taxon">
            <#assign taxonDatasets = taxonDatasets + [{"key": dataset.key, "title": dataset.title, "description": dataset.description}]/>
        <#elseif dataset.typeName == "Habitat">
            <#assign habitatDatasets = habitatDatasets + [{"key": dataset.key, "title": dataset.title, "description": dataset.description}]/>
        <#elseif dataset.typeName == "Site Boundary">
            <#assign siteBoundaryDatasets = siteBoundaryDatasets + [{"key": dataset.key, "title": dataset.title, "description": dataset.description}]/>
        </#if>
    </#list>
    <#if (taxonDatasets?size > 0)>
        <@datasetTable taxonDatasets "Species datasets" />
    </#if>
    <#if (habitatDatasets?size > 0)>
        <@datasetTable habitatDatasets "Habitat datasets" />
    </#if>
    <#if (siteBoundaryDatasets?size > 0)>
        <@datasetTable siteBoundaryDatasets "Site boundary datasets" />
    </#if>
</#macro>

<#macro datasetTable datasets title>
    <div class="tabbed nbn-organisation-tabbed">
        <h3>${title}</h3>
        <table class="nbn-simple-table"><tr><th>Title</th><th>Description</th></tr>
            <#list datasets as dataset>
                <tr><td class="nbn-org-datasets-title-td"><a href="/Datasets/${dataset.key}">${dataset.title}</a></td><td class="nbn-org-datasets-desc-td">${dataset.description}</td></tr>
            </#list>
        </table>
    </div>
</#macro>


<@template.master title="NBN Gateway - Organisations" 
    javascripts=["/js/jqueryui.simple-table-style.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css"]>

    <#assign organisationId="${URLParameters.organisation}">
    <#assign organisation=json.readURL("${api}/organisations/${organisationId}")>
    <#assign datasets=json.readURL("${api}/organisations/${organisationId}/datasets")>

    <h1>${organisation.name}</h1>
    <table class="nbn-dataset-table nbn-simple-table">
        <tr>
            <th>Summary</th>
        </tr>
        <tr>
            <td><img class="nbn-align-img-right" src="${organisation.logo}" />${organisation.summary}</td>
        </tr>
        <tr>
            <th>Contact details</th>
        </tr>
        <tr>
            <td>
                ${organisation.contactName}<br/>
                ${organisation.contactEmail}<br/>
                ${organisation.address}
            </td>
        </tr>
        <tr>
            <th>Taxon datasets</th>
        </tr>
        <tr>
            <td>
                <@parseDatasets datasetList=datasets />
            </td>
        </tr>
    </table>

<#macro parseDatasets datasetList>
    [TODO - add logic to parse datasets into taxon, habitat and admin lists]
    <ul>
        <#list datasetList as dataset>
            <li>${dataset.name}</li>
        </#list>
    </ul>
</#macro>

</@template.master>
